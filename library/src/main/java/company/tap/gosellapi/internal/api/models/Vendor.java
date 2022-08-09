package company.tap.gosellapi.internal.api.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Vendor implements Serializable {

    @SerializedName("id")
    @Expose
    @NonNull
    private String id;

    @SerializedName("name")
    @Expose
    @NonNull
    private String name;
}
