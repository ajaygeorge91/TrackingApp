package online.westbay.trackingapp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.util.HashMap;

import online.westbay.trackingapp.login.LoginRegisterActivity;
import online.westbay.trackingapp.utils.SharedPrefs;


/**
 * Created by ajay on 02-May-16.
 */
public class App extends Application {


    private static App instance;
    ConnectivityManager connectivityManager;
    boolean connected = false;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }


    public static synchronized App getInstance() {
        return instance;
    }

    public static synchronized void logout() {
        SharedPrefs.logout(App.getInstance());
        Intent intent = new Intent(App.getInstance().getApplicationContext(), LoginRegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        App.getInstance().startActivity(intent);
    }

    public boolean isOnline() {
        try {
            connectivityManager = (ConnectivityManager) instance
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;


        } catch (Exception e) {
            System.out.println("CheckConnectivity Exception: " + e.getMessage());
            Log.v("connectivity", e.toString());
        }
        return connected;
    }


}
