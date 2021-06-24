package company.tap.gosellapi.open.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MetaData{
    @SerializedName("udf1")
    @Expose
    private String udf1;
    @SerializedName("udf2")
    @Expose
    private String udf2;

    public MetaData(String udf1, String udf2){
        this.udf1 = udf1;
        this.udf2 = udf2;
    }
}
