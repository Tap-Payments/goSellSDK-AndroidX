# CHANGELOG
## [3.19.46] - 2025-12-07
### Enhancement Feature:
- Handling for app headers ascii charcs
- ### Impact on existing integrations:
- No Need to add any additional libs from previous builds only tap lib
## [3.19.44] - 2025-11-09
### Enhancement Feature:
- Remove Locale
- ### Impact on existing integrations:
- No Need to add any additional libs from previous builds only tap lib
## [3.19.43] - 2025-11-03
### Enhancement Feature:
- Update to handle Deprecated 
- ### Impact on existing integrations:
- No Need to add any additional libs from previous builds only tap lib
## [3.19.40] - 2025-09-23
### Enhancement Feature:
- Update to handle 16KB android
- ### Impact on existing integrations:
- No Need to add any additional libs from previous builds only tap lib
## [3.19.38.1] - 2025-09-18
### Enhancement Feature:
- Update to handle 16KB android
### Impact on existing integrations:
- Please follow notes from previous release for updating to 35
## [3.19.36] - 2025-08-25
### Enhancement Feature:
- Fix to handle locale cases
### Impact on existing integrations:
- Please follow notes from previous release for updating to 35
## [3.19.36] - 2025-08-20
### Enhancement Feature:
- Fix to handle dark ui cases
### Impact on existing integrations:
- Please follow notes from previous release for updating to 35
## [3.19.34] - 2025-07-24
### Enhancement Feature:
- Updating to support latest android api 35
### Impact on existing integrations:
- May need to update gradles to support latest apis support sdk 35
- compileSdkVersion 35
- targetSdkVersion 35
- Project build.gradle  classpath 'com.android.tools.build:gradle:8.6.0'
- Gradle wrapper.properties distributionUrl=https\://services.gradle.org/distributions/gradle-8.10.2-bin.zip
- May need to add proguard rules# Add project specific ProGuard rules here.
 You can control the set of applied configuration files using the
 proguardFiles setting in build.gradle.
 For more details, see http://developer.android.com/guide/developing/tools/proguard.html
If your project uses WebView with JS, uncomment the following
 and specify the fully qualified class name to the JavaScript interface
 class:
```
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keepattributes Signature
-keepclassmembernames,allowobfuscation interface * {
@retrofit2.http.* <methods>;
}
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

    #########################################################################
    # OkHttp
    #########################################################################
    -dontwarn okhttp3.**
    -dontwarn okhttp2.**
    -dontwarn okio.**
    -dontwarn javax.annotation.**
    -dontwarn org.conscrypt.**
    -keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-keep class company.tap.gosellapi.** { *; }
-keep class gotap.com.tapglkitandroid.** { *; }


-dontwarn okhttp2.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
#-dontobfuscate
-optimizations !code/allocation/variable
-keep class company.tap.gosellapi.** { *; }
# KEEP TapGLKit classes
-keep class gotap.com.tapglkitandroid.** { *; }
-keep class gotap.com.tapglkitandroid.gl.Views.TapLoadingView { *; }

-dontwarn gotap.com.tapglkitandroid.**
-keepclassmembers class gotap.com.tapglkitandroid.gl.Views.TapLoadingView {
public <init>(android.content.Context);
public <init>(android.content.Context, android.util.AttributeSet);
public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keep class company.tap.tapcardvalidator_android.** { *; }
-dontwarn company.tap.tapcardvalidator_android.**


# GSON.
-keepnames class com.google.gson.** {*;}
-keepnames enum com.google.gson.** {*;}
-keepnames interface com.google.gson.** {*;}
-keep class com.google.gson.** { *; }
-keepnames class org.** {*;}
-keepnames enum org.** {*;}
-keepnames interface org.** {*;}
-keep class org.** { *; }
-keepclassmembers enum * { *; }

# Retrofit
-keepattributes Signature
-keepattributes RuntimeVisibleAnnotations
-keep class retrofit2.** { *; }
-keep interface retrofit2.** { *; }
-keepclassmembers class * {
@retrofit2.http.* <methods>;
}

# OkHttp
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# Gson
-keep class com.google.gson.** { *; }
-keepattributes EnclosingMethod
-keepattributes InnerClasses
-keepattributes Signature

# Gson TypeToken
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken
```
- Might be need to add additional libs (If use face issues for class not found etc then add below)
```
implementation 'com.google.android.material:material:1.14.0-alpha03'
implementation 'com.github.Tap-Payments:TapGLKit-Android:1.19.1'
implementation 'com.github.Tap-Payments:TapCardValidator-Android:2.16.2.1'
// OkHttp core and logging interceptor
implementation 'com.squareup.okhttp3:logging-interceptor:5.1.0'
implementation 'com.squareup.okhttp3:okhttp:5.1.0'
implementation 'com.squareup.retrofit2:retrofit:3.0.0'
implementation 'com.squareup.retrofit2:converter-gson:3.0.0'
implementation 'com.squareup.okhttp3:logging-interceptor:5.1.0'
implementation 'com.squareup.picasso:picasso:2.71828'
implementation 'com.google.android.gms:play-services-wallet:19.4.0'
implementation 'jp.wasabeef:blurry:4.0.1'
implementation 'io.card:android-sdk:5.5.1'
 implementation 'com.github.bumptech.glide:glide:4.12.0'

```

