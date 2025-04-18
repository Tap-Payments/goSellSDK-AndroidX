package company.tap.gosellapi.internal.data_managers;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;

import com.google.gson.Gson;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

import company.tap.gosellapi.R;
import company.tap.gosellapi.internal.Constants;
import company.tap.gosellapi.internal.activities.BaseActivity;
import company.tap.gosellapi.internal.api.callbacks.APIRequestCallback;
import company.tap.gosellapi.internal.api.callbacks.GoSellError;
import company.tap.gosellapi.internal.api.enums.AuthenticationType;
import company.tap.gosellapi.internal.api.enums.ExtraFeesStatus;
import company.tap.gosellapi.internal.api.enums.PaymentType;
import company.tap.gosellapi.internal.api.enums.Permission;
import company.tap.gosellapi.internal.api.facade.GoSellAPI;
import company.tap.gosellapi.internal.api.models.AmountedCurrency;
import company.tap.gosellapi.internal.api.models.Authorize;
import company.tap.gosellapi.internal.api.models.CardIssuer;
import company.tap.gosellapi.internal.api.models.Charge;
import company.tap.gosellapi.internal.api.models.CreateTokenCard;
import company.tap.gosellapi.internal.api.models.CreateTokenSavedCard;
import company.tap.gosellapi.internal.api.models.ExtraFee;
import company.tap.gosellapi.internal.api.models.Merchant;
import company.tap.gosellapi.internal.api.models.Order;
import company.tap.gosellapi.internal.api.models.PaymentOption;
import company.tap.gosellapi.internal.api.models.SaveCard;
import company.tap.gosellapi.internal.api.models.SavedCard;
import company.tap.gosellapi.internal.api.models.SourceRequest;
import company.tap.gosellapi.internal.api.models.Token;
import company.tap.gosellapi.internal.api.models.TrackingURL;
import company.tap.gosellapi.internal.api.requests.CreateAuthorizeRequest;
import company.tap.gosellapi.internal.api.requests.CreateChargeRequest;
import company.tap.gosellapi.internal.api.requests.CreateOTPVerificationRequest;
import company.tap.gosellapi.internal.api.requests.CreateSaveCardRequest;
import company.tap.gosellapi.internal.api.requests.CreateTokenGPayRequest;
import company.tap.gosellapi.internal.api.requests.CreateTokenWithCardDataRequest;
import company.tap.gosellapi.internal.api.requests.CreateTokenWithExistingCardDataRequest;
import company.tap.gosellapi.internal.api.responses.DeleteCardResponse;
import company.tap.gosellapi.internal.data_managers.payment_options.PaymentOptionsDataManager;
import company.tap.gosellapi.internal.data_managers.payment_options.view_models.CardCredentialsViewModel;
import company.tap.gosellapi.internal.data_managers.payment_options.view_models.GooglePayViewModel;
import company.tap.gosellapi.internal.data_managers.payment_options.view_models.PaymentOptionViewModel;
import company.tap.gosellapi.internal.data_managers.payment_options.view_models.RecentSectionViewModel;
import company.tap.gosellapi.internal.data_managers.payment_options.view_models.WebPaymentViewModel;
import company.tap.gosellapi.internal.data_managers.payment_options.view_models_data.CardCredentialsViewModelData;
import company.tap.gosellapi.internal.interfaces.ICardDeleteListener;
import company.tap.gosellapi.internal.interfaces.IPaymentDataProvider;
import company.tap.gosellapi.internal.interfaces.IPaymentProcessListener;
import company.tap.gosellapi.internal.utils.AmountCalculator;
import company.tap.gosellapi.internal.utils.Utils;
import company.tap.gosellapi.open.enums.TransactionMode;
import company.tap.gosellapi.open.models.AuthorizeAction;
import company.tap.gosellapi.open.models.Customer;
import company.tap.gosellapi.open.models.Destinations;
import company.tap.gosellapi.open.models.Receipt;
import company.tap.gosellapi.open.models.Reference;
import company.tap.gosellapi.open.models.TopUp;

/**
 * The type Payment process manager.
 */
final class PaymentProcessManager {

    private final ICardDeleteListener cardDeleteListener;

    /**
     * Decision for web payment url payment data manager . web payment url decision.
     *
     * @param url the url
     * @return the payment data manager . web payment url decision
     */
    PaymentDataManager.WebPaymentURLDecision decisionForWebPaymentURL(String url) {

        boolean urlIsReturnURL = url.startsWith(Constants.RETURN_URL);
        boolean shouldLoad = !urlIsReturnURL;
        boolean redirectionFinished = urlIsReturnURL;
        boolean shouldCloseWebPaymentScreen = false;

        if (getCurrentPaymentViewModel().getPaymentOption() instanceof PaymentOption)
            shouldCloseWebPaymentScreen = redirectionFinished && ((PaymentOption) getCurrentPaymentViewModel()
                    .getPaymentOption()).getPaymentType() == PaymentType.CARD;

        if (getCurrentPaymentViewModel().getPaymentOption() instanceof CardCredentialsViewModelData)
            shouldCloseWebPaymentScreen = redirectionFinished;

        if (getCurrentPaymentViewModel().getPaymentOption() instanceof RecentSectionViewModel)
            shouldCloseWebPaymentScreen = redirectionFinished;

        Log.d("PaymentProcessManager", " shouldOverrideUrlLoading : shouldCloseWebPaymentScreen :" + shouldCloseWebPaymentScreen);
        @Nullable String tapID = null;

        Uri uri = Uri.parse(url);
        if (uri.getQueryParameterNames().contains(
                Constants.TAP_ID)) {  // if ReturnURL contains TAP_ID which means web flow finished then get TAP_ID and stop reloading web view with any urls

            tapID = uri.getQueryParameter(Constants.TAP_ID);
        }

        return PaymentDataManager.getInstance().new WebPaymentURLDecision(shouldLoad,
                shouldCloseWebPaymentScreen, redirectionFinished, tapID);
    }


