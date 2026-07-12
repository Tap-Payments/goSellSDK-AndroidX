package company.tap.gosellapi.internal.activities;


import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

import company.tap.gosellapi.GoSellSDK;
import company.tap.gosellapi.internal.api.api_service.AppInfo;
import company.tap.gosellapi.open.controllers.ThemeObject;

/**
 * The type Base activity.
 */
public class BaseActivity extends AppCompatActivity {
    private static String getLocaleLang;
    private  Context _context;
    /**
     * Gets current.
     *
     * @return the current
     */
    @Nullable public static BaseActivity getCurrent() {

        return currentActivity;
    }

    private static void setCurrent(@Nullable BaseActivity activity) {

        currentActivity = activity;
    }

    @Nullable private static BaseActivity currentActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);


        _context=  this;
        /**
         * Google throws this exception on Activity's onCreate method after v27, their meaning is :
         * if an Activity is translucent or floating, its orientation should be relied on parent(background) Activity,
         * can't make decision on itself.
         * Even if you remove android:screenOrientation="portrait" from the floating or translucent Activity
         *  but fix orientation on its parent(background) Activity, it is still fixed by the parent.
         *  In android Oreo (API 26) you can not change orientation for Activity that have below line in style
         */
        System.out.println("Build.VERSION.SDK_INT : "+Build.VERSION.SDK_INT);

        if (Build.VERSION.SDK_INT == 26) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    @Override
    protected void onResume() {

        super.onResume();

        if (Build.VERSION.SDK_INT == 26) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        BaseActivity.setCurrent(this);
    }
  /*  @Override
    protected void attachBaseContext(Context context) {
        context =_context;
        System.out.println("getSdkLanguage"+ThemeObject.getInstance().getSdkLanguage());
        System.out.println("context"+context);
        AppInfo.setLocale(ThemeObject.getInstance().getSdkLanguage());
        Locale locale = new Locale(ThemeObject.getInstance().getSdkLanguage());
        getLocaleLang =ThemeObject.getInstance().getSdkLanguage();
        Locale.setDefault(Locale.forLanguageTag(ThemeObject.getInstance().getSdkLanguage()));
        Configuration config = new Configuration();
        config.locale = locale;
        _context.getResources().updateConfiguration(config, _context.getResources().getDisplayMetrics());


    }*/
  @Override
  protected void attachBaseContext(Context newBase) {

      String lang = null;

      // Safely get SDK language
      if (ThemeObject.getInstance() != null) {
          lang = ThemeObject.getInstance().getSdkLanguage();
      }

      // If SDK language is null/empty, use device locale
      if (lang == null || lang.trim().isEmpty()) {

          if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
              if (newBase != null
                      && newBase.getResources() != null
                      && newBase.getResources().getConfiguration() != null
                      && !newBase.getResources().getConfiguration().getLocales().isEmpty()) {

                  lang = newBase.getResources()
                          .getConfiguration()
                          .getLocales()
                          .get(0)
                          .getLanguage();
              }
          } else {
              if (newBase != null
                      && newBase.getResources() != null
                      && newBase.getResources().getConfiguration() != null
                      && newBase.getResources().getConfiguration().locale != null) {

                  lang = newBase.getResources()
                          .getConfiguration()
                          .locale
                          .getLanguage();
              }
          }
      }

      // Final fallback
      if (lang == null || lang.trim().isEmpty()) {
          lang = "en";
      }

      Locale locale = new Locale(lang);
      Locale.setDefault(locale);

      getLocaleLang = lang;

      Configuration config = new Configuration(newBase.getResources().getConfiguration());
      config.setLocale(locale);
      config.setLayoutDirection(locale);

      Context localizedContext;

      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
          localizedContext = newBase.createConfigurationContext(config);
      } else {
          newBase.getResources().updateConfiguration(
                  config,
                  newBase.getResources().getDisplayMetrics()
          );
          localizedContext = newBase;
      }
      System.out.println("Sdk language: " + lang);

      AppInfo.setLocale(lang);

      super.attachBaseContext(localizedContext);
  }


    @Override
    protected void onPause() {

        clearCurrentActivity();
        super.onPause();
    }

    @Override
    protected void onDestroy() {

        clearCurrentActivity();
        super.onDestroy();
    }

    private void clearCurrentActivity() {

        BaseActivity current = BaseActivity.getCurrent();
        if (this.equals(current)) {

            BaseActivity.setCurrent(null);
        }
    }
}
