package company.tap.gosellapi.internal.api.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PaymentAgreement implements Serializable {
    @SerializedName("id")
    @Expose
    @Nullable
    private String id;

    @SerializedName("type")
    @Expose
    @Nullable
    private String type;


    @SerializedName("trace_id")
    @Expose
    @Nullable
    private String traceId;

    @SerializedName("total_payments_count")
    @Expose
    @Nullable
    private int totalPaymentsCount;

    @SerializedName("contract")
    @Expose
    @Nullable
    private Contract contract;


    @SerializedName("metadata")
    @Expose
    @Nullable
    private Map<String,String> metadata;


    /**
     * get PaymentAgreement ID
     * @return PaymentAgreement
     */
    @Nullable
    public String getId() {
        return id;
    } /**
     * get PaymentAgreement type
     * @return type
     */
    @Nullable
    public String getType() {
        return type;
    } /**
     * get PaymentAgreement traceId
     * @return PaymentAgreement
     */
    @Nullable
    public String getTraceId() {
        return traceId;
    } /**
     * get PaymentAgreement totalPaymentsCount
     * @return PaymentAgreement
     */
    @Nullable
    public int getTotalPaymentCount() {
        return totalPaymentsCount;
    }


    @Nullable
    public Contract getContract() {
        return contract;
    }
    @Nullable
    public Map<String,String>  getMetadata() {
        return metadata;
    }
}
