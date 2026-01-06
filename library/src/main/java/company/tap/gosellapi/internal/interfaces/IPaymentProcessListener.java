package company.tap.gosellapi.internal.interfaces;

import androidx.annotation.NonNull;

import company.tap.gosellapi.internal.api.callbacks.GoSellError;
import company.tap.gosellapi.internal.api.models.Authorize;
import company.tap.gosellapi.internal.api.models.Charge;
import company.tap.gosellapi.internal.api.models.SaveCard;
import company.tap.gosellapi.internal.api.models.Token;

/**
 * The interface Payment process listener.
 */
public interface IPaymentProcessListener {
    /**
     * Did receive charge.
     *
     * @param charge the charge
     */
    void didReceiveCharge(@NonNull  Charge charge);

    /**
     * Did receive authorize.
     *
     * @param authorize the authorize
     */
    void didReceiveAuthorize(@NonNull Authorize authorize);

    /**
     * Did receive error.
     *
     * @param error the error
     */
    void didReceiveError(@NonNull GoSellError error);

    /**
     * Did receive save card.
     *
     * @param saveCard the save card
     */
    void didReceiveSaveCard(@NonNull SaveCard saveCard);

    /**
     * Did card saved before.
     */
    void didCardSavedBefore();

    /**
     * Card Tokenization process completed
     * @param token
     */
    void fireCardTokenizationProcessCompleted(@NonNull Token token);


}