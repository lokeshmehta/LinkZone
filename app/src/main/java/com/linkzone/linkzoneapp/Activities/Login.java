package com.linkzone.linkzoneapp.Activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.linkedin.platform.APIHelper;
import com.linkedin.platform.LISessionManager;
import com.linkedin.platform.errors.LIApiError;
import com.linkedin.platform.errors.LIAuthError;
import com.linkedin.platform.listeners.AuthListener;
import com.linkedin.platform.utils.Scope;
import com.linkzone.linkzoneapp.ApiDetails.ApiListners.ApiListener;
import com.linkzone.linkzoneapp.ApiDetails.ApiListners.ApiResponse;
import com.linkzone.linkzoneapp.ApiDetails.ApiUrls.ApiUrl;
import com.linkzone.linkzoneapp.DataHolders.UserDetails;
import com.linkzone.linkzoneapp.Functions.Functions;
import com.linkzone.linkzoneapp.R;
import com.linkzone.linkzoneapp.widget.MyEditText;
import com.linkzone.linkzoneapp.widget.MyTextView;
import com.linkzone.linkzoneapp.widget.RippleView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Login extends AppCompatActivity implements ApiResponse {

    @BindView(R.id.ExapndLogin)
    ExpandableLinearLayout expandLogin;
    @BindView(R.id.ExapndSignup)
    ExpandableLinearLayout expandSignup;
    @BindView(R.id.CollapseSocial)
    ExpandableLinearLayout collapseSocial;
    @BindView(R.id.GotoLogin)
    RelativeLayout gotoLogin;
    @BindView(R.id.GotoSignUp)
    RelativeLayout gotoSign;
    @BindView(R.id.SocialVisi)
    ImageView ViewSocial;
    @BindView(R.id.FbLogin)
    RelativeLayout GotoFb;
    @BindView(R.id.GotoGoogle)
    RelativeLayout GLogin;
    @BindView(R.id.ParentLog)
    LinearLayout LoginParrenLayout;

    /////////////////*****************EMAIL LOGIN*******************///////////////////////
    @BindView(R.id.rememberMe)
    CheckBox rememberMe;
    @BindView(R.id.EmailLogin)
    RippleView GotoEmailLogin;
    @BindView(R.id.EmailUserName)
    MyEditText UserEmail;
    @BindView(R.id.EmailUserPass)
    MyEditText UserPass;
    @BindView(R.id.forgot)
    MyTextView ForgotPass;
    @BindView(R.id.showPass)
    ImageView HideShowPass;
    int p1 = 0;
/////////**************SIGNUP*************//////////////////////////////

    @BindView(R.id.SignUpemail)
    MyEditText SignEmail;
    @BindView(R.id.SignUpName)
    MyEditText SignName;
    @BindView(R.id.SignUpPass)
    MyEditText SignPass;
    @BindView(R.id.SignUpNow)
    RippleView SigninUp;


    int loginProvider = 0;
    String TAG = "Login";
    String Type = "";
    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListner;
    CallbackManager mCallbackManager;
    private LoginButton loginButton;
    @BindView(R.id.GotoLinked)
    RelativeLayout lnkdin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login);
        AppEventsLogger.activateApp(this);
        ButterKnife.bind(Login.this);


        PermissionCheck();
        initializeFirebaseAuth();
        initializeGmailLogin();
        initializeFacebookLogin();


        if (Functions.getUserDetails(Login.this).get(0) != "null") {
            try {
                JSONObject jsonObject = new JSONObject(Functions.getUserDetails(Login.this).get(2));
                JSONArray jsonArray = jsonObject.getJSONArray("userData");
                UserDetails.setUID(jsonArray.getJSONObject(0).getString("id"));
                UserDetails.setUName(jsonArray.getJSONObject(0).getString("user_name"));
                UserDetails.setUMob(jsonArray.getJSONObject(0).getString("user_mobile"));
                UserDetails.setUEmail(jsonArray.getJSONObject(0).getString("user_email"));
                UserDetails.setProfileP(jsonObject.getString("profileP"));
                UserDetails.setUser_gender(jsonArray.getJSONObject(0).getString("user_gender"));
                UserDetails.setCover_pic(jsonArray.getJSONObject(0).getString("cover_pic"));
                Intent intent = new Intent(Login.this, Home.class);
                UserDetails.setLogintype(0);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                finish();
            } catch (JSONException e) {
            }
        }

        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapseSocial.collapse();
                expandSignup.collapse();
                expandLogin.expand();
                ViewSocial.setVisibility(View.VISIBLE);
            }
        });

        gotoSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandLogin.collapse();
                collapseSocial.collapse();
                expandSignup.expand();

                ViewSocial.setVisibility(View.VISIBLE);
            }
        });

        ViewSocial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                expandLogin.collapse();
                collapseSocial.expand();
                expandSignup.collapse();

                ViewSocial.setVisibility(View.GONE);
            }
        });

        GotoEmailLogin.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                if (UserEmail.getText().toString().length() <= 0 && UserPass.getText().toString().length() <= 0) {

                    Functions.showErrorMessage(LoginParrenLayout, "Please Enter Vaild Username & Passoword", Login.this);
                } else {
                    if (Functions.checkNetworkConnection(Login.this)) {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("user_email", UserEmail.getText().toString());
                            jsonObject.put("user_password", UserPass.getText().toString());
                            jsonObject.put("chat_token", UserDetails.getChat_id());
                            Functions.showLoadingDialog(Login.this, "Please Wait Logging in.....");
                            new ApiListener(Login.this, ApiUrl.LoginUser, jsonObject).execute();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    } else {
                        Functions.showErrorMessage(LoginParrenLayout, "Check your internet connection", Login.this);
                    }
                }
            }
        });

        SigninUp.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                if (SignEmail.getText().toString().length() <= 0 && SignPass.getText().toString().length() <= 0 && SignName.getText().toString().length() <= 0) {

                    Functions.showErrorMessage(LoginParrenLayout, "Please Fill All Field", Login.this);
                } else {
                    if (Functions.checkNetworkConnection(Login.this)) {

                        Functions.showLoadingDialog(Login.this, "Please Wait Logging in.....");
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("user_email", SignEmail.getText().toString());
                            jsonObject.put("user_password", SignPass.getText().toString());
                            jsonObject.put("user_name", SignName.getText().toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        AndroidNetworking.initialize(Login.this);
                        AndroidNetworking.post(ApiUrl.UserSignUp)
                                .setPriority(Priority.MEDIUM)
                                .setTag("Token")
                                .addJSONObjectBody(jsonObject)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        Log.e("Response", response.toString());
                                        Functions.closeLoadingDialog();

                                        try {
                                            if (response.getString("response").equals("success"))

                                            {
                                                Functions.showMessage(LoginParrenLayout, "Account Successfully Registered", Login.this);
                                            } else

                                            {
                                                if (response.getString("response").equals("email already registered")) {
                                                    Functions.showErrorMessage(LoginParrenLayout, "Email Already Register", Login.this);
                                                }

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


                    } else {
                        Functions.showErrorMessage(LoginParrenLayout, "Check your internet connection", Login.this);
                    }
                }
            }
        });

        GotoFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProvider = 1;
                loginButton.performClick();
                Functions.showLoadingDialog(Login.this, "Please Wait Logging in.....");
            }
        });

        lnkdin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LISessionManager.getInstance(getApplicationContext()).init(Login.this, buildScope(), new AuthListener() {
                    @Override
                    public void onAuthSuccess()
                    {
                        Functions.showLoadingDialog(Login.this, "Please Wait Logging in.....");
                        linkededinApiHelper();
                        //Toast.makeText(getApplicationContext(), "success" + LISessionManager.getInstance(getApplicationContext()).getSession().getAccessToken().toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onAuthError(LIAuthError error)
                    {
                        Log.e("LinkError",error.toString());
                        Functions.closeLoadingDialog();
                        Functions.showErrorMessage(LoginParrenLayout, "LinkedIn Login Failed, Try Later.", Login.this);
                        Log.e("Linkedin Error",error.toString());
                    }
                }, true);
            }
        });

        ForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgotDialog(Login.this);
            }
        });

        HideShowPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (p1 == 0) {
                    HideShowPass.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_off_black_36dp));
                    p1 = 1;
                    UserPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    UserPass.setSelection(UserPass.length());
                } else {
                    HideShowPass.setImageDrawable(getResources().getDrawable(R.drawable.ic_visibility_black_36dp));
                    p1 = 0;
                    UserPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    UserPass.setSelection(UserPass.length());
                }

            }
        });
    }

    @Override
    public void onApiResponse(final String response) {

        Log.e("Response", response);
        Functions.closeLoadingDialog();
        if (response.equals("null")) {
            Functions.showErrorMessage(LoginParrenLayout, "Network error occurred, Try Later.", Login.this);
        } else {
            if (response.contains("invalid")) {
                Functions.showErrorMessage(LoginParrenLayout, "Invalid Login Details", Login.this);
            } else {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("userData");
                    UserDetails.setUID(jsonArray.getJSONObject(0).getString("id"));
                    UserDetails.setUName(jsonArray.getJSONObject(0).getString("user_name"));
                    UserDetails.setUMob(jsonArray.getJSONObject(0).getString("user_mobile"));
                    UserDetails.setUEmail(jsonArray.getJSONObject(0).getString("user_email"));
                    UserDetails.setUDob(jsonArray.getJSONObject(0).getString("user_dob"));
                    UserDetails.setUstatus(jsonArray.getJSONObject(0).getString("timeline_status"));
                    UserDetails.setUinterestin(jsonArray.getJSONObject(0).getString("interested_in"));
                    UserDetails.setUlives(jsonArray.getJSONObject(0).getString("lives_in"));
                    UserDetails.setUworkat(jsonArray.getJSONObject(0).getString("working_at"));
                    UserDetails.setUworkin(jsonArray.getJSONObject(0).getString("working_as"));
                    UserDetails.setUHHC(jsonArray.getJSONObject(0).getString("high_school"));
                    UserDetails.setUSSC(jsonArray.getJSONObject(0).getString("intermediate"));
                    UserDetails.setUGradu(jsonArray.getJSONObject(0).getString("graduation"));
                    UserDetails.setUPostGra(jsonArray.getJSONObject(0).getString("Post_graduation"));
                    UserDetails.setUfacebook_page(jsonArray.getJSONObject(0).getString("facebook_page"));
                    UserDetails.setUadd_website(jsonArray.getJSONObject(0).getString("add_website"));
                    UserDetails.setUImage(jsonArray.getJSONObject(0).getString("image"));
                    UserDetails.setProfileP(jsonObject.getString("profileP"));
                    UserDetails.setUser_gender(jsonArray.getJSONObject(0).getString("user_gender"));
                    UserDetails.setUser_gender(jsonArray.getJSONObject(0).getString("cover_pic"));

                    if (rememberMe.isChecked() && jsonObject.getString("status").equals("verified")) {
                        Functions.setUserInformation(Login.this, UserEmail.getText().toString(), UserPass.getText().toString(), response);
                    }

                    if(jsonObject.getString("status").equals("verified")){
                        Intent intent = new Intent(Login.this, Home.class);
                        UserDetails.setLogintype(0);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                        finish();
                    }else if(jsonObject.getString("status").equals("unverified")){

                        AlertDialog.Builder adb = new AlertDialog.Builder(Login.this, R.style.ForgotP);
                        View view = getLayoutInflater().inflate(R.layout.otp_verification, null);
                        adb.setView(view);
                        ForGotPAss = adb.create();
                        ForGotPAss.getWindow().getAttributes().windowAnimations = R.style.DialogAnimations_SmileWindow;
                        ForGotPAss.setCancelable(false);
                        RippleView yes = (RippleView) view.findViewById(R.id.yes);
                        final MyEditText aadhartxt = (MyEditText) view.findViewById(R.id.aadhartxt);

                        yes.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                            @Override
                            public void onComplete(RippleView rippleView) {
                                if(aadhartxt.getText().length()>0){
                                    JSONObject jObj = new JSONObject();
                                    try {
                                        Functions.showLoadingDialog(Login.this,"Verifying");
                                        jObj.put("otp",aadhartxt.getText().toString());
                                        jObj.put("user_email",UserEmail.getText().toString());

                                        Log.d("OtpParams", jObj.toString());

                                        AndroidNetworking.initialize(Login.this);
                                        AndroidNetworking.post(ApiUrl.VerifiyEmail)
                                                .setPriority(Priority.MEDIUM)
                                                .setTag("Token")
                                                .addJSONObjectBody(jObj)
                                                .build()
                                                .getAsJSONObject(new JSONObjectRequestListener() {
                                                    @Override
                                                    public void onResponse(JSONObject res) {

                                                        Log.e("Response", res.toString());
                                                        Functions.closeLoadingDialog();

                                                        try {
                                                            if (res.getString("response").equals("invalid"))

                                                            {
                                                                Toast.makeText(Login.this, "Invalid OTP", Toast.LENGTH_SHORT).show();

                                                            } else

                                                            {
                                                                Functions.setUserInformation(Login.this, UserEmail.getText().toString(), UserPass.getText().toString(), response);



                                                                Intent intent = new Intent(Login.this, Home.class);
                                                                UserDetails.setLogintype(0);
                                                                startActivity(intent);
                                                                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                                                                finish();
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



                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });


                        ForGotPAss.show();



                    }


                } catch (JSONException e) {

                }
            }

        }


    }

    private void initializeGmailLogin() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        GLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginProvider = 2;
                googleSignIn();
            }
        });
    }

    private void initializeFirebaseAuth() {
        mAuth = FirebaseAuth.getInstance();
        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    UserDetails.setUName(firebaseAuth.getCurrentUser().getDisplayName());
                    UserDetails.setUEmail(firebaseAuth.getCurrentUser().getEmail());
                    UserDetails.setSocialImage(firebaseAuth.getCurrentUser().getPhotoUrl().toString());
                    UserDetails.setLogintype(1);

                    APILogin(firebaseAuth.getCurrentUser().getDisplayName(),
                            firebaseAuth.getCurrentUser().getEmail(), firebaseAuth.getCurrentUser().getPhotoUrl().toString());


//                    expandLogin.collapse();
//                    collapseSocial.collapse();
//                    expandSignup.expand();
//                    ViewSocial.setVisibility(View.VISIBLE);
//
//                    SignEmail.setText(firebaseAuth.getCurrentUser().getEmail());
//                    SignName.setText(firebaseAuth.getCurrentUser().getDisplayName());


                }
            }
        };
    }


    public void APILogin(String name, final String email, String image) {

        if (Functions.checkNetworkConnection(Login.this)) {


            JSONObject jsonObject = new JSONObject();

            try {
                jsonObject.put("social_name", name);
                jsonObject.put("social_email", email);
                jsonObject.put("social_image", image);
                jsonObject.put("chat_token", UserDetails.getChat_id());

                Log.d("SocialLogParams", jsonObject.toString());

            } catch (JSONException e) {
                e.printStackTrace();
            }

            AndroidNetworking.initialize(Login.this);
            AndroidNetworking.post(ApiUrl.socialLogin)
                    .setPriority(Priority.MEDIUM)
                    .setTag("Token")
                    .addJSONObjectBody(jsonObject)
                    .build()
                    .getAsJSONObject(new JSONObjectRequestListener() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Log.d("LoginRes", response.toString());

                            Functions.closeLoadingDialog();

                            try {
                                if(response.getString("response").equals("success")){

                                    JSONArray jsonArray = response.getJSONArray("userData");
                                    UserDetails.setUID(jsonArray.getJSONObject(0).getString("id"));
                                    UserDetails.setUName(jsonArray.getJSONObject(0).getString("user_name"));
                                    UserDetails.setUMob(jsonArray.getJSONObject(0).getString("user_mobile"));
                                    UserDetails.setUEmail(jsonArray.getJSONObject(0).getString("user_email"));
                                    UserDetails.setUDob(jsonArray.getJSONObject(0).getString("user_dob"));
                                    UserDetails.setUstatus(jsonArray.getJSONObject(0).getString("timeline_status"));
                                    UserDetails.setUinterestin(jsonArray.getJSONObject(0).getString("interested_in"));
                                    UserDetails.setUlives(jsonArray.getJSONObject(0).getString("lives_in"));
                                    UserDetails.setUworkat(jsonArray.getJSONObject(0).getString("working_at"));
                                    UserDetails.setUworkin(jsonArray.getJSONObject(0).getString("working_as"));
                                    UserDetails.setUHHC(jsonArray.getJSONObject(0).getString("high_school"));
                                    UserDetails.setUSSC(jsonArray.getJSONObject(0).getString("intermediate"));
                                    UserDetails.setUGradu(jsonArray.getJSONObject(0).getString("graduation"));
                                    UserDetails.setUPostGra(jsonArray.getJSONObject(0).getString("Post_graduation"));
                                    UserDetails.setUfacebook_page(jsonArray.getJSONObject(0).getString("facebook_page"));
                                    UserDetails.setUadd_website(jsonArray.getJSONObject(0).getString("add_website"));
                                    UserDetails.setUImage(jsonArray.getJSONObject(0).getString("image"));
                                    UserDetails.setProfileP(response.getString("profileP"));
                                    UserDetails.setUser_gender(jsonArray.getJSONObject(0).getString("user_gender"));
                                    UserDetails.setUser_gender(jsonArray.getJSONObject(0).getString("cover_pic"));

                                    Functions.setUserInformation(Login.this, email, "", response.toString());

                                    Intent intent = new Intent(Login.this, Home.class);
                                    intent.putExtra("type","social");
                                    startActivity(intent);
                                    overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                                    finish();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }




                        }

                        @Override
                        public void onError(ANError anError) {
                            Functions.closeLoadingDialog();
                            Toast.makeText(Login.this, anError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Functions.closeLoadingDialog();
            Toast.makeText(Login.this, "Check Your Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    private void googleSignIn() {
        Functions.showLoadingDialog(Login.this, "Please Wait Logging in.....");
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            try {
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                if (result.isSuccess()) {
                    GoogleSignInAccount account = result.getSignInAccount();
                    firebaseAuthWithGoogle(account);
                } else {
                    Functions.showErrorMessage(LoginParrenLayout, "Google Login Failed, Try Later.", Login.this);
                    Functions.closeLoadingDialog();
                }

                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                }
            } catch (Exception e) {
            }
        }

        LISessionManager.getInstance(getApplicationContext()).onActivityResult(this, requestCode, resultCode, data);
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        Type = "GM";
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        if (!task.isSuccessful()) {
                            Log.e(TAG, "signInWithCredential", task.getException());
                            Functions.closeLoadingDialog();
                        }
                    }
                });
    }

    private void initializeFacebookLogin() {
        loginButton = (LoginButton) findViewById(R.id.FB);
        mCallbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Functions.showErrorMessage(LoginParrenLayout, "Facebook Login Cancel", Login.this);
                Functions.closeLoadingDialog();
            }

            @Override
            public void onError(FacebookException error) {
                Log.e("FB", error.getMessage());
                Functions.showErrorMessage(LoginParrenLayout, "Facebook Login Failed, Try Later.", Login.this);
                Functions.closeLoadingDialog();
            }
        });

    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        Type = "FB";
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Functions.showErrorMessage(LoginParrenLayout, "Facebook Login Failed, Try Later.", Login.this);
                            Functions.closeLoadingDialog();
                        }
                    }
                });
    }

    public void PermissionCheck() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {

                Toast.makeText(Login.this, "Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> arrayList) {

            }
        };

        new TedPermission(this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS,
                        Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }

    private static Scope buildScope() {
        return Scope.build(Scope.R_BASICPROFILE, Scope.R_EMAILADDRESS);
    }

    public void linkededinApiHelper() {
        APIHelper apiHelper = APIHelper.getInstance(getApplicationContext());
        apiHelper.getRequest(Login.this, ApiUrl.topCardUrl, new com.linkedin.platform.listeners.ApiListener() {
            @Override
            public void onApiSuccess(com.linkedin.platform.listeners.ApiResponse apiResponse) {
                try {
                    UserDetails.setUName(apiResponse.getResponseDataAsJson().getString("formattedName"));
                    UserDetails.setUEmail(apiResponse.getResponseDataAsJson().getString("emailAddress"));
                    UserDetails.setSocialImage(apiResponse.getResponseDataAsJson().getString("pictureUrl"));
                    UserDetails.setLogintype(0);

                    Log.d("Img",apiResponse.getResponseDataAsJson().getString("pictureUrl") );

                    APILogin(apiResponse.getResponseDataAsJson().getString("formattedName"),
                            apiResponse.getResponseDataAsJson().getString("emailAddress"), apiResponse.getResponseDataAsJson().getString("pictureUrl"));

                } catch (JSONException e) {
                    Functions.closeLoadingDialog();
                    Functions.showErrorMessage(LoginParrenLayout, "LinkedIn Login Failed, Try Later.", Login.this);
                    e.printStackTrace();
                }
            }

            @Override
            public void onApiError(LIApiError LIApiError) {
                Functions.closeLoadingDialog();
                Functions.showErrorMessage(LoginParrenLayout, "LinkedIn Login Failed, Try Later.", Login.this);
            }
        });
    }

    static AlertDialog ForGotPAss;

    public void showForgotDialog(final Activity activity) {
        AlertDialog.Builder adb = new AlertDialog.Builder(activity, R.style.ForgotP);
        View view = activity.getLayoutInflater().inflate(R.layout.forgotpass_dialog, null);
        adb.setView(view);
        ForGotPAss = adb.create();
        ForGotPAss.getWindow().getAttributes().windowAnimations = R.style.DialogAnimations_SmileWindow;
        ForGotPAss.setCancelable(true);

        final RippleView ValidOTP, SendEmail, ChangePass1;
        final MyEditText EnterOTP, EnterEmail, ExEnterPass, ExSendedEmailtxt;
        final ExpandableLinearLayout exEmail, exOTP, ExSendedEmail;
        ExSendedEmail = (ExpandableLinearLayout) view.findViewById(R.id.ExSendedEmail);
        ExEnterPass = (MyEditText) view.findViewById(R.id.ExEnterPass);
        ExSendedEmailtxt = (MyEditText) view.findViewById(R.id.ExSendedEmailtxt);
        ChangePass1 = (RippleView) view.findViewById(R.id.ChangePassword);
        ValidOTP = (RippleView) view.findViewById(R.id.ValidOTP);
        EnterOTP = (MyEditText) view.findViewById(R.id.EnterOTP);
        SendEmail = (RippleView) view.findViewById(R.id.SendEmail);
        EnterEmail = (MyEditText) view.findViewById(R.id.EnterEmail);
        exEmail = (ExpandableLinearLayout) view.findViewById(R.id.ExSendEmail);
        exOTP = (ExpandableLinearLayout) view.findViewById(R.id.ExSendPass);

        SendEmail.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                Functions.showLoadingDialog(Login.this, "Sending OTP.....");
                hideKeyBoard();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("email", EnterEmail.getText().toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AndroidNetworking.initialize(Login.this);
                AndroidNetworking.post(ApiUrl.ForgotPassword)
                        .setPriority(Priority.MEDIUM)
                        .setTag("Token")
                        .addJSONObjectBody(jsonObject)
                        .build()
                        .getAsJSONObject(new JSONObjectRequestListener() {
                            @Override
                            public void onResponse(JSONObject response) {

                                Log.e("FinalSecondResponse", response.toString());
                                Functions.closeLoadingDialog();

                                try {

                                    if (response.getString("response").matches("success")) {
                                        JSONArray jsonArray = response.getJSONArray("reset_code");
                                        UserDetails.setReset_code(jsonArray.getJSONObject(0).getString("code"));
                                        exEmail.collapse();
                                        exOTP.expand();
                                        Functions.showMessage(LoginParrenLayout, "Check your email for OTP", Login.this);

                                    } else {
                                        Functions.showErrorMessage(LoginParrenLayout, "Opps Something Went Wrong", Login.this);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(ANError anError) {

                            }
                        });


                ValidOTP.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {

                        if (UserDetails.getReset_code().equalsIgnoreCase(EnterOTP.getText().toString())) {
                            ExSendedEmail.expand();
                            exEmail.collapse();
                            exOTP.collapse();
                        } else {
                            Functions.showErrorMessage(LoginParrenLayout, "OTP not Matched", Login.this);
                        }
                    }
                });

                ChangePass1.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
                    @Override
                    public void onComplete(RippleView rippleView) {

                        Functions.showLoadingDialog(Login.this, "Adding Password.....");
                        hideKeyBoard();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("user_email", ExSendedEmailtxt.getText().toString());
                            jsonObject.put("set_password", ExEnterPass.getText().toString());

                            Log.e("JsonData", jsonObject.toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        AndroidNetworking.initialize(Login.this);
                        AndroidNetworking.post(ApiUrl.SetfogetPassword)
                                .setPriority(Priority.MEDIUM)
                                .setTag("Token")
                                .addJSONObjectBody(jsonObject)
                                .build()
                                .getAsJSONObject(new JSONObjectRequestListener() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.e("FinalResponse", response.toString());
                                        try {
                                            if (response.getString("response").matches("success")) {
                                                Functions.closeLoadingDialog();
                                                ForGotPAss.dismiss();
                                                Functions.showMessage(LoginParrenLayout, "Password Successfully Changed", Login.this);
                                            } else {
                                                Functions.showErrorMessage(LoginParrenLayout, "Something Went Wrong", Login.this);
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onError(ANError anError) {

                                    }
                                });
                    }
                });
            }
        });
        ForGotPAss.show();
    }

    void hideKeyBoard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
