package company.tap.gosellapi.internal.data_managers.payment_options.view_models_data;

import java.util.ArrayList;

import company.tap.gosellapi.internal.api.models.PaymentOption;

public class GooglePaymentViewModelData {

    private ArrayList<PaymentOption> paymentOptions;

    /**
     * Instantiates a new Card credentials view model data.
     *
     * @param paymentOptions          the payment options
     */
    public GooglePaymentViewModelData(ArrayList<PaymentOption> paymentOptions) {

        this.paymentOptions = paymentOptions;
    }

    public GooglePaymentViewModelData(GooglePaymentViewModelData data) {
    }

    /**
     * Gets payment options.
     *
     * @return the payment options
     */
    public ArrayList<PaymentOption> getPaymentOptions() {

        return paymentOptions;
    }

    /**
     * Sets payment options.
     *
     * @param paymentOptions the payment options
     */
    public void setPaymentOptions(ArrayList<PaymentOption> paymentOptions) {

        this.paymentOptions = paymentOptions;
    }

}