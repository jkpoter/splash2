package com.example.splash2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.login.widget.LoginButton;
import com.google.android.gms.maps.GoogleMap;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {

    private LoginButton loginButton;
    private CircleImageView circleImageView;
    private TextView txtname,  txtEmail;

GoogleMap Map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        setTheme(R.style.AppTheme);
        startActivity(new Intent(MainActivity.this,RegistrarEmpresa.class));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
