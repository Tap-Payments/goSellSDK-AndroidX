package company.tap.gosellapi.internal.api.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import company.tap.gosellapi.internal.api.enums.AddressFormat;
import company.tap.gosellapi.internal.api.enums.AddressType;

/**
 * The type Address.
 */
public final class AddressCustomer implements Serializable {


    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("line1")
    @Expose
    private String line1;

    @SerializedName("line2")
    @Expose
    private String line2;

    @SerializedName("line3")
    @Expose
    private String line3;

    @SerializedName("line4")
    @Expose
    private String line4;

    @SerializedName("avenue")
    @Expose
    private String avenue;

    @SerializedName("street")
    @Expose
    private String street;
    @SerializedName("building")
    @Expose
    private String building;

    @SerializedName("apartment")
    @Expose
    private String apartment;

    @SerializedName("country")
    @Expose
    private String country;


    @SerializedName("state")
    @Expose
    private String state;

    @SerializedName("city")
    @Expose
    private String city;


    @SerializedName("area")
    @Expose
    private String area;

    @SerializedName("zip_code")
    @Expose
    private String zipCode;

    @SerializedName("postal_code")
    @Expose
    private String postalCode;

    @SerializedName("locale")
    @Expose
    private String locale;
}
