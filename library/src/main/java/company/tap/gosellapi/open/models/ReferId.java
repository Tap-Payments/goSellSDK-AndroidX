package company.tap.gosellapi.open.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import company.tap.gosellapi.internal.api.enums.AmountModificatorType;

public class ReferId implements Serializable {

    @SerializedName("order")
    @Expose
    @Nullable
    private String order;
    public ReferId(@Nullable String order
    ) {

        this.order = order;

    }
}
