package company.tap.gosellapi.internal.api.models;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * The type Authorize.
 */
public  class Authorize extends Charge implements Serializable {

    @SerializedName("auto")
    @Expose
    @NonNull private AuthorizeActionResponse autorizeAction;
}
