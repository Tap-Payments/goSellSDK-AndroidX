package company.tap.gosellapi.internal.api.models;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * The type Tax.
 */
public final class Tax2 implements Serializable {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    @Nullable private String description;

    @SerializedName("rate")
    @Expose
    private AmountModificator amount;

    /**
     * Instantiates a new Tax.
     *
     * @param name        the name
     * @param description the description
     * @param amount      the amount
     */
    public Tax2(String name, @Nullable String description, AmountModificator amount) {
        this.name = name;
        this.description = description;
        this.amount = amount;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    @Nullable public String getDescription() {
        return description;
    }

    /**
     * Gets amount.
     *
     * @return the amount
     */
    public AmountModificator getAmount() {
        return amount;
    }
}
