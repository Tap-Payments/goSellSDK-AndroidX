package company.tap.gosellapi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import java.util.Locale;

import company.tap.gosellapi.internal.api.api_service.AppInfo;
import company.tap.gosellapi.open.controllers.ThemeObject;

/**
 * The type Go sell sdk.
 */
public class GoSellSDK {
    @SuppressLint("StaticFieldLeak")
    private static Context context;
    private static String getLocaleLang;

    /**
     * Init.
     *
     * @param context   the context
     * @param authToken the auth token
     */
    public static void init(Context context, String authToken, String packageId) {
        AppInfo.setAuthToken(context, authToken,packageId);
        GoSellSDK.context = context;
    }

    /**
     * Sets locale.
     *
     * @param localeString the locale string
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
   /* public static void setLocale(String localeString) {
        AppInfo.setLocale(localeString);
        Locale locale = new Locale(localeString);
        getLocaleLang = localeString;
        Locale.setDefault(Locale.forLanguageTag(ThemeObject.getInstance().getSdkLanguage()));
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }*/
    public static void setLocale(Context context, String localeString) {
        AppInfo.setLocale(localeString);
        getLocaleLang = localeString;

        Locale locale = new Locale(localeString);
        Locale.setDefault(locale);

        // ✅ Try AppCompatDelegate.setApplicationLocales (AppCompat 1.6.0+)
        try {
            LocaleListCompat appLocale = LocaleListCompat.forLanguageTags(localeString);
            AppCompatDelegate.setApplicationLocales(appLocale);
            return; // success, no need to fallback
        } catch (NoSuchMethodError | NoClassDefFoundError e) {
            // ❌ Not available on older AppCompat
        }

        // ✅ Fallback for API 24+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Configuration config = new Configuration(context.getResources().getConfiguration());
            config.setLocale(locale);
            context.createConfigurationContext(config);
        } else {
            // ✅ Legacy fallback for pre-N (shouldn’t hit since you want API ≥ 24)
            Configuration config = new Configuration();
            config.locale = locale;
            context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        }
    }


    public static String getLocaleString(){
        if(getLocaleLang==null)return Locale.getDefault().getLanguage();
        else
        return getLocaleLang;
    }
}
