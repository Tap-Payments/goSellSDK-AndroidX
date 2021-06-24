# CHANGELOG
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
