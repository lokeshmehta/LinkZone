package com.linkzone.linkzoneapp.Activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.linkzone.linkzoneapp.R;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SplashScreen extends AppCompatActivity {

    @BindView(R.id.applogo)ImageView tittle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);
        ButterKnife.bind(this);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        tittle.startAnimation(animation);

        final Intent intent = new Intent(this,Login.class);
        Thread thread = new Thread()
        {
            @Override
            public void run() {

                try {
                    sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally {
                    startActivity(intent);
                    finish();

                }
            }
        };
        thread.start();
    }
}