    public void deleteCard(@NonNull String customerID, @NonNull String cardId) {
        callDeleteCardAPI(customerID, cardId);
    }

    /**
     * Check saved card payment extra fees.
     *
     * @param savedCard                  the saved card
     * @param paymentOptionsDataListener the payment options data listener
     */
    public void checkSavedCardPaymentExtraFees(SavedCard savedCard,
                                               PaymentOptionsDataManager.PaymentOptionsDataListener paymentOptionsDataListener) {
        PaymentOption paymentOption = findSavedCardPaymentOption(savedCard);
        checkPaymentExtraFees(paymentOption, paymentOptionsDataListener, PaymentType.SavedCard);

    }

    /**
     * Check payment extra fees.
     *
     * @param paymentOption              the payment option
     * @param paymentOptionsDataListener the payment options data listener
     * @param paymentType                the payment type
     */
    public void checkPaymentExtraFees(
            @NonNull final PaymentOption paymentOption,
            PaymentOptionsDataManager.PaymentOptionsDataListener paymentOptionsDataListener,
            PaymentType paymentType) {
//       Log.d("checkPaymentExtraFees"," step 2 : check extra fees : in class "+ "["+this.getClass().getName()+"] +  payment type: ["+paymentType.name()+"]");
        BigDecimal feesAmount = calculateExtraFeesAmount(paymentOption);
        fireExtraFeesDecision(feesAmount, paymentOptionsDataListener, paymentType);
    }

    private void showExtraFees(AmountedCurrency amount,
                               AmountedCurrency extraFeesAmount,
                               PaymentOptionsDataManager.PaymentOptionsDataListener paymentOptionsDataListener,
                               PaymentType paymentType
    ) {
//     Log.d("showExtraFees"," step 2 : show extra fees : in class "+ "["+this.getClass().getName()+"] +  PaymentType: ["+paymentType.name()+"]");
        showExtraFeesAlert(amount, extraFeesAmount, positiveButtonClicked -> {
            if (positiveButtonClicked) {
                if (paymentType == PaymentType.WEB) {
                    paymentOptionsDataListener
                            .fireWebPaymentExtraFeesUserDecision(ExtraFeesStatus.ACCEPT_EXTRA_FEES);
                } else if (paymentType == PaymentType.CARD) {

                    paymentOptionsDataListener
                            .fireCardPaymentExtraFeesUserDecision(ExtraFeesStatus.ACCEPT_EXTRA_FEES);
                } else if (paymentType == PaymentType.SavedCard) {
                    paymentOptionsDataListener
                            .fireSavedCardPaymentExtraFeesUserDecision(ExtraFeesStatus.ACCEPT_EXTRA_FEES);
                }
            } else {

                if (paymentType == PaymentType.WEB) {
                    paymentOptionsDataListener
                            .fireWebPaymentExtraFeesUserDecision(ExtraFeesStatus.REFUSE_EXTRA_FEES);
                } else if (paymentType == PaymentType.CARD) {
                    paymentOptionsDataListener
                            .fireCardPaymentExtraFeesUserDecision(ExtraFeesStatus.REFUSE_EXTRA_FEES);
                } else if (paymentType == PaymentType.SavedCard) {
                    paymentOptionsDataListener
                            .fireSavedCardPaymentExtraFeesUserDecision(ExtraFeesStatus.REFUSE_EXTRA_FEES);
                }
            }
        });
    }

    private PaymentOption findSavedCardPaymentOption(@NonNull SavedCard savedCard) {
        PaymentOption paymentOption = PaymentDataManager.getInstance().getPaymentOptionsDataManager()
                .findPaymentOption(savedCard.getPaymentOptionIdentifier());
        if (paymentOption != null)
            Log.d("PaymentProcessManager", "saved card payment name : " + paymentOption.getName());
        return paymentOption;
    }

    private void fireExtraFeesDecision(BigDecimal feesAmount,
                                       PaymentOptionsDataManager.PaymentOptionsDataListener paymentOptionsDataListener,
                                       PaymentType paymentType) {
//     Log.d("fireExtraFeesDecision"," step 2 : fire extra fees : in class "+ "["+this.getClass().getName()+"] +  fees amount: ["+feesAmount+"]");
        if (feesAmount.compareTo(BigDecimal.ZERO) == 1) {
//       Log.d("fireExtraFeesDecision"," step 2 : fire extra fees : in class "+ "["+this.getClass().getName()+"] +  there is  fees ");
            IPaymentDataProvider provider = getDataProvider();
            AmountedCurrency amount = provider.getSelectedCurrency();
            AmountedCurrency extraFeesAmount = new AmountedCurrency(amount.getCurrency(), feesAmount);
            showExtraFees(amount, extraFeesAmount, paymentOptionsDataListener, paymentType);
        } else {
//       Log.d("fireExtraFeesDecision"," step 2 : no extra fees : in class "+ "["+this.getClass().getName()+"] +  PaymentType: ["+paymentType.name()+"]");
            if (paymentType == PaymentType.WEB) {
                paymentOptionsDataListener
                        .fireWebPaymentExtraFeesUserDecision(ExtraFeesStatus.NO_EXTRA_FEES);
            } else if (paymentType == PaymentType.CARD) {
                paymentOptionsDataListener.fireCardPaymentExtraFeesUserDecision(ExtraFeesStatus.NO_EXTRA_FEES);
            } else if (paymentType == PaymentType.SavedCard) {
                paymentOptionsDataListener.fireSavedCardPaymentExtraFeesUserDecision(ExtraFeesStatus.NO_EXTRA_FEES);
            }
        }

    }


