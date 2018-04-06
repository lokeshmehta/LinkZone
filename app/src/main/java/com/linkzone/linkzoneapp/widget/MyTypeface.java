package com.linkzone.linkzoneapp.widget;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Vikrant on 1/15/2017.
 */

public class MyTypeface
{
    public static Typeface getBoldFont(Context c)
    {
        return Typeface.createFromAsset(c.getAssets(),"fonts/rabold.ttf");
    }

    public static Typeface getNormalFont(Context c)
    {
        return Typeface.createFromAsset(c.getAssets(),"fonts/rareg.ttf");
    }

}
