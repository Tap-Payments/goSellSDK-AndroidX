package company.tap.gosellapi.open.controllers;

import company.tap.gosellapi.internal.api.models.PhoneNumber;
import company.tap.gosellapi.open.enums.GPayWalletMode;
import company.tap.gosellapi.open.enums.OperationMode;
import company.tap.gosellapi.open.models.Customer;

public class GooglePay {


    private String merchantName;
    private GPayWalletMode walletTestMode;
    private String merchantGatewayId;


    public String getMerchantName() {
        return merchantName;
    }

    public GPayWalletMode getWalletTestMode() {
        return walletTestMode;
    }

    public String getMerchantGatewayId() {
        return merchantGatewayId;
    }
    //  Constructor is private to prevent access from client app, it must be through inner Builder class only
    private GooglePay(String merchantName, GPayWalletMode walletTestMode,String merchantGatewayId) {
        this.merchantName = merchantName;
        this.walletTestMode = walletTestMode;
        this.merchantGatewayId = merchantGatewayId;

    }


    public static class GooglePayBuilder {

        private String merchantName;
        private GPayWalletMode walletTestMode;
        private String merchantGatewayId;

        public GooglePayBuilder(String merchantName, GPayWalletMode walletTestMode, String merchantGatewayId) {
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
