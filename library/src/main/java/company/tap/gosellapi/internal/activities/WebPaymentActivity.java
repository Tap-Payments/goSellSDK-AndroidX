package company.tap.gosellapi.internal.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.PermissionRequest;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
  private static final String TAG = "WebPaymentActivity";
  String[] PERMISSIONS = {
          Manifest.permission.CAMERA,
          Manifest.permission.WRITE_EXTERNAL_STORAGE
  };

  private ValueCallback<Uri[]> mFilePathCallback;
  private String mCameraPhotoPath;
    /**
     * The Payment url.
     */
    String paymentURL;
    /**
     * The Return url.
     */
    String returnURL;
  private Charge chargeOrAuthorize;

  @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
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

    checkRunTimePermission();

    webView.setWebChromeClient(new WebChromeClient() {

      public void onPermissionRequest(final PermissionRequest request) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          request.grant(request.getResources());
        }
      }

      @Override
      public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath, WebChromeClient.FileChooserParams fileChooserParams) {
        if (!hasPermissions(WebPaymentActivity.this, PERMISSIONS)) {
          checkStoragePermission();
          return false;
        }
        // Double check that we don't have any existing callbacks
        if (mFilePathCallback != null) {
          mFilePathCallback.onReceiveValue(null);
        }
        mFilePathCallback = filePath;

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
          // Create the File where the photo should go
          File photoFile = createImageFile();
          takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);

          // Continue only if the File was successfully created
          if (photoFile != null) {
            mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(photoFile));
          } else {
            takePictureIntent = null;
          }
        }

        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");

        Intent[] intentArray;
        if (takePictureIntent != null) {
          intentArray = new Intent[]{takePictureIntent};
        } else {
          intentArray = new Intent[0];
        }

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Select Photo");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

        startActivityForResult(chooserIntent,444);

        return true;

      }

    });
     webView.setWebViewClient(new WebPaymentWebViewClient());
    WebSettings settings = webView.getSettings();
    settings.setJavaScriptEnabled(true);
    settings.setDomStorageEnabled(true);
    settings.setPluginState(WebSettings.PluginState.ON);
   // settings.setMediaPlaybackRequiresUserGesture(false);
    settings.setAllowFileAccess(true);


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

  private File createImageFile() {
    OutputStream os = null;
    File file = null;
    try {
      file = new File(Environment.getExternalStorageDirectory(), "image" + System.currentTimeMillis() + ".png");
      os = new FileOutputStream(file);
      // finalBMP.compress(Bitmap.CompressFormat.PNG, 100, os);
      //  finalBMP.recycle(); // this is very important. make sure you always recycle your bitmap when you're done with it.
      //   screenGrabFilePath = file.getPath();
    } catch (IOException e) {
      //  finalBMP.recycle(); // this is very important. make sure you always recycle your bitmap when you're done with it.
      Log.e("combineImages", "problem combining images", e);
    }
return file;
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

  private void updateWebView() {

    WebView webView1 = findViewById(R.id.webPaymentWebView);
    webView1.setVisibility(View.VISIBLE);

    if (paymentURL == null) return;
    webView1.setWebChromeClient(new WebChromeClient() {
      @Override
      public void onPermissionRequest(PermissionRequest request) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
          request.grant(request.getResources());
        }
      }
    });

    webView1.loadUrl(paymentURL);
    System.out.println("pay url "+paymentURL);
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

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
//       Log.d("shouldOverrideUrlLoad",("rrrrrrrrrrrrrrrrrrrrrrrrr");
//      Log.d("WebPaymentActivity"," shouldOverrideUrlLoading : " + url);
      PaymentDataManager.WebPaymentURLDecision decision = PaymentDataManager.getInstance().decisionForWebPaymentURL(url);

      boolean shouldOverride = !decision.shouldLoad();
//      Log.d("WebPaymentActivity"," shouldOverrideUrlLoading : decision : " + shouldOverride);
      if (shouldOverride) { // if decision is true and response has TAP_ID
        // call backend to get charge response >> based of charge object type [Authorize - Charge] call retrieveCharge / retrieveAuthorize
        PaymentDataManager.getInstance().retrieveChargeOrAuthorizeOrSaveCardAPI(getChargeOrAuthorize());
      }
      return shouldOverride;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
//       Log.d("onPageFinished","rrrrrrrrrrrrrrrrrrrrrrrrr  >>> onPageFinished");
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
    finish();
  }

  private void finishActivityWithResultCancelled(GoSellError error) {
    setResult(RESULT_CANCELED,new Intent().putExtra("error", error));
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
  private void checkRunTimePermission() {
    String[] permissionArrays = new String[]{ Manifest.permission.BLUETOOTH,Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,Manifest.permission.MODIFY_AUDIO_SETTINGS,Manifest.permission_group.MICROPHONE,Manifest.permission_group.CAMERA};

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      requestPermissions(permissionArrays, 11111);
    }
  }



  private void checkStoragePermission() {
    String permission = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(WebPaymentActivity.this, new String[]{permission}, 222);

    } else {
      checkCameraPermission();
    }
  }

  private void checkCameraPermission() {
    String permission = Manifest.permission.CAMERA;
    if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(WebPaymentActivity.this, new String[]{permission}, 333);
    } else {
     // onPermissionGranted();
    }
  }
  public static boolean hasPermissions(Context context, String... permissions) {
    if (context != null && permissions != null) {
      for (String permission : permissions) {
        if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
          return false;
        }
      }
    }
    return true;
  }


}
