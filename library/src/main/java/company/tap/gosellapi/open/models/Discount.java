/*
package company.tap.gosellapi.open.models;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

import company.tap.gosellapi.internal.api.enums.AmountModificatorType;

public class Discount implements Serializable {
    @SerializedName("type")
    @Expose
    @NonNull
    private AmountModificatorType type;



    @SerializedName("value")
    @Expose
    @NonNull
    private BigDecimal value;

    //  Constructor is private to prevent access from client app, it must be through inner Builder class only
    public Discount(@Nullable AmountModificatorType type , @Nullable BigDecimal value
    ) {

        this.type = type;
        this.value = value;


    }

    @NonNull
    public AmountModificatorType getType() {
        return type;
    }

    @NonNull
    public double getValue() {
        return value;
    }
}
*/
