package company.tap.gosellapi.internal.api.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Discount implements Serializable {
    @SerializedName("type")
    @Expose
    @NonNull
    private String type;

    @SerializedName("value")
    @Expose
    @NonNull
    private String value;
}
