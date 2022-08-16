package company.tap.gosellapi.open.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Description implements Serializable {

    @SerializedName("en")
    @Expose
    @NonNull
    private String en;
}
