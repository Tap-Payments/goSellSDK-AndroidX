package company.tap.gosellapi.internal.api.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
    @Nullable private Customer customer;

    @SerializedName("items")
    @Expose
    @Nullable private Items items;

    @SerializedName("tax")
    @Expose
    @Nullable private Tax2 tax;

    @SerializedName("shipping")
    @Expose
    @Nullable private ShippingN shipping;

    @SerializedName("merchant")
    @Expose
    @Nullable private Merchant merchant;

    @SerializedName("metadata")
    @Expose
    @Nullable private MetaData metaData;

    @SerializedName("reference")
    @Expose
    @Nullable private ReferId reference;


    //  Constructor is private to prevent access from client app, it must be through inner Builder class only
    public OrderObject(@NonNull BigDecimal amount , @NonNull String currency, @Nullable Customer customer, @Nullable Items items,
                       @Nullable Tax2 tax , @Nullable ShippingN shipping, @Nullable Merchant merchant , @Nullable MetaData metaData, @Nullable ReferId reference
    ) {

        this.amount = amount;
        this.currency = currency;
        this.customer = customer;
        this. items = items;
        this.tax = tax;
        this.shipping = shipping;
        this.merchant = merchant;
        this.metaData = metaData;
      
    }

}