    /**
     * Calculate extra fees amount big decimal.
     *
     * @param paymentOption the payment option
     * @return the big decimal
     */
    public BigDecimal calculateExtraFeesAmount(PaymentOption paymentOption) {
        if (paymentOption != null) {
            IPaymentDataProvider provider = getDataProvider();
            AmountedCurrency amount = provider.getSelectedCurrency();
            ArrayList<ExtraFee> extraFees = paymentOption.getExtraFees();
            if (extraFees == null)
                extraFees = new ArrayList<>();
            ArrayList<AmountedCurrency> supportedCurrencies = provider.getSupportedCurrencies();
            BigDecimal feesAmount = AmountCalculator
                    .calculateExtraFeesAmount(extraFees, supportedCurrencies, amount);
            return feesAmount;
        } else
            return BigDecimal.ZERO;
    }

    /**
     * Calculate total amount string.
     *
     * @param feesAmount the fees amount
     * @return the string
     */
    public String calculateTotalAmount(BigDecimal feesAmount) {
        IPaymentDataProvider provider = getDataProvider();
        AmountedCurrency amount = provider.getSelectedCurrency();
        AmountedCurrency extraFeesAmount = new AmountedCurrency(amount.getCurrency(), feesAmount);
        AmountedCurrency totalAmount = new AmountedCurrency(amount.getCurrency(),
                amount.getAmount().add(extraFeesAmount.getAmount()), amount.getSymbol());
        String totalAmountText = Utils.getFormattedCurrency(totalAmount);
        return totalAmountText;
    }

    /**
     * Start payment process.
     *
     * @param paymentOptionModel the payment option model
     */
    void startPaymentProcess(@NonNull final PaymentOptionViewModel paymentOptionModel) {
//       Log.d("startPaymentProcess"," step 3 : startPaymentProcess : in class "+ "["+this.getClass().getName()+"]  ");
        forceStartPaymentProcess(paymentOptionModel,null);
    }


    /**
     * Start payment process.
     *
     * @param paymentOptionModel the payment option model
     */
    void startGooglePaymentProcess(@NonNull final PaymentOptionViewModel paymentOptionModel, @Nullable CreateTokenGPayRequest createTokenGPayRequest) {
//       Log.d("startPaymentProcess"," step 3 : startPaymentProcess : in class "+ "["+this.getClass().getName()+"]  ");
        forceStartPaymentProcess(paymentOptionModel ,createTokenGPayRequest);
    }

    /**
     * start card tokenization payment process
     *
     * @param paymentOptionModel
     */
    void startCardTokenization(@NonNull final PaymentOptionViewModel paymentOptionModel) {
        forceStartCardTokenizationProcess(paymentOptionModel);
    }

    /**
     * Start saved card payment process.
     *
     * @param paymentOptionModel     the payment option model
     * @param recentSectionViewModel the recent section view model
     */
    void startSavedCardPaymentProcess(@NonNull final SavedCard paymentOptionModel,
                                      RecentSectionViewModel recentSectionViewModel) {
        forceStartSavedCardPaymentProcess(paymentOptionModel, recentSectionViewModel);
    }

    /**
     * Instantiates a new Payment process manager.
     *
     * @param dataProvider       the data provider
     * @param listener           the listener
     * @param cardDeleteListener
     */
    PaymentProcessManager(@NonNull IPaymentDataProvider dataProvider,
                          @NonNull IPaymentProcessListener listener,
                          @NonNull ICardDeleteListener cardDeleteListener) {

        this.dataProvider = dataProvider;
        this.processListener = listener;
        this.cardDeleteListener = cardDeleteListener;
    }

    /**
     * Gets data provider.
     *
     * @return the data provider
     */
    @NonNull
    IPaymentDataProvider getDataProvider() {

        return dataProvider;
    }

    /**
     * Gets process listener.
     *
     * @return the process listener
     */
    @NonNull
    IPaymentProcessListener getProcessListener() {

        return processListener;
    }

    /**
     * @return
     */
    ICardDeleteListener getCardDeletListener() {
        return cardDeleteListener;
    }


    @Nullable
    private PaymentOptionViewModel currentPaymentViewModel;

    /**
     * Sets current payment view model.
     *
     * @param currentPaymentViewModel the current payment view model
     */
    public void setCurrentPaymentViewModel(
            @Nullable PaymentOptionViewModel currentPaymentViewModel) {
        this.currentPaymentViewModel = currentPaymentViewModel;
    }


    /**
     * Gets current payment view model.
     *
     * @return the current payment view model
     */
    @Nullable
    public PaymentOptionViewModel getCurrentPaymentViewModel() {
        return currentPaymentViewModel;
    }

    private IPaymentDataProvider dataProvider;
    private IPaymentProcessListener processListener;

    private void showExtraFeesAlert(AmountedCurrency amount, AmountedCurrency extraFeesAmount,
                                    DialogManager.DialogResult callback) {
        AmountedCurrency totalAmount = new AmountedCurrency(amount.getCurrency(),
                amount.getAmount().add(extraFeesAmount.getAmount()), amount.getSymbol());

//    String extraFeesText = CurrencyFormatter.format(extraFeesAmount);
//    String totalAmountText = CurrencyFormatter.format(totalAmount);
        String extraFeesText = Utils.getFormattedCurrency(extraFeesAmount);
        String totalAmountText = Utils.getFormattedCurrency(totalAmount);

        String title = "Confirm extra charges";
        String localizedMessage = "You will be charged an additional fee of %s for this type of payment, totaling an amount of %s";
        String confirm = "Confirm";
        String cancelled = "Cancel";

        if (BaseActivity.getCurrent() != null) {
            title = BaseActivity.getCurrent().getResources().getString(R.string.extra_fees_alert_confirm_message_title);
            localizedMessage = BaseActivity.getCurrent().getResources().getString(R.string.extra_fees_alert_message);
            confirm = BaseActivity.getCurrent().getResources().getString(R.string.extra_fees_alert_confirm_message_confirm);
            cancelled = BaseActivity.getCurrent().getResources().getString(R.string.extra_fees_alert_confirm_message_cancel);
        }

        String message = String.format(
                localizedMessage,
                extraFeesText, totalAmountText);

        DialogManager.getInstance().showDialog(title, message, confirm, cancelled, callback);
    }

