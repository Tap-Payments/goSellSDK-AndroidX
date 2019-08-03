package company.tap.gosellapi.internal.data_managers.payment_options.view_models;

import company.tap.gosellapi.internal.api.models.PaymentOption;
import company.tap.gosellapi.internal.data_managers.payment_options.PaymentOptionsDataManager;
import company.tap.gosellapi.internal.viewholders.PaymentOptionsBaseViewHolder;
import company.tap.gosellapi.internal.viewholders.WebPaymentViewHolder;

/**
 * The type Web payment view model.
 */
public class WebPaymentViewModel extends PaymentOptionViewModel<PaymentOption, WebPaymentViewHolder, WebPaymentViewModel> {

    /**
     * Instantiates a new Web payment view model.
     *
     * @param parentDataManager the parent data manager
     * @param data              the data
     */
    public WebPaymentViewModel(PaymentOptionsDataManager parentDataManager, PaymentOption data) {
        super(parentDataManager, data, PaymentOptionsBaseViewHolder.ViewHolderType.WEB);
    }

    /**
     * Item clicked.
     */
    public void itemClicked() {
        parentDataManager.webPaymentSystemViewHolderClicked(this, position);
    }
}
