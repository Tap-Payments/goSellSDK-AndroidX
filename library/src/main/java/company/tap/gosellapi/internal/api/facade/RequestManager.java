package company.tap.gosellapi.internal.api.facade;

import android.util.Log;

import androidx.annotation.RestrictTo;

import java.io.IOException;
import java.util.ArrayList;

import company.tap.gosellapi.internal.api.api_service.APIService;
import company.tap.gosellapi.internal.api.callbacks.APIRequestCallback;
import company.tap.gosellapi.internal.api.callbacks.BaseCallback;
import company.tap.gosellapi.internal.api.callbacks.GoSellError;
import company.tap.gosellapi.internal.api.responses.BaseResponse;
import company.tap.gosellapi.internal.api.responses.SDKSettings;
import company.tap.gosellapi.internal.data_managers.PaymentDataManager;
import okhttp3.RequestBody;
import okio.Buffer;
import retrofit2.Call;

/**
 * The type Request manager.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
class RequestManager {
    private APIService apiHelper;

    //all requests are wrapped in DelayedRequest, until init() would be finished
    private boolean initIsRunning;
    private ArrayList<DelayedRequest> delayedRequests;

    /**
     * Instantiates a new Request manager.
     *
     * @param apiHelper the api helper
     */
    RequestManager(APIService apiHelper) {
        this.apiHelper = apiHelper;
        delayedRequests = new ArrayList<>();
    }

    /**
     * Request.
     *
     * @param delayedRequest the delayed request
     */
    void request(DelayedRequest delayedRequest,boolean checkInit) {
        delayedRequests.add(delayedRequest);
        if (PaymentDataManager.getInstance().getSDKSettings() == null && checkInit) {
            if (!initIsRunning) {
                init();
            }
        } else {
            runDelayedRequests();
        }
    }

  /**
   * Retrieve SDKSettings from server.
   */
  private void init() {
        initIsRunning = true;
        apiHelper
                .init()
                .enqueue(new BaseCallback<>(new APIRequestCallback<SDKSettings>() {
                    @Override
                    public void onSuccess(int responseCode, SDKSettings serializedResponse) {
                        initIsRunning = false;
                        Log.e("dataRequestBody Response Success", String.valueOf(serializedResponse.getData()));

                        PaymentDataManager.getInstance().setSDKSettings(serializedResponse);
                        /**
                         * Checking added based on the param @Verified_application to check if application
                         * package id is registered or not
                         * @returns boolean value.
                         */
                      if(PaymentDataManager.getInstance().getSDKSettings()!=null){
                          if(PaymentDataManager.getInstance().getSDKSettings().getData().isVerified_application())
                              runDelayedRequests();
                          }
                      }


                    @Override
                    public void onFailure(GoSellError errorDetails) {
                        initIsRunning = false;
                        Log.e("dataRequestBody Response Error ", String.valueOf(errorDetails.getErrorBody()));

                        failDelayedRequests(errorDetails);
                    }
                }));
    }


    private void runDelayedRequests() {
        for (DelayedRequest delayedRequest : delayedRequests) {
//            Log.d("runDelayedRequests","delayedRequest.toString() : " + delayedRequest.getRequest().request());
          try {
            final Buffer buffer = new Buffer();
            if(delayedRequest.getRequest().request().body()!=null ) {
              delayedRequest.getRequest().request().body().writeTo(buffer);
//                System.out.println("delayedRequest.toString() :" + buffer.readUtf8().toString());
            }
          }catch (IOException s){
              Log.d("runDelayedRequests","ex : " + s.getLocalizedMessage());
          }
          delayedRequest.run();
        }
        delayedRequests.clear();
    }


    private void failDelayedRequests(GoSellError errorDetails) {
        for (DelayedRequest delayedRequest : delayedRequests) {
            delayedRequest.fail(errorDetails);
        }
        delayedRequests.clear();
    }

    /**
     * The type Delayed request.
     *
     * @param <T> the type parameter
     */
    static class DelayedRequest<T extends BaseResponse> {
        private Call<T> request;
        private APIRequestCallback<T> requestCallback;

        /**
         * Instantiates a new Delayed request.
         *
         * @param request         the request
         * @param requestCallback the request callback
         */
        DelayedRequest(Call<T> request, APIRequestCallback<T> requestCallback) {
            this.request = request;
            this.requestCallback = requestCallback;
        }

      private String bodyToString(final RequestBody request){
        try {
          final RequestBody copy = request;
          final Buffer buffer = new Buffer();
          if(copy != null)
            copy.writeTo(buffer);
          else
            return "body null";
          return buffer.readUtf8();
        }
        catch (final IOException e) {
          return "did not work";
        }

      }


        /**
         * Run.
         */
        void run() {
            request.enqueue(new BaseCallback<>(requestCallback));
        }

        /**
         * Fail.
         *
         * @param errorDetails the error details
         */
        void fail(GoSellError errorDetails) {
            Log.e("dataRequestBody Response Error ", String.valueOf(errorDetails.getErrorBody()));
            requestCallback.onFailure(errorDetails);
        }

        /**
         * Get request call.
         *
         * @return the call
         */
        Call  getRequest(){
          return this.request;
        }
    }


}
