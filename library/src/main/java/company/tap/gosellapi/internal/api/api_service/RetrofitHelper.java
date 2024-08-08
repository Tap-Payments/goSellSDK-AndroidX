package company.tap.gosellapi.internal.api.api_service;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import company.tap.gosellapi.BuildConfig;
import company.tap.gosellapi.internal.api.deserializers.AmountedCurrencyDeserializer;
import company.tap.gosellapi.internal.api.deserializers.PaymentOptionsResponseDeserializer;
import company.tap.gosellapi.internal.api.models.AmountedCurrency;
import company.tap.gosellapi.internal.api.responses.PaymentOptionsResponse;
import company.tap.gosellapi.internal.api.serializers.SecureSerializer;
import company.tap.gosellapi.internal.exceptions.NoAuthTokenProvidedException;
import company.tap.gosellapi.internal.interfaces.SecureSerializable;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * The type Retrofit helper.
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
public final class RetrofitHelper {
    private static Retrofit retrofit;
    private static APIService helper;
    private static final Boolean showDebug = true;

    /**
     * Gets api helper.
     *
     * @return the api helper
     */
    @Nullable
    public static APIService getApiHelper() {
        /**
         * Lazy loading
         */
        if (retrofit == null) {
            if (AppInfo.getAuthToken() == null) {
                throw new NoAuthTokenProvidedException();
            }

            OkHttpClient okHttpClient = getOkHttpClient();
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_Constants.BASE_URL)
                    .addConverterFactory(buildGsonConverter())
                    .client(okHttpClient)
                    .build();
        }

        if (helper == null) {
            helper = retrofit.create(APIService.class);
        }

        return helper;
    }

    private static GsonConverterFactory buildGsonConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        // Adding custom deserializers
        gsonBuilder.registerTypeAdapter(PaymentOptionsResponse.class, new PaymentOptionsResponseDeserializer());
        gsonBuilder.registerTypeAdapter(AmountedCurrency.class, new AmountedCurrencyDeserializer());
        gsonBuilder.registerTypeHierarchyAdapter(SecureSerializable.class, new SecureSerializer());
        Gson myGson = gsonBuilder.excludeFieldsWithoutExposeAnnotation().create();

        return GsonConverterFactory.create(myGson);
    }

    private static OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();

        httpClientBuilder.connectTimeout(30, TimeUnit.SECONDS);
        httpClientBuilder.readTimeout(30, TimeUnit.SECONDS);
        // add application interceptor to httpClientBuilder
        httpClientBuilder.addInterceptor(chain -> {
            Request request = chain.request()
                    .newBuilder()
                    .addHeader(API_Constants.AUTH_TOKEN_KEY, API_Constants.AUTH_TOKEN_PREFIX + AppInfo.getAuthToken())
                    .addHeader(API_Constants.APPLICATION, AppInfo.getApplicationInfo())
                    .addHeader(API_Constants.ACCEPT_KEY, API_Constants.ACCEPT_VALUE)
                    .addHeader(API_Constants.CONTENT_TYPE_KEY, API_Constants.CONTENT_TYPE_VALUE).build();
            /**
             * this will be used for debugging
             */
          /*  if (showDebug) {
                Log.e("dataRequestBody Request", String.valueOf(request.toString()));
                if (request.body() != null) {
                    Log.e("dataRequestBody body", bodyToString(request.body()));
                }

                Log.e("dataRequestBody Headers", String.valueOf(request.headers().toString()));
                Response response = chain.proceed(request);
                try {
                    Log.e("dataRequestBody response", response.toString());
                } catch (Exception ex) {
                    return response;
                }
            }*/

            return chain.proceed(request);
        });

        httpClientBuilder.addInterceptor(new HttpLoggingInterceptor().setLevel(showDebug ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.BODY));

        return httpClientBuilder.build();
    }

    private static String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "body null";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }

    }

}