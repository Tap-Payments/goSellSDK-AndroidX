package company.tap.gosellapi.open.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by AhlaamK on 7/7/21.
 * <p>
 * Copyright (c) 2021    Tap Payments.
 * All rights reserved.
 **/
public class TopupPost {
    @SerializedName("url")
    @Expose
    @Nullable
     String url;

    public TopupPost(String url ){
        this.url = url;

    }
    public String getUrl() {
        return url;
    }
}
