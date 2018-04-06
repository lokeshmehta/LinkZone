package com.linkzone.linkzoneapp.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
//import com.facebook.login.LoginManager;
import com.facebook.login.LoginManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.firebase.auth.FirebaseAuth;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.linkzone.linkzoneapp.Adapters.HomeAdapters;

import com.linkzone.linkzoneapp.ApiDetails.ApiListners.ApiListener;
import com.linkzone.linkzoneapp.ApiDetails.ApiListners.ApiResponse;
import com.linkzone.linkzoneapp.ApiDetails.ApiUrls.ApiUrl;
import com.linkzone.linkzoneapp.DataHolders.GetUserDetails;
import com.linkzone.linkzoneapp.DataHolders.Home_Profiles;
import com.linkzone.linkzoneapp.DataHolders.UserDetails;
import com.linkzone.linkzoneapp.Functions.Functions;
import com.linkzone.linkzoneapp.R;
import com.linkzone.linkzoneapp.widget.MyBoldTextView;
import com.linkzone.linkzoneapp.widget.RippleView;
import com.squareup.picasso.Picasso;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.recyclerview.animators.BaseItemAnimator;
import jp.wasabeef.recyclerview.animators.FlipInTopXAnimator;

public class Home extends AppCompatActivity implements ApiResponse {

    @BindView(R.id.GotoMessage)
    RippleView HomeToMsj;
    @BindView(R.id.GotoProfile)
    RippleView GoProfile;
    @BindView(R.id.GotoMsj)
    RippleView GoMsj;
    @BindView(R.id.GotoPhoto)
    RippleView GoPhoto;
    @BindView(R.id.GotoFeedback)
    RippleView GotoFeed;
    @BindView(R.id.GotoFav)
    RippleView GotoFav;
    @BindView(R.id.Logout)
    RippleView LogOff;
    @BindView(R.id.GotoNotification)
    RippleView GotoNotification;
    @BindView(R.id.NavDp)
    CircleImageView ProfileDp;
    @BindView(R.id.NavUsername)
    MyBoldTextView ProfileName;
    @BindView(R.id.searchIv)
    ImageView searchIv;
    @BindView(R.id.profilePercentTv)
    TextView profilePercentTv;
    @BindView(R.id.coverIv)
    ImageView coverIv;

    SwipeRefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Home_Profiles> packageNames = new ArrayList<>();
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    Toolbar toolbar1;
    ActionBarDrawerToggle actionBarDrawerToggle;
    DrawerLayout drawerLayout;
    GoogleMap googleMap;

    private static final int TIME_DELAY = 2000;
    private static long back_pressed;
    private TrackGPS gps;
    double longitude;
    double latitude;


    enum Type {
        FlipInTopX(new FlipInTopXAnimator());
        private BaseItemAnimator mAnimator;

        Type(BaseItemAnimator animator) {
            mAnimator = animator;
        }

        public BaseItemAnimator getAnimator() {
            return mAnimator;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        ButterKnife.bind(this);
        initializeFirebaseAuth();
        toolbar1 = (Toolbar) findViewById(R.id.ToolBarLay);
        setSupportActionBar(toolbar1);
        drawerLayout = (DrawerLayout) findViewById(R.id.Dlayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar1, R.string.drawerOpen, R.string.drawerClose);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        if (UserDetails.getProfileP().contains(".")) {
            int pos = UserDetails.getProfileP().indexOf(".");
            profilePercentTv.setText("Profile Completion " + UserDetails.getProfileP().substring(0, pos) + " %");
        } else {
            profilePercentTv.setText("Profile Completion " + UserDetails.getProfileP() + " %");
        }

        HomeToMsj.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Intent intent = new Intent(Home.this, MyContacts.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
            }
        });
        GoPhoto.setVisibility(View.GONE);
        GoProfile.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Intent intent = new Intent(Home.this, Profile.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
            }
        });
        GoMsj.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Intent intent = new Intent(Home.this, MyContacts.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
            }
        });
        GoPhoto.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Intent intent = new Intent(Home.this, ShowImageGall.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                finish();
            }
        });

        GotoFeed.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Intent intent = new Intent(Home.this, FeedbackLay.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
            }
        });
        GotoFav.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Intent intent = new Intent(Home.this, FavLayout.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);

            }
        });

        GotoNotification.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Intent intent = new Intent(Home.this, UserNotification.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);

            }
        });

        LogOff.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                showLogoutDialog(Home.this);
            }
        });

        searchIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        gps = new TrackGPS(Home.this);
        if (gps.canGetLocation()) {
            longitude = gps.getLongitude();
            latitude = gps.getLatitude();

            SharedPreferences shrd = getSharedPreferences("LatLong", MODE_PRIVATE);
            SharedPreferences.Editor editor = shrd.edit();
            editor.putString("lat", latitude + "");
            editor.putString("long", longitude + "");
            editor.apply();

        } else {
            gps.showSettingsAlert();
        }

        APILocation();

        bindPackageList();
        swipeRefreshLayout = (SwipeRefreshLayout) (findViewById(R.id.SwipeRefresh));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                APILocation();
            }
        });


        ProfileDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable) ProfileDp.getDrawable()).getBitmap();
                showProfileImage(bitmap);
            }
        });

    }

    private void bindPackageList() {
        recyclerView = (RecyclerView) findViewById(R.id.HomeRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new HomeAdapters(Home.this, packageNames);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(Type.FlipInTopX.getAnimator());
        recyclerView.getItemAnimator().setAddDuration(500);
        recyclerView.getItemAnimator().setRemoveDuration(500);
    }

    private void initializeFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
//                    if(UserDetails.getLogintype()==1)
//                    {
//                        UserDetails.getHome().finish();
//                        finish();
//                    }
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListner != null) {
            mAuth.removeAuthStateListener(mAuthListner);
        }
    }

    AlertDialog logout;

    public void showLogoutDialog(final Activity activity) {
        AlertDialog.Builder adb = new AlertDialog.Builder(activity, R.style.ForgotP);
        View view = activity.getLayoutInflater().inflate(R.layout.logout_dialog, null);
        adb.setView(view);
        logout = adb.create();
        logout.getWindow().getAttributes().windowAnimations = R.style.DialogAnimations_SmileWindow;
        logout.setCancelable(false);

        RippleView yes, no;
        yes = (RippleView) view.findViewById(R.id.yes);
        yes.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                try {
                    LoginManager.getInstance().logOut();
                    mAuth.signOut();
                    Functions.setLogout(activity);
//                    UserDetails.getHome().finish();
//                    activity.finish();
                    Intent intent = new Intent(Home.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                } catch (Exception e) {
                }
                Home.this.finish();
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

    public void APILocation() {

        if (Functions.checkNetworkConnection(Home.this)) {

            Functions.showLoadingDialog(Home.this, "Getting Details");
            JSONObject jsonObject = new JSONObject();

            SharedPreferences shrd = getSharedPreferences("LatLong", MODE_PRIVATE);

            try {
                jsonObject.put("user_id", UserDetails.getUID());
                jsonObject.put("user_email", UserDetails.getUEmail());
                jsonObject.put("lati_no", shrd.getString("lat", "0.0"));
                jsonObject.put("longi_no", shrd.getString("long", "0.0"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            AndroidNetworking.initialize(Home.this);
            AndroidNetworking.post(ApiUrl.UpdateLocation)
                    .setPriority(Priority.MEDIUM)
                    .setTag("Token")
                    .addJSONObjectBody(jsonObject)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            APIWork();
                            APIGetDetails();
                            Functions.closeLoadingDialog();
                            swipeRefreshLayout.setRefreshing(false);

                        }

                        @Override
                        public void onError(ANError anError) {

                        }
                    });
        } else {
            Toast.makeText(Home.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    public void APIWork() {
        SharedPreferences shrd = getSharedPreferences("LatLong", MODE_PRIVATE);

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_email", UserDetails.getUEmail());
            jsonObject.put("lati_no", shrd.getString("lat", "0.0"));
            jsonObject.put("longi_no", shrd.getString("long", "0.0"));
            // Functions.showLoadingDialog(Home.this, "Getting Details");
            new ApiListener(Home.this, ApiUrl.NearByUser, jsonObject).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onApiResponse(String response) {

        Log.e("Response", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray ar = jsonObject.getJSONArray("nearBy");

            if (jsonObject.getString("response").matches("success")) {
                packageNames.clear();
                for (int i = 0; i < ar.length(); i++) {
                    Home_Profiles home_pro = new Home_Profiles();
                    home_pro.setUName(ar.getJSONObject(i).getString("user_name"));
                    home_pro.setUID(ar.getJSONObject(i).getString("id"));
                    home_pro.setUEmail(ar.getJSONObject(i).getString("user_email"));
                    home_pro.setUImage(ar.getJSONObject(i).getString("image"));
                    home_pro.setUMobile(ar.getJSONObject(i).getString("user_mobile"));
                    home_pro.setUDob(ar.getJSONObject(i).getString("user_dob"));
                    home_pro.setUStatus(ar.getJSONObject(i).getString("timeline_status"));
                    home_pro.setUInte(ar.getJSONObject(i).getString("interested_in"));
                    home_pro.setULive(ar.getJSONObject(i).getString("lives_in"));
                    home_pro.setUWorkAt(ar.getJSONObject(i).getString("working_at"));
                    home_pro.setUWorkAs(ar.getJSONObject(i).getString("working_as"));
                    home_pro.setUHHC(ar.getJSONObject(i).getString("high_school"));
                    home_pro.setUSSC(ar.getJSONObject(i).getString("intermediate"));
                    home_pro.setUGrad(ar.getJSONObject(i).getString("graduation"));
                    home_pro.setUPGrad(ar.getJSONObject(i).getString("Post_graduation"));
                    home_pro.setUWeb(ar.getJSONObject(i).getString("add_website"));
                    home_pro.setUFacebook(ar.getJSONObject(i).getString("facebook_page"));
                    home_pro.setUDistenace(ar.getJSONObject(i).getString("distance"));
                    home_pro.setFriend(ar.getJSONObject(i).getString("is_friend"));
                    home_pro.setIs_favorite(ar.getJSONObject(i).getString("is_favorite"));
                    home_pro.setCoverImg(ar.getJSONObject(i).getString("cover_pic"));

                    packageNames.add(home_pro);
                }
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(Home.this, "Seems that member already added", Toast.LENGTH_SHORT).show();

            }
        } catch (JSONException e) {

        }

    }

    public void APIGetDetails() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_email", UserDetails.getUEmail());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.initialize(Home.this);
        AndroidNetworking.post(ApiUrl.GetUpdateProfile)
                .setPriority(Priority.MEDIUM)
                .setTag("Token")
                .addJSONObjectBody(jsonObject)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("ResponseGetDetails", response.toString());

                        try {
                            JSONArray ar = response.getJSONArray("userData");
                            if (response.getString("response").matches("success")) {
                                GetUserDetails getUserDetails = new GetUserDetails();
                                getUserDetails.setUName(ar.getJSONObject(0).getString("user_name"));
                                getUserDetails.setUEmail(ar.getJSONObject(0).getString("user_email"));
                                getUserDetails.setUImage(ar.getJSONObject(0).getString("image"));
                                getUserDetails.setUMob(ar.getJSONObject(0).getString("user_mobile"));
                                getUserDetails.setUDob(ar.getJSONObject(0).getString("user_dob"));
                                getUserDetails.setUstatus(ar.getJSONObject(0).getString("timeline_status"));
                                getUserDetails.setUinterestin(ar.getJSONObject(0).getString("interested_in"));
                                getUserDetails.setUlives(ar.getJSONObject(0).getString("lives_in"));
                                getUserDetails.setUworkat(ar.getJSONObject(0).getString("working_at"));
                                getUserDetails.setUworkin(ar.getJSONObject(0).getString("working_as"));
                                getUserDetails.setUHHC(ar.getJSONObject(0).getString("high_school"));
                                getUserDetails.setUSSC(ar.getJSONObject(0).getString("intermediate"));
                                getUserDetails.setUGradu(ar.getJSONObject(0).getString("graduation"));
                                getUserDetails.setUPostGra(ar.getJSONObject(0).getString("Post_graduation"));
                                getUserDetails.setUadd_website(ar.getJSONObject(0).getString("add_website"));
                                getUserDetails.setUfacebook_page(ar.getJSONObject(0).getString("facebook_page"));
                                getUserDetails.setUser_gender(ar.getJSONObject(0).getString("user_gender"));
                                getUserDetails.setCover_pic(ar.getJSONObject(0).getString("cover_pic"));

                                ProfileName.setText(UserDetails.getUName());

                                Picasso.with(Home.this)
                                        .load("http://xperiaindia.com/linkZone/userImages/" + UserDetails.getUID() + "/" + GetUserDetails.getCover_pic())
                                        .placeholder(R.drawable.coverback)
                                        .into(coverIv);

                                Log.d("ProfileIv",GetUserDetails.getUImage()+",,,");
                                Log.d("SocialIv",UserDetails.getSocialImage()+",,,");

                                if (response.getString("profileP").contains(".")) {
                                    int pos = UserDetails.getProfileP().indexOf(".");
                                    profilePercentTv.setText("Profile Completion " + response.getString("profileP").substring(0, pos) + " %");
                                } else {
                                    profilePercentTv.setText("Profile Completion " + response.getString("profileP") + " %");
                                }


                                if (GetUserDetails.getUImage().length() > 0) {

                                    Picasso.with(Home.this)
                                            .load("http://xperiaindia.com/linkZone/userImages/" + UserDetails.getUID() + "/" + GetUserDetails.getUImage())
                                            .placeholder(R.drawable.dpplaceh)
                                            .into(ProfileDp);

                                } else {

                                    Picasso.with(Home.this)
                                            .load(UserDetails.getSocialImage())
                                            .placeholder(R.drawable.dpplaceh)
                                            .into(ProfileDp);
                                }
                                Log.e("ImageRes", "http://xperiaindia.com/linkZone/userImages/" + UserDetails.getUID() + "/" + GetUserDetails.getUImage());

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.e("Error", anError.getMessage());
                    }
                });

    }

    @Override
    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(getBaseContext(), "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
    }

    class TrackGPS extends Service implements LocationListener {

        private final Context mContext;

        boolean checkGPS = false;

        boolean checkNetwork = false;

        boolean canGetLocation = false;

        Location loc;
        double latitude;
        double longitude;


        private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;


        private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;

        protected LocationManager locationManager;

        public TrackGPS(Context mContext) {
            this.mContext = mContext;
            getLocation();
        }

        private Location getLocation() {

            try {
                locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

                // getting GPS status
                checkGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                // getting network status
                checkNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!checkGPS && !checkNetwork) {
                    Toast.makeText(mContext, "No Service Provider Available", Toast.LENGTH_SHORT).show();
                } else {
                    this.canGetLocation = true;
                    // First get location from Network Provider
                    if (checkNetwork) {
                        //Toast.makeText(mContext, "Network", Toast.LENGTH_SHORT).show();

                        try {
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            //Log.d("Network", "Network");
                            if (locationManager != null) {
                                loc = locationManager
                                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                            }

                            if (loc != null) {
                                latitude = loc.getLatitude();
                                longitude = loc.getLongitude();
                                Log.d("Lat", latitude + "");

                            }
                        } catch (SecurityException e) {

                        }
                    }
                }
                // if GPS Enabled get lat/long using GPS Services
                if (checkGPS) {
                    //Toast.makeText(mContext, "GPS", Toast.LENGTH_SHORT).show();
                    if (loc == null) {
                        try {
                            locationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_BW_UPDATES,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locationManager != null) {
                                loc = locationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (loc != null) {
                                    latitude = loc.getLatitude();
                                    longitude = loc.getLongitude();

                                    Log.d("Lat2", latitude + "");

                                }
                            }
                        } catch (SecurityException e) {

                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return loc;
        }

        public double getLongitude() {
            if (loc != null) {
                longitude = loc.getLongitude();
            }
            return longitude;
        }

        public double getLatitude() {
            if (loc != null) {
                latitude = loc.getLatitude();
            }
            return latitude;
        }

        public boolean canGetLocation() {
            return this.canGetLocation;
        }

        public void showSettingsAlert() {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);


            alertDialog.setTitle("GPS Not Enabled");

            alertDialog.setMessage("Do you wants to turn On GPS");


            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    mContext.startActivity(intent);
                }
            });


            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });


            alertDialog.show();
        }


        public void stopUsingGPS() {
            if (locationManager != null) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.removeUpdates(TrackGPS.this);
            }
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    }


    private void showProfileImage(Bitmap bitmap) {
        Dialog dialog = new Dialog(Home.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.picture_alert_layout);
        dialog.setCancelable(true);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.profileIv);
        imageView.setImageBitmap(bitmap);


        dialog.show();
    }

}
