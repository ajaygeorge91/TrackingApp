package online.westbay.trackingapp.utils;

import android.content.Context;

import com.google.gson.Gson;

import online.westbay.trackingapp.models.UserDTO;

/**
 * Created by ajay on 02-May-16.
 */
public class SharedPrefs {
    private static final String SharedPrefs = "MyAppSharedPrefs";

    private static final String sp_Token = "sp_Token";
    private static final String sp_User = "sp_User";

    public static String getToken(Context context) {
        return context.getSharedPreferences(SharedPrefs, Context.MODE_PRIVATE).getString(sp_Token, "");
    }

    public static void setToken(Context context, String token) {
        context.getSharedPreferences(SharedPrefs, Context.MODE_PRIVATE).edit().putString(sp_Token, token).apply();
    }

    public static UserDTO getUser(Context context) {
        final String string = context.getSharedPreferences(SharedPrefs, Context.MODE_PRIVATE).getString(sp_User, "");
        return string.isEmpty() ? null : new Gson().fromJson(string, UserDTO.class);
    }

    public static void setUser(Context context, UserDTO userDTO) {
        context.getSharedPreferences(SharedPrefs, Context.MODE_PRIVATE).edit().putString(sp_User, new Gson().toJson(userDTO)).apply();
    }

    public static void logout(Context context) {

        context.getSharedPreferences(SharedPrefs, Context.MODE_PRIVATE).edit().putString(sp_Token, "").apply();
        context.getSharedPreferences(SharedPrefs, Context.MODE_PRIVATE).edit().putString(sp_User, "").apply();
    }

}
