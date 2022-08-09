package company.tap.gosellapi.internal.api.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ReferId implements Serializable {

    @SerializedName("order")
    @Expose
    @Nullable
    private String order;
}
