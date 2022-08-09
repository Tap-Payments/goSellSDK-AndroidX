package company.tap.gosellapi.internal.api.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;

import company.tap.gosellapi.open.models.Customer;
import company.tap.gosellapi.open.models.MetaData;
import company.tap.gosellapi.open.models.Tax;

public class OrderObject {

    @SerializedName("amount")
    @Expose
    @NonNull private BigDecimal amount;

    @SerializedName("currency")
    @Expose
    @NonNull private String currency;

    @SerializedName("customer")
    @Expose
    @NonNull private Customer customer;

    @SerializedName("items")
    @Expose
    @NonNull private Items items;

    @SerializedName("tax")
    @Expose
    @NonNull private Tax2 tax;

    @SerializedName("shipping")
    @Expose
    @NonNull private ShippingN shipping;

    @SerializedName("merchant")
    @Expose
    @NonNull private Merchant merchant;

    @SerializedName("metadata")
    @Expose
    @NonNull private MetaData metaData;

    @SerializedName("reference")
    @Expose
    @NonNull private ReferId reference;

}
