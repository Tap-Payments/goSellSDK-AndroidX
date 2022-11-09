package company.tap.gosellapi.internal.viewholders;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.IsReadyToPayRequest;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.callback.OnCompleteListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

import company.tap.gosellapi.R;
import company.tap.gosellapi.internal.data_managers.PaymentDataManager;
import company.tap.gosellapi.internal.data_managers.payment_options.view_models.GooglePayViewModel;
import company.tap.gosellapi.internal.data_managers.payment_options.view_models_data.EmptyViewModelData;
import company.tap.gosellapi.internal.data_managers.payment_options.view_models_data.GooglePaymentViewModelData;
import company.tap.gosellapi.internal.utils.PaymentsUtil;
import company.tap.gosellapi.open.controllers.SDKSession;
import company.tap.gosellapi.open.data_manager.PaymentDataSource;

/**
 * Created by AhlaamK on 11/28/21.
 * <p>
 * Copyright (c) 2021    Tap Payments.
 * All rights reserved.
 **/
public class GooglePaymentViewHolder extends PaymentOptionsBaseViewHolder<GooglePaymentViewModelData, GooglePaymentViewHolder, GooglePayViewModel> {
    public static View googlePayButton;
    // A client for interacting with the Google Pay API.
    private PaymentsClient paymentsClient;


    GooglePaymentViewModelData googlePaymentViewModelData;

    // Arbitrarily-picked constant integer you define to track a request for payment data activity.
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;
    /**
     * Instantiates a new Currency view holder.
     *
     * @param view the view
     */
    Activity activity;
    @RequiresApi(api = Build.VERSION_CODES.N)
    GooglePaymentViewHolder(View view) {
        super(view);

        // Initialize a Google Pay API client for an environment suitable for testing.
        // It's recommended to create the PaymentsClient object inside of the onCreate method.
        paymentsClient = PaymentsUtil.createPaymentsClient((Activity) view.getContext());
        googlePayButton = view.findViewById(R.id.googlePayButton);

        activity = (Activity) view.getContext();
        if(PaymentDataSource.getInstance().getGooglePaymentOptions().size()!=0){
            possiblyShowGooglePayButton();
            googlePayButton.setOnClickListener(
                    view1 -> requestPayment(view1)

            );
        }


    }



    @Override
    public void bind(GooglePaymentViewModelData data) {
        this.googlePaymentViewModelData = data;
     //   System.out.println("googlePaymentViewModelData>>"+googlePaymentViewModelData);
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
    private void possiblyShowGooglePayButton() {

        final Optional<JSONObject> isReadyToPayJson = PaymentsUtil.getIsReadyToPayRequest();
     //   System.out.println("ready to pay request>>>>"+isReadyToPayJson);
        if (!isReadyToPayJson.isPresent()) {
            return;
        }else {

            // The call to isReadyToPay is asynchronous and returns a Task. We need to provide an
            // OnCompleteListener to be triggered when the result of the call is known.
            IsReadyToPayRequest request = IsReadyToPayRequest.fromJson(isReadyToPayJson.get().toString());
            Task<Boolean> task = paymentsClient.isReadyToPay(request);
            task.addOnCompleteListener(activity, new com.google.android.gms.tasks.OnCompleteListener<Boolean>() {
                @Override
                public void onComplete(@NonNull Task<Boolean> task) {
                   // System.out.println("task value is" + task.getResult());
                    if (task.isSuccessful()) {
                        setGooglePayAvailable(task.getResult());
                    } else {
                        SDKSession.getListener().googlePayFailed(task.getException().toString());
                        Log.w("isReadyToPay failed", task.getException());
                    }
                }
            });
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void requestPayment(View view) {
        // Disables the button to prevent multiple clicks.
      //  googlePayButton.setClickable(false);

        assert PaymentDataSource.getInstance().getAmount() != null;

        if(PaymentDataManager.getInstance().getPaymentDataProvider()!=null && PaymentDataManager.getInstance().getPaymentDataProvider().getSelectedCurrency()!=null && PaymentDataManager.getInstance().getPaymentDataProvider().getSelectedCurrency().getAmount()!=null){
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

        }

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
      //  System.out.println("GPayavailable"+available);
        if (available) {
            googlePayButton.setVisibility(View.VISIBLE);
        } else {
            googlePayButton.setVisibility(View.GONE);
            Toast.makeText(itemView.getContext(),R.string.googlepay_button_not_supported, Toast.LENGTH_LONG).show();
        }
    }





}
