package company.tap.gosellapi.internal.viewholders;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentDataRequest;
import com.google.android.gms.wallet.PaymentsClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

import company.tap.gosellapi.R;
import company.tap.gosellapi.internal.data_managers.PaymentDataManager;
import company.tap.gosellapi.internal.data_managers.payment_options.view_models.GooglePayViewModel;
import company.tap.gosellapi.internal.data_managers.payment_options.view_models_data.EmptyViewModelData;
import company.tap.gosellapi.internal.utils.PaymentsUtil;
import company.tap.gosellapi.open.data_manager.PaymentDataSource;

/**
 * Created by AhlaamK on 11/28/21.
 * <p>
 * Copyright (c) 2021    Tap Payments.
 * All rights reserved.
 **/
public class GooglePaymentViewHolder extends PaymentOptionsBaseViewHolder<String, GooglePaymentViewHolder, GooglePayViewModel> {
    private View googlePayButton;
    // A client for interacting with the Google Pay API.
    private PaymentsClient paymentsClient;

    // Arbitrarily-picked constant integer you define to track a request for payment data activity.
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;
    /**
     * Instantiates a new Currency view holder.
     *
     * @param view the view
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    GooglePaymentViewHolder(View view) {
        super(view);

        // Initialize a Google Pay API client for an environment suitable for testing.
        // It's recommended to create the PaymentsClient object inside of the onCreate method.
        paymentsClient = PaymentsUtil.createPaymentsClient((Activity) view.getContext());
        googlePayButton = view.findViewById(R.id.googlePayButton);
        googlePayButton.setOnClickListener(
                view1 -> requestPayment(view1)

        );

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void requestPayment(View view) {
        // Disables the button to prevent multiple clicks.
        googlePayButton.setClickable(false);

        Optional<JSONObject> paymentDataRequestJson = PaymentsUtil.getPaymentDataRequest(PaymentDataSource.getInstance().getAmount().toBigInteger().longValue());
        if (!paymentDataRequestJson.isPresent()) {
            return;
        }

        PaymentDataRequest request =
                PaymentDataRequest.fromJson(paymentDataRequestJson.get().toString());

        System.out.println("request value is>>>"+request);

        // Since loadPaymentData may show the UI asking the user to select a payment method, we use
        // AutoResolveHelper to wait for the user interacting with it. Once completed,
        // onActivityResult will be called with the result.
     if (request != null) {
            AutoResolveHelper.resolveTask(
                    paymentsClient.loadPaymentData(request),
                    (Activity) view.getContext(), LOAD_PAYMENT_DATA_REQUEST_CODE);
        }

    }


    @Override
    public void bind(String data) {


    }








}