    private void forceStartPaymentProcess(@NonNull PaymentOptionViewModel paymentOptionModel,@Nullable CreateTokenGPayRequest request) {

        Log.d("PaymentProcessManager",
                "paymentOptionModel instance of  :" + paymentOptionModel);
//     Log.d("forceStartPaymentP"," step 4 : forcePaymentProcess : in class "+ "["+this.getClass().getName()+"]  ");
        if (paymentOptionModel instanceof WebPaymentViewModel) {
            setCurrentPaymentViewModel(paymentOptionModel);
            startPaymentProcessWithWebPaymentModel((WebPaymentViewModel) paymentOptionModel);
        } else if (paymentOptionModel instanceof CardCredentialsViewModel) {
            setCurrentPaymentViewModel(paymentOptionModel);
            startPaymentProcessWithCardPaymentModel((CardCredentialsViewModel) paymentOptionModel);
        }else if (paymentOptionModel instanceof GooglePayViewModel) {
            setCurrentPaymentViewModel(paymentOptionModel);
            startGooglePayProcess((GooglePayViewModel) paymentOptionModel,request,null);
        }
    }


    private void forceStartCardTokenizationProcess(@NonNull PaymentOptionViewModel paymentOptionModel) {
        setCurrentPaymentViewModel(paymentOptionModel);
        startCardTokenizationPaymentProcessWithCardPaymentModel((CardCredentialsViewModel) paymentOptionModel);
    }


    private void startPaymentProcessWithWebPaymentModel(
            @NonNull WebPaymentViewModel paymentOptionModel) {

        PaymentOption paymentOption = paymentOptionModel.getPaymentOption();
      //  Log.d("PaymentProcessManager",
     //           "startPaymentProcessWithWebPaymentModel >>> paymentOption.getSourceId : " + paymentOption
     //                   .getSourceId());
        SourceRequest source = new SourceRequest(paymentOption.getSourceId());

        callChargeOrAuthorizeOrSaveCardAPI(source, paymentOption, null, null);
    }


    private void startPaymentProcessWithCardPaymentModel(
            @NonNull CardCredentialsViewModel paymentOptionModel) {


        @Nullable CreateTokenCard card = paymentOptionModel.getCard();
//     Log.d("startPaymentProcessWit"," step 4 : startPaymentProcessWithCardPaymentModel : in class "+ "["+this.getClass().getName()+"] with card=["+card+"]  ");
        if (card == null) {
            return;
        }
        startPaymentProcessWithCard(card,
                paymentOptionModel.getSelectedCardPaymentOption(),
                getSaveCardStatus(paymentOptionModel));
    }

    private boolean getSaveCardStatus(CardCredentialsViewModel paymentOptionModel) {

        if (PaymentDataManager.getInstance().getExternalDataSource() == null) return false;

        if (PaymentDataManager.getInstance().getSDKSettings() == null) return false;

        if (PaymentDataManager.getInstance().getSDKSettings().getData() == null) return false;

        if (PaymentDataManager.getInstance().getSDKSettings().getData().getPermissions() == null)
            return false;

        if (!PaymentDataManager.getInstance().getSDKSettings().getData().getPermissions().contains(Permission.MERCHANT_CHECKOUT))
            return false;


        if (PaymentDataManager.getInstance().getExternalDataSource().getTransactionMode() == TransactionMode.SAVE_CARD)
            return true; // always allow save card in case of trx_mode = TransactionMode.SAVE_CARD

        if (PaymentDataManager.getInstance().getExternalDataSource().getAllowedToSaveCard() && paymentOptionModel.shouldSaveCard())
            return true;

        return false;

    }

    private void startCardTokenizationPaymentProcessWithCardPaymentModel(
            @NonNull CardCredentialsViewModel paymentOptionModel) {

        @Nullable CreateTokenCard card = paymentOptionModel.getCard();
        if (card == null) {
            return;
        }

        startCardTokenizationPaymentProcessWithCard(card,
                paymentOptionModel.getSelectedCardPaymentOption(),
                false);

    }



    private void startPaymentProcessWithCard(@NonNull CreateTokenCard card,
                                             PaymentOption paymentOption, boolean saveCard) {
//     Log.d("startPaymentProcessWith"," step 4 : startPaymentProcessWithCard : in class "+ "["+this.getClass().getName()+"] with card=["+card+"]  ");

        CreateTokenWithCardDataRequest request = new CreateTokenWithCardDataRequest(card,PaymentDataManager.getInstance().getExternalDataSource().getMerchant());
//     Log.d("startPaymentProcessWith"," step 4 : startPaymentProcessWithCard>CreateTokenWithCardDataRequest : in class "+ "["+this.getClass().getName()+"] with request=["+request.toString()+"]  ");

        callTokenAPI(request, paymentOption, saveCard);
    }

    private void callTokenAPI(@NonNull CreateTokenWithCardDataRequest request,
                              @NonNull final PaymentOption paymentOption,
                              @Nullable final boolean saveCard) {

        GoSellAPI.getInstance().createTokenWithEncryptedCard(request, new APIRequestCallback<Token>() {
            boolean canUserSaveCard = saveCard;

            @Override
            public void onSuccess(int responseCode, Token serializedResponse) {
                // stop alerting user with card saved before and make it save = false.
                if (PaymentDataManager.getInstance().getPaymentOptionsRequest().getTransactionMode() == TransactionMode.SAVE_CARD
                        || saveCard) {
                    if (isCardSavedBefore(serializedResponse.getCard().getFingerprint())) {
//            fireCardSavedBeforeDialog();
//            return;
                       // Log.e("success",serializedResponse.toString());
                        canUserSaveCard = false;
                    }
                }
//         Log.d("callTokenAPI"," step 4 : startPaymentProcessWithCard>callTokenAPI : in class "+ "["+this.getClass().getName()+"] with token id=["+serializedResponse.getId()+"]  ");
                SourceRequest source = new SourceRequest(serializedResponse);
//         Log.d("callTokenAPI"," step 4 : startPaymentProcessWithCard>preparing charge request : in class "+ "["+this.getClass().getName()+"] with token type=["+serializedResponse.getType()+"]  ");
                callChargeOrAuthorizeOrSaveCardAPI(source, paymentOption, serializedResponse.getCard().getFirstSix(),
                        canUserSaveCard);
            }

            @Override
            public void onFailure(GoSellError errorDetails) {
                Log.d("PaymentProcessManager", "GoSellAPI.createToken : " + errorDetails.getErrorBody());
//         Log.d("callTokenAPI"," step 4 : startPaymentProcessWithCard>error: in class "+ "["+this.getClass().getName()+"] with token type=["+errorDetails.getErrorBody()+"]  ");
                closePaymentWithError(errorDetails);
            }
        });
    }


