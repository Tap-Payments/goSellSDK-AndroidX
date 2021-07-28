package company.tap.gosellapi.open.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TopUpReference implements Serializable {
    public String getOrder() {
        return order;
    }

    public String getTransaction() {
        return transaction;
    }

    @SerializedName("order")
    @Expose
    private String order;
    @SerializedName("transaction")
    @Expose
    private String transaction;

    public TopUpReference(String order ,String transaction){
        this.order = order;
        this.transaction = transaction;
    }

}