## [3.19.28] - 2025-07-23
### Enhancement Feature:
- Updating to support latest android api 35
### Impact on existing integrations:
- May need to update gradles to support latest apis support sdk 35
- compileSdkVersion 35
- targetSdkVersion 35
- Project build.gradle classpath 'com.android.tools.build:gradle:8.4.0'
- Gradle wrapper.properties distributionUrl=https\://services.gradle.org/distributions/gradle-8.6-bin.zip

## [3.19.27] - 2024-04-10
### Enhancement Feature:
- Updating the SAR symbol
### Impact on existing integrations:
- May need to update gradles to support latest apis.

## [3.19.24] - 2024-12-02
### Hot Fix:
- Hot fix for NPE check while calling start session delegate
### Impact on existing integrations:
- May need to update gradles to support latest apis.

## [3.19.23] - 2024-10-24
### Hot Fix:
- Hot fix passing merchant id in token and card verify api
### Impact on existing integrations:
- May need to update gradles to support latest apis.

## [3.19.22] - 2024-09-22
### Hot Fix:
- Hot fix for cardbrand-scheme check
### Impact on existing integrations:
- May need to update gradles to support latest apis.

## [3.19.21] - 2024-09-19
### Hot Fix:
- Hot fix for cardbrand-scheme check
### Impact on existing integrations:
- May need to update gradles to support latest apis.

## [3.19.19] - 2024-08-29
### Hot Fix:
- Hot fix for making url in tracking url public
### Impact on existing integrations:
- May need to update gradles to support latest apis.

## [3.19.19] - 2024-08-26
### Hot Fix:
- Hot fix for callback session cancelled.
### Impact on existing integrations:
- May need to update gradles to support latest apis.

## [3.19.18] - 2024-08-25
### Hot Fix:
- Hot fix for enable settings for webview.
### Impact on existing integrations:
- May need to update gradles to support latest apis.

## [3.19.17] - 2024-08-08
### Hot Fix:
- Hot fix for updating paymentagreement model.
### Impact on existing integrations:
- May need to update gradles to support latest apis.


## [3.19.14] - 2024-07-02
### Hot Fix:
- Hot fix for removing licensed fonts caching.
### Impact on existing integrations:
- May need to update gradles to support latest apis.

## [3.19.13] - 2024-06-30
### Hot Fix:
- Hot fix for removing licensed fonts like roboto.
### Impact on existing integrations:
- May need to update gradles to support latest apis.


## [3.19.12] - 2024-05-13
### Hot Fix:
- Hot fix for add clear buttons for fields.
- Fix error for AMEX card.
- Limit card length to 16 or max based on card type.
- show amount next to  selected currency .
### Impact on existing integrations:
- May need to update gradles to support latest apis.

## [3.19.11] - 2024-04-25
### Hot Fix:
- Hot fix for show amount on the button
### Impact on existing integrations:
- May need to update gradles to support latest apis.

## [3.19.10] - 2024-04-25
### Hot Fix:
- Disable Mutilple click on screen when payment starts
### Impact on existing integrations:
- May need to update gradles to support latest apis.

## [3.19.9] - 2024-03-12
### Hot Fix:
- Update for RN module
### Impact on existing integrations:
- May need to update gradles to support latest apis support sdk 34
- compileSdkVersion 34
- targetSdkVersion 34
- Project build.gradle classpath 'com.android.tools.build:gradle:7.3.1'
- Gradle wrapper.properties distributionUrl=https\://services.gradle.org/distributions/gradle-7.4.2-bin.zip
  