    private void startCardTokenizationPaymentProcessWithCard(@NonNull CreateTokenCard card,
                                                             PaymentOption paymentOption, boolean saveCard) {

        CreateTokenWithCardDataRequest request = new CreateTokenWithCardDataRequest(card,PaymentDataManager.getInstance().getExternalDataSource().getMerchant());

        callCardTokenizationTokenAPI(request, paymentOption, saveCard);
    }


    private void callCardTokenizationTokenAPI(@NonNull CreateTokenWithCardDataRequest request,
                                              @NonNull final PaymentOption paymentOption,
                                              @Nullable final boolean saveCard) {

        GoSellAPI.getInstance().createTokenWithEncryptedCard(request, new APIRequestCallback<Token>() {

            @Override
            public void onSuccess(int responseCode, Token serializedResponse) {
                fireCardTokenizationProcessCompleted(serializedResponse);

            }

            @Override
            public void onFailure(GoSellError errorDetails) {
                Log.d("PaymentProcessManager", "GoSellAPI.callCardTokenizationTokenAPI : " + errorDetails.getErrorBody());
                closePaymentWithError(errorDetails);
            }
        });
    }

    private void startGooglePayProcess( @NonNull GooglePayViewModel paymentOptionModel, @NonNull CreateTokenGPayRequest request,
                                                             PaymentOption paymentOption) {
        callGooglePayTokenizationTokenAPI(request, paymentOption);
    }

    private void callGooglePayTokenizationTokenAPI(@NonNull CreateTokenGPayRequest request,
                                              @NonNull final PaymentOption paymentOption) {

        GoSellAPI.getInstance().createTokenForGPay(request, new APIRequestCallback<Token>() {

            @Override
            public void onSuccess(int responseCode, Token serializedResponse) {
                SourceRequest source = new SourceRequest(serializedResponse);
                System.out.println("source google"+source);
                callChargeOrAuthorizeOrSaveCardAPI(source, paymentOption, serializedResponse.getCard().getFirstSix(),null);

            }

            @Override
            public void onFailure(GoSellError errorDetails) {
                Log.d("PaymentProcessManager", "GoSellAPI.callCardTokenizationTokenAPI : " + errorDetails.getErrorBody());
                closePaymentWithError(errorDetails);
            }
        });
    }

    private void fireCardTokenizationProcessCompleted(Token token) {

        PaymentDataManager.getInstance().fireCardTokenizationProcessCompleted(token);
    }

//  private void fireCardSavedBeforeDialog(){
//      String title ="";
//      String message = "";
//      if(BaseActivity.getCurrent()!=null) {
//       title =  BaseActivity.getCurrent().getResources().getString(R.string.save_card);
//        message =  BaseActivity.getCurrent().getResources().getString(R.string.card_saved_before);
//      }
//
//      DialogManager.getInstance().showDialog(title, message, "OK", null, new DialogManager.DialogResult() {
//          @Override
//          public void dialogClosed(boolean positiveButtonClicked) {
//              PaymentDataManager.getInstance().fireCardSavedBeforeListener();
//          }
//      });
//  }

    private boolean isCardSavedBefore(@NonNull String fingerprint) {
        if (PaymentDataManager.getInstance().getPaymentOptionsDataManager() == null) return false;
        return PaymentDataManager.getInstance().getPaymentOptionsDataManager().isCardSavedBefore(fingerprint);
    }

    /////////////////////////////////////////////////////////  Saved Card Payment process ////////////////////////////

    private void forceStartSavedCardPaymentProcess(@NonNull SavedCard savedCard,
                                                   RecentSectionViewModel recentSectionViewModel) {
        setCurrentPaymentViewModel(recentSectionViewModel);
        PaymentOption paymentOption = findPaymentOption(savedCard);
        CreateTokenSavedCard createTokenSavedCard = new CreateTokenSavedCard(savedCard.getId(), dataProvider.getCustomer().getIdentifier());
        startPaymentProcessWithSavedCardPaymentModel(createTokenSavedCard, paymentOption);
    }


    private void startPaymentProcessWithSavedCardPaymentModel(
            @NonNull CreateTokenSavedCard createTokenSavedCard, PaymentOption paymentOption) {
        CreateTokenWithExistingCardDataRequest request = new CreateTokenWithExistingCardDataRequest.Builder(createTokenSavedCard).build();
        callSavedCardTokenAPI(request, paymentOption, false);
    }


    private void callSavedCardTokenAPI(@NonNull CreateTokenWithExistingCardDataRequest request,
                                       @NonNull final PaymentOption paymentOption,
                                       @Nullable final boolean saveCard) {

        GoSellAPI.getInstance().createTokenWithExistingCard(request, new APIRequestCallback<Token>() {

            @Override
            public void onSuccess(int responseCode, Token serializedResponse) {
//        Log.d("PaymentProcessManager","startPaymentProcessWithSavedCard >> serializedResponse: " + serializedResponse);
                SourceRequest source = new SourceRequest(serializedResponse);
                callChargeOrAuthorizeOrSaveCardAPI(source, paymentOption, serializedResponse.getCard().getFirstSix(), saveCard);
            }

            @Override
            public void onFailure(GoSellError errorDetails) {
                Log.d("PaymentProcessManager", "GoSellAPI.callSavedCardTokenAPI : " + errorDetails.getErrorBody());
            }
        });
    }

