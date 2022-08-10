package company.tap.gosellapi.internal.api.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

import company.tap.gosellapi.open.models.Customer;
import company.tap.gosellapi.open.models.MetaData;

public class ShippingN implements Serializable {

    @SerializedName("amount")
    @Expose
    @NonNull
    private String amount;

    @SerializedName("currency")
    @Expose
    @NonNull private String currency;

    @SerializedName("description")
    @Expose
    @Nullable private Description description;

    @SerializedName("recipient_name")
    @Expose
    @Nullable private String recipientName;

    @SerializedName("address")
    @Expose
    @Nullable private AddressCustomer address;


    @SerializedName("provider")
    @Expose
    @Nullable private Provider provider;

    public ShippingN(@NonNull String amount , @NonNull String currency, @Nullable Description description, @Nullable String recipientName,
                     @Nullable AddressCustomer address , @Nullable Provider provider

    ) {

        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this. recipientName = recipientName;
        this.address = address;
        this.provider = provider;


    }
}
