package company.tap.gosellapi.internal.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import company.tap.gosellapi.R;
import company.tap.gosellapi.internal.api.callbacks.GoSellError;
import company.tap.gosellapi.internal.api.enums.AuthenticationStatus;
import company.tap.gosellapi.internal.api.enums.ChargeStatus;
import company.tap.gosellapi.internal.api.models.Authenticate;
import company.tap.gosellapi.internal.api.models.Authorize;
import company.tap.gosellapi.internal.api.models.Charge;
import company.tap.gosellapi.internal.api.models.PaymentOption;
import company.tap.gosellapi.internal.api.models.SaveCard;
import company.tap.gosellapi.internal.api.models.Token;
import company.tap.gosellapi.internal.data_managers.LoadingScreenManager;
import company.tap.gosellapi.internal.data_managers.PaymentDataManager;
import company.tap.gosellapi.internal.data_managers.payment_options.view_models.WebPaymentViewModel;
import company.tap.gosellapi.internal.interfaces.IPaymentProcessListener;
import company.tap.gosellapi.internal.utils.ActivityDataExchanger;


/**
 * The type Web payment activity.
 */
public class WebPaymentActivity extends BaseActionBarActivity implements IPaymentProcessListener {
  private WebPaymentViewModel viewModel;
  private WebView webView;
    /**
     * The Payment url.
     */
    String paymentURL;
    /**
     * The Return url.
     */
    String returnURL;
  private Charge chargeOrAuthorize;

  @SuppressLint("SetJavaScriptEnabled")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    /**
     * Fix for web view
     * Force it to portrait to fix resend request each time configurations change "Portrait to Landscape"
     */
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    /**
     * ActivityDataExchanger old way Replaced by Haitham >> I created a new way of getting PaymentOptionModel from ActivityDataExchanger
     */
//        viewModel = (WebPaymentViewModel) ActivityDataExchanger.getInstance().getExtra(getIntent(), IntentParameters.paymentOptionModel);
    Object viewModelObject = null;
    if (ActivityDataExchanger.getInstance().getWebPaymentViewModel() != null) {
      viewModelObject = ActivityDataExchanger.getInstance().getWebPaymentViewModel();
    }

    viewModel = (viewModelObject instanceof WebPaymentViewModel) ? (WebPaymentViewModel) viewModelObject : null;
    Log.d("WebPaymentActivity"," WebPaymentActivity >>  viewModel :" + viewModel);
    setContentView(R.layout.gosellapi_activity_web_payment);

    webView = findViewById(R.id.webPaymentWebView);

    WebSettings settings = webView.getSettings();
    settings.setJavaScriptEnabled(true);

