package company.tap.gosellapi.internal.api.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import company.tap.gosellapi.internal.api.responses.BaseResponse;

public final class OrderId implements BaseResponse {

    @SerializedName("id")
    @Expose
    @NonNull
    private String id;

}
