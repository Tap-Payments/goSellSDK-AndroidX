package company.tap.gosellapi.internal.api.models;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by AhlaamK on 1/14/21.
 * <p>
 * Copyright (c) 2021    Tap Payments.
 * All rights reserved.
 **/
public final class CardIssuer implements Serializable {

    @SerializedName("id")
    @Expose
    @Nullable
    private String id;

    @SerializedName("name")
    @Expose
    @Nullable private String name;

    @SerializedName("country")
    @Expose
    @Nullable  private String country;

    public String getName() {
        return name;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getCountry() {
        return country;
    }
}