    settings.setDisplayZoomControls(false);
    settings.supportZoom();
    settings.setSupportZoom(true);
    settings.setBuiltInZoomControls(true);
    settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    settings.setLoadWithOverviewMode(true);
    settings.setUseWideViewPort(true);
    settings.setDomStorageEnabled(true);
    settings.setAppCacheEnabled(true);
    settings.setLoadsImagesAutomatically(true);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
    }


    webView.setWebViewClient(new WebPaymentWebViewClient());
    setTitle(getPaymentOption().getName());
    setImage(getPaymentOption().getImage());
    //Get the view which you added by activity setContentView() method
    View view = getWindow().getDecorView().findViewById(android.R.id.content);
    /**
     * post causes the Runnable to be added to the message queue
     * Runnable : used to run code in a different Thread
     * run () : Starts executing the active part of the class' code.
     *  >>> Here we try to get data in another thread not the  main thread to avoid UI Freezing
     */
    view.post(new Runnable() {
      @Override
      public void run() {
        getData();
      }
    });
  }


  @Override
  protected void onResume() {
    super.onResume();
    /**
     * Fix for web view
     * Force it to portrait to fix resend request each time configurations change "Portrait to Landscape"
     */
    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

  }

  private void getData() {

    LoadingScreenManager.getInstance().showLoadingScreen(this);
    PaymentDataManager.getInstance().initiatePayment(viewModel, this);
  }

  @SuppressLint("SetJavaScriptEnabled")
  private void updateWebView() {

    WebView webView = findViewById(R.id.webPaymentWebView);
    webView.setVisibility(View.VISIBLE);

    if (paymentURL == null) return;
    webView.getSettings().setJavaScriptEnabled(true);
    webView.getSettings().setDisplayZoomControls(false);
    webView.getSettings().supportZoom();
    webView.getSettings().setSupportZoom(true);
    webView.getSettings().setBuiltInZoomControls(true);
    webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
    webView.getSettings().setLoadWithOverviewMode(true);
    webView.getSettings().setUseWideViewPort(true);
    webView.getSettings().setDomStorageEnabled(true);
    webView.getSettings().setAppCacheEnabled(true);
    webView.getSettings().setLoadsImagesAutomatically(true);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
    }

    webView.loadUrl(paymentURL);
  }



  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    return super.onCreateOptionsMenu(menu);
  }

  /**
   * Listen to webview events
   */
  private class WebPaymentWebViewClient extends WebViewClient {

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
      super.onPageStarted(view, url, favicon);
//       Log.d("onPageStarted","rrrrrrrrrrrrrrrrrrrrrrrrr  >>> onPageStarted");
    }

    @SuppressLint("QueryPermissionsNeeded")
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
      Intent intent;
      //  Log.d("shouldOverrideUrlLoad",("rrrrrrrrrrrrrrrrrrrrrrrrr");
      Log.d("WebPaymentActivity", " shouldOverrideUrlLoading1 : " + url);
      if (url.contains("https://app.adjust.com/jsr")|| url.contains("careem://pay.careem.com/payment")|| url.contains("market://details")){
        intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        if (intent.resolveActivity(getPackageManager()) != null) {
          startActivity(intent);
        }
      } else {


        PaymentDataManager.WebPaymentURLDecision decision = PaymentDataManager.getInstance().decisionForWebPaymentURL(url);
        boolean shouldOverride = !decision.shouldLoad();
//      Log.d("WebPaymentActivity"," shouldOverrideUrlLoading : decision : " + shouldOverride);
        if (shouldOverride) { // if decision is true and response has TAP_ID
          // call backend to get charge response >> based of charge object type [Authorize - Charge] call retrieveCharge / retrieveAuthorize
          PaymentDataManager.getInstance().retrieveChargeOrAuthorizeOrSaveCardAPI(getChargeOrAuthorize());
        }
        return shouldOverride;

      }
      return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
       Log.d("onPageFinished","rrrrrrrrrrrrrrrrrrrrrrrrr  >>> onPageFinished"+url);
      super.onPageFinished(view, url);
      LoadingScreenManager.getInstance().closeLoadingScreen();
    }

    @Override
    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
      super.onReceivedError(view, request, error);
