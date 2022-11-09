package company.tap.gosellapi.open.enums;

import com.google.android.gms.wallet.WalletConstants;
import com.google.gson.annotations.SerializedName;

public  enum GPayWalletMode {
    /**
     * Sandbox is for testing purposes
     */
    @SerializedName("ENVIRONMENT_TEST")  ENVIRONMENT_TEST,

    /**
     *  Production is for live
     */
    @SerializedName("ENVIRONMENT_PRODUCTION")            ENVIRONMENT_PRODUCTION
}
