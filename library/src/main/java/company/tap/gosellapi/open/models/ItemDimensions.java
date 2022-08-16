package company.tap.gosellapi.open.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ItemDimensions implements Serializable {
    @SerializedName("weight_type")
    @Expose
    @Nullable
    private String weightType;

    @SerializedName("weight")
    @Expose
    @Nullable
    private Double weight;


    @SerializedName("measurements")
    @Expose
    @Nullable
    private String measurements;

    @SerializedName("length")
    @Expose
    @Nullable
    private Double length;

    @SerializedName("width")
    @Expose
    @Nullable
    private Double width;

    @SerializedName("height")
    @Expose
    @Nullable
    private Double height;

    public ItemDimensions(@Nullable String weightType , @Nullable Double weight, @Nullable String measurements, @Nullable Double length,
                     @Nullable Double width , @Nullable Double height

    ) {

        this.weightType = weightType;
        this.weight = weight;
        this.measurements = measurements;
        this. length = length;
        this.width = width;
        this.height = height;


    }

}
