package company.tap.gosellapi.open.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Vendor implements Serializable {

    @SerializedName("id")
    @Expose
    @Nullable
    private String id;

    @SerializedName("name")
    @Expose
    @Nullable
    private String name;

    //  Constructor is private to prevent access from client app, it must be through inner Builder class only
    public Vendor(@Nullable String id , @Nullable String name
    ) {

        this.id = id;
        this.name = name;


    }
}
