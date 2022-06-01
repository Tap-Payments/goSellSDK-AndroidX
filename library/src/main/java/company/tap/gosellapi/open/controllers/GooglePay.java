package company.tap.gosellapi.open.controllers;

import company.tap.gosellapi.internal.api.models.PhoneNumber;
import company.tap.gosellapi.open.enums.OperationMode;
import company.tap.gosellapi.open.models.Customer;

public class GooglePay {
    private String merchantName;
    private OperationMode walletTestMode;
    private String merchantGatewayId;

    //  Constructor is private to prevent access from client app, it must be through inner Builder class only
    private GooglePay(String merchantName, OperationMode walletTestMode,String merchantGatewayId) {
        this.merchantName = merchantName;
        this.walletTestMode = walletTestMode;
        this.merchantGatewayId = merchantGatewayId;

    }


    public static class GooglePayBuilder {

        private String merchantName;
        private OperationMode walletTestMode;
        private String merchantGatewayId;

        public GooglePayBuilder(String merchantName, OperationMode walletTestMode, String merchantGatewayId) {
            this.merchantName =merchantName;
            this.walletTestMode =walletTestMode;
            this.merchantGatewayId =merchantGatewayId;
        }

        /**
         * Build GooglePay.
         *
         * @return the GooglePay
         */
        public GooglePay build() {
            return new GooglePay(merchantGatewayId, walletTestMode, merchantName);
        }
    }
    ////////////////////////// ############################ End of Builder Region ########################### ///////////////////////

}
