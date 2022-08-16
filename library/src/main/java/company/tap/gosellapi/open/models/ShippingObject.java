package company.tap.gosellapi.open.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

public class ShippingObject implements Serializable {

    @SerializedName("amount")
    @Expose
    @NonNull
    private BigDecimal amount;

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

    public ShippingObject(@NonNull BigDecimal amount , @NonNull String currency, @Nullable Description description, @Nullable String recipientName,
                          @Nullable AddressCustomer address , @Nullable Provider provider

    ) {

        this.amount = amount;
        this.currency = currency;
        this.description = description;
        this. recipientName = recipientName;
        this.address = address;
        this.provider = provider;


    }

    @NonNull
    public BigDecimal getAmount() {
        return amount;
    }

    @NonNull
    public String getCurrency() {
        return currency;
    }

    @Nullable
    public Description getDescription() {
        return description;
    }

    @Nullable
    public String getRecipientName() {
        return recipientName;
    }

    @Nullable
    public AddressCustomer getAddress() {
        return address;
    }

    @Nullable
    public Provider getProvider() {
        return provider;
    }
}
