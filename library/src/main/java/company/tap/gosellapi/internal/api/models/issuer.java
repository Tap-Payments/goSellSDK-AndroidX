package company.tap.gosellapi.internal.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import company.tap.gosellapi.internal.interfaces.SecureSerializable;

public final class issuer implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;


    @SerializedName("bank")
    @Expose
    private String bank;

    @SerializedName("country")
    @Expose
    private String country;

    public String getId() {
        return id;
    }


    public String getBank() {
        return bank;
    }

    public String getCountry() {
        return country;
    }

}
