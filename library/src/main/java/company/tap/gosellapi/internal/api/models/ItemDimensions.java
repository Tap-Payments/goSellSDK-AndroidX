package company.tap.gosellapi.internal.api.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ItemDimensions implements Serializable {
    @SerializedName("weight_type")
    @Expose
    @NonNull
    private String weightType;

    @SerializedName("weight")
    @Expose
    @NonNull
    private Double weight;


    @SerializedName("measurements")
    @Expose
    @NonNull
    private String measurements;

    @SerializedName("length")
    @Expose
    @NonNull
    private Double length;

    @SerializedName("width")
    @Expose
    @NonNull
    private Double width;

    @SerializedName("height")
    @Expose
    @NonNull
    private Double height;

}
