package company.tap.gosellapi.internal.api.enums;

import com.google.gson.annotations.SerializedName;

import company.tap.tapcardvalidator_android.CardBrand;

/**
 * The enum Card scheme.
 */
public enum CardScheme {

    /**
     * Knet card scheme.
     */
    @SerializedName("KNET")             KNET,
    /**
     * Visa card scheme.
     */
    @SerializedName("VISA")             VISA,
    /**
     * Mastercard card scheme.
     */
    @SerializedName("MASTERCARD")       MASTERCARD,
    /**
     * American express card scheme.
     */
    @SerializedName("AMERICAN_EXPRESS") AMERICAN_EXPRESS,
    /**
     * Mada card scheme.
     */
    @SerializedName("MADA")             MADA,
    /**
     * Benefit card scheme.
     */
    @SerializedName("BENEFIT")          BENEFIT,
    /**
     * Sadad card scheme.
     */
    @SerializedName("SADAD_ACCOUNT")    SADAD,
    /**
     * Fawry card scheme.
     */
    @SerializedName("FAWRY")            FAWRY,
    /**
     * Naps card scheme.
     */
    @SerializedName("NAPS")             NAPS,
    /**
     * Oman net card scheme.
     */
    @SerializedName("OMAN_NET")         OMAN_NET,

    @SerializedName("MEEZA")         MEEZA,


    @SerializedName("Careem Pay")        CareemPay,

    @SerializedName("Google_Pay")                                       GooglePay,
    @SerializedName("PayPal")                                        PayPal,
    @SerializedName("SAMSUNG_PAY")  SamsungPay;
    /**
     * Gets card brand.
     *
     * @return the card brand
     */
    public CardBrand getCardBrand() {

        switch (this) {

            case KNET:              return CardBrand.knet;
            case VISA:              return CardBrand.visa;
            case MASTERCARD:        return CardBrand.masterCard;
            case AMERICAN_EXPRESS:  return CardBrand.americanExpress;
            case MADA:              return CardBrand.mada;
            case BENEFIT:           return CardBrand.benefit;
            case SADAD:             return CardBrand.sadad;
            case FAWRY:             return CardBrand.fawry;
            case NAPS:              return CardBrand.naps;
            case OMAN_NET:          return CardBrand.omanNet;
            case MEEZA:              return CardBrand.meeza;
            case CareemPay:          return CardBrand.careemPay;
            case GooglePay:          return CardBrand.googlePay;
            case PayPal:             return CardBrand.payPal;
            case SamsungPay:          return CardBrand.SAMSUNG_PAY;

            default:                return CardBrand.unknown;
        }
    }

}