## [3.19.8] - 2024-02-07
### Hot Fix:
- Update for copy-paste feature in cardnumber fields
### Impact on existing integrations:
- May need to update gradles to support latest apis.

## [3.19.7] - 2024-02-04
### Enhancement Feature:
- Update for androidx lib for supporting api34
### Impact on existing integrations:
- May need to update gradles to support latest apis.

## [3.19.4] - 2023-12-13
### Hot Fix:
- Fix for  issue loading screen opened from app overview
### Impact on existing integrations:
- The update does not impact existing integrations.

## [3.19.1] - 2023-12-05
### Enhancement Feature:
- Now merchants can pass allowed payment methods like only visa mastercard , mada , knet etc
### Impact on existing integrations:
- The update does not impact existing integrations.

## [3.18.12] - 2023-11-06
### Hot Fix:
- Fix for  issue for Flutter sdk
### Impact on existing integrations:
- The update does not impact existing integrations.

## [3.18.10] - 2023-11-05
### Hot Fix:
- Fix for UI issue for Flutter sdk
### Impact on existing integrations:
- The update does not impact existing integrations.

[3.18.8] - 2023-10-26
### Hot Fix:
- Fix for googlePay
### Impact on existing integrations:
- The update does not impact existing integrations.

## [3.18.7] - 2023-10-25
### Enhancement Feature:
- Back button functionality enhancement
### Impact on existing integrations:
- The update does not impact existing integrations.

[3.18.5] - 2023-10-08
### Hot Fix:
- Bug Fix for RN plugin
### Impact on existing integrations:
- The update does not impact existing integrations.
### Recommended changes for existing integrations before updating:
- Any one who uses this will need to update target and compile sdk version to 33

## [3.18.1] - 2023-10-08
### Hot Fix:
- Bug Fix in HolderName
### Impact on existing integrations:
- The update does not impact existing integrations.
### Recommended changes for existing integrations before updating:
- Any one who uses this will need to update target and compile sdk version to 33
  
## [3.18.0] - 2023-10-05
### Enhancement Feature:
- Updated to support android api33
### Impact on existing integrations:
- The update does not impact existing integrations.
### Recommended changes for existing integrations before updating:
- Any one who uses this will need to update target and compile sdk version to 33
  
