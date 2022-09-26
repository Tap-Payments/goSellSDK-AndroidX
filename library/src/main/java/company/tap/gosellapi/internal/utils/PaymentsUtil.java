package company.tap.gosellapi.internal.utils;

/**
 * Created by AhlaamK on 11/28/21.
 * <p>
 * Copyright (c) 2021    Tap Payments.
 * All rights reserved.
 **/

import android.app.Activity;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.android.gms.wallet.PaymentsClient;
import com.google.android.gms.wallet.Wallet;
import com.google.android.gms.wallet.WalletConstants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import company.tap.gosellapi.internal.Constants;
import company.tap.gosellapi.internal.api.enums.CardScheme;
import company.tap.gosellapi.internal.api.models.PaymentOption;
import company.tap.gosellapi.internal.data_managers.PaymentDataManager;
import company.tap.gosellapi.open.data_manager.PaymentDataSource;
import company.tap.gosellapi.open.enums.GPayWalletMode;
import company.tap.gosellapi.open.enums.OperationMode;
import company.tap.tapcardvalidator_android.CardBrand;

/**
 * Contains helper static methods for dealing with the Payments API.
 *
 * <p>Many of the parameters used in the code are optional and are set here merely to call out their
 * existence. Please consult the documentation to learn more and feel free to remove ones not
 * relevant to your implementation.
 */
public class PaymentsUtil {

    public static final BigDecimal CENTS_IN_A_UNIT = new BigDecimal(100d);

    /**
     * Create a Google Pay API base request object with properties used in all requests.
     *
     * @return Google Pay API base request object.
     * @throws JSONException
     */
    private static JSONObject getBaseRequest() throws JSONException {
        return new JSONObject().put("apiVersion", PaymentDataSource.getInstance().getGooglePaymentOptions().get(0).getApiVersion()).put("apiVersionMinor",  PaymentDataSource.getInstance().getGooglePaymentOptions().get(0).getApiVersionMinor());
    }

    /**
     * Creates an instance of {@link PaymentsClient} for use in an {@link Activity} using the
     * environment and theme set in {@link Constants}.
     *
     * @param activity is the caller's activity.
     */
    public static PaymentsClient createPaymentsClient(Activity activity) {
        Wallet.WalletOptions walletOptions = null;

        if(PaymentDataSource.getInstance().getGooglePayWalletMode()!=null && PaymentDataSource.getInstance().getGooglePayWalletMode()!=null){
            if(PaymentDataSource.getInstance().getGooglePayWalletMode().equals(GPayWalletMode.ENVIRONMENT_TEST)){
               walletOptions =
                        new Wallet.WalletOptions.Builder().setEnvironment(WalletConstants.ENVIRONMENT_TEST).build();
            }else  if(PaymentDataSource.getInstance().getGooglePayWalletMode().equals(GPayWalletMode.ENVIRONMENT_PRODUCTION)){
                walletOptions =
                        new Wallet.WalletOptions.Builder().setEnvironment(WalletConstants.ENVIRONMENT_PRODUCTION).build();
            }else walletOptions= new Wallet.WalletOptions.Builder().setEnvironment(WalletConstants.ENVIRONMENT_TEST).build();
        }

        assert walletOptions != null;
        return Wallet.getPaymentsClient(activity, walletOptions);
    }

    /**
     * Gateway Integration: Identify your gateway and your app's gateway merchant identifier.
     *
     * <p>The Google Pay API response will return an encrypted payment method capable of being charged
     * by a supported gateway after payer authorization.
     *
     * <p>TODO: Check with your gateway on the parameters to pass and modify them in Constants.java.
     *
     * @return Payment data tokenization for the CARD payment method.
     * @throws JSONException
     * @see <a href=
     * "https://developers.google.com/pay/api/android/reference/object#PaymentMethodTokenizationSpecification">PaymentMethodTokenizationSpecification</a>
     */
    private static JSONObject getGatewayTokenizationSpecification() throws JSONException {
        //todo :29Jun22 replace value coming from API>>PENDING GATEWAY ID
        return new JSONObject() {{
            put("type", "PAYMENT_GATEWAY");
            put("parameters", new JSONObject() {{
                assert PaymentDataSource.getInstance().getGooglePaymentOptions() != null;
                put("gateway", Constants.GATEWAY_ID);
              //  put("gateway", PaymentDataSource.getInstance().getGooglePaymentOptions().get(0).getGatewayName());
                put("gatewayMerchantId", PaymentDataSource.getInstance().getGooglePaymentOptions().get(0).getGatewayMerchantId());
            }});
        }};
    }