    private void callDeleteCardAPI(@NonNull String customerId, @NonNull String cardId) {
        GoSellAPI.getInstance().deleteCard(customerId, cardId,
                new APIRequestCallback<DeleteCardResponse>() {
                    @Override
                    public void onSuccess(int responseCode, DeleteCardResponse serializedResponse) {
                        if (getCardDeletListener() != null)
                            getCardDeletListener().didCardDeleted(serializedResponse);
                    }

                    @Override
                    public void onFailure(GoSellError errorDetails) {
                        if (getCardDeletListener() != null)
                            getCardDeletListener().didDeleteCardReceiveError(errorDetails);
                    }
                });

    }

    private void closePaymentWithError(GoSellError goSellError) {
        handleChargeOrAuthorizeOrSaveCardResponse(null, goSellError);
    }

    private void callChargeOrAuthorizeOrSaveCardAPI(@NonNull SourceRequest source,
                                                    @NonNull PaymentOption paymentOption,
                                                    @Nullable String cardBIN, @Nullable Boolean saveCard) {

        Log.e("OkHttp", "CALL CHARGE API OR AUTHORIZE API");

        IPaymentDataProvider provider = getDataProvider();

        ArrayList<AmountedCurrency> supportedCurrencies = provider.getSupportedCurrencies();
        String orderID = provider.getPaymentOptionsOrderID();
//    Log.d("PaymentProcessManager","orderID : " + orderID);
//    Log.d("PaymentProcessManager","saveCard : " + saveCard);
        @Nullable String postURL = provider.getPostURL();
//    Log.d("PaymentProcessManager","postURL : " + postURL);
        @Nullable TrackingURL post = postURL == null ? null : new TrackingURL(postURL);

        AmountedCurrency amountedCurrency = provider.getSelectedCurrency();
       // Log.e("selectedCurrency currency ", "CALL CHARGE API " + amountedCurrency.getCurrency());
       // Log.e("selectedCurrency Amount", "CALL CHARGE API " + amountedCurrency.getAmount());

//     Log.d("callChargeOrAuthorizeOr"," step 5 : callChargeOrAuthorizeOrSaveCardAPI : in class "+ "["+this.getClass().getName()+"] with amountedCurrency=["+amountedCurrency.getAmount()+"]  ");

        AmountedCurrency transactionCurrency = provider.getTransactionCurrency();

        Customer customer = provider.getCustomer();

        BigDecimal fee = BigDecimal.ZERO;

        if (paymentOption != null)
            fee = AmountCalculator.calculateExtraFeesAmount(paymentOption.getExtraFees(), supportedCurrencies, amountedCurrency);
        Log.d("PaymentProcessManager", "fee : " + fee);
        Order order = new Order(orderID);

        TrackingURL redirect = new TrackingURL(Constants.RETURN_URL);
//     Log.d("callChargeOrAuthorizeOr"," step 5 : callChargeOrAuthorizeOrSaveCardAPI : in class "+ "["+this.getClass().getName()+"] redirect=["+redirect+"]  ");
        String paymentDescription = provider.getPaymentDescription();

        HashMap<String, String> paymentMetadata = provider.getPaymentMetadata();
        Reference reference = provider.getPaymentReference();
        boolean shouldSaveCard = saveCard == null ? false : saveCard;
        String statementDescriptor = provider.getPaymentStatementDescriptor();
        boolean require3DSecure = provider
                .getRequires3DSecure();// this.dataSource.getRequires3DSecure() || this.chargeRequires3DSecure();
        Receipt receipt = provider.getReceiptSettings();

        Destinations destinations = provider.getDestination();
        @Nullable Merchant merchant = provider.getMerchant();
        @Nullable CardIssuer cardIssuer = provider.getCardIssuer();
        @Nullable TopUp topUp = provider.getTopUp();

        TransactionMode transactionMode = provider.getTransactionMode();
        Log.d("PaymentProcessManager", "transactionMode : " + transactionMode);
//        Log.d("PaymentProcessManager", "topUp : " + topUp.toString());
        /**
         * Condition added for 3Ds for merchant
         *
         * */

        if(paymentOption!=null) {
            if (paymentOption.getThreeDS() != null) {
                if (paymentOption.getThreeDS().equals("N")) {
                    require3DSecure = false;
                } else if (paymentOption.getThreeDS().equals("Y")) {
                    require3DSecure = true;
                } else if (paymentOption.getThreeDS().equals("U")) {
                    require3DSecure = provider.getRequires3DSecure();
                }

            }
        }
        switch (transactionMode) {

            case PURCHASE:

                CreateChargeRequest chargeRequest = new CreateChargeRequest(
                        merchant,
                        transactionCurrency.getAmount() ,
                        transactionCurrency.getCurrency(),
                        amountedCurrency.getAmount(),
                        amountedCurrency.getCurrency(),
                        customer,
                        fee,
                        order,
                        redirect,
                        post,
                        source,
                        paymentDescription,
                        paymentMetadata,
                        reference,
                        shouldSaveCard,
                        statementDescriptor,
                        require3DSecure,
                        receipt,
                        destinations,
                        topUp
                );

                GoSellAPI.getInstance().createCharge(chargeRequest, new APIRequestCallback<Charge>() {
                    @Override
                    public void onSuccess(int responseCode, Charge serializedResponse) {
//             Log.d("callChargeOrAuthorizeOr"," step 5 : callChargeOrAuthorizeOrSaveCardAPI  >>> success : in class "+ "["+this.getClass().getName()+"] serializedResponse> post url=["+serializedResponse.getPost()+"]  ");
                        handleChargeOrAuthorizeOrSaveCardResponse(serializedResponse, null);
                    }

                    @Override
                    public void onFailure(GoSellError errorDetails) {
//             Log.d("callChargeOrAuthorizeOr"," step 5 : callChargeOrAuthorizeOrSaveCardAPI  >>> failer : in class "+ "["+this.getClass().getName()+"] error"+errorDetails.getErrorBody());
                        handleChargeOrAuthorizeOrSaveCardResponse(null, errorDetails);
                    }
                });

                break;

            case AUTHORIZE_CAPTURE:

                AuthorizeAction authorizeAction = provider.getAuthorizeAction();
               /* System.out.println(">>> ["+transactionCurrency.getAmount()+"]");
                System.out.println(">>> ["+transactionCurrency.getCurrency()+"]");
                System.out.println(">>> ["+amountedCurrency.getAmount()+"]");
                System.out.println(">>> ["+amountedCurrency.getCurrency()+"]");*/
                CreateAuthorizeRequest authorizeRequest = new CreateAuthorizeRequest(
                        merchant,
                        transactionCurrency.getAmount() ,
                        transactionCurrency.getCurrency(),
                        amountedCurrency.getAmount(),
                        amountedCurrency.getCurrency(),
                        customer,
                        fee,
                        order,
                        redirect,
                        post,
                        source,
                        paymentDescription,
                        paymentMetadata,
                        reference,
                        shouldSaveCard,
                        statementDescriptor,
                        require3DSecure,
                        receipt,
                        authorizeAction,
                        destinations,
                        null
                );
                GoSellAPI.getInstance()
                        .createAuthorize(authorizeRequest, new APIRequestCallback<Authorize>() {
                            @Override
                            public void onSuccess(int responseCode, Authorize serializedResponse) {
                                handleChargeOrAuthorizeOrSaveCardResponse(serializedResponse, null);
                            }

                            @Override
                            public void onFailure(GoSellError errorDetails) {

                                handleChargeOrAuthorizeOrSaveCardResponse(null, errorDetails);
                            }
                        });
                break;

            case SAVE_CARD:
                CreateSaveCardRequest saveCardRequest = new CreateSaveCardRequest(
                        amountedCurrency.getCurrency(),
                        customer,
                        order,
                        redirect,
                        post,
                        source,
                        paymentDescription,
                        paymentMetadata,
                        reference,
                        true,
                        statementDescriptor,
                        require3DSecure,
                        receipt,
                        true,
                        true,
                        true,
                        true,
                        true,
                       cardIssuer , merchant
                );

                GoSellAPI.getInstance().createSaveCard(saveCardRequest, new APIRequestCallback<SaveCard>() {
                    @Override
                    public void onSuccess(int responseCode, SaveCard serializedResponse) {

                        handleChargeOrAuthorizeOrSaveCardResponse(serializedResponse, null);
                    }

                    @Override
                    public void onFailure(GoSellError errorDetails) {

                        handleChargeOrAuthorizeOrSaveCardResponse(null, errorDetails);
                    }
                });

                break;
        }
    }

