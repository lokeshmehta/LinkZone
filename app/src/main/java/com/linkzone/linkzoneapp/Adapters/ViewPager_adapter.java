package com.linkzone.linkzoneapp.Adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.linkzone.linkzoneapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ViewPager_adapter extends PagerAdapter {
    Context c;
    private List<String> _imagePaths;
    private LayoutInflater inflater;

    public ViewPager_adapter(Context c, List<String> imagePaths) {
        this._imagePaths = imagePaths;
        this.c = c;
    }



    @Override
    public int getCount() {
        return this._imagePaths.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        TouchImageView imgDisplay;

        inflater = (LayoutInflater) c
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.viewpager_cell, container,
                false);

        imgDisplay = (TouchImageView) viewLayout.findViewById(R.id.getimg);

        Picasso.with(c).load(_imagePaths.get(position)).into(imgDisplay);
        (container).addView(viewLayout);
        imgDisplay.setMaxZoom(4f);

        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        (container).removeView((RelativeLayout) object);

    }
}