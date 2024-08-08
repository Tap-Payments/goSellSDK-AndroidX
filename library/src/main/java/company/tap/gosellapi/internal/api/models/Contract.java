package company.tap.gosellapi.internal.api.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Contract implements Serializable {

    @SerializedName("id")
    @Expose
    @Nullable
    private String id;

    @SerializedName("customer_id")
    @Expose
    @Nullable
    private String customerId;


    @SerializedName("type")
    @Expose
    @Nullable
    private String type;


    @Nullable
    public String getId() {
        return id;
    }

    @Nullable
    public String getType() {
        return type;
    }

    @Nullable
    public String getCustomerId() {
        return customerId;
    }
}