    private void handleChargeOrAuthorizeOrSaveCardResponse(@Nullable Charge chargeOrAuthorizeOrSave,
                                                           @Nullable GoSellError error) {
    // Log.d("handleChargeOrAuthorize"," step 5 : handleChargeOrAuthorizeOrSaveCardResponse  >>> success : in class "+ "["+this.getClass().getName()+"] chargeOrAuthorizeOrSave> status=["+chargeOrAuthorizeOrSave+"]  ");
        Gson gson = new Gson();
        String jsonCharge = gson.toJson(chargeOrAuthorizeOrSave);
       /// Log.e("Charge ",String.valueOf(jsonCharge));
        if (chargeOrAuthorizeOrSave != null) {

        Log.d("PaymentProcessManager","handleChargeOrAuthorizeResponse >>  chargeOrAuthorize : "+ chargeOrAuthorizeOrSave.getStatus());

            if (chargeOrAuthorizeOrSave instanceof Authorize) {
                getProcessListener().didReceiveAuthorize((Authorize) chargeOrAuthorizeOrSave);

            } else if (chargeOrAuthorizeOrSave instanceof SaveCard) {
                getProcessListener().didReceiveSaveCard((SaveCard) chargeOrAuthorizeOrSave);

            } else {

                getProcessListener().didReceiveCharge(chargeOrAuthorizeOrSave);
            }
        } else {
           // Log.d("PaymentProcessManager", "handleChargeOrAuthorizeResponse >>  error : " + error);
            String errorReturned = gson.toJson(error);
           // Log.e("error ",String.valueOf(errorReturned));
            getProcessListener().didReceiveError(error);
        }
        if(chargeOrAuthorizeOrSave!=null && chargeOrAuthorizeOrSave.getSource().getPaymentType()!=null && chargeOrAuthorizeOrSave.getSource().getPaymentType().equals(PaymentType.GOOGLE_PAY)){
            LoadingScreenManager.getInstance().closeLoadingScreen();

        }
    }

