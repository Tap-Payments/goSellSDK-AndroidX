package company.tap.gosellapi.internal.api.models;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public final  class Expiry implements Serializable {

        @SerializedName  ("period")
        @Expose
        private double period;

        @SerializedName("type")
        @Expose
        private String type;

    public double getPeriod() {
        return period;
    }

    public String getType() {
        return type;
    }
}

