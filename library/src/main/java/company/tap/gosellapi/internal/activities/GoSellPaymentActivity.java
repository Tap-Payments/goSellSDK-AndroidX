package company.tap.gosellapi.internal.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.TranslateAnimation;
import android.webkit.ClientCertRequest;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wallet.AutoResolveHelper;
import com.google.android.gms.wallet.PaymentData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;

import company.tap.gosellapi.R;
import company.tap.gosellapi.internal.api.callbacks.APIRequestCallback;
import company.tap.gosellapi.internal.api.callbacks.GoSellError;
import company.tap.gosellapi.internal.api.enums.AuthenticationStatus;
import company.tap.gosellapi.internal.api.enums.ChargeStatus;
import company.tap.gosellapi.internal.api.enums.ExtraFeesStatus;
import company.tap.gosellapi.internal.api.facade.GoSellAPI;
import company.tap.gosellapi.internal.api.models.AmountedCurrency;
import company.tap.gosellapi.internal.api.models.Authenticate;
import company.tap.gosellapi.internal.api.models.Authorize;
import company.tap.gosellapi.internal.api.models.Charge;
import company.tap.gosellapi.internal.api.models.PaymentOption;
import company.tap.gosellapi.internal.api.models.SaveCard;
import company.tap.gosellapi.internal.api.models.SavedCard;
import company.tap.gosellapi.internal.api.models.Token;
import company.tap.gosellapi.internal.api.requests.CreateTokenGPayRequest;
import company.tap.gosellapi.internal.api.responses.BINLookupResponse;
import company.tap.gosellapi.internal.api.responses.DeleteCardResponse;
import company.tap.gosellapi.internal.custom_views.OTPFullScreenDialog;
import company.tap.gosellapi.internal.data_managers.LoadingScreenManager;
import company.tap.gosellapi.internal.data_managers.PaymentDataManager;
import company.tap.gosellapi.internal.data_managers.payment_options.PaymentOptionsDataManager;
import company.tap.gosellapi.internal.data_managers.payment_options.view_models.CardCredentialsViewModel;
import company.tap.gosellapi.internal.data_managers.payment_options.view_models.GooglePayViewModel;
import company.tap.gosellapi.internal.data_managers.payment_options.view_models.GroupViewModel;
import company.tap.gosellapi.internal.data_managers.payment_options.view_models.RecentSectionViewModel;
import company.tap.gosellapi.internal.data_managers.payment_options.view_models.WebPaymentViewModel;
import company.tap.gosellapi.internal.fragments.GoSellPaymentOptionsFragment;
import company.tap.gosellapi.internal.interfaces.ICardDeleteListener;
import company.tap.gosellapi.internal.interfaces.IPaymentProcessListener;
import company.tap.gosellapi.internal.utils.ActivityDataExchanger;
import company.tap.gosellapi.internal.utils.Utils;
import company.tap.gosellapi.internal.viewholders.GooglePaymentViewHolder;
import company.tap.gosellapi.internal.viewholders.PaymentOptionsBaseViewHolder;
import company.tap.gosellapi.open.buttons.PayButtonView;
import company.tap.gosellapi.open.controllers.SDKSession;
import company.tap.gosellapi.open.controllers.ThemeObject;
import company.tap.gosellapi.open.enums.AppearanceMode;
import company.tap.gosellapi.open.enums.TransactionMode;
import io.card.payment.CardIOActivity;
import io.card.payment.CreditCard;

/**
 * The type Go sell payment activity.
 */
public class GoSellPaymentActivity extends BaseActivity implements PaymentOptionsDataManager.PaymentOptionsDataListener, IPaymentProcessListener, OTPFullScreenDialog.ConfirmOTP,
        ICardDeleteListener {
    private static final int SCAN_REQUEST_CODE = 123;
    private static final int CURRENCIES_REQUEST_CODE = 124;
    private static final int WEB_PAYMENT_REQUEST_CODE = 125;
    private static final int ASYNCHRONOUS_REQUEST_CODE = 126;

    private PaymentOptionsDataManager dataSource;
    private FragmentManager fragmentManager;

    private PayButtonView payButton;

    private CardCredentialsViewModel cardCredentialsViewModel;
    private RecentSectionViewModel recentSectionViewModel;
    private boolean saveCardChecked;
    private Charge chargeOrAuthorizeOrSaveCard;
    private SavedCard savedCard;
    private WebPaymentViewModel webPaymentViewModel;
    private GooglePayViewModel googlePayViewModel;

    private AppearanceMode apperanceMode;

    static int mAppHeight;
    static int currentOrientation = -1;

    private boolean keyboardVisible = false;

    private GroupViewModel groupViewModel;

    private boolean selectedCurrencyAsynchronous = false;

    private ScrollView main_windowed_scrollview;
    private static final String TAG = "GoSellPaymentActivity";

    // Arbitrarily-picked constant integer you define to track a request for payment data activity.
    private static final int LOAD_PAYMENT_DATA_REQUEST_CODE = 991;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.slide_in_top, android.R.anim.fade_out);

        apperanceMode = ThemeObject.getInstance().getAppearanceMode();

        if (apperanceMode == AppearanceMode.WINDOWED_MODE) {
            setContentView(R.layout.gosellapi_activity_main_windowed);
            main_windowed_scrollview = findViewById(R.id.main_windowed_scrollview);
        } else {
            setContentView(R.layout.gosellapi_activity_main);
        }


        fragmentManager = getSupportFragmentManager();
        /**
         *  PaymentOptionsDataManager >> is the main actor who decide layout content
         */
        dataSource = PaymentDataManager.getInstance().getPaymentOptionsDataManager(this);

        final FrameLayout fragmentContainer = findViewById(R.id.paymentActivityFragmentContainer);

        //Register a callback to be invoked when the global layout state or the visibility of views within the view tree changes
        fragmentContainer.getViewTreeObserver()
                .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        dataSource.setAvailableHeight(fragmentContainer.getHeight());

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
                            fragmentContainer.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        else
                            fragmentContainer.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                    }

                });

        initViews();
        SDKSession.getListener().sessionHasStarted();

        saveCardChecked = false;
