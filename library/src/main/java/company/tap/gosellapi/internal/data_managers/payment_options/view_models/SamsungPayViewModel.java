package company.tap.gosellapi.internal.data_managers.payment_options.view_models;

import company.tap.gosellapi.internal.data_managers.payment_options.PaymentOptionsDataManager;
import company.tap.gosellapi.internal.data_managers.payment_options.view_models_data.SamsungPaymentViewModelData;
import company.tap.gosellapi.internal.viewholders.PaymentOptionsBaseViewHolder;
import company.tap.gosellapi.internal.viewholders.SamsungPaymentViewHolder;

public class SamsungPayViewModel extends PaymentOptionViewModel<SamsungPaymentViewModelData, SamsungPaymentViewHolder, SamsungPayViewModel> {
        /**
         * Instantiates a new Currency view model.
         *  @param parentDataManager the parent data manager
         * @param data              the data*/
        public SamsungPayViewModel(PaymentOptionsDataManager parentDataManager, SamsungPaymentViewModelData data) {
                super(parentDataManager, data, PaymentOptionsBaseViewHolder.ViewHolderType.SAMSUNGPAY);
        }



        /**
         * Holder clicked.
         */
        public void holderClicked() {
        }
}
