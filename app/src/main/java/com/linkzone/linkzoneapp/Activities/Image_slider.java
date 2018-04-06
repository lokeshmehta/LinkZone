package com.linkzone.linkzoneapp.Activities;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.linkzone.linkzoneapp.Adapters.ViewPager_adapter;
import com.linkzone.linkzoneapp.R;

import java.util.ArrayList;

public class Image_slider extends AppCompatActivity {

    ViewPager viewPager;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setContentView(R.layout.activity_image_slider);
            ArrayList<String> getimglink;
            getimglink= (ArrayList<String>) getIntent().getSerializableExtra("image");

            viewPager = (ViewPager) findViewById(R.id.vwpgr);
            for (int i=0;i<getimglink.size();i++){
                viewPager.setAdapter(new ViewPager_adapter(getApplicationContext(),getimglink));
            }
            viewPager.setCurrentItem(getIntent().getExtras().getInt("adapterPosition"));

    }
}



