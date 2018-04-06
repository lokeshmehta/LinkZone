package com.linkzone.linkzoneapp.Functions;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.linkzone.linkzoneapp.Activities.Login;
import com.linkzone.linkzoneapp.DataHolders.UserDetails;
import com.linkzone.linkzoneapp.R;
import com.linkzone.linkzoneapp.widget.RippleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

import static com.linkzone.linkzoneapp.R.styleable.RippleView;

public class Functions {

    private static SharedPreferences Pref;
    private static SharedPreferences.Editor editor;

    public static void setUserInformation(Activity activity, String uName, String uPassword, String json) {
        Pref = activity.getSharedPreferences("OmniSession", activity.MODE_PRIVATE);
        editor = Pref.edit();
        try {
            editor.clear();
            editor.commit();
        } catch (Exception e) {
        }
        editor.putString("Email", uName);
        editor.putString("Password", uPassword);
        editor.putString("Json", json);
        editor.commit();
    }

    public static ArrayList<String> getUserDetails(Activity activity) {
        ArrayList<String> usrDetails = new ArrayList<String>();
        Pref = activity.getSharedPreferences("OmniSession", activity.MODE_PRIVATE);
        try {
            usrDetails.add(Pref.getString("Email", "null"));
            usrDetails.add(Pref.getString("Password", "null"));
            usrDetails.add(Pref.getString("Json", "null"));
            return usrDetails;
        } catch (Exception e) {
            return usrDetails;
        }
    }

    public static String getUserName(Context activity) {
        String usrDetails = "null";
        Pref = activity.getSharedPreferences("OmniSession", activity.MODE_PRIVATE);
        try {
            usrDetails = Pref.getString("Email", "null");
            return usrDetails;
        } catch (Exception e) {
            return usrDetails;
        }
    }

    public static void setLogout(Activity activity) {
        Pref = activity.getSharedPreferences("OmniSession", activity.MODE_PRIVATE);
        editor = Pref.edit();
        try {
            editor.clear();
            editor.commit();
        } catch (Exception e) {
        }
    }

    public static JSONObject readData(InputStream inputStream) {
        int size = 0;
        JSONObject Data;
        try {
            size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            Data = new JSONObject(new String(buffer));
            return Data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void showMessage(View v, String msg, Activity activity) {
        Snackbar bar = Snackbar.make(v, msg, Snackbar.LENGTH_LONG);
        TextView tv = (TextView) bar.getView().findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextSize(17);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        else
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
        bar.getView().setBackgroundColor(ContextCompat.getColor(activity, R.color.rightColor));
        bar.show();
    }

    public static void showErrorMessage(View v, String msg, Activity activity) {
        Snackbar bar = Snackbar.make(v, msg, Snackbar.LENGTH_LONG);
        TextView tv = (TextView) bar.getView().findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextSize(17);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        else
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
        bar.getView().setBackgroundColor(ContextCompat.getColor(activity, R.color.errorColor));
        bar.show();
    }

    public static void ShowRetryMessage(View v, String msg) {
        Snackbar bar = Snackbar.make(v, msg, Snackbar.LENGTH_LONG)
                .setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        bar.setActionTextColor(Color.RED);
        TextView tv = (TextView) bar.getView().findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextColor(Color.WHITE);
        bar.show();
    }

    public static boolean isGpsEnable(Activity activity) {
        final LocationManager manager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkNetworkConnection(Activity activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    static AlertDialog ad;

    public static void showLoadingDialog(final Activity activity, final String Message) {
        LayoutInflater lf = activity.getLayoutInflater();
        AlertDialog.Builder adb = new AlertDialog.Builder(activity);
        View v = lf.inflate(R.layout.loader_layout, null);
        adb.setView(v);
        TextView txtName = (TextView) v.findViewById(R.id.loadMessage);
        txtName.setText(Message);
        ad = adb.create();
        ad.getWindow().getAttributes().windowAnimations = R.style.DialogAnimations_SmileWindow;
        ad.setCancelable(false);
        txtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ad.dismiss();
            }
        });
        ad.show();
    }

    public static void closeLoadingDialog() {
        if (ad != null) {
            ad.dismiss();
        }
    }

    static AlertDialog adSuccess;

    public static void showSuccessDialog(final Activity activity, final int messagetype, final String userName) {
        LayoutInflater lf = activity.getLayoutInflater();
        AlertDialog.Builder adb = new AlertDialog.Builder(activity);
        View v = lf.inflate(R.layout.signup_success_layout, null);
        adb.setView(v);
        TextView txtName = (TextView) v.findViewById(R.id.loadMessage);
        String message = (messagetype == 1) ? "\nThank you for registering with Omni.Care!\n\nA welcome email/sms has been sent to your registered email id/mobile number." : "\nUser Name already in used. Choose another User Name.";
        txtName.setText(message);
        adSuccess = adb.create();
        adSuccess.getWindow().getAttributes().windowAnimations = R.style.DialogAnimations_SmileWindow;
        adSuccess.setCancelable(false);
        v.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (messagetype == 1) {
                    Intent i = new Intent(activity, Login.class);
                    i.putExtra("uName", userName);
                    activity.startActivity(i);
                    activity.overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                    activity.finish();
                    adSuccess.dismiss();
                } else {
                    adSuccess.dismiss();
                }
            }
        });
        adSuccess.show();
    }

    static AlertDialog logout;

    public static void showLogoutDialog(final Activity activity) {
        AlertDialog.Builder adb = new AlertDialog.Builder(activity, R.style.ForgotP);
        View view = activity.getLayoutInflater().inflate(R.layout.logout_dialog, null);
        adb.setView(view);
        logout = adb.create();
        logout.getWindow().getAttributes().windowAnimations = R.style.DialogAnimations_SmileWindow;
        logout.setCancelable(false);

        com.linkzone.linkzoneapp.widget.RippleView yes, no;
        yes = (RippleView) view.findViewById(R.id.yes);
        yes.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Functions.setLogout(activity);
                UserDetails.getHome().finish();
                activity.finish();
                logout.dismiss();
            }
        });

        no = (RippleView) view.findViewById(R.id.no);
        no.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                logout.dismiss();
            }
        });

        logout.show();
    }
}
