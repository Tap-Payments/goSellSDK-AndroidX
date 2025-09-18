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
        String lang = ThemeObject.getInstance().getSdkLanguage(); // your SDK language
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        getLocaleLang = ThemeObject.getInstance().getSdkLanguage();
        Configuration config = new Configuration();
        config.locale = locale;
        config.setLocale(locale);
        config.setLayoutDirection(locale);

        newBase.getResources().updateConfiguration(config, newBase.getResources().getDisplayMetrics());
        System.out.println("getSdkLanguage: " + lang);
      //  System.out.println("localizedContext: " + localizedContext);
        AppInfo.setLocale(ThemeObject.getInstance().getSdkLanguage());
        super.attachBaseContext(newBase);
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