    /**
     * {@code DIRECT} Integration: Decrypt a response directly on your servers. This configuration has
     * additional data security requirements from Google and additional PCI DSS compliance complexity.
     *
     * <p>Please refer to the documentation for more information about {@code DIRECT} integration. The
     * type of integration you use depends on your payment processor.
     *
     * @return Payment data tokenization for the CARD payment method.
     * @throws JSONException
     * @see <a
     * href="https://developers.google.com/pay/api/android/reference/object#PaymentMethodTokenizationSpecification">PaymentMethodTokenizationSpecification</a>
     */
/*
    private static JSONObject getDirectTokenizationSpecification()
            throws JSONException, RuntimeException {
        if (Constants.DIRECT_TOKENIZATION_PARAMETERS.isEmpty()
                || Constants.DIRECT_TOKENIZATION_PUBLIC_KEY.isEmpty()
                || Constants.DIRECT_TOKENIZATION_PUBLIC_KEY == null
                || Constants.DIRECT_TOKENIZATION_PUBLIC_KEY == "REPLACE_ME") {
            throw new RuntimeException(
                    "Please edit the Constants.java file to add protocol version & public key.");
        }
        JSONObject tokenizationSpecification = new JSONObject();

        tokenizationSpecification.put("type", "DIRECT");
        JSONObject parameters = new JSONObject(Constants.DIRECT_TOKENIZATION_PARAMETERS);
        tokenizationSpecification.put("parameters", parameters);

        return tokenizationSpecification;
    }
*/

    /**
     * Card networks supported by your app and your gateway.
     *
     * <p>TODO: Confirm card networks supported by your app and gateway & update in Constants.java.
     *
     * @return Allowed card networks
     * @see <a
     * href="https://developers.google.com/pay/api/android/reference/object#CardParameters">CardParameters</a>
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static JSONArray getAllowedCardNetworks() {
        return new JSONArray(Constants.SUPPORTED_NETWORKS);
    }

    /**
     * Card authentication methods supported by your app and your gateway.
     *
     * <p>TODO: Confirm your processor supports Android device tokens on your supported card networks
     * and make updates in Constants.java.
     *
     * @return Allowed card authentication methods.
     * @see <a
     * href="https://developers.google.com/pay/api/android/reference/object#CardParameters">CardParameters</a>
     */
    private static JSONArray getAllowedCardAuthMethods() {
        return new JSONArray(Constants.SUPPORTED_METHODS);
    }

