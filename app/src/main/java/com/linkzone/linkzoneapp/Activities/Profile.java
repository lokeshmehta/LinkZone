package com.linkzone.linkzoneapp.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.media.ExifInterface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.text.InputFilter;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.bumptech.glide.Glide;
import com.linkzone.linkzoneapp.ApiDetails.ApiListners.ApiListener;
import com.linkzone.linkzoneapp.ApiDetails.ApiListners.ApiResponse;
import com.linkzone.linkzoneapp.ApiDetails.ApiUrls.ApiUrl;
import com.linkzone.linkzoneapp.DataHolders.GetUserDetails;
import com.linkzone.linkzoneapp.DataHolders.SearchUserDetails;
import com.linkzone.linkzoneapp.DataHolders.UserDetails;
import com.linkzone.linkzoneapp.Functions.Functions;
import com.linkzone.linkzoneapp.R;
import com.linkzone.linkzoneapp.widget.MyBoldTextView;
import com.linkzone.linkzoneapp.widget.MyEditText;
import com.linkzone.linkzoneapp.widget.MyTextView;
import com.linkzone.linkzoneapp.widget.RippleView;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity implements ApiResponse {

    int RequestCode = 1;
    ArrayList<Image> images = new ArrayList<>();
    File Uploadfile;

    @BindView(R.id.UpdatePass)
    RippleView UpPass;

    ///////////////////////**************PROFILE SECTION*******************/////////////////////////

    @BindView(R.id.UName)
    MyEditText UserName;
    @BindView(R.id.ProfileDp)
    CircleImageView UserDp;
    @BindView(R.id.ProfileDpName)
    MyBoldTextView DpName;
    @BindView(R.id.UDob)
    MyTextView UserDOB;
    @BindView(R.id.UMob)
    MyEditText UserMob;
    @BindView(R.id.UEmail)
    MyTextView UserEmail;
    @BindView(R.id.Ustatus)
    MyEditText UserStatus;
    @BindView(R.id.UInterest)
    MyEditText UserInte;
    @BindView(R.id.ULives)
    MyEditText UserLives;
    @BindView(R.id.Uworkingas)
    MyEditText UserWorkingAs;
    @BindView(R.id.Uworkingat)
    MyEditText UserWorkingAt;
    @BindView(R.id.UHHC)
    MyEditText UserHHC;
    @BindView(R.id.USSC)
    MyEditText UserSSC;
    @BindView(R.id.UGraduation)
    MyEditText UserGrad;
    @BindView(R.id.UPGraduation)
    MyEditText UserPGrad;
    @BindView(R.id.UWebsite)
    MyEditText UserWebsite;
    @BindView(R.id.UpdateProfile)
    RippleView UpdateUser;
    @BindView(R.id.ParentLog)
    LinearLayout LoginParrenLayout;
    @BindView(R.id.SetMobText)
    TextView SetMOB;
    @BindView(R.id.SwitchGender)
    Switch SwitchGender;
    @BindView(R.id.coverIv)
    ImageView coverIv;

    DatePickerDialog dobDialog;
    long date2;
    Date date1;
    SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
    Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(Profile.this);
        Switch CheckMob = (Switch) findViewById(R.id.SwitchMob);

        CheckMob.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    SetMOB.setText("");
                    Functions.showErrorMessage(LoginParrenLayout, "Please Update Profile For Applying Changes. No One Will Able To See Your Number", Profile.this);
                } else {
                    SetMOB.setText(UserMob.getText().toString());
                }
            }
        });
        UpPass.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                PopupMenu popupMenu = new PopupMenu(Profile.this, UpPass);
                popupMenu.getMenuInflater().inflate(R.menu.profile_menu, popupMenu.getMenu());
                popupMenu.show();

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getTitle().equals("Change Password")) {
                            Intent intent = new Intent(Profile.this, ChangePass.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                        }

                        if (item.getTitle().equals("Add Your Aadhar"))

                        {
                            showAdhaarDialog(Profile.this);
                        }
                        if (item.getTitle().equals("Upload Image")) {
                            showUploadDialog(Profile.this);
                        }

                        if (item.getTitle().equals("Show Gallery")) {
                            Intent intent = new Intent(Profile.this, ShowImageGall.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                        }

                        return true;
                    }
                });
            }
        });

        if (GetUserDetails.getUImage().length()>0) {
            Picasso.with(Profile.this)
                    .load("http://xperiaindia.com/linkZone/userImages/" + UserDetails.getUID() + "/" + GetUserDetails.getUImage())
                    .placeholder(R.drawable.dpplaceh)
                    .into(UserDp);
        }else {
            Picasso.with(Profile.this)
                    .load(UserDetails.getSocialImage())
                    .placeholder(R.drawable.dpplaceh)
                    .into(UserDp);
        }


            Picasso.with(Profile.this)
                    .load("http://xperiaindia.com/linkZone/userImages/" + UserDetails.getUID() + "/" + GetUserDetails.getCover_pic())
                    .placeholder(R.drawable.coverback)
                    .into(coverIv);


        Log.e("CoverP", "http://xperiaindia.com/linkZone/userImages/" + UserDetails.getUID() + "/" + GetUserDetails.getCover_pic());

        UserDp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable) UserDp.getDrawable()).getBitmap();
                showProfileImage(bitmap);
            }
        });

        coverIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap bitmap = ((BitmapDrawable) coverIv.getDrawable()).getBitmap();
                showProfileImage(bitmap);
            }
        });

        DpName.setText(GetUserDetails.getUName());
        UserEmail.setText(GetUserDetails.getUEmail());
        UserName.setText(GetUserDetails.getUName());
        UserDOB.setText(GetUserDetails.getUDob());
        UserMob.setText(GetUserDetails.getUMob());
        UserStatus.setText(GetUserDetails.getUstatus());
        UserInte.setText(GetUserDetails.getUinterestin());
        UserLives.setText(GetUserDetails.getUlives());
        UserWorkingAs.setText(GetUserDetails.getUworkin());
        UserWorkingAt.setText(GetUserDetails.getUworkat());
        UserHHC.setText(GetUserDetails.getUHHC());
        UserSSC.setText(GetUserDetails.getUSSC());
        UserGrad.setText(GetUserDetails.getUGradu());
        UserPGrad.setText(GetUserDetails.getUPostGra());
        UserWebsite.setText(GetUserDetails.getUadd_website());
        if (GetUserDetails.getUser_gender().equals("Female")) {
            SwitchGender.setChecked(true);
        } else {
            SwitchGender.setChecked(false);
        }

        UpdateUser.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                if (Functions.checkNetworkConnection(Profile.this)) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("user_name", UserName.getText().toString());
                        jsonObject.put("user_email", UserEmail.getText().toString());
                        jsonObject.put("user_mobile", SetMOB.getText().toString());
                        jsonObject.put("user_dob", UserDOB.getText().toString());
                        jsonObject.put("timeline_status", UserStatus.getText().toString());
                        jsonObject.put("interested_in", UserInte.getText().toString());
                        jsonObject.put("lives_in", UserLives.getText().toString());
                        jsonObject.put("working_at", UserWorkingAt.getText().toString());
                        jsonObject.put("working_as", UserWorkingAs.getText().toString());
                        jsonObject.put("high_school", UserHHC.getText().toString());
                        jsonObject.put("intermediate", UserSSC.getText().toString());
                        jsonObject.put("graduation", UserGrad.getText().toString());
                        jsonObject.put("Post_graduation", UserPGrad.getText().toString());
                        jsonObject.put("add_website", UserWebsite.getText().toString());
                        jsonObject.put("facebook_page", "");
                        if (SwitchGender.isChecked()) {
                            jsonObject.put("user_gender", "Female");
                        } else {
                            jsonObject.put("user_gender", "Male");
                        }
                        Functions.showLoadingDialog(Profile.this, "Updating Your Profile....");
                        new ApiListener(Profile.this, ApiUrl.UpdateProfile, jsonObject).execute();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Functions.showErrorMessage(LoginParrenLayout, "Check your internet connection", Profile.this);
                }

            }
        });

        Calendar newDate = Calendar.getInstance();
        newDate.set(Calendar.DAY_OF_MONTH, 01);
        newDate.set(Calendar.MONTH, 11);
        newDate.set(Calendar.YEAR, 2016);
        date1 = newDate.getTime();

        UserDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDetailsDialog();
            }
        });

    }

    public void addDetailsDialog() {
        Calendar newCalendar = Calendar.getInstance();

        dobDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                UserDOB.setText(dateFormatter.format(newDate.getTime()));
                date2 = newDate.getTimeInMillis();

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        dobDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
    }

    @Override
    public void onApiResponse(String response) {

        Functions.closeLoadingDialog();
        if (response.contains("success")) {
            Functions.showMessage(LoginParrenLayout, "Profile Successfully Updated", Profile.this);
            Functions.setUserInformation(Profile.this, "", "", response);

            try {
                JSONObject jsonObject = new JSONObject(response);


                Intent intent = new Intent(Profile.this, Home.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("flag","updated");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                finish();

            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {
            Functions.showErrorMessage(LoginParrenLayout, "Oops Something Went Wrong", Profile.this);
        }
    }

    AlertDialog aadharDialog;

    public void showAdhaarDialog(final Activity activity) {
        AlertDialog.Builder adb = new AlertDialog.Builder(activity, R.style.ForgotP);
        View view = activity.getLayoutInflater().inflate(R.layout.aadhar, null);
        adb.setView(view);
        aadharDialog = adb.create();
        aadharDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimations_SmileWindow;
        aadharDialog.setCancelable(true);

        RippleView Add_Aadhar;
        final MyEditText aadharText;
        Add_Aadhar = (RippleView) view.findViewById(R.id.yes);
        aadharText = (MyEditText) view.findViewById(R.id.aadhartxt);
        Add_Aadhar.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {


                int maxLength = 12;
                aadharText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxLength)});
                if (aadharText.getText().toString().length() < 12) {
                    Functions.showErrorMessage(LoginParrenLayout, "Please Enter Valid Aadhar Number", Profile.this);
                } else {
                    hideKeyBoard();
                    Functions.showLoadingDialog(Profile.this, "Please Wait....");
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("user_email", UserDetails.getUEmail());
                        jsonObject.put("adhar_no", aadharText.getText().toString());
                        Log.e("Aadhardata", jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    AndroidNetworking.initialize(Profile.this);
                    AndroidNetworking.post(ApiUrl.AddAdhar)
                            .setPriority(Priority.MEDIUM)
                            .setTag("Token")
                            .addJSONObjectBody(jsonObject)
                            .build()
                            .getAsJSONObject(new JSONObjectRequestListener() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.e("AadharRes", response.toString());
                                    try {
                                        if (response.getString("response").equalsIgnoreCase("success")) {
                                            Functions.closeLoadingDialog();
                                            aadharDialog.dismiss();
                                            Functions.showMessage(LoginParrenLayout, "Thanks for providing details ! Its takes 24Hour to verify", Profile.this);

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
            }
        });

        aadharDialog.show();
    }

    void hideKeyBoard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void showProfileImage(Bitmap bitmap) {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.picture_alert_layout);
        dialog.setCancelable(true);
        ImageView imageView = (ImageView) dialog.findViewById(R.id.profileIv);
        imageView.setImageBitmap(bitmap);


        dialog.show();
    }

    AlertDialog logout;

    public void showUploadDialog(final Activity activity) {
        AlertDialog.Builder adb = new AlertDialog.Builder(activity, R.style.ForgotP);
        View view = activity.getLayoutInflater().inflate(R.layout.uploadimg, null);
        adb.setView(view);
        logout = adb.create();
        logout.getWindow().getAttributes().windowAnimations = R.style.DialogAnimations_SmileWindow;
        logout.setCancelable(true);

        RippleView UpDp, UpMoments, UpCover;
        UpDp = (RippleView) view.findViewById(R.id.yes);
        UpDp.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Intent intent = new Intent(Profile.this, CropDP.class);
                startActivity(intent);
                logout.dismiss();
            }
        });

        UpMoments = (RippleView) view.findViewById(R.id.no);
        UpMoments.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Intent intent = new Intent(Profile.this, UploadImageToData.class);
                startActivity(intent);
                logout.dismiss();
            }
        });

        UpCover = (RippleView) view.findViewById(R.id.CoverDp);
        UpCover.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                chooseImage();
                logout.dismiss();
            }
        });
        logout.show();
    }

    void chooseImage() {
        Intent intent = new Intent(this, ImagePickerActivity.class);
        intent.putExtra(ImagePickerActivity.INTENT_EXTRA_FOLDER_MODE, true);
        intent.putExtra(ImagePickerActivity.INTENT_EXTRA_MODE, ImagePickerActivity.MODE_SINGLE);
        intent.putExtra(ImagePickerActivity.INTENT_EXTRA_LIMIT, 10);
        intent.putExtra(ImagePickerActivity.INTENT_EXTRA_SHOW_CAMERA, true);
        intent.putExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES, images);
        intent.putExtra(ImagePickerActivity.INTENT_EXTRA_FOLDER_TITLE, "Album");
        intent.putExtra(ImagePickerActivity.INTENT_EXTRA_IMAGE_TITLE, "Tap to select images");
        intent.putExtra(ImagePickerActivity.INTENT_EXTRA_IMAGE_DIRECTORY, "Camera");
        startActivityForResult(intent, RequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode && resultCode == RESULT_OK && data != null) {
            images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            final File f = new File(images.get(0).getPath());

            CropImage.activity(Uri.fromFile(f))
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(16, 9)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri fileUri = result.getUri();
            String file_path = fileUri.getPath();
            File f = new File(fileUri.getPath());

            compressImage(f.getAbsolutePath());
            APIWorkForUpload();
        }
    }

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public void APIWorkForUpload() {
        Functions.showLoadingDialog(Profile.this, "Please Wait........");

        AndroidNetworking.upload("http://xperiaindia.com/linkZone/multiUpload.php")
                .addMultipartFile("upload_report", Uploadfile)
                .addMultipartParameter("user_id", UserDetails.getUID())
                .setTag("uploadTest")
                .setPriority(Priority.HIGH)
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("CoverUpload", response);
                        initialize();
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + "Cover" + ".jpg");
        Uploadfile = new File(uriSting);
        Log.e("UpFile", Uploadfile.getPath().toString());
        return uriSting;

    }

    void initialize() {
        JSONObject object = new JSONObject();
        try {

            object.put("user_email", UserDetails.getUEmail());
            object.put("cover_pic", Uploadfile.getName().toString());

            Log.e("CoverData", object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.initialize(Profile.this);
        AndroidNetworking.post(ApiUrl.SetCoverImage)
                .setPriority(Priority.MEDIUM)
                .setTag("Token")
                .addJSONObjectBody(object)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Functions.closeLoadingDialog();
                        try {
                            if (response.getString("response").matches("success")) {
                                Intent intent = new Intent(Profile.this, Home.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

                    }
                });
    }

}