## [3.17.18] - 2023-10-04
### Hot Fix:
- Bug Fix for RN plugin
## [3.17.2] - 2023-06-26
### Enhancement Feature:
- Updated blurry library
### Impact on existing integrations:
- The update does not impact existing integrations.
### Recommended changes for existing integrations before updating:
- None.
## [3.17.0] - 2023-05-24
### Enhancement Feature:
- The merchant can now configure the scanner to be shown or hide
### Impact on existing integrations:
- The update does not impact existing integrations.
### Recommended changes for existing integrations before updating:
- None.
## [3.16.4] - 2023-05-22
### Hot Fix:
- bug fix for text
### Impact on existing integrations:
- The update does not impact existing integrations.
### Recommended changes for existing integrations before updating:
- None.
## [3.16.3] - 2023-05-14
### Hot Fix:
- Bug fix for UI 
### Impact on existing integrations:
- The update does not impact existing integrations.
### Recommended changes for existing integrations before updating:
- None.
## [3.16.2] - 2023-04-04
### Hot Fix:
- Bug fix for NPE 
### Impact on existing integrations:
- The update does not impact existing integrations.
### Recommended changes for existing integrations before updating:
- None.
## [3.16.1] - 2023-01-23
### Enhancement Feature:
- New Callback added to get when a charge is initiated
### Impact on existing integrations:
- The update does not impact existing integrations.
### Recommended changes for existing integrations before updating:
- None.
## [3.15.18] - 2022-12-29
### Hot Fix:
- Hot fix issues for cardbrand
### Impact on existing integrations:
- The update does not impact existing integrations.
### Recommended changes for existing integrations before updating:
- None.
## [3.15.17] - 2022-12-08
### Hot Fix:
- Updated React-Native sdk
### Impact on existing integrations:
- The update does not impact existing integrations.
### Recommended changes for existing integrations before updating:
- None.
## [3.15.12] - 2022-11-09
### Hot Fix:
- Updated Tokenize Method
### Impact on existing integrations:
- The update does not impact existing integrations.
### Recommended changes for existing integrations before updating:
- None.
# CHANGELOG
## [3.15.12] - 2022-10-06
### Hot Fix:
- Minor Hot fix for dynamic card brand name
### Impact on existing integrations:
- The update does not impact existing integrations.
### Recommended changes for existing integrations before updating:
- None.
## [3.15.11] - 2022-08-28
### Hot Fix:
- Minor Hot fix for callback added
### Impact on existing integrations:
- The update does not impact existing integrations.
### Recommended changes for existing integrations before updating:
- None.
## [3.15.10] - 2022-08-25
### Minor version release for Feature update:
- Added new callback for Asynchronous Payment
### Impact on existing integrations:
- The update does not impact existing integrations.
### Recommended changes for existing integrations before updating:
- None.
## [3.15.9] - 2022-07-27
### Minor version release for Hot Fix:
- Hot fixes for react native sdk
### Impact on existing integrations:
- The update does not impact existing integrations.
### Recommended changes for existing integrations before updating:
- None.
## [3.15.8] - 2022-07-18
### Minor version release for Hot Fix:
- Hot fixes for react native sdk
### Impact on existing integrations:
- The update does not impact existing integrations.
### Recommended changes for existing integrations before updating:
- None.
## [3.15.7] - 2022-07-14
### Minor version release for Hot Fix:
- Hot fixes for react native sdk
### Impact on existing integrations:
- The update does not impact existing integrations.
### Recommended changes for existing integrations before updating:
- None.
## [3.15.6] - 2022-07-13
### Minor version release for Hot Fix:
- Hot fixes for react native sdk
### Impact on existing integrations:
- The update does not impact existing integrations.
### Recommended changes for existing integrations before updating:
- None.
## [3.15.5] - 2022-07-13
### Minor version release for Hot Fix:
- Hot fixes for react native sdk
### Impact on existing integrations:
- The update does not impact existing integrations.
### Recommended changes for existing integrations before updating:
- None.
## [3.15.4] - 2022-07-06
### Minor version release for Hot Fix:
- Hot fix Scanner in react native
### Impact on existing integrations:
- The update does not impact existing integrations.
### Recommended changes for existing integrations before updating:
- None.
## [3.15.3] - 2022-03-10
### Minor version release for Hot Fix:
- Hot fix crash / NPE
### Impact on existing integrations:
- The update does not impact existing integrations.
### Recommended changes for existing integrations before updating:
- None.
## [3.15.2] - 2022-03-06
### Minor version release for Hot Fix:
- Hot fix crash
### Impact on existing integrations:
- The update does not impact existing integrations.
### Recommended changes for existing integrations before updating:
- None.
## [3.15.1] - 2022-03-03
### Minor version release for Hot Fix:
- Hot fix in Asynchronous payment read dynamically name
### Impact on existing integrations:
- The update does not impact existing integrations.
### Recommended changes for existing integrations before updating:
- None.
## [3.15.0] - 2022-02-22
### New Feature Release:
- Merchants can now pay using Careem Pay
### Impact on existing integrations:
- The update does not impact existing integrations.
### Recommended changes for existing integrations before updating:
- If Merchants wants to use Careem Pay will require to update sdk.
## [3.14.3] - 2022-02-10
### Minor version release for Hot Fix:
- Hot fix in NPE in cancel session
- Align Header with Ios Tokenize card
- New Object in PaymentOptions Response
### Impact on existing integrations:
- The update does not impact existing integrations.
### Recommended changes for existing integrations before updating:
- None.
## [3.14.1] - 2021-12-01
### Minor version release for Hot Fix:
- Hot fix in button themeing
### Impact on existing integrations:
- The update does not impact existing integrations.
### Recommended changes for existing integrations before updating:
- None.
## [3.14.0] - 2021-11-25
###  Version release with New Feature:
- New Feature added for Merchant to know tokenized card save enabled or not.
- Update Readme and ChangeLog.
## [3.13.11] - 2021-11-22
###  Version release with Hot Fix:
- Fix issue for null safety kotlin versions.
- Update Readme and ChangeLog.
## [3.13.10] - 2021-11-14
###  Version release with Hot Fix:
- Fix issue for react-native plugin invalid card number color.
- Update Readme and ChangeLog.
## [3.13.9] - 2021-11-14
###  Version release with Hot Fix:
- Fix issue for react-native plugin invalid card number color.
- Update Readme and ChangeLog.
## [3.13.8] - 2021-11-08
###  Version release with Hot Fix:
- Fix issue for react-native plugin.
- Update Readme and ChangeLog.
## [3.13.7] - 2021-09-16
###  Version release with Hot Fix:
- Fix issue for build error
- Update Readme and ChangeLog.
## [3.13.5] - 2021-09-16
###  Version release with Hot Fix:
- Fix issue for build error
- Update Readme and ChangeLog.
## [3.13.4] - 2021-09-16
###  Version release with Hot Fix:
- Fix issue for cardbrand type alert
- Update Readme and ChangeLog.
## [3.13.3] - 2021-09-15
###  Version release with Hot Fix:
- Fix issue for NPE with 3DS
- Update Readme and ChangeLog.
## [3.13.2] - 2021-07-28
###  Version release with Hot Fix:
- Fix issue for Serializable.
- Update Readme and ChangeLog.
## [3.13.1] - 2021-07-07
###  Version release with Enhancement:
- Added more parameters to Topup object inside the sdk
- Update Readme and ChangeLog.
## [3.13.0] - 2021-06-24
###  Version release with New Feature:
- New Feature added to support Topup object inside the sdk
- Update Readme and ChangeLog.
## [3.12.2] - 2021-05-30
###  Version release with Hot fix:
- Hot fix to add cardType ALL to enum
- Update Readme and ChangeLog.
## [3.12.1] - 2021-03-17
###  Version release with Hot fix:
- Hot fix with encrypting device name
- NPE fix 
- Update Readme and ChangeLog.
## [3.12.0] - 2021-01-14
### Version release with New Feature:
- Merchant can now get CardIssuer object and its details
- Update Readme and ChangeLog.
## [3.11.2] - 2020-10-18
### Version release with Hot fix
- Hot fix for card form layout issue .
- Hot fix for cardholder name to be optional.
- Hot fix for pay button text localization.
- Update Readme and ChangeLog.
## [3.11.1] - 2020-10-07
### Version release with Hot fix
- Hot fix for setting language of the sdk .
- Update Readme and ChangeLog.
## [3.11.0] - 2020-09-09
### Version release with New Feature:
- Merchant can now customize the text on pay button for the checkout sdk launched.
- Update Readme and ChangeLog.
## [3.10.0] - 2020-08-18
### Version release with New Feature:
- Merchant can now cancel the session and stop all process initiating the SDK.
- Update Readme and ChangeLog.
## [3.9.1] - 2020-08-12
### Version release with Proguard update:
- Update Readme and ChangeLog.
## [3.8.0] - 2020-07-16
### Version release with New Feature:
- Merchant can now enable or disable defaultCardHolderName (Optional) and pass through the sdk session.This allows him from editing the name .
- Update Readme and ChangeLog.
## [3.7.0] - 2020-06-08
### Version release with New Feature:
- Merchant can now set defaultCardHolderName (Optional) and pass through the sdk session.This allows him from typing the name repeatedly.
- Update Readme and ChangeLog.
## [3.6.11] - 2020-04-19
### Version release HotFix:
- CardType validation on Scanning clear on cancel
- Update Readme and ChangeLog.
## [3.6.10] - 2020-04-19
### Version release HotFix:
- CardType validation on Scanning
- Update Readme and ChangeLog.
## [3.6.9] - 2020-04-12
### Version release Enhancement:
- Added Expiry object in Card Object
- Update Readme and ChangeLog.
### Impact on existing integrations:
- The new update will not affect current integration.
## [3.6.8] - 2020-04-09
### Version release HotFixes:
- Keyboard Listener fixes
- Update Readme and ChangeLog.
### Impact on existing integrations:
- The new update will not affect current integration.
### Required changes for existing integrations before updating:
- Good to update to this release.
## [3.6.7] - 2020-04-07
### Version release HotFixes:
- Reloading of 3D securepage stopped
- Update Readme and ChangeLog
### Impact on existing integrations:
- The new update will not affect current integration.
### Required changes for existing integrations before updating:
- Any one facing the reloading issue of the 3d secure page must switch to latest release.
## [3.6.5] - 2020-04-06
### Version release HotFixes:
- TapGLKit updated from 1.15 to 1.18
### Impact on existing integrations:
- The new update will not affect current integration.
### Required changes for existing integrations before updating:
- Good to update to latest release.
## [3.6.3] - 2020-03-09
### Version release HotFixes:
- Proguard update
### Impact on existing integrations:
- The new update will not affect current integration.
### Required changes for existing integrations before updating:
- Good to update to latest release.
## [3.6.1] - 2020-03-04
### Version release HotFixes:
- Added goSellId in reference
### Impact on existing integrations:
- The new update will not affect current integration.
### Required changes for existing integrations before updating:
- If the merchant wants to use the latest feature then update to the above version.
## [3.5.1] - 2020-03-04
### Version release additional feature:
- 3DS handling on the SDK
### Impact on existing integrations:
- The new update will not affect current integration.
### Required changes for existing integrations before updating:
- If the merchant wants to use the latest featureof setting cardType then update to the above version.
## [3.5.0] - 2020-02-20
### Version release for with new feature:
- Merchant can now set cardType[CREDIT/DEBIT] and pass through the sdk session.
- sdk allows theming of dialog alert.
- extra fees check as min fees and max fees.
- payment Type added in request.
- check for application_verified.
### Impact on existing integrations:
- The new update will not affect current integration.
### Required changes for existing integrations before updating:
- If the merchant wants to use the latest featureof setting cardType then update to the above version.

