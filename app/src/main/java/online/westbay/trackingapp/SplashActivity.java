package online.westbay.trackingapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import online.westbay.trackingapp.login.LoginRegisterActivity;
import online.westbay.trackingapp.mainpage.MainActivity;
import online.westbay.trackingapp.models.UserDTO;
import online.westbay.trackingapp.utils.SharedPrefs;

import online.westbay.trackingapp.R;

/**
 * Created by Gowtham Chandrasekar on 24-05-2016.
 */
public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;

    String onBoardingStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        moveActivitySplashTimeOut();

    }

    private void moveActivitySplashTimeOut() {

        new Handler().postDelayed(() -> {
            UserDTO userDTO = SharedPrefs.getUser(SplashActivity.this);
            if (userDTO == null) {
                Intent i = new Intent(SplashActivity.this, LoginRegisterActivity.class);
                startActivity(i);
            } else {

                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
            }
            finish();
        }, SPLASH_TIME_OUT);
    }


}
