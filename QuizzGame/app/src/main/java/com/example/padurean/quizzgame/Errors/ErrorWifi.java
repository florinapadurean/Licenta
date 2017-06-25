package com.example.padurean.quizzgame.Errors;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;

import com.example.padurean.quizzgame.Login.Login;
import com.example.padurean.quizzgame.R;

public class ErrorWifi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_error_wifi);
        ImageButton turnOnWiFi = (ImageButton) findViewById(R.id.wifi_off_btn);
        turnOnWiFi.startAnimation(AnimationUtils.loadAnimation(this, R.anim.shake));
        turnOnWiFi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            Log.i("errwifi", "not null");
            startActivity(new Intent(ErrorWifi.this, Login.class));
            this.finish();
        }
    }


}