## [3.3.1] - 2020-02-17
### Hot fixes :
- [#Issue:6](https://github.com/Tap-Payments/goSellSDK-AndroidX/issues/6) version released for updating from Appcompat to MaterialComponents theme.
## [3.3.0] - 2020-02-03
### Version release for supporting additional languages:
- Sdk release to support new languages German and Turkish

### Impact on existing integrations:
- The new update will not affect current integration.

### Required changes for existing integrations before updating:
- Not req
## [3.2.13] - 2020-01-30
### Version release to add interceptor:
- Added sdk version number in request interceptors internally

### Impact on existing integrations:
- The new update will not affect current integration.

### Required changes for existing integrations before updating:
- Not req
## [3.2.12] - 2020-01-22
### Version release to fix minor Bugs:
- Fix for multiple looping issue

### Impact on existing integrations:
- The new update will not affect current integration.

### Required changes for existing integrations before updating:
- Not req
## [3.2.11] - 2020-01-14
### Version release to fix minor Bugs:
- Fix for currency code

### Impact on existing integrations:
- The new update will not affect current integration.

### Required changes for existing integrations before updating:
- Not req

## [3.2.10] - 2020-01-12
### Version release to fix minor Bugs:
- Fix for date field formatting

### Impact on existing integrations:
- The new update will not affect current integration.

### Required changes for existing integrations before updating:
- Not req
## [3.2.9] - 2020-01-05
### Version release to fix minor Bugs:
- SDK serialization handling

### Impact on existing integrations:
- The new update will not affect current integration.

### Required changes for existing integrations before updating:
- Not required

## [3.2.8] - 2019-12-15
### Version release to fix minor Bugs:
- Allow hosting app to pass language to SDK through setting up SDK Session.
- Disable SDK UI click actions if user clicks pay button
- Send device information along with request header to track devices that has issues with our SDK UI and functionality
- Considers Supported currency if transaction currency is not supported

### Impact on existing integrations:
- The new update will not affect current integration.

### Required changes for existing integrations before updating:
-The hosting app that integrates with SDK has to pass local directly after configuring SDK.

### Recommended changes for existing integrations before updating:
- Merchant needs to set language as arabic in main activty .
  By default it  will consider it as english.

## [3.2.7] - 2019-11-30
### Version release to fix Arabic UI:
- Minor fixes of arabic Ui.

### Impact on existing integrations:
- The update does not impact existing integrations.

### Required changes for existing integrations before updating:
- None.

## [3.2.6] - 2019-11-21
### Minor version release to validate ThemeObject:
- If Merchant does not configure ThemeObject then it will take default.
- SDK will validate for customized Theme Object.

### Impact on existing integrations:
- The update does not impact existing integrations.

### Required changes for existing integrations before updating:
- None.

### Recommended changes for existing integrations before updating:
- None.


## [3.2.2] - 2019-10-26
### Minor version release to make fix mobile configuration change:
- Merchant has to force portrait mode for the activity that launches SDK.
- SDK internally force portrait orientation for its own activities.

### Impact on existing integrations:
- The update does not impact existing integrations.

### Required changes for existing integrations before updating:
- Double-check that activity that launches SDK is Portrait mode.

### Recommended changes for existing integrations before updating:
- None.
