package online.westbay.trackingapp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextUtils;
import android.util.TypedValue;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Anil on 5/5/2014.
 */
public class Util {


    public static String getReadableTimeFromLong(long t) {
        Date date = new Date(t);
        SimpleDateFormat df2 = new SimpleDateFormat("hh:mm a");
        return df2.format(date);
    }

    public static void showDialog(ProgressDialog mProgressDialog) {
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    /*
    function to hide dialog
     */
    public static void hideDialog(ProgressDialog mProgressDialog) {
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    public static int convertDpToPx(Context context, int dp) {
        Resources r = context.getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                r.getDisplayMetrics()
        );
        return px;
    }

    public static double getDoubleFromStringWith2DecimalPlaces(String numberString) {
        double result = 0;
        if (numberString.isEmpty()) {
            result = 0;
        } else {
            String numberWithoutComma = numberString.replace(",", "");
            String[] wholeDecimal = numberWithoutComma.split(".");
            if (wholeDecimal.length > 1) { // decimalPartExist
                if (wholeDecimal[1].isEmpty()) {
                    // this could be like 2. or something. just return 2
                    try {
                        result = Double.parseDouble(wholeDecimal[0]);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        result = 0;
                    }
                } else {
                    try {
                        result = Double.parseDouble(numberWithoutComma);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        result = 0;
                    }
                }
            } else {
                try {
                    result = Double.parseDouble(numberWithoutComma);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    result = 0;
                }
            }

        }
        return round(result, 2);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static Drawable getColoredDrawable(Activity mActivity, int drawableResId, int color) {
        Drawable d = mActivity.getResources().getDrawable(drawableResId);
        ColorFilter filter = new LightingColorFilter(
                color,
                color);
        d.setColorFilter(filter);
        return d;
    }

    public static void showNeutralAlertDialog(String message, final Activity mActivity, final boolean isCloseActivity) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(mActivity);

        if (message.contains("</font>")) {
            alert.setMessage(Html.fromHtml(message));
        } else {
            alert.setMessage(message);
        }

        alert.setNeutralButton("OK", (dialog, i) -> {
            dialog.cancel();
            if (isCloseActivity) {
                mActivity.finish();
            }
        });

        alert.show();
    }

    public static long getMillisFromStringTime(String timeSelected) {
        long millis = 0l;
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        Date val = null;
        try {
            val = dateFormat.parse(timeSelected);
            millis = val.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            millis = 0;
        }
        return millis;
    }

}