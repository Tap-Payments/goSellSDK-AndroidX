package company.tap.gosellapi.internal.api.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReferenceItem implements Serializable {
    @SerializedName("SKU")
    @Expose
    @NonNull
    private String SKU;

    @SerializedName("GTIN")
    @Expose
    @NonNull
    private String GTIN;

}
