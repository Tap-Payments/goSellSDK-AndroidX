# CHANGELOG
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
