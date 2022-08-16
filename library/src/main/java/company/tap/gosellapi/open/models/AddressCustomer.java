package company.tap.gosellapi.open.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import company.tap.gosellapi.internal.api.enums.AddressFormat;
import company.tap.gosellapi.internal.api.enums.AddressType;
import company.tap.gosellapi.internal.api.models.AmountModificator;

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

    /**
     * Instantiates a new AddressCustomer.
     *
     *
     */
    public AddressCustomer(@Nullable String type, @NonNull String line1, @Nullable String line2, @Nullable String line3, @Nullable String line4,
                           @Nullable String avenue, @Nullable String street, @Nullable String building,@Nullable String apartment,@Nullable String country,
                           @Nullable String state,@Nullable String city,@Nullable String area,@Nullable String zipCode,@Nullable String postalCode,@Nullable String locale) {
        this.type = type;
        this.line1 = line1;
        this.line2 = line2;
        this.line3 = line3;
        this.line4 = line4;
        this.avenue = avenue;
        this.street = street;
        this.building = building;
        this.apartment = apartment;
        this.country = country;
        this.state = state;
        this.city = city;
        this.area = area;
        this.zipCode = zipCode;
        this.postalCode = postalCode;
        this.locale = locale;
    }

}
