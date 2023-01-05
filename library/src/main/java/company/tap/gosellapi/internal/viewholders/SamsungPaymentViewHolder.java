package company.tap.gosellapi.internal.viewholders;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.samsung.android.sdk.samsungpay.v2.PartnerInfo;
import com.samsung.android.sdk.samsungpay.v2.SamsungPay;
import com.samsung.android.sdk.samsungpay.v2.SpaySdk;
import com.samsung.android.sdk.samsungpay.v2.StatusListener;

import company.tap.gosellapi.internal.data_managers.payment_options.view_models.SamsungPayViewModel;
import company.tap.gosellapi.internal.data_managers.payment_options.view_models_data.SamsungPaymentViewModelData;
import company.tap.gosellapi.open.data_manager.PaymentDataSource;

public class SamsungPaymentViewHolder extends PaymentOptionsBaseViewHolder<SamsungPaymentViewModelData, SamsungPaymentViewHolder, SamsungPayViewModel> {
    public static View googlePayButton;

    private PartnerInfo partnerInfo;
private SamsungPay samsungPay;

    SamsungPaymentViewModelData samsungPaymentViewModelData;

    // Arbitrarily-picked constant integer you define to track a request for payment data activity.
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;
    /**
     * Instantiates a new Currency view holder.
     *
     * @param view the view
     */
    Activity activity;
    private String SERVICE_ID ="";
    @RequiresApi(api = Build.VERSION_CODES.N)
    SamsungPaymentViewHolder(View view) {
        super(view);

        // Initialize a Google Pay API client for an environment suitable for testing.
        // It's recommended to create the PaymentsClient object inside of the onCreate method.

        activity = (Activity) view.getContext();
        possiblyShowSamsungPayButton();
        Bundle bundle =  new Bundle();
        bundle.putString(SpaySdk.PARTNER_SERVICE_TYPE, SpaySdk.ServiceType.INAPP_PAYMENT.toString());
        partnerInfo = new PartnerInfo(SERVICE_ID, bundle);

    }



    @Override
    public void bind(SamsungPaymentViewModelData data) {
        this.samsungPaymentViewModelData = data;
        System.out.println("samsungPaymentViewModelData>>"+samsungPaymentViewModelData);
    }

    /**
     * Determine the viewer's ability to pay with a payment method supported by your app and display a
     * Google Pay payment button.
     *
     * @see <a href="https://developers.google.com/android/reference/com/google/android/gms/wallet/
     * PaymentsClient.html#isReadyToPay(com.google.android.gms.wallet.
     * IsReadyToPayRequest)">PaymentsClient#IsReadyToPay</a>
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void possiblyShowSamsungPayButton() {

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void requestPayment(View view) {
        // Disables the button to prevent multiple clicks.
        //  googlePayButton.setClickable(false);

        assert PaymentDataSource.getInstance().getAmount() != null;

      /*  if(PaymentDataManager.getInstance().getPaymentDataProvider()!=null && PaymentDataManager.getInstance().getPaymentDataProvider().getSelectedCurrency()!=null && PaymentDataManager.getInstance().getPaymentDataProvider().getSelectedCurrency().getAmount()!=null){
            Optional<JSONObject> paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(PaymentDataManager.getInstance().getPaymentDataProvider().getSelectedCurrency().getAmount().longValue());
            if (!paymentDataRequestJson.isPresent()) {
                return;
            }
            PaymentDataRequest request =
                    PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString());
            System.out.println("request value is>>>"+request.toJson());

            // Since loadPaymentData may show the UI asking the user to select a payment method, we use
            // AutoResolveHelper to wait for the user interacting with it. Once completed,
            // onActivityResult will be called with the result.
            if (request != null) {
                AutoResolveHelper.resolveTask(
                        paymentsClient.loadPaymentData(request),
                        (Activity) view.getContext(), LOAD_PAYMENT_DATA_REQUEST_CODE);

            }

        }*/

    }




    /**
     * If isReadyToPay returned {@code true}, show the button and hide the "checking" text. Otherwise,
     * notify the user that Google Pay is not available. Please adjust to fit in with your current
     * user flow. You are not required to explicitly let the user know if isReadyToPay returns {@code
     * false}.
     *
     * @param available isReadyToPay API response.
     */
    private void setGooglePayAvailable(boolean available) {
        System.out.println("available"+available);
       /* if (available) {
            googlePayButton.setVisibility(View.VISIBLE);
        } else {
            googlePayButton.setVisibility(View.GONE);
            Toast.makeText(itemView.getContext(), R.string.googlepay_button_not_supported, Toast.LENGTH_LONG).show();
        }*/
    }


    private void   updateSamsungPayButton() {
        samsungPay = new SamsungPay(itemView.getContext(), partnerInfo);

        samsungPay.getSamsungPayStatus(new StatusListener() {
            @Override
            public void onSuccess(int status, Bundle bundle) {
                switch (status) {
                    case SpaySdk.SPAY_READY:
                      //  dataBinding.samsungPayButton.visibility = View.VISIBLE;
                        activateSamsungPay();
                        // Perform your operation.
                        break;
                    case SpaySdk.SPAY_NOT_READY:
                        // Samsung Pay is supported but not fully ready.

                        // If EXTRA_ERROR_REASON is ERROR_SPAY_APP_NEED_TO_UPDATE,
                        // Call goToUpdatePage().

                        // If EXTRA_ERROR_REASON is ERROR_SPAY_SETUP_NOT_COMPLETED,
                        // Call activateSamsungPay().

                       // dataBinding.samsungPayButton.visibility = View.INVISIBLE;
                        break;
                 //   case SpaySdk.SPAY_NOT_ALLOWED_TEMPORALLY:
                        // If EXTRA_ERROR_REASON is ERROR_SPAY_CONNECTED_WITH_EXTERNAL_DISPLAY,
                        // guide user to disconnect it.

                    //    dataBinding.samsungPayButton.visibility = View.INVISIBLE;
                             //   break;

                    case SpaySdk.SPAY_NOT_SUPPORTED:
                      //  dataBinding.samsungPayButton.visibility = View.INVISIBLE;
                        break;
                    default:
                        //dataBinding.samsungPayButton.visibility = View.INVISIBLE;


                }
            }
            @Override
            public void onFail(int status, Bundle bundle) {
               // dataBinding.samsungPayButton.visibility = View.INVISIBLE;
              //  Toast.makeText(applicationContext, "getSamsungPayStatus fail", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void activateSamsungPay(){

    }


    }







