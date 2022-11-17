package company.tap.gosellapi.internal.api.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

import company.tap.gosellapi.internal.api.enums.CardScheme;
import company.tap.gosellapi.internal.api.interfaces.CurrenciesSupport;
import company.tap.tapcardvalidator_android.CardBrand;

/**
 * The type Saved card.
 */
public class SavedCard implements Comparable<SavedCard>, CurrenciesSupport, Serializable {

    @SerializedName("id")
    @Expose
    @Nullable
    private String id;

    @SerializedName("object")
    @Expose
    @NonNull
    private String object;

    @SerializedName("first_six")
    @Expose
    @NonNull
    private String firstSix;

    @SerializedName("last_four")
    @Expose
    @NonNull
    private String lastFour;

    @SerializedName("brand")
    @Expose
    @NonNull
    private CardBrand brand;

    @SerializedName("payment_method_id")
    @Expose
    @Nullable
    private String paymentOptionIdentifier;

    @SerializedName("expiry")
    @Expose
    @Nullable
    private ExpirationDate expiry;

    @SerializedName("name")
    @Expose
    @Nullable
    private String cardholderName;

    @SerializedName("currency")
    @Expose
    @Nullable
    private String currency;

    @SerializedName("scheme")
    @Expose
    @Nullable
    private CardScheme scheme;

    @SerializedName("supported_currencies")
    @Expose
    @Nullable
    private ArrayList<String> supportedCurrencies;

    @SerializedName("order_by")
    @Expose
    @NonNull
    private int orderBy;

    @SerializedName("image")
    @Nullable
    private String image;

    @SerializedName("fingerprint")
    @NonNull
    private String fingerprint;

    // added for mapping Expiry month and Date in case of get list card API

    @SerializedName("exp_month")
    @Expose
    @Nullable
    private String exp_month;

    @SerializedName("exp_year")
    @Expose
    @Nullable
    private String exp_year;


    @SerializedName("funding")
    @Expose
    @Nullable
    private String funding;

    @SerializedName("customer")
    @Expose
    @Nullable
    private String customer;


    /**
     * Gets fingerprint.
     *
     * @return Card fingerprint
     */
    @NonNull
    public String getFingerprint() {
        return fingerprint;
    }

    /**
     * Gets id.
     *
     * @return Card identifier.
     */
    @Nullable
    public String getId() {
        return id;
    }

    /**
     * Gets object.
     *
     * @return Object type.
     */
    @NonNull
    public String getObject() {
        return object;
    }

    /**
     * Gets last four.
     *
     * @return Last 4 digits of the card.
     */
    @NonNull
    public String getLastFour() {
        return lastFour;
    }

    /**
     * Gets expiry.
     *
     * @return Expiration date.
     */
    @Nullable
    public ExpirationDate getExpiry() {
        return expiry;
    }

    /**
     * Gets brand.
     *
     * @return Card brand.
     */
    @NonNull
    public CardBrand getBrand() {
        return brand;
    }


    /**
     * Gets Brand name as string
     *
     * @return
     */
    @NonNull
    public String getBrandName() {
        if (brand != null && brand.name() != null)
            return brand.name();
        return "UNKNOWN";
    }

    /**
     * Gets Brand raw value as string
     *
     * @return
     */
    @NonNull
    public String getBrandRawValue() {
        if (brand != null && brand.getRawValue() != null)
            return brand.getRawValue();
        return "UNKNOWN";
    }


    /**
     * Gets cardholder name.
     *
     * @return Cardholder name.
     */
    @Nullable
    public String getCardholderName() {
        return cardholderName;
    }

    /**
     * Gets first six.
     *
     * @return Card BIN number.
     */
    @NonNull
    public String getFirstSix() {
        return firstSix;
    }

    /**
     * Gets currency.
     *
     * @return Card currency.
     */
    @Nullable
    public String getCurrency() {
        return currency;
    }

    /**
     * Gets payment option identifier.
     *
     * @return the payment option identifier
     */
    @Nullable
    public String getPaymentOptionIdentifier() {
        return paymentOptionIdentifier;
    }

    /**
     * Gets scheme.
     *
     * @return the scheme
     */
    @Nullable
    public CardScheme getScheme() {
        return scheme;
    }

    @Nullable
    public ArrayList<String> getSupportedCurrencies() {
        return supportedCurrencies;
    }

    /**
     * Get image string.
     *
     * @return the string
     */
    @Nullable
    public String getImage() {
        return image;
    }

    /**
     * Gets order by.
     *
     * @return Ordering field.
     */

    @NonNull
    public int getOrderBy() {
        return orderBy;
    }


    /**
     * @return expiration month as String
     */
    @Nullable
    public String getExp_month() {
        return exp_month;
    }

    /**
     * @return expiration year as String
     */

    @Nullable
    public String getExp_year() {
        return exp_year;
    }


    /**
     * @return funding as String
     */
    @Nullable
    public String getFunding() {
        return funding;
    }

    /**
     * @return customer reference
     */
    @Nullable
    public String getCustomer() {
        return customer;
    }

    @Override
    public int compareTo(@NonNull SavedCard o) {
        return getOrderBy() - o.getOrderBy();
    }
}
