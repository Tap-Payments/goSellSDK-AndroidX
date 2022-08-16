package company.tap.gosellapi.open.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.ArrayList;

import company.tap.gosellapi.internal.api.models.Merchant;
import company.tap.gosellapi.internal.utils.AmountCalculator;

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
    @Nullable private ArrayList<Items> items;



    @SerializedName("tax")
    @Expose
    @Nullable private ArrayList<TaxObject> tax;

    @SerializedName("shipping")
    @Expose
    @Nullable private ArrayList<ShippingObject > shipping;

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
    public OrderObject(@NonNull BigDecimal amount , @NonNull String currency, @Nullable Customer customer, @Nullable ArrayList<Items> items,
                       @Nullable ArrayList<TaxObject> tax , @Nullable ArrayList<ShippingObject >shipping, @Nullable Merchant merchant , @Nullable MetaData metaData, @Nullable ReferId reference
    ) {

        this.amount = amount;
        this.currency = currency;
        this.customer = customer;
        if (items != null && items.size() > 0) {

            this.items          = items;
            this.amount    = AmountCalculator.calculateTotalAmountOfOrder(items, tax, shipping,this);
        }
        else {

            this.items = null;

            BigDecimal plainAmount = amount == null ? BigDecimal.ZERO : amount;
            this.amount = AmountCalculator.calculateTotalAmountOfOrder(new ArrayList<>(), tax, shipping,this).add(plainAmount);
        }
        this.tax = tax;
        this.shipping = shipping;
        this.merchant = merchant;
        this.metaData = metaData;
      
    }
    /**
     * Gets taxes amount.
     *
     * @return the taxes amount
     */
    public BigDecimal getTaxesAmount() {

        BigDecimal taxationAmount = amount.subtract(this.items.get(0).getDiscountAmount());

        return AmountCalculator.calculateTaxesOnItems(taxationAmount, this.tax);
    }

    @NonNull
    public BigDecimal getAmount() {
        return amount;
    }
}
