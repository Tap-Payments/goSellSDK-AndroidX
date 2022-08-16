package company.tap.gosellapi.open.enums;

import com.google.gson.annotations.SerializedName;

public enum Category {
    /**
     * Sandbox is for testing purposes
     */
    @SerializedName("PHYSICAL_GOODS")              PHYSICAL_GOODS,

    /**
     *  Production is for live
     */
    @SerializedName("DIGITAL_GOODS")            DIGITAL_GOODS
}
