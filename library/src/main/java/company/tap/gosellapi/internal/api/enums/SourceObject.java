package company.tap.gosellapi.internal.api.enums;

import com.google.gson.annotations.SerializedName;

/**
 * The enum Source object.
 */
public enum SourceObject {

    @SerializedName("CARD")
    card("CARD"),

    @SerializedName("TOKEN")
    token("TOKEN"),

    @SerializedName("SOURCE")
    source("SOURCE");


    private String rawValue;

    private SourceObject(String rawValue) {
        this.rawValue = rawValue;
    }

    public String getRawValue() {
        return this.rawValue;
    }
}