    /**
     * verify
     *
     * @param <T>                         the type parameter
     * @param chargeOrAuthorizeOrSaveCard the charge or authorize or save card
     */
    <T extends Charge> void retrieveChargeOrAuthorizeOrSaveCardAPI(T chargeOrAuthorizeOrSaveCard) {
//       Log.d("retrieveChargeOrAuthori"," step 8 : retrieveChargeOrAuthorizeOrSaveCardAPI : in class "+ "["+this.getClass().getName()+"]  ");
        APIRequestCallback<T> callBack = new APIRequestCallback<T>() {
            @Override
            public void onSuccess(int responseCode, T serializedResponse) {
//        Log.d("PaymentProcessManager"," retrieveChargeOrAuthorizeOrSaveCardAPI >>> " + responseCode);
//        if(serializedResponse!=null) Log.d("PaymentProcessManager"," retrieveChargeOrAuthorizeOrSaveCardAPI >>> " + serializedResponse.getId());
                handleChargeOrAuthorizeOrSaveCardResponse(serializedResponse, null);
            }

            @Override
            public void onFailure(GoSellError errorDetails) {
                if (errorDetails != null)
                    Log.d("PaymentProcessManager", "retrieveChargeOrAuthorizeOrSaveCardAPI : onFailure >>> " + errorDetails.getErrorBody());
            }
        };

        if (chargeOrAuthorizeOrSaveCard instanceof Authorize) {
            GoSellAPI.getInstance()
                    .retrieveAuthorize(chargeOrAuthorizeOrSaveCard.getId(), (APIRequestCallback<Authorize>) callBack);
            Log.d("PaymentProcessManager", "#################### Authorize 1 :" + chargeOrAuthorizeOrSaveCard.getId());
        } else if (chargeOrAuthorizeOrSaveCard instanceof SaveCard) {
            GoSellAPI.getInstance()
                    .retrieveSaveCard(chargeOrAuthorizeOrSaveCard.getId(), (APIRequestCallback<SaveCard>) callBack);
            Log.d("PaymentProcessManager", "#################### saveCardId 1 :" + chargeOrAuthorizeOrSaveCard.getId());
        } else {
            Log.d("PaymentProcessManager", "#################### charge 1 :" + chargeOrAuthorizeOrSaveCard.getId());
            GoSellAPI.getInstance()
                    .retrieveCharge(chargeOrAuthorizeOrSaveCard.getId(), (APIRequestCallback<Charge>) callBack);
        }
    }


    /**
     * Find payment option payment option.
     *
     * @param savedCard the saved card
     * @return the payment option
     */
    public PaymentOption findPaymentOption(SavedCard savedCard) {
        return findSavedCardPaymentOption(savedCard);
    }

    /**
     * Confirm charge otp code.
     *
     * @param charge  the charge
     * @param otpCode the otp code
     */
    void confirmChargeOTPCode(@NonNull Charge charge, String otpCode) {
        CreateOTPVerificationRequest createOTPVerificationRequest = new CreateOTPVerificationRequest.Builder(AuthenticationType.OTP, otpCode).build();
        APIRequestCallback<Charge> callBack = new APIRequestCallback<Charge>() {
            @Override
            public void onSuccess(int responseCode, Charge serializedResponse) {
                Log.d("PaymentProcessManager", " confirmChargeOTPCode >>> " + serializedResponse.getResponse().getMessage());
                handleChargeOrAuthorizeOrSaveCardResponse(serializedResponse, null);
            }

            @Override
            public void onFailure(GoSellError errorDetails) {
                Log.d("PaymentProcessManager", " confirmChargeOTPCode >>> error : " + errorDetails.getErrorBody());
                handleChargeOrAuthorizeOrSaveCardResponse(null, errorDetails);
            }
        };

        GoSellAPI.getInstance().authenticate_charge_transaction(charge.getId(), createOTPVerificationRequest, callBack);
    }


    /**
     * Confirm authorize otp code.
     *
     * @param authorize the authorize
     * @param otpCode   the otp code
     */
    void confirmAuthorizeOTPCode(@NonNull Authorize authorize, String otpCode) {
        CreateOTPVerificationRequest createOTPVerificationRequest = new CreateOTPVerificationRequest.Builder(AuthenticationType.OTP, otpCode).build();
        APIRequestCallback<Authorize> callBack = new APIRequestCallback<Authorize>() {
            @Override
            public void onSuccess(int responseCode, Authorize serializedResponse) {
                Log.d("PaymentProcessManager", " confirmAuthorizeOTPCode >>> " + serializedResponse.getResponse().getMessage());
                handleChargeOrAuthorizeOrSaveCardResponse(serializedResponse, null);
            }

            @Override
            public void onFailure(GoSellError errorDetails) {
                Log.d("PaymentProcessManager", " confirmAuthorizeOTPCode >>> error : " + errorDetails.getErrorBody());
                handleChargeOrAuthorizeOrSaveCardResponse(null, errorDetails);
            }
        };

        GoSellAPI.getInstance()
                .authenticate_authorize_transaction(authorize.getId(), createOTPVerificationRequest, callBack);

    }


    /**
     * Resend charge otp code.
     *
     * @param <T>    the type parameter
     * @param charge the charge
     */
    <T extends Charge> void resendChargeOTPCode(@NonNull Charge charge) {


        APIRequestCallback<Charge> callBack = new APIRequestCallback<Charge>() {
            @Override
            public void onSuccess(int responseCode, Charge serializedResponse) {
                Log.d("PaymentProcessManager", " resendChargeOTPCode >>> inside call back type " + serializedResponse.getClass());
                handleChargeOrAuthorizeOrSaveCardResponse(serializedResponse, null);
            }

            @Override
            public void onFailure(GoSellError errorDetails) {
                Log.d("PaymentProcessManager", " resendChargeOTPCode >>> error : " + errorDetails.getErrorBody());
                handleChargeOrAuthorizeOrSaveCardResponse(null, errorDetails);
            }
        };

        GoSellAPI.getInstance()
                .request_authenticate_for_charge_transaction(charge.getId(), callBack);

    }


    /**
     * Resend authorize otp code.
     *
     * @param authorize the authorize
     */
    void resendAuthorizeOTPCode(@NonNull Authorize authorize) {


        APIRequestCallback<Authorize> callBack = new APIRequestCallback<Authorize>() {
            @Override
            public void onSuccess(int responseCode, Authorize serializedResponse) {
                Log.d("PaymentProcessManager", " resendAuthorizeOTPCode >>> inside call back type " + serializedResponse.getClass());
                handleChargeOrAuthorizeOrSaveCardResponse(serializedResponse, null);
            }

            @Override
            public void onFailure(GoSellError errorDetails) {
                Log.d("PaymentProcessManager", " resendAuthorizeOTPCode >>> error : " + errorDetails.getErrorBody());
                handleChargeOrAuthorizeOrSaveCardResponse(null, errorDetails);
            }
        };

        GoSellAPI.getInstance()
                .request_authenticate_for_authorize_transaction(authorize.getId(), callBack);


    }
}
