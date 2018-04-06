package com.linkzone.linkzoneapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


public class MyReciever extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {

        if (intent.getAction() != null) {
            {
                Toast.makeText(context, "Gps Settings Changed", Toast.LENGTH_SHORT).show();

//                SmartLocation.with(context).location()
//                        .start(new OnLocationUpdatedListener() {
//                            @Override
//                            public void onLocationUpdated(Location location) {
//                                Log.d("Position-->",location.getLatitude()+"");
//                            }
//                        });

                Intent mIntent = new Intent(context,LocationService.class);
                context.startService(mIntent);



            }

        }
    }
}
