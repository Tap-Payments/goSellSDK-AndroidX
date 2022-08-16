package company.tap.gosellapi.open.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Provider implements Serializable {
    @SerializedName("id")
    @Expose
    @NonNull
    private String id;

    @SerializedName("name")
    @Expose
    @NonNull
    private String name;

    public Provider(@NonNull String id , @NonNull String name) {

        this.id = id;
        this.name = name;
    }
}
