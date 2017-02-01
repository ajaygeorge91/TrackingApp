package online.westbay.trackingapp.services;

import android.content.Intent;
import android.util.Log;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import online.westbay.trackingapp.App;
import online.westbay.trackingapp.login.LoginRegisterActivity;
import online.westbay.trackingapp.utils.Constants;
import online.westbay.trackingapp.utils.SharedPrefs;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ajay on 02-May-16.
 */

public class ServiceGenerator {

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        return createService(serviceClass, SharedPrefs.getToken(App.getInstance()));
    }

    public static <S> S createService(Class<S> serviceClass, final String authToken) {
        if (authToken != null) {
            Log.e("app",authToken);
            httpClient.addInterceptor(chain -> {
                Request original = chain.request();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .header("X-Auth-Token", authToken)
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();

                return chain.proceed(request);
            });
        }

        httpClient.addInterceptor(chain -> {
            Request request = chain.request();
            Response response = chain.proceed(request);

            boolean unAuthorized = (response.code() == 401);
            if (unAuthorized) {
                App.logout();
            }
            return response;
        });

        OkHttpClient client = httpClient
                .connectTimeout(21, TimeUnit.SECONDS)
                .readTimeout(21, TimeUnit.SECONDS)
                .writeTimeout(21, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = builder.
                client(client).build();
        return retrofit.create(serviceClass);
    }


}
