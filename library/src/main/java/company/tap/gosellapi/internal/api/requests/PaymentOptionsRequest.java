package company.tap.gosellapi.internal.api.requests;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigDecimal;
import java.util.ArrayList;

import company.tap.gosellapi.internal.api.models.OrderObject;
import company.tap.gosellapi.internal.utils.AmountCalculator;
import company.tap.gosellapi.open.enums.TransactionMode;
import company.tap.gosellapi.open.models.PaymentItem;
import company.tap.gosellapi.open.models.Shipping;
import company.tap.gosellapi.open.models.Tax;
import company.tap.gosellapi.open.models.TopUp;

/**
 * The type Payment options request.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public final class PaymentOptionsRequest {



    @SerializedName("currency")
    @Expose
    @NonNull private String currency;

    @SerializedName("order")
    @Expose
    @NonNull private OrderObject orderObject;



    /**
     * Instantiates a new Payment options request.
     *

     * @param currency        the currency
     * @param orderObject        the orderObject
     */
    public PaymentOptionsRequest(
                                 @Nullable String currency,
                                 @Nullable OrderObject orderObject

    ) {

        this.currency           = currency;
        this.orderObject           = orderObject;

    }



    /**
     * Get payment option request info string.
     *
     * @return the string
     */
    public String getPaymentOptionRequestInfo(){
        return

            "currency : " + this.currency + " /n " +
            "order : " + this.orderObject + " /n "

        ;
    }
}
