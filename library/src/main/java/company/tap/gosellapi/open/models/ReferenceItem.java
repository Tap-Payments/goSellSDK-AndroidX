package company.tap.gosellapi.open.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.math.BigDecimal;

import company.tap.gosellapi.open.models.Customer;
import company.tap.gosellapi.open.models.MetaData;

public class ReferenceItem implements Serializable {
    @SerializedName("SKU")
    @Expose
    @Nullable
    private String SKU;

    @SerializedName("GTIN")
    @Expose
    @Nullable
    private String GTIN;

    //  Constructor is private to prevent access from client app, it must be through inner Builder class only
    public ReferenceItem(@Nullable String SKU , @Nullable String GTIN
    ) {

        this.SKU = SKU;
        this.GTIN = GTIN;
       

    }

}
