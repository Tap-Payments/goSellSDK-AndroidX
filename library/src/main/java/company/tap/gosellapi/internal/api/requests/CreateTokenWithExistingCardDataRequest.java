package company.tap.gosellapi.internal.api.requests;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import company.tap.gosellapi.internal.api.models.CreateTokenSavedCard;
import company.tap.gosellapi.internal.api.models.Merchant;
import company.tap.gosellapi.internal.data_managers.PaymentDataManager;

/**
 * The type Create token with existing card data request.
 */
public final class CreateTokenWithExistingCardDataRequest {

    @SerializedName("saved_card")
    @Expose
    private CreateTokenSavedCard savedCard;

    @SerializedName("merchant")
    @Expose
    @Nullable
    private Merchant merchantId;

    private CreateTokenWithExistingCardDataRequest(CreateTokenSavedCard savedCard, @Nullable Merchant merchantId) {
        this.savedCard = savedCard;
        this.merchantId = merchantId;
    }

    /**
     * The type Builder.
     */
    public final static class Builder {
        private CreateTokenWithExistingCardDataRequest createTokenWithExistingCardDataRequest;

        /**
         * Instantiates a new Builder.
         *
         * @param card the card
         */
        public Builder(CreateTokenSavedCard card) {
            createTokenWithExistingCardDataRequest = new CreateTokenWithExistingCardDataRequest(card, PaymentDataManager.getInstance().getExternalDataSource().getMerchant());
        }

        /**
         * Build create token with existing card data request.
         *
         * @return the create token with existing card data request
         */
        public CreateTokenWithExistingCardDataRequest build() {
            return createTokenWithExistingCardDataRequest;
        }
    }
}
