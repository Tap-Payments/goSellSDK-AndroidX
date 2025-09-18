package company.tap.sample;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Locale;

import company.tap.gosellapi.GoSellSDK;
import company.tap.gosellapi.open.controllers.ThemeObject;

public class SampleApp extends Application {


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onCreate() {
        super.onCreate();

        GoSellSDK.init(this, "sk_test_kovrMB0mupFJXfNZWx6Etg5y", "company.tap.goSellSDKExample");                 // to be replaced by merchant
       // GoSellSDK.setLocale(this,"en");

    }


}
