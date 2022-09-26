package company.tap.gosellapi.internal.data_managers.payment_options.view_models;

import java.util.ArrayList;

import company.tap.gosellapi.internal.api.models.PaymentOption;
import company.tap.gosellapi.internal.data_managers.payment_options.PaymentOptionsDataManager;
import company.tap.gosellapi.internal.data_managers.payment_options.view_models_data.CardCredentialsViewModelData;
import company.tap.gosellapi.internal.data_managers.payment_options.view_models_data.CurrencyViewModelData;
import company.tap.gosellapi.internal.data_managers.payment_options.view_models_data.GooglePaymentViewModelData;
import company.tap.gosellapi.internal.viewholders.CurrencyViewHolder;
import company.tap.gosellapi.internal.viewholders.GooglePaymentViewHolder;
import company.tap.gosellapi.internal.viewholders.GroupViewHolder;
import company.tap.gosellapi.internal.viewholders.PaymentOptionsBaseViewHolder;

/**
 * The type GooglePayViewModel view model.
 */
public class GooglePayViewModel extends PaymentOptionViewModel<GooglePaymentViewModelData, GooglePaymentViewHolder, GooglePayViewModel> {
    /**
     * Instantiates a new Currency view model.
     *  @param parentDataManager the parent data manager
     * @param data              the data*/
 public GooglePayViewModel(PaymentOptionsDataManager parentDataManager,GooglePaymentViewModelData data) {
        super(parentDataManager, data, PaymentOptionsBaseViewHolder.ViewHolderType.GOOGLEPAY);
    }



    /**
     * Holder clicked.
     */
    public void holderClicked() {
    }
}