//        setKeyboardVisibilityListener();


        if (recentSectionViewModel != null) recentSectionViewModel.EnableRecentView();
        if (webPaymentViewModel != null) webPaymentViewModel.enableWebView();
        PaymentDataManager.getInstance().setCardPaymentProcessStatus(false);
        if (cardCredentialsViewModel != null) cardCredentialsViewModel.enableCardScanView();
    }

    private void initViews() {
        GoSellPaymentOptionsFragment paymentOptionsFragment = GoSellPaymentOptionsFragment
                .newInstance(dataSource);

        fragmentManager
                .beginTransaction()
                .replace(R.id.paymentActivityFragmentContainer, paymentOptionsFragment, "CARD")
                .commit();

        setupHeader();

        payButton = findViewById(R.id.payButtonId);
        payButton.setEnabled(false);
        if (ThemeObject.getInstance().getPayButtonDisabledTitleColor() != 0)
            payButton.getPayButton().setTextColor(ThemeObject.getInstance().getPayButtonDisabledTitleColor());

        payButton.setOnClickListener(v -> {
            payButton.setEnabled(false);
            if (payButton.getLoadingView() != null) payButton.getLoadingView().start();

            // notify merchant with user decision about saving card

            if (cardCredentialsViewModel != null)
                SDKSession.getListener().userEnabledSaveCardOption(cardCredentialsViewModel.shouldSaveCard());



            boolean keyBoardHidden =  Utils.hideKeyboard(GoSellPaymentActivity.this);
            Log.d(TAG, "sKeyboard hidden .... after click pay button : "+keyBoardHidden );
            startPaymentWithTimer();


        });

        setupPayButton();
    }


    @Override
    public void onBackPressed() {
        SDKSession.getListener().sessionCancelled();
        if (recentSectionViewModel != null) recentSectionViewModel.EnableRecentView();
        if (webPaymentViewModel != null) webPaymentViewModel.enableWebView();
        PaymentDataManager.getInstance().setCardPaymentProcessStatus(false);
        if (cardCredentialsViewModel != null) cardCredentialsViewModel.enableCardScanView();
        super.onBackPressed();

    }

    private void setupHeader() {
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        TextView cancel_payment_text = findViewById(R.id.cancel_payment_icon);

        LinearLayout cancel_payment_ll = findViewById(R.id.cancel_payment);

        cancel_payment_ll.setOnClickListener(v -> onBackPressed());

        ////////////////////////////////////////////////////////////////////////////////////////////
        ImageView businessIcon = findViewById(R.id.businessIcon);
        TextView businessName = findViewById(R.id.businessName);
        String header_title = "";

        if (isTransactionModeSaveCard() || isTransactionModeTokenizeCard()) {
            header_title = getString(R.string.textview_disclaimer_save_card_header_title);

            LinearLayout businessIconNameContainer = findViewById(R.id.businessIconNameContainer);
            businessIconNameContainer.removeView(businessIcon);
            businessIconNameContainer.removeView(businessName);
            LinearLayout.LayoutParams ll = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ll.leftMargin = 0;
            ll.bottomMargin = 5;
            ll.topMargin = 18;
            ll.gravity = Gravity.CENTER_VERTICAL;
            businessName.setLayoutParams(ll);
            businessIconNameContainer.addView(businessName);

            cancel_payment_ll.removeView(cancel_payment_text);
            LinearLayout.LayoutParams ll2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ll2.leftMargin = 0;
            ll2.bottomMargin = 5;
            ll2.topMargin = 18;
            ll2.gravity = Gravity.CENTER_VERTICAL;
            cancel_payment_text.setLayoutParams(ll2);
            cancel_payment_ll.addView(cancel_payment_text);


        } else {
            String logoPath = "";
            if (PaymentDataManager.getInstance().getSDKSettings() != null &&
                    PaymentDataManager.getInstance().getSDKSettings().getData() != null &&
                    PaymentDataManager.getInstance().getSDKSettings().getData().getMerchant() != null
            ) {

                logoPath = PaymentDataManager.getInstance().getSDKSettings().getData().getMerchant().getLogo();

                if (!logoPath.equalsIgnoreCase("") && logoPath != null)
                    Glide.with(this).load(logoPath).apply(RequestOptions.circleCropTransform()).into(businessIcon);

                header_title = PaymentDataManager.getInstance().getSDKSettings().getData().getMerchant().getName();
            }
        }
        businessName.setText(header_title);


        if (ThemeObject.getInstance().getHeaderFont() != null)
            businessName.setTypeface(ThemeObject.getInstance().getHeaderFont());
        if (ThemeObject.getInstance().getHeaderTextColor() != 0)
            businessName.setTextColor(ThemeObject.getInstance().getHeaderTextColor());
        if (ThemeObject.getInstance().getHeaderTextSize() != 0)
            businessName.setTextSize(ThemeObject.getInstance().getHeaderTextSize());
        if (ThemeObject.getInstance().getHeaderBackgroundColor() != 0)
            toolbar.setBackgroundColor(ThemeObject.getInstance().getHeaderBackgroundColor());
    }

    private void setupPayButton() {
        if (ThemeObject.getInstance().getPayButtonTextSize() != 0)
            payButton.getPayButton().setTextSize(ThemeObject.getInstance().getPayButtonTextSize());
        if (ThemeObject.getInstance().isPayButtSecurityIconVisible())
            payButton.getSecurityIconView().setVisibility(ThemeObject.getInstance().isPayButtSecurityIconVisible() ? View.VISIBLE : View.INVISIBLE);
        if (ThemeObject.getInstance().isPayButtLoaderVisible())
            payButton.getLoadingView().setVisibility(ThemeObject.getInstance().isPayButtLoaderVisible() ? View.VISIBLE : View.INVISIBLE);

        if (isTransactionModeSaveCard() || isTransactionModeTokenizeCard()) {
            setupSaveCardMode();
        } else {
            setupChargeOrAuthorizeMode();
        }
    }

    private void setupChargeOrAuthorizeMode() {
        if (ThemeObject.getInstance().getPayButtonResourceId() != 0)
            payButton.setBackgroundSelector(ThemeObject.getInstance().getPayButtonResourceId());

        if (ThemeObject.getInstance().getPayButtonFont() != null)
            payButton.getPayButton().setTypeface(ThemeObject.getInstance().getPayButtonFont());
        if (ThemeObject.getInstance().getPayButtonDisabledTitleColor() != 0)
            payButton.getPayButton().setTextColor(ThemeObject.getInstance().getPayButtonDisabledTitleColor());

        if (ThemeObject.getInstance().getPayButtonText() != null){
            payButton.getPayButton().setText(String
                    .format("%s %s %s", getResources().getString(R.string.pay),
                            dataSource.getSelectedCurrency().getSymbol(),
                            dataSource.getSelectedCurrency().getAmount()));

        }else {

            if (dataSource.getSelectedCurrency() != null)
                payButton.getPayButton().setText(String
                        .format("%s %s %s", getResources().getString(R.string.pay),
                                dataSource.getSelectedCurrency().getSymbol(),
                                dataSource.getSelectedCurrency().getAmount()));
        }
    }

    private void setupSaveCardMode() {
        if (ThemeObject.getInstance().getPayButtonResourceId() != 0)
            payButton.setBackgroundSelector(ThemeObject.getInstance().getPayButtonResourceId());

        if (ThemeObject.getInstance().getPayButtonFont() != null)
            payButton.getPayButton().setTypeface(ThemeObject.getInstance().getPayButtonFont());

        if (ThemeObject.getInstance().getPayButtonDisabledTitleColor() != 0)
            payButton.getPayButton().setTextColor(ThemeObject.getInstance().getPayButtonDisabledTitleColor());

        if (isTransactionModeSaveCard()) {

            if (ThemeObject.getInstance().getPayButtonText() != null) {
                payButton.getPayButton().setText(ThemeObject.getInstance().getPayButtonText());

            }else
            payButton.getPayButton().setText(getResources().getString(R.string.save_card));
        }
        if (isTransactionModeTokenizeCard()){

            if (ThemeObject.getInstance().getPayButtonText() != null) {
                payButton.getPayButton().setText(ThemeObject.getInstance().getPayButtonText());

            }else
                payButton.getPayButton().setText(getResources().getString(R.string.tokenize));
        }


    }


    private boolean isTransactionModeSaveCard() {
        if(PaymentDataManager.getInstance().getPaymentOptionsRequest()!=null){
            return PaymentDataManager.getInstance().getPaymentOptionsRequest().getTransactionMode() == TransactionMode.SAVE_CARD;
        }else return false;
    }

    private boolean isTransactionModeTokenizeCard() {
        if(PaymentDataManager.getInstance().getPaymentOptionsRequest()!=null){
        return PaymentDataManager.getInstance().getPaymentOptionsRequest().getTransactionMode() == TransactionMode.TOKENIZE_CARD;
        }else return false;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////
    private void setSelectedCard(SavedCard recentItem) {
        this.savedCard = recentItem;
    }

    private SavedCard getSavedCard() {
        return this.savedCard;
    }

    //  ///////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void startCurrencySelection(ArrayList<AmountedCurrency> currencies,
                                       AmountedCurrency selectedCurrency) {
        Intent intent = new Intent(this, CurrenciesActivity.class);
        intent.putExtra(CurrenciesActivity.CURRENCIES_ACTIVITY_DATA, currencies);
        intent.putExtra(CurrenciesActivity.CURRENCIES_ACTIVITY_INITIAL_SELECTED_CURRENCY,
                selectedCurrency);

        startActivityForResult(intent, CURRENCIES_REQUEST_CODE);

        // custom animation like swapping from left to right
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left);
    }

    /**
     * start web payment activity
     */

    @Override
    public void startWebPayment(WebPaymentViewModel model) {

        if (model == null) return;

        this.webPaymentViewModel = model;

        if (model.getData() != null)
            selectedCurrencyAsynchronous = model.getData().isAsynchronous();

        PaymentDataManager.getInstance().checkWebPaymentExtraFees(model, this);
    }

    private void startWebPaymentProcess1() {
        if (selectedCurrencyAsynchronous) {
            PaymentDataManager.getInstance().initiatePayment(webPaymentViewModel, this);
            payButton.setEnabled(true);
            payButton.getLoadingView().start();

        } else {
            Intent intent = new Intent(this, WebPaymentActivity.class);
            ActivityDataExchanger.getInstance().setWebPaymentViewModel(webPaymentViewModel);
            startActivityForResult(intent, WEB_PAYMENT_REQUEST_CODE);
        }
        if (webPaymentViewModel != null) webPaymentViewModel.disableWebView();

    }

    @Override
    public void startScanCard() {
        Intent scanCard = new Intent(this, CardIOActivity.class);
        scanCard.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true); // default: false
        scanCard.putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, true); // default: false
        scanCard.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false); // default: false
        scanCard.putExtra(CardIOActivity.EXTRA_SUPPRESS_CONFIRMATION, true);
        scanCard.putExtra(CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME, true);
        scanCard.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, false);
        scanCard.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true);
        scanCard.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true);

        startActivityForResult(scanCard, SCAN_REQUEST_CODE);
    }

    private void startSavedCardPaymentProcess() {
        PaymentDataManager.getInstance().checkSavedCardPaymentExtraFees(getSavedCard(), this);

    }

    private void startCardPaymentProcess(CardCredentialsViewModel paymentOptionViewModel) {
        if (cardCredentialsViewModel != null) cardCredentialsViewModel.disableCardScanView();
        if (webPaymentViewModel != null) webPaymentViewModel.disableWebView();
        if (recentSectionViewModel != null) recentSectionViewModel.disableRecentView();
        PaymentDataManager.getInstance().setCardPaymentProcessStatus(true);
        if (PaymentDataManager.getInstance().getExternalDataSource().getTransactionMode() == TransactionMode.TOKENIZE_CARD)
            initCardTokenization();
        else
            PaymentDataManager.getInstance().checkCardPaymentExtraFees(paymentOptionViewModel, this);
    }

    ///////////////////////////////////////////////////  start function that initiate payment by creating charge --------------------------
    private void getVisibleViewModels() {
        for (int i = 0; i < PaymentDataManager.getInstance().getPaymentOptionsDataManager().getSize(); i++) {
            if (PaymentDataManager.getInstance().getPaymentOptionsDataManager().getViewModel(i) instanceof RecentSectionViewModel) {
                recentSectionViewModel = (RecentSectionViewModel) PaymentDataManager.getInstance().getPaymentOptionsDataManager().getViewModel(i);

            } else if (PaymentDataManager.getInstance().getPaymentOptionsDataManager().getViewModel(i) instanceof GroupViewModel) {
                groupViewModel = (GroupViewModel) PaymentDataManager.getInstance().getPaymentOptionsDataManager().getViewModel(i);

            } else if (PaymentDataManager.getInstance().getPaymentOptionsDataManager().getViewModel(i) instanceof WebPaymentViewModel) {
                webPaymentViewModel = (WebPaymentViewModel) PaymentDataManager.getInstance().getPaymentOptionsDataManager().getViewModel(i);

            } else if (PaymentDataManager.getInstance().getPaymentOptionsDataManager().getViewModel(i) instanceof CardCredentialsViewModel) {
                cardCredentialsViewModel = (CardCredentialsViewModel) PaymentDataManager.getInstance().getPaymentOptionsDataManager().getViewModel(i);

            }else if (PaymentDataManager.getInstance().getPaymentOptionsDataManager().getViewModel(i) instanceof GooglePayViewModel) {
                googlePayViewModel = (GooglePayViewModel) PaymentDataManager.getInstance().getPaymentOptionsDataManager().getViewModel(i);

            }
        }
    }

    private void initSavedCardPaymentProcess() {
        getVisibleViewModels();

        if (cardCredentialsViewModel != null) cardCredentialsViewModel.disableCardScanView();
        if (webPaymentViewModel != null) webPaymentViewModel.disableWebView();
        PaymentDataManager.getInstance().setCardPaymentProcessStatus(true);
        PaymentDataManager.getInstance()
                .initiateSavedCardPayment(getSavedCard(), recentSectionViewModel, this);
    }

    private void initCardPaymentProcess() {
        getVisibleViewModels();

        if (recentSectionViewModel != null) recentSectionViewModel.disableRecentView();
        if (cardCredentialsViewModel != null) cardCredentialsViewModel.disableCardScanView();
        if (webPaymentViewModel != null) webPaymentViewModel.disableWebView();
        PaymentDataManager.getInstance().setCardPaymentProcessStatus(true);
        PaymentDataManager.getInstance().initiatePayment(cardCredentialsViewModel, this);
    }

    private void initCardTokenization() {
        getVisibleViewModels();

        if (cardCredentialsViewModel != null) cardCredentialsViewModel.disableCardScanView();
        PaymentDataManager.getInstance().initCardTokenizationPayment(cardCredentialsViewModel, this);
    }

    ///////////////////////////////////////////////////  end of function that initiate payment by creating charge --------------------------

    @Override
    public void updatePayButtonWithExtraFees(PaymentOption paymentOption) {

        updatePayButtonWithFees(paymentOption);

    }

    @Override
    public void savedCardClickedForDeletion(@NonNull String cardId) {

        if (PaymentDataManager.getInstance().getExternalDataSource().getCustomer() == null)
            return;
        payButton.setEnabled(false);
        if (ThemeObject.getInstance().getPayButtonDisabledTitleColor() != 0)
            payButton.getPayButton().setTextColor(ThemeObject.getInstance().getPayButtonDisabledTitleColor());

        LoadingScreenManager.getInstance().showLoadingScreen(this);
        PaymentDataManager.getInstance().deleteCard(
                PaymentDataManager.getInstance().getExternalDataSource().getCustomer().getIdentifier(),
                cardId,
                this);
    }

    @Override
    public void disablePayButton() {
        payButton.setEnabled(false);
        if (ThemeObject.getInstance().getPayButtonDisabledTitleColor() != 0)
            payButton.getPayButton().setTextColor(ThemeObject.getInstance().getPayButtonDisabledTitleColor());
    }


    private void updatePayButtonWithFees(PaymentOption paymentOption) {

        BigDecimal feesAmount = PaymentDataManager.getInstance()
                .calculateCardExtraFees(paymentOption);

//        Log.d("GoSellPaymentActivity"," update pay button with : fees : " + feesAmount);

        if (isTransactionModeSaveCard() || isTransactionModeTokenizeCard()) return;
       if( ThemeObject.getInstance().getPayButtonText() != null){

           payButton.getPayButton().setText(
                   String.format("%s %s", ThemeObject.getInstance().getPayButtonText(),
                           PaymentDataManager.getInstance()
                                   .calculateTotalAmount(feesAmount)));
       }else
        payButton.getPayButton().setText(
                String.format("%s %s", getResources().getString(R.string.pay),
                        PaymentDataManager.getInstance()
                                .calculateTotalAmount(feesAmount)));
    }

    @Override
    public void updatePayButtonWithSavedCardExtraFees(SavedCard recentItem,
                                                      RecentSectionViewModel _recentSectionViewModel) {
        this.recentSectionViewModel = _recentSectionViewModel;
        getVisibleViewModels();


        setSelectedCard(recentItem);

        if (recentItem != null) {
            payButton.setEnabled(true);
            if (ThemeObject.getInstance().getPayButtonEnabledTitleColor() != 0)
                payButton.getPayButton().setTextColor(ThemeObject.getInstance().getPayButtonEnabledTitleColor());
            PaymentOption paymentOption = PaymentDataManager.getInstance()
                    .findSavedCardPaymentOption(recentItem);

            updatePayButtonWithFees(paymentOption);
        }
    }

    @Override
    public void cardExpirationDateClicked(CardCredentialsViewModel model) {

        String selectedMonth = null;
        String selectedYear = null;


        String modelExpirationMonth = model.getExpirationMonth();
        if (modelExpirationMonth != null && !modelExpirationMonth.isEmpty()) {

            selectedMonth = modelExpirationMonth;
        }

        String modelExpirationYear = model.getExpirationYear();
        if (modelExpirationYear != null && !modelExpirationYear.isEmpty()) {

            selectedYear = modelExpirationYear;
        }

       /* DatePicker.showInContext(this, selectedMonth, selectedYear,
                (month, year) -> dataSource.cardExpirationDateSelected(month, year));*/
    }


    @Override
    public void cardDetailsFilled(boolean isFilled,
                                  CardCredentialsViewModel _cardCredentialsViewModel) {
        setSelectedCard(null);
        cardCredentialsViewModel = _cardCredentialsViewModel;

        if (!isFilled && payButton.getLoadingView() != null && payButton.getLoadingView().isShown()) {
            payButton.getLoadingView().setForceStop(true);
        }

        if (isFilled)
            if (ThemeObject.getInstance().getPayButtonEnabledTitleColor() != 0)
                payButton.getPayButton().setTextColor(ThemeObject.getInstance().getPayButtonEnabledTitleColor());
            else if (ThemeObject.getInstance().getPayButtonDisabledTitleColor() != 0)
                payButton.getPayButton().setTextColor(ThemeObject.getInstance().getPayButtonDisabledTitleColor());

        payButton.setEnabled(isFilled);
    }


    @Override
    public void addressOnCardClicked() {
        BINLookupResponse binLookupResponse = PaymentDataManager.getInstance().getBinLookupResponse();
        if (binLookupResponse == null) return;
        Intent intent = new Intent(this, GoSellCardAddressActivity.class);
        intent.putExtra(GoSellCardAddressActivity.INTENT_EXTRA_KEY_COUNTRY,
                binLookupResponse.getCountry());
        startActivity(intent);
    }

    @Override
    public void saveCardSwitchClicked(boolean isChecked, int saveCardBlockPosition) {
        saveCardChecked = isChecked;
    }

    @Override
    public void binNumberEntered(String binNumber) {
//        Log.d("GoSellPaymentActivity"," binNumberEntered >>> binNumber:" + binNumber);
        GoSellAPI.getInstance()
                .retrieveBINLookupBINLookup(binNumber, new APIRequestCallback<BINLookupResponse>() {
                    @Override
                    public void onSuccess(int responseCode, BINLookupResponse serializedResponse) {
                        dataSource.showAddressOnCardCell(true);
                        PaymentDataManager.getInstance().setBinLookupResponse(serializedResponse);
                        dataSource.setCurrentBINData(serializedResponse);
                    }

                    @Override
                    public void onFailure(GoSellError errorDetails) {
                        dataSource.showAddressOnCardCell(false);
                        PaymentDataManager.getInstance().setBinLookupResponse(null);
                        dataSource.setCurrentBINData(null);
                    }
                });
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void fireWebPaymentExtraFeesUserDecision(ExtraFeesStatus choice) {
        switch (choice) {
            case NO_EXTRA_FEES:
            case ACCEPT_EXTRA_FEES:
                startWebPaymentProcess1();
                break;
            case REFUSE_EXTRA_FEES:
                LoadingScreenManager.getInstance().closeLoadingScreen();
                if (payButton != null && payButton.getLoadingView() != null)
                    payButton.getLoadingView().setForceStop(true);
                payButton.setEnabled(true);
                break;
        }
    }

    @Override
    public void fireCardPaymentExtraFeesUserDecision(ExtraFeesStatus userChoice) {
        if (payButton != null && payButton.getLoadingView() != null)
            payButton.getLoadingView().setForceStop(true);
        switch (userChoice) {
            case ACCEPT_EXTRA_FEES:
            case NO_EXTRA_FEES:
                initCardPaymentProcess();
                break;
            case REFUSE_EXTRA_FEES:
                LoadingScreenManager.getInstance().closeLoadingScreen();
                payButton.setEnabled(true);
                break;
        }
    }

    @Override
    public void fireSavedCardPaymentExtraFeesUserDecision(ExtraFeesStatus userChoice) {
        if (payButton != null && payButton.getLoadingView() != null)
            payButton.getLoadingView().setForceStop(true);
        switch (userChoice) {
            case ACCEPT_EXTRA_FEES:
            case NO_EXTRA_FEES:
                initSavedCardPaymentProcess();
                break;
            case REFUSE_EXTRA_FEES:
                LoadingScreenManager.getInstance().closeLoadingScreen();
                payButton.setEnabled(true);
                break;
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////

    private void openOTPScreen(Charge charge) {
        stopPayButtonLoadingView();
        if (charge.getAuthenticate() != null) {
            String phoneNumber = charge.getAuthenticate().getValue();
            FragmentTransaction ft = getSupportFragmentManager()
                    .beginTransaction();
            DialogFragment dialogFragment = new OTPFullScreenDialog();
            Bundle b = new Bundle();
            b.putString("phoneNumber", phoneNumber);
            dialogFragment.setArguments(b);
            ft.add(dialogFragment, OTPFullScreenDialog.TAG);
            ft.commitAllowingStateLoss();
        } else {
            closePaymentActivity();
        }
    }

    @Override
    public void confirmOTP() {
        LoadingScreenManager.getInstance().showLoadingScreen(GoSellPaymentActivity.this);

        Fragment fragment = getSupportFragmentManager().findFragmentByTag(OTPFullScreenDialog.TAG);
        if (fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    @Override
    public void resendOTP() {
        LoadingScreenManager.getInstance().showLoadingScreen(GoSellPaymentActivity.this);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(OTPFullScreenDialog.TAG);
        if (fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case SCAN_REQUEST_CODE:
                if (data != null && data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)) {
                    CreditCard scanResult = data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT);
                    dataSource.cardScanned(scanResult);
                }
                break;

            case CURRENCIES_REQUEST_CODE:
                if (data == null) break;
                AmountedCurrency userChoiceCurrency = (AmountedCurrency) data
                        .getSerializableExtra(CurrenciesActivity.CURRENCIES_ACTIVITY_USER_CHOICE_CURRENCY);
                if (userChoiceCurrency != null) {
                    stopPayButtonLoadingView();
                   if(ThemeObject.getInstance().getPayButtonText()!=null){
                       payButton.getPayButton().setText(
                               String.format("%s %s%s", ThemeObject.getInstance().getPayButtonText(), userChoiceCurrency
                                       .getSymbol(), userChoiceCurrency.getAmount()));
                   }else{
                       payButton.getPayButton().setText(
                               String.format("%s %s%s", getResources().getString(R.string.pay), userChoiceCurrency
                                       .getSymbol(), userChoiceCurrency.getAmount()));
                   }
                   updateDisplayedCards(userChoiceCurrency);
                }
                break;

            case WEB_PAYMENT_REQUEST_CODE:
                if (data == null) break;
                if (resultCode == RESULT_OK) {
//                    Log.d("GoSellPaymentActivity","data coming after closing WebPaymentActivity :"+data.getSerializableExtra("charge"));
                    if (data.getSerializableExtra("charge") != null) {
                        Charge charge = (Charge) data.getSerializableExtra("charge");
                        if (charge != null) {
                            fireWebPaymentCallBack(charge);
                        } else {
                            closePaymentActivity();
                            SDKSession.getListener().sdkError(null);
                        }
                    } else {
                        closePaymentActivity();
                        SDKSession.getListener().sdkError(null);
                    }
                } else if (resultCode == RESULT_CANCELED) {
//                    Log.d("GoSellPaymentActivity","data coming after closing WebPaymentActivity :"+data.getSerializableExtra("error"));
                    if (data.getSerializableExtra("error") != null) {
                        GoSellError goSellError = (GoSellError) data.getSerializableExtra("error");
                        if (goSellError != null) {
                            closePaymentActivity();
                            SDKSession.getListener().sdkError(goSellError);
                        } else {
                            closePaymentActivity();
                            SDKSession.getListener().sdkError(null);
                        }
                    } else {
                        closePaymentActivity();
                        SDKSession.getListener().sdkError(null);
                    }
                }
                break;
            case ASYNCHRONOUS_REQUEST_CODE:
                stopPayButtonLoadingView();
                payButton.setEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // selectedCurrencyAsynchronous=false;
                        finish();

                    }
                }, 1000);

                break;
/**
 * Handling of received GooglePayLoad
**/
            case LOAD_PAYMENT_DATA_REQUEST_CODE:
                switch (resultCode) {

                    case Activity.RESULT_OK:
                        PaymentData paymentData = PaymentData.getFromIntent(data);
                        if(paymentData!=null){
                            handlePaymentSuccess(paymentData);

                        }
                        break;

                    case Activity.RESULT_CANCELED:
                        // The user cancelled the payment attempt
                        try {
                            closePaymentActivity();
                            SDKSession.getListener().sessionCancelled();
                        } catch (Exception e) {
                            closePaymentActivity();
                        }
                        break;

                    case AutoResolveHelper.RESULT_ERROR:
                        Status status = AutoResolveHelper.getStatusFromIntent(data);
                        if(status!=null){
                            System.out.println("status values are>>"+status!=null ?status.getStatusMessage():status + " >> code "+status.getStatusCode());
                            handleError(status);
                        }
                        break;
                }

                // Re-enables the Google Pay payment button.
              //  googlePayButton.setClickable(true);
                break;

        }
    }

    private void fireWebPaymentCallBack(Charge charge) {
        switch (charge.getStatus()) {
            case CAPTURED:
            case AUTHORIZED:
                try {
                    closePaymentActivity();
                    SDKSession.getListener().paymentSucceed(charge);
                } catch (Exception e) {
                    Log.d("GoSellPaymentActivity", " Error while calling fireWebPaymentCallBack >>> method paymentSucceed(charge)");
                    closePaymentActivity();
                }
                break;
            case FAILED:
            case ABANDONED:
            case CANCELLED:
            case DECLINED:
            case RESTRICTED:
            case UNKNOWN:
            case TIMEDOUT:
                try {
                    closePaymentActivity();
                    SDKSession.getListener().paymentFailed(charge);
                } catch (Exception e) {
                    Log.d("GoSellPaymentActivity", " Error while calling fireWebPaymentCallBack >>> method paymentFailed(charge)");
                    closePaymentActivity();
                }
                break;

        }
    }

    /**
     * @param amountedCurrency this method will be called after user changes currency
     */
    private void updateDisplayedCards(AmountedCurrency amountedCurrency) {
        // filter views
        dataSource.currencySelectedByUser(amountedCurrency);
        // refresh layout [ filter view models according to new currency - reload views ]
        initViews();
        // update currency section
        dataSource.updateCurrencySection();
    }


    private void closePaymentActivity() {
        clearPaymentProcessListeners();
        finishActivity();
    }


    private void finishActivity() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(OTPFullScreenDialog.TAG);
        if (fragment != null)
            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        LoadingScreenManager.getInstance().closeLoadingScreen();
        closeActivity();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.slide_out_bottom);

    }


    /////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void didReceiveCharge(Charge charge) {

        if (charge == null) return;
        Log.e("Charge status", String.valueOf(charge.getStatus()));
        switch (charge.getStatus()) {
            case INITIATED:
                Authenticate authenticate = charge.getAuthenticate();
                if (authenticate != null && authenticate.getStatus() == AuthenticationStatus.INITIATED) {
                    switch (authenticate.getType()) {
                        case BIOMETRICS:

                            break;

                        case OTP:
                            Log.d("GoSellPaymentActivity", " coming charge type is ...  caller didReceiveCharge");
                            PaymentDataManager.getInstance().setChargeOrAuthorize(charge);
                            openOTPScreen(charge);
                            break;
                    }
                }
                break;
            case CAPTURED:
            case AUTHORIZED:
                try {
                    Log.d("didReceiveCharge", "payment succeeded ................................");
                    closePaymentActivity();
                    SDKSession.getListener().paymentSucceed(charge);
                } catch (Exception e) {
                    closePaymentActivity();
                    Log.d("GoSellPaymentActivity", " Error while calling delegate method paymentSucceed(charge)");
                }
                break;
            case FAILED:
            case ABANDONED:
            case CANCELLED:
            case DECLINED:
            case RESTRICTED:
            case UNKNOWN:
            case TIMEDOUT:
                try {
                    Log.d("didReceiveCharge", "payment failed...................................");
                    closePaymentActivity();
                    SDKSession.getListener().paymentFailed(charge);
                } catch (Exception e) {
                    Log.d("GoSellPaymentActivity", " Error while calling delegate method paymentFailed(charge)");
                    closePaymentActivity();
                }
                break;
            case IN_PROGRESS:
                if (charge.getTransaction() != null && charge.getTransaction().isAsynchronous())
                    if (main_windowed_scrollview != null) {
                        RelativeLayout.LayoutParams layoutParams =
                                new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                        main_windowed_scrollview.setLayoutParams(layoutParams);
                        TranslateAnimation animate = new TranslateAnimation(0, 0, main_windowed_scrollview.getHeight(), 0);
                        animate.setDuration(500);
                        main_windowed_scrollview.startAnimation(animate);

                    }
                PaymentDataManager.getInstance().setChargeOrAuthorize(charge);
                clearPaymentProcessListeners();
                selectedCurrencyAsynchronous = false;
                if (webPaymentViewModel != null) webPaymentViewModel.enableWebView();
                new Handler().postDelayed(() -> openAsyncActivity(), 800);
                break;
        }
        obtainPaymentURLFromChargeOrAuthorizeOrSaveCard(charge);

    }

    private void openAsyncActivity() {
        payButton.setEnabled(false);
        stopPayButtonLoadingView();
        Intent intent = new Intent(this, AsynchronousPaymentActivity.class);
        startActivityForResult(intent, ASYNCHRONOUS_REQUEST_CODE);
        overridePendingTransition(R.anim.slide_in_top, 0);
    }


    @Override
    public void didReceiveSaveCard(SaveCard saveCard) {
        // Log.d("GoSellPaymentActivity"," Cards >> didReceiveSaveCard * * * " + saveCard);
        if (saveCard == null) return;
        //   Log.d("GoSellPaymentActivity"," Cards >> didReceiveSaveCard * * * status :" + saveCard.getStatus());

        switch (saveCard.getStatus()) {
            case INITIATED:
                Authenticate authenticate = saveCard.getAuthenticate();
                if (authenticate != null && authenticate.getStatus() == AuthenticationStatus.INITIATED) {
                    switch (authenticate.getType()) {
                        case BIOMETRICS:

                            break;

                        case OTP:
                            Log.d("GoSellPaymentActivity", " start otp for save card mode........");
                            PaymentDataManager.getInstance().setChargeOrAuthorize(saveCard);
                            openOTPScreen(saveCard);
                            break;
                    }
                }
                break;
            case CAPTURED:
            case AUTHORIZED:
            case VALID:
                try {
                    closePaymentActivity();
                    SDKSession.getListener().cardSaved(saveCard);
                } catch (Exception e) {
                    Log.d("GoSellPaymentActivity", " Error while calling delegate method cardSaved(saveCard)");
                    closePaymentActivity();
                }
                break;
            case INVALID:
            case FAILED:
            case ABANDONED:
            case CANCELLED:
            case DECLINED:
            case RESTRICTED:
                try {
                    closePaymentActivity();
                    SDKSession.getListener().cardSavingFailed(saveCard);
                } catch (Exception e) {
                    Log.d("GoSellPaymentActivity", " Error while calling delegate method cardSavingFailed(saveCard)");
                    closePaymentActivity();
                }
                break;
        }
        obtainPaymentURLFromChargeOrAuthorizeOrSaveCard(saveCard);
    }

    @Override
    public void didCardSavedBefore() {
        Log.d("GoSellPaymentActivity", " card already saved before ....");
        stopPayButtonLoadingView();

    }

    @Override
    public void fireCardTokenizationProcessCompleted(Token token) {
        closePaymentActivity();
        SDKSession.getListener().cardTokenizedSuccessfully(token);
        if(cardCredentialsViewModel!=null)
        SDKSession.getListener().cardTokenizedSuccessfully(token,cardCredentialsViewModel.shouldSaveCard());
    }


    private void obtainPaymentURLFromChargeOrAuthorizeOrSaveCard(Charge chargeOrAuthorizeOrSaveCard) {
        if (chargeOrAuthorizeOrSaveCard.getStatus() != ChargeStatus.INITIATED) {
            return;
        }

        Authenticate authentication = chargeOrAuthorizeOrSaveCard.getAuthenticate();
        if (authentication != null)
            if (authentication != null && authentication.getStatus() == AuthenticationStatus.INITIATED) {
                return;
            }

        String url = chargeOrAuthorizeOrSaveCard.getTransaction().getUrl();

        if (url != null) {
            setChargeOrAuthorizeOrSaveCard(chargeOrAuthorizeOrSaveCard);
            LoadingScreenManager.getInstance().closeLoadingScreen();
            showWebView(chargeOrAuthorizeOrSaveCard.getTransaction().getUrl());
        }
    }

    private void showWebView(String url) {
        RelativeLayout popup_window = new RelativeLayout(this);
        FrameLayout.LayoutParams fl = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.FILL_PARENT,
                FrameLayout.LayoutParams.FILL_PARENT);
        popup_window.setLayoutParams(fl);
        WebView w = new WebView(this);
        w.setScrollContainer(false);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
        w.setLayoutParams(params);
        w.setWebViewClient(new CardPaymentWebViewClient());
        WebSettings settings = w.getSettings();
        settings.setJavaScriptEnabled(true);

        popup_window.addView(w);
        setContentView(popup_window);
        w.loadUrl(url);
    }

    @Override
    public void didCardDeleted(DeleteCardResponse deleteCardResponse) {
        LoadingScreenManager.getInstance().closeLoadingScreen();
        if (dataSource != null) dataSource.updateSavedCards(deleteCardResponse.getId());
    }

    @Override
    public void didDeleteCardReceiveError(GoSellError errorDetails) {
        LoadingScreenManager.getInstance().closeLoadingScreen();
        PaymentOptionsDataManager dataOptionsManager = PaymentDataManager.getInstance().getPaymentOptionsDataManager(this);
        if (dataOptionsManager != null) dataOptionsManager.cancelItemClicked();

        if (errorDetails != null)
            Log.d("GoSellPaymentActivity", "didDeleteCardReceiveError: response >>> " + errorDetails.getErrorBody());

        this.showDialog(getResources().getString(R.string.error_deleting_card_title), getResources().getString(R.string.error_deleting_card_msg), false);
    }


    /**
     * The type Card payment web view client.
     */
    public class CardPaymentWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);


        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            PaymentDataManager.WebPaymentURLDecision decision = PaymentDataManager.getInstance()
                    .decisionForWebPaymentURL(url);

            boolean shouldOverride = !decision.shouldLoad();
            Log.e("shouldOverride", String.valueOf(shouldOverride));
            if (shouldOverride) { // if decision is true and response has TAP_ID
                PaymentDataManager.getInstance().retrieveChargeOrAuthorizeOrSaveCardAPI(getChargeOrAuthorize());
            }
            return shouldOverride;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }


        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && error != null) {
                Log.d("GoSellPaymentActivity", " onReceivedError : error  : " + error.getErrorCode());
                Log.d("GoSellPaymentActivity", " onReceivedError : desc  : " + error.getDescription());
            }

        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Log.d("onReceivedHttpError", "web view ........reason lonReceivedHttpError ....." + errorResponse.getReasonPhrase());
                Log.d("onReceivedHttpError", "web view ........statuscode lonReceivedHttpError ....." + errorResponse.getStatusCode());
            }
        }


        @Override
        public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
            super.onReceivedClientCertRequest(view, request);
            Log.d("onReceivedClientCertReq", "web view ........ onReceivedClientCertRequest .....");
        }


        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
            String err = (error != null) ? error.getUrl() : "";
            Log.d("onLoadResource", "web view ........ onReceivedSslError ..... >> url[" + err + "]");
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            Log.d("onReceivedError", "web view ........ onReceivedError ..... >> errorCode[" + errorCode + "]");
            Log.d("onReceivedError", "web view ........ onReceivedError ..... >> description[" + description + "]");
        }
    }
