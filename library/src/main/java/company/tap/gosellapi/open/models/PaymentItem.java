package company.tap.gosellapi.open.models;

import static company.tap.gosellapi.internal.api.enums.measurements.Mass.KILOGRAMS;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.ArrayList;

import company.tap.gosellapi.internal.api.enums.measurements.Measurement;
import company.tap.gosellapi.internal.api.models.AmountModificator;
import company.tap.gosellapi.internal.api.models.Quantity;
import company.tap.gosellapi.internal.utils.AmountCalculator;

/**
 * The type Payment item.
 */
public class PaymentItem {

  @SerializedName("name")
  @Expose
  private String name;


  @SerializedName("description")
  @Expose
  @Nullable private String description;

  @SerializedName("quantity")
  @Expose
   private Quantity quantity;

  @SerializedName("amount_per_unit")
  @Expose
  private BigDecimal amountPerUnit;

  @SerializedName("discount")
  @Expose
  @Nullable private AmountModificator discount;

  @SerializedName("taxes")
  @Expose
  @Nullable private ArrayList<Tax> taxes;

  @SerializedName("total_amount")
  @Expose
  private BigDecimal totalAmount;

  /**
   * Constructor is private to prevent access from client app, it must be through inner Builder class only
   */
  private PaymentItem(String name,
                      @Nullable String description,
                      @Nullable Quantity quantity,
                      BigDecimal amountPerUnit,
                      @Nullable AmountModificator discount,
                      @Nullable ArrayList<Tax> taxes) {

    this.name = name;
    this.description = description;
    if(quantity == null  && amountPerUnit != null) {
      this.quantity = new Quantity(Measurement.MASS, KILOGRAMS, BigDecimal.ONE);

    } else if( quantity!=null && amountPerUnit == null){
        this.quantity =null;
        this.amountPerUnit =null;

    }else {
        this.quantity = quantity;
        this.amountPerUnit = amountPerUnit;
    }

   // this.amountPerUnit = amountPerUnit;

    this.discount = discount;
    this.taxes = taxes;
    if(quantity!=null && amountPerUnit!=null)this.totalAmount = AmountCalculator.calculateTotalAmountOf(this);

    System.out.println("calculated total amount : " + this.totalAmount);
  }

    /**
     * Gets amount per unit.
     *
     * @return the amount per unit
     */
    public BigDecimal getAmountPerUnit() {
    return this.amountPerUnit;
  }

    /**
     * Gets quantity.
     *
     * @return the quantity
     */
    public Quantity getQuantity() {
    return this.quantity;
  }

    /**
     * Gets discount.
     *
     * @return the discount
     */
    public AmountModificator getDiscount() {
    return this.discount;
  }

    /**
     * Gets plain amount.
     *
     * @return the plain amount
     */
    public BigDecimal getPlainAmount() {
   if(getAmountPerUnit()!=null) System.out.println("  #### getPlainAmount : " + this.getAmountPerUnit() );

   if(getQuantity()!=null) System.out.println("  #### this.getQuantity().getValue() : " + this.getQuantity().getValue() );
//    System.out.println("  #### result : " + this.getAmountPerUnit().multiply(this.getQuantity().getValue()) );

      if(getAmountPerUnit()!=null && getQuantity()!=null) return this.getAmountPerUnit().multiply(this.getQuantity().getValue());
      else if(getAmountPerUnit()!=null && getQuantity() == null) return getAmountPerUnit();
      else if(getAmountPerUnit()==null && getQuantity()!=null) return this.getQuantity().getValue();
      else return getAmountPerUnit();


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
       // if(getPlainAmount()!=null)
        return this.getPlainAmount().multiply(this.getDiscount().getNormalizedValue());

      case FIXED:

        return this.getDiscount().getValue();

      default:
        return BigDecimal.ZERO;
    }
  }

    /**
     * Gets taxes amount.
     *
     * @return the taxes amount
     */
    public BigDecimal getTaxesAmount() {
        BigDecimal taxationAmount = BigDecimal.ZERO;

    if(getPlainAmount()!=null)  taxationAmount= this.getPlainAmount().subtract(this.getDiscountAmount());


    return AmountCalculator.calculateTaxesOn(taxationAmount, this.taxes);
  }

    /**
     * The type Payment item builder.
     */
////////////////////////// ############################ Start of Builder Region ########################### ///////////////////////
  public static class PaymentItemBuilder {

    private String nestedName;
    @Nullable private String nestedDescription;
    private Quantity nestedQuantity;
    private BigDecimal nestedAmountPerUnit;
    @Nullable private AmountModificator nestedDiscount;
    @Nullable private ArrayList<Tax> nestedTaxes;
    private BigDecimal nestedTotalAmount;

        /**
         * public constructor with only required data
         *
         * @param name          the name
         * @param quantity      the quantity
         * @param amountPerUnit the amount per unit
         */
        public PaymentItemBuilder(String name,
                              @Nullable Quantity quantity,
                              @Nullable BigDecimal amountPerUnit) {
      this.nestedName = name;
      this.nestedQuantity = quantity;
      this.nestedAmountPerUnit = amountPerUnit;
    }

        /**
         * Description payment item builder.
         *
         * @param innerDescription the inner description
         * @return the payment item builder
         */
        public PaymentItemBuilder description(String innerDescription) {
      this.nestedDescription = innerDescription;
      return this;
    }

        /**
         * Discount payment item builder.
         *
         * @param innerDiscount the inner discount
         * @return the payment item builder
         */
        public PaymentItemBuilder discount(AmountModificator innerDiscount) {
      this.nestedDiscount = innerDiscount;
      return this;
    }

        /**
         * Taxes payment item builder.
         *
         * @param innerTaxes the inner taxes
         * @return the payment item builder
         */
        public PaymentItemBuilder taxes(ArrayList<Tax> innerTaxes) {
      this.nestedTaxes = innerTaxes;
      return this;
    }

        /**
         * Total amount payment item builder.
         *
         * @param innerTotalAmount the inner total amount
         * @return the payment item builder
         */
        public PaymentItemBuilder x(BigDecimal innerTotalAmount) {
      this.nestedTotalAmount = innerTotalAmount;
      return this;
    }

        /**
         * Build payment item.
         *
         * @return the payment item
         */
        public PaymentItem build() {
      return new PaymentItem(nestedName, nestedDescription, nestedQuantity, nestedAmountPerUnit,
          nestedDiscount, nestedTaxes);
    }
  }
  ////////////////////////// ############################ End of Builder Region ########################### ///////////////////////
}
