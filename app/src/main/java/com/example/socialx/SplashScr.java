package com.example.socialx;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import com.example.socialx.Home_page.HomePage;
import com.example.socialx.Sign_UP_In.SignUpIn;

public class SplashScr extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_scr);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        new Handler().postDelayed(() -> {
                    startActivity(new Intent(SplashScr.this, SignUpIn.class));
                    finish();
                },
                2000);

    }


}