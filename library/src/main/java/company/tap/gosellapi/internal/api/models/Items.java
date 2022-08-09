package company.tap.gosellapi.internal.api.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import company.tap.gosellapi.open.models.MetaData;

public class Items implements Serializable {

    @SerializedName("product_id")
    @Expose
    @NonNull
    private String product_id;

    @SerializedName("name")
    @Expose
    @NonNull
    private String name;

    @SerializedName("amount")
    @Expose
    @NonNull
    private String amount;


    @SerializedName("currency")
    @Expose
    @NonNull
    private String currency;

    @SerializedName("quantity")
    @Expose
    @NonNull
    private String quantity;

    @SerializedName("category")
    @Expose
    @NonNull
    private String category;

    @SerializedName("discount")
    @Expose
    @NonNull
    private Discount discount;

    @SerializedName("vendor")
    @Expose
    @NonNull
    private Vendor vendor;

    @SerializedName("fulfillment_service")
    @Expose
    @NonNull
    private String fulfillment_service;

    @SerializedName("requires_shipping")
    @Expose
    @NonNull
    private String requireShipping;

    @SerializedName("item_code")
    @Expose
    @NonNull
    private String item_code;

    @SerializedName("account_code")
    @Expose
    @NonNull
    private String account_code;

    @SerializedName("description")
    @Expose
    @NonNull
    private String description;


    @SerializedName("image")
    @Expose
    @NonNull
    private String image;

    @SerializedName("reference")
    @Expose
    @NonNull
    private ReferenceItem reference;

    @SerializedName("dimensions")
    @Expose
    @NonNull
    private ItemDimensions dimensions;


    @SerializedName("tags")
    @Expose
    @NonNull
    private String tags;

    @SerializedName("meta_data")
    @Expose
    @NonNull
    private MetaData metaData;


}
