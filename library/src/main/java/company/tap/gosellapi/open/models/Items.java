package company.tap.gosellapi.open.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

import company.tap.gosellapi.internal.api.models.AmountModificator;
import company.tap.gosellapi.internal.api.models.Vendor;
import company.tap.gosellapi.internal.utils.AmountCalculator;
import company.tap.gosellapi.open.enums.Category;

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
    private BigDecimal amount;


    @SerializedName("currency")
    @Expose
    @Nullable
    private String currency;

    @SerializedName("quantity")
    @Expose
    @Nullable
    private BigDecimal quantity;

    @SerializedName("category")
    @Expose
    @Nullable
    private Category category;

    @SerializedName("discount")
    @Expose
    @Nullable private AmountModificator discount;

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
    private boolean requireShipping;

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
    public Items(@Nullable String productId , String name, @Nullable BigDecimal amount, @Nullable String currency, @Nullable BigDecimal quantity , @Nullable Category category, @Nullable AmountModificator discount , @Nullable Vendor vendor, @Nullable String fulfillmentService, @Nullable boolean requireShipping, @Nullable String itemCode, @Nullable String accountCode,
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
    @Nullable
    public String getProductId() {
        return productId;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public BigDecimal getAmount() {
        return amount;
    }

    @Nullable
    public String getCurrency() {
        return currency;
    }

    @Nullable
    public BigDecimal getQuantity() {
        return quantity;
    }

    @Nullable
    public Category getCategory() {
        return category;
    }

    @Nullable
    public AmountModificator getDiscount() {
        return discount;
    }

    @Nullable
    public Vendor getVendor() {
        return vendor;
    }

    @Nullable
    public String getFulfillmentService() {
        return fulfillmentService;
    }

    public boolean isRequireShipping() {
        return requireShipping;
    }

    @Nullable
    public String getItemCode() {
        return itemCode;
    }

    @Nullable
    public String getAccountCode() {
        return accountCode;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @Nullable
    public String getImage() {
        return image;
    }

    @Nullable
    public ReferenceItem getReference() {
        return reference;
    }

    @Nullable
    public ItemDimensions getDimensions() {
        return dimensions;
    }

    @Nullable
    public String getTags() {
        return tags;
    }

    @Nullable
    public MetaData getMetaData() {
        return metaData;
    }



    /**
     * Gets plain amount.
     *
     * @return the plain amount
     */
    public BigDecimal getPlainAmount() {
        System.out.println("  #### getPlainAmount : " + this.amount );
        assert this.amount != null;
        return this.amount.multiply(quantity);
    }

    /**
     * Gets discount amount.
     *
     * @return the discount amount
     */
    public BigDecimal getDiscountAmount() {

        if (this.getDiscount() == null) {

            return BigDecimal.ZERO;
        }

        switch (this.getDiscount().getType()) {

            case PERCENTAGE:

                return this.getPlainAmount().multiply(this.getDiscount().getNormalizedValue());

            case FIXED:

                return this.getDiscount().getValue();

            default:
                return BigDecimal.ZERO;
        }
    }


}