////////////////////////////////////////////////////////////////////////////////////////////////////

    @Override
    public void didReceiveAuthorize(Authorize authorize) {
        Log.d("GoSellPaymentActivity", " Cards >> didReceiveAuthorize * * * ");
        if (authorize == null) return;
        //  Log.d("GoSellPaymentActivity"," Cards >> didReceiveAuthorize * * * " + authorize.getStatus());

        switch (authorize.getStatus()) {
            case INITIATED:
                Authenticate authenticate = authorize.getAuthenticate();
                if (authenticate != null && authenticate.getStatus() == AuthenticationStatus.INITIATED) {
                    switch (authenticate.getType()) {
                        case BIOMETRICS:

                            break;

                        case OTP:
                            PaymentDataManager.getInstance().setChargeOrAuthorize((Authorize) authorize);
                            openOTPScreen((Authorize) authorize);
                            break;
                    }
                }
                break;
            case CAPTURED:
            case AUTHORIZED:
                try {
                    closePaymentActivity();
                    SDKSession.getListener().authorizationSucceed(authorize);
                } catch (Exception e) {
                    Log.d("GoSellPaymentActivity", " Error while calling delegate method authorizationSucceed(authorize)");
                    closePaymentActivity();
                }
                break;
            case FAILED:
            case ABANDONED:
            case CANCELLED:
            case DECLINED:
            case RESTRICTED:
                try {
                    closePaymentActivity();
                    SDKSession.getListener().authorizationFailed(authorize);
                } catch (Exception e) {
                    Log.d("GoSellPaymentActivity", " Error while calling delegate method authorizationFailed(authorize)");
                    closePaymentActivity();
                }
                break;
        }
        obtainPaymentURLFromChargeOrAuthorizeOrSaveCard(authorize);
    }

    @Override
    public void didReceiveError(GoSellError error) {
        try {
            closePaymentActivity();
            SDKSession.getListener().sdkError(error);
        } catch (Exception e) {
            Log.d("GoSellPaymentActivity", "Error while try to call delegate method  sdkError(error)");
            closePaymentActivity();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////

    private void setChargeOrAuthorizeOrSaveCard(Charge chargeOrAuthorizeOrSaveCard) {
        this.chargeOrAuthorizeOrSaveCard = chargeOrAuthorizeOrSaveCard;
    }


    private Charge getChargeOrAuthorize() {
        return chargeOrAuthorizeOrSaveCard;
    }


    private void stopPayButtonLoadingView() {
        LoadingScreenManager.getInstance().closeLoadingScreen();
        if (payButton.getLoadingView() != null) {
            if (payButton.getLoadingView().isShown())
                payButton.getLoadingView().setForceStop(true);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    private void clearPaymentProcessListeners() {
        if (PaymentDataManager.getInstance() != null)
            PaymentDataManager.getInstance().clearPaymentProcessListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupPayButton();
        if (recentSectionViewModel != null) recentSectionViewModel.EnableRecentView();
        if (webPaymentViewModel != null) webPaymentViewModel.enableWebView();
        PaymentDataManager.getInstance().setCardPaymentProcessStatus(false);
        if (cardCredentialsViewModel != null) cardCredentialsViewModel.enableCardScanView();

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        setupPayButton();
        if (recentSectionViewModel != null) recentSectionViewModel.EnableRecentView();
        if (webPaymentViewModel != null) webPaymentViewModel.enableWebView();
        PaymentDataManager.getInstance().setCardPaymentProcessStatus(false);
        if (cardCredentialsViewModel != null) cardCredentialsViewModel.enableCardScanView();
    }


    private void closeActivity() {
        clearPaymentProcessListeners();
        setResult(RESULT_OK);
        if (recentSectionViewModel != null) recentSectionViewModel.EnableRecentView();
        if (webPaymentViewModel != null) webPaymentViewModel.enableWebView();
        PaymentDataManager.getInstance().setCardPaymentProcessStatus(false);
        if (cardCredentialsViewModel != null) cardCredentialsViewModel.enableCardScanView();
        finish();
    }


///////////////////////////////////////////////////////////////////////////////////////////////////

    private void startPaymentProcess() {
        checkInternetConnectivity();
    }

    private void checkInternetConnectivity() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                LoadingScreenManager.getInstance().showLoadingScreen(this);
                if (getSavedCard() != null) {
                    startSavedCardPaymentProcess();
                } else {
                    startCardPaymentProcess(cardCredentialsViewModel);
                }
            } else
                showDialog(getResources().getString(R.string.internet_connectivity_title), getResources().getString(R.string.internet_connectivity_message), true);
        } else
            Log.d("checkInternetConnectv", " some error in connectivity manager...");
    }

    private void showDialog(String title, String message, boolean showNegativeButton) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle(title);
        dialogBuilder.setMessage(message);
        dialogBuilder.setCancelable(false);

        dialogBuilder.setPositiveButton(getResources().getString(R.string.dismiss), (dialog, which) -> System.out.println(" user dismissed process....."));

        if (showNegativeButton)
            dialogBuilder.setNegativeButton(getResources().getString(R.string.retry), (dialog, which) -> checkInternetConnectivity());

        dialogBuilder.show();

    }

    private void startPaymentWithTimer() {
        new CountDownTimer(1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                startPaymentProcess();
            }
        }.start();
    }
    private void handlePaymentSuccess(PaymentData paymentData) {
        // Token will be null if PaymentDataRequest was not constructed using fromJson(String).
        final String paymentInfo = paymentData.toJson();
        if (paymentInfo == null) {
            return;
        }
        try {
            JSONObject paymentMethodData = new JSONObject(paymentInfo).getJSONObject("paymentMethodData");
            // If the gateway is set to "example", no payment information is returned - instead, the
            // token will only consist of "examplePaymentMethodToken".
            final JSONObject tokenizationData = paymentMethodData.getJSONObject("tokenizationData");
           // System.out.println("tokenizationData>>>"+tokenizationData);
            final String tokenizationType = tokenizationData.getString("type");
          //  System.out.println("tokenizationType is"+tokenizationType);
            final String token = tokenizationData.getString("token");
          //  System.out.println("token is"+token);
            Gson gson = new Gson();
            JsonObject jsonToken = gson.fromJson(token, JsonObject.class);
            /**
             * At this stage, Passing the googlePaylaod to Tap Backend TokenAPI call followed by chargeAPI.
             * ***/
            CreateTokenGPayRequest createTokenGPayRequest = new CreateTokenGPayRequest("googlepay",jsonToken);
            initiateGooglePayProcess(createTokenGPayRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * At this stage, the user has already seen a popup informing them an error occurred. Normally,
     * only logging is required.
     *
     * @param status object
     * @see <a href="https://developers.google.com/android/reference/com/google/android/gms/wallet/
     * WalletConstants#constant-summary">Wallet Constants Library</a>
     */
    private void handleError(Status status) {
        Log.e("loadPaymentData failed", String.format("Error code: %d", status.getStatusCode()));
        try {
            closePaymentActivity();
            SDKSession.getListener().googlePayFailed(status);
        } catch (Exception e) {
            closePaymentActivity();
        }

    }


    public void initiateGooglePayProcess(CreateTokenGPayRequest createTokenGPayRequest){
        getVisibleViewModels();
        PaymentDataManager.getInstance().initiateGooglePayTokenPayment(googlePayViewModel, this,createTokenGPayRequest);
    }

}