    /**
     * Describe your app's support for the CARD payment method.
     *
     * <p>The provided properties are applicable to both an IsReadyToPayRequest and a
     * PaymentDataRequest.
     *
     * @return A CARD PaymentMethod object describing accepted cards.
     * @throws JSONException
     * @see <a
     * href="https://developers.google.com/pay/api/android/reference/object#PaymentMethod">PaymentMethod</a>
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static JSONObject getBaseCardPaymentMethod() throws JSONException {
        List<String> capCardBrandList = new ArrayList<>();
        JSONObject cardPaymentMethod = new JSONObject();
        cardPaymentMethod.put("type", "CARD");
       // System.out.println("Payment data"+PaymentDataSource.getInstance().getGooglePaymentOptions().get(0).getSupportedCardBrands());
        JSONObject parameters = new JSONObject();
        parameters.put("allowedAuthMethods", new JSONArray(PaymentDataSource.getInstance().getGooglePaymentOptions().get(0).getAllowed_auth_methods()));
       // parameters.put("allowedAuthMethods", getAllowedCardAuthMethods());
       // parameters.put("allowedCardNetworks", getAllowedCardNetworks());
        String newValue = null;
        //Logic done to get cardbrands in capitals
        for (int i = 0; i < PaymentDataSource.getInstance().getGooglePaymentOptions().get(0).getSupportedCardBrands().size(); i++) {
            if (PaymentDataSource.getInstance().getGooglePaymentOptions().get(0).getSupportedCardBrands().get(i) != null) {
                newValue = String.valueOf(PaymentDataSource.getInstance().getGooglePaymentOptions().get(0).getSupportedCardBrands().get(i)).toUpperCase();
            }

            capCardBrandList.add(i, newValue);
        }
        parameters.put("allowedCardNetworks", new JSONArray(capCardBrandList));
        cardPaymentMethod.put("parameters", parameters);

        return cardPaymentMethod;
    }

    /**
     * Describe the expected returned payment data for the CARD payment method
     *
     * @return A CARD PaymentMethod describing accepted cards and optional fields.
     * @throws JSONException
     * @see <a
     * href="https://developers.google.com/pay/api/android/reference/object#PaymentMethod">PaymentMethod</a>
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static JSONObject getCardPaymentMethod() throws JSONException {
        JSONObject cardPaymentMethod = getBaseCardPaymentMethod();
        cardPaymentMethod.put("tokenizationSpecification", getGatewayTokenizationSpecification());

        return cardPaymentMethod;
    }

    /**
     * An object describing accepted forms of payment by your app, used to determine a viewer's
     * readiness to pay.
     *
     * @return API version and payment methods supported by the app.
     * @see <a
     * href="https://developers.google.com/pay/api/android/reference/object#IsReadyToPayRequest">IsReadyToPayRequest</a>
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Optional<JSONObject> getIsReadyToPayRequest() {
        try {
            JSONObject isReadyToPayRequest = getBaseRequest();
            isReadyToPayRequest.put(
                    "allowedPaymentMethods", new JSONArray().put(getBaseCardPaymentMethod()));

            return Optional.of(isReadyToPayRequest);

        } catch (JSONException e) {
            return Optional.empty();
        }
    }

    /**
     * Provide Google Pay API with a payment amount, currency, and amount status.
     *
     * @return information about the requested payment.
     * @throws JSONException
     * @see <a
     * href="https://developers.google.com/pay/api/android/reference/object#TransactionInfo">TransactionInfo</a>
     */
    private static JSONObject getTransactionInfo(String price) throws JSONException {
        assert PaymentDataSource.getInstance().getGooglePaymentOptions() != null;
        JSONObject transactionInfo = new JSONObject();
        transactionInfo.put("totalPrice", price);
        transactionInfo.put("totalPriceStatus", "FINAL");
        transactionInfo.put("countryCode",PaymentDataSource.getInstance().getGooglePaymentOptions().get(0).getAcquirerCountryCode());
        transactionInfo.put("currencyCode", PaymentDataManager.getInstance().getPaymentOptionsDataManager().getSelectedCurrency().getCurrency());
        transactionInfo.put("checkoutOption", "COMPLETE_IMMEDIATE_PURCHASE");

        return transactionInfo;
    }

    /**
     * Information about the merchant requesting payment information
     *
     * @return Information about the merchant.
     * @throws JSONException
     * @see <a
     * href="https://developers.google.com/pay/api/android/reference/object#MerchantInfo">MerchantInfo</a>
     */
    private static JSONObject getMerchantInfo() throws JSONException {
        assert PaymentDataManager.getInstance().getSDKSettings()!=null;
        return new JSONObject().put("merchantName", PaymentDataManager.getInstance().getSDKSettings().getData().getMerchant().getName());
    }

    /**
     * An object describing information requested in a Google Pay payment sheet
     *
     * @return Payment data expected by your app.
     * @see <a
     * href="https://developers.google.com/pay/api/android/reference/object#PaymentDataRequest">PaymentDataRequest</a>
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Optional<JSONObject> getPaymentDataRequest(long priceCents) {

        final String price = PaymentsUtil.centsToString(priceCents);

        try {
            JSONObject paymentDataRequest = PaymentsUtil.getBaseRequest();
            paymentDataRequest.put(
                    "allowedPaymentMethods", new JSONArray().put(PaymentsUtil.getCardPaymentMethod()));
            paymentDataRequest.put("transactionInfo", PaymentsUtil.getTransactionInfo(price));
            paymentDataRequest.put("merchantInfo", PaymentsUtil.getMerchantInfo());

      /* An optional shipping address requirement is a top-level property of the PaymentDataRequest
      JSON object. */
           // paymentDataRequest.put("shippingAddressRequired", true);

           // JSONObject shippingAddressParameters = new JSONObject();
           // shippingAddressParameters.put("phoneNumberRequired", false);

          //  JSONArray allowedCountryCodes = new JSONArray(Constants.SHIPPING_SUPPORTED_COUNTRIES);

           // shippingAddressParameters.put("allowedCountryCodes", allowedCountryCodes);
          //  paymentDataRequest.put("shippingAddressParameters", shippingAddressParameters);
            return Optional.of(paymentDataRequest);

        } catch (JSONException e) {
            return Optional.empty();
        }
    }

    /**
     * Converts cents to a string format accepted by {@link PaymentsUtil#getPaymentDataRequest}.
     *
     * @param cents value of the price in cents.
     */
    public static String centsToString(long cents) {
        return new BigDecimal(cents)
                .toString();
    }
}