//       Log.d("onReceivedError","rrrrrrrrrrrrrrrrrrrrrrrrr  >>> error ");
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && error!=null) {
        Log.d("onReceivedError"," shouldOverrideUrlLoading : error  : " + error.getDescription());
      }
      view.stopLoading();
      view.loadUrl("about:blank");
    }
  }

  @Override
  public void onBackPressed() {
    setResult(RESULT_CANCELED);
    super.onBackPressed();
  }

  @Override
  public void didReceiveCharge(Charge charge) {
    if (charge != null) {
      Log.d("didReceiveCharge"," web payment activity* * * " + charge.getStatus());
      switch (charge.getStatus()) {
        case INITIATED:
          break;
        case CAPTURED:
        case AUTHORIZED:
          finishActivityWithResultCodeOK(charge);
          break;
        case FAILED:
        case ABANDONED:
        case CANCELLED:
        case DECLINED:
        case RESTRICTED:
        case UNKNOWN:
        case TIMEDOUT:
          finishActivityWithResultCodeOK(charge);
        break;
      }
    }
    obtainPaymentURLFromChargeOrAuthorize(charge);
  }

  @Override
  public void didReceiveSaveCard(@NonNull SaveCard saveCard) {
    Log.d("WebPaymentActivity"," didReceiveSaveCard() not available in case of WebPayment ");
  }

  @Override
  public void didCardSavedBefore() {
    Log.d("WebPaymentActivity"," didCardSavedBefore() not available in case of WebPayment ");
  }

  @Override
  public void fireCardTokenizationProcessCompleted(Token token) {
    Log.d("WebPaymentActivity"," fireCardTokenizationProcessCompleted() not available in case of WebPayment ");
  }

  /**
     * Gets dismiss intent.
     *
     * @param notificationId the notification id
     * @param context        the context
     * @return the dismiss intent
     */
    public static Intent getDismissIntent(int notificationId, Context context) {
    Intent intent = new Intent(context, GoSellPaymentActivity.class);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    intent.putExtra("NOTIFICATION_ID", notificationId);
    //PendingIntent dismissIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    return intent;
  }

  private void finishActivityWithResultCodeOK(Charge charge) {
    setResult(RESULT_OK,new Intent().putExtra("charge", charge));
    webView.clearHistory();
    webView.clearCache(true);
    finish();
  }

  private void finishActivityWithResultCancelled(GoSellError error) {
    setResult(RESULT_CANCELED,new Intent().putExtra("error", error));
    webView.clearHistory();
    webView.clearCache(true);
    finish();
  }
  private void setPaymentResult(Charge chargeOrAuthorize) {
//    PaymentProcessDelegate.getInstance().setPaymentResult(chargeOrAuthorize);
  }

  @Override
  public void didReceiveAuthorize(Authorize authorize) {

    obtainPaymentURLFromChargeOrAuthorize(authorize);
  }

  @Override
  public void didReceiveError(GoSellError error) {
    Log.d("WebPaymentActivity"," web payment : didReceiveError");
    closeLoadingScreen();
    finishActivityWithResultCancelled(error);
  }

  private void obtainPaymentURLFromChargeOrAuthorize(Charge chargeOrAuthorize) {
    Log.d("WebPaymentActivity"," WebPaymentActivity >> chargeOrAuthorize : " + chargeOrAuthorize.getStatus());

    if (chargeOrAuthorize.getStatus() != ChargeStatus.INITIATED) {
      return;
    }

    Authenticate authentication = chargeOrAuthorize.getAuthenticate();
    if (authentication != null)
      Log.d("WebPaymentActivity"," WebPaymentActivity >> authentication : " + authentication.getStatus());
    if (authentication != null && authentication.getStatus() == AuthenticationStatus.INITIATED) {
      return;
    }

    String url = chargeOrAuthorize.getTransaction().getUrl();
//    Log.d("WebPaymentActivity","WebPaymentActivity >> Transaction().getUrl() :" + url);
//    Log.d("WebPaymentActivity","WebPaymentActivity >> chargeOrAuthorize :" + chargeOrAuthorize.getId());


    if (url != null) {
      // save charge id
      setChargeOrAuthorize(chargeOrAuthorize);
      this.paymentURL = url;
      updateWebView();
    }
  }

  private void setChargeOrAuthorize(Charge chargeOrAuthorize) {
    this.chargeOrAuthorize = chargeOrAuthorize;
  }

  private Charge getChargeOrAuthorize() {
    return this.chargeOrAuthorize;
  }

  private void closeLoadingScreen() {

    LoadingScreenManager.getInstance().closeLoadingScreen();
  }

  private PaymentOption getPaymentOption() {

    return viewModel.getData();
  }

    /**
     * The type Intent parameters.
     */
    public final class IntentParameters {

        /**
         * The constant paymentOptionModel.
         */
        public static final String paymentOptionModel = "payment_option_model";
  }
}
