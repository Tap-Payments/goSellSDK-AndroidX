package company.tap.gosellapi.internal.api.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

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
    @NonNull private Description description;

    @SerializedName("recipient_name")
    @Expose
    @NonNull private String recipientName;

    @SerializedName("address")
    @Expose
    @NonNull private AddressCustomer address;


    @SerializedName("provider")
    @Expose
    @NonNull private Provider provider;
}
