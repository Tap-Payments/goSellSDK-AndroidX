package company.tap.gosellapi.internal.api.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

import company.tap.gosellapi.open.models.MetaData;
import company.tap.gosellapi.open.models.TopCustomerModel;
import company.tap.gosellapi.open.models.TopUpApplication;
import company.tap.gosellapi.open.models.TopUpReference;
import company.tap.gosellapi.open.models.TopchargeModel;
import company.tap.gosellapi.open.models.TopupPost;

public class Items implements Serializable {

    @SerializedName("product_id")
    @Expose
   @Nullable
    private String productId;

    @SerializedName("name")
    @Expose
    @Nullable
    private String name;

    @SerializedName("amount")
    @Expose
    @Nullable
    private String amount;


    @SerializedName("currency")
    @Expose
    @Nullable
    private String currency;

    @SerializedName("quantity")
    @Expose
    @Nullable
    private String quantity;

    @SerializedName("category")
    @Expose
    @Nullable
    private String category;

    @SerializedName("discount")
    @Expose
    @Nullable
    private Discount discount;

    @SerializedName("vendor")
    @Expose
    @Nullable
    private Vendor vendor;

    @SerializedName("fulfillment_service")
    @Expose
    @Nullable
    private String fulfillmentService;

    @SerializedName("requires_shipping")
    @Expose
    @Nullable
    private String requireShipping;

    @SerializedName("item_code")
    @Expose
    @Nullable
    private String itemCode;

    @SerializedName("account_code")
    @Expose
    @Nullable
    private String accountCode;

    @SerializedName("description")
    @Expose
    @Nullable
    private String description;


    @SerializedName("image")
    @Expose
    @Nullable
    private String image;

    @SerializedName("reference")
    @Expose
    @Nullable
    private ReferenceItem reference;

    @SerializedName("dimensions")
    @Expose
    @Nullable
    private ItemDimensions dimensions;


    @SerializedName("tags")
    @Expose
    @Nullable
    private String tags;

    @SerializedName("meta_data")
    @Expose
    @Nullable
    private MetaData metaData;

    //  Constructor is private to prevent access from client app, it must be through inner Builder class only
    public Items(@Nullable String productId , String name, @Nullable String amount, @Nullable String currency, @Nullable String quantity , @Nullable String category, @Nullable Discount discount , @Nullable Vendor vendor, @Nullable String fulfillmentService, @Nullable String requireShipping, @Nullable String itemCode, @Nullable String accountCode,
                 @Nullable String description, @Nullable String image,@Nullable ReferenceItem reference,@Nullable ItemDimensions dimensions,@Nullable String tags , @Nullable MetaData metaData
    ) {

        this.productId = productId;
        this.name = name;
        this.amount = amount;
        this. currency = currency;
        this.quantity = quantity;
        this.category = category;
        this.discount = discount;
        this.vendor = vendor;
        this.fulfillmentService = fulfillmentService;
        this.requireShipping = requireShipping;
        this.itemCode = itemCode;
        this.accountCode = accountCode;
        this.description = description;
        this.image = image;
        this.reference = reference;
        this.dimensions = dimensions;
        this.tags = tags;
        this.metaData = metaData;
    }


}
