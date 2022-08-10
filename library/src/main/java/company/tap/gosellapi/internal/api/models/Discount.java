package company.tap.gosellapi.internal.api.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

    //  Constructor is private to prevent access from client app, it must be through inner Builder class only
    public Discount(@Nullable String type , @Nullable String value
    ) {

        this.type = type;
        this.value = value;


    }
}
