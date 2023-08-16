package company.tap.gosellapi.internal.api.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import company.tap.tapcardvalidator_android.CardBrand;

/**
 * The type Tokenized card.
 */
public final class TokenizedCard implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("object")
    @Expose
    private String object;

    @SerializedName("address")
    @Expose
    @Nullable private Address address;

    @SerializedName("funding")
    @Expose
    private String funding;

    @SerializedName("fingerprint")
    @Expose
    private String fingerprint;

    @SerializedName("brand")
    @Expose
    private CardBrand brand;

    @SerializedName("exp_month")
    @Expose
    private int expirationMonth;

    @SerializedName("exp_year")
    @Expose
    private int expirationYear;

    @SerializedName("last_four")
    @Expose
    private String lastFour;


    @SerializedName("first_six")
    @Expose
    private String firstSix;


    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("issuer")
    @Expose
    private issuer issuer;






    /**
     * Gets first six.
     *
     * @return the first six
     */
    public String getFirstSix() {
        return firstSix;
    }
    public issuer getIssuer() {
        return issuer;
    }

    @Nullable
    public String getBrand() {
        if (brand==null) return "";
        return brand.getRawValue();

    }

    /**
     * Gets fingerprint.
     *
     * @return the fingerprint
     */
    @NonNull
    public String getFingerprint() {
        return fingerprint;
    }

    public String getId() {
        return id;
    }

    public String getObject() {
        return object;
    }

    public String getLastFour() {
        return lastFour;
    }

    public int getExpirationMonth() {
        return expirationMonth;
    }

    public int getExpirationYear() {
        return expirationYear;
    }




    public String getFunding() {
        return funding;
    }

    public String getName() {
        return name;
    }



    @Nullable
    public Address getAddress() {
        return address;
    }
}
