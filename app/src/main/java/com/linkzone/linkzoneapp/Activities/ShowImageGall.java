package com.linkzone.linkzoneapp.Activities;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.media.ExifInterface;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.linkzone.linkzoneapp.Adapters.Recycler_Cell_adapter;
import com.linkzone.linkzoneapp.ApiDetails.ApiUrls.ApiUrl;
import com.linkzone.linkzoneapp.DataHolders.GetSetImg;
import com.linkzone.linkzoneapp.DataHolders.UserDetails;
import com.linkzone.linkzoneapp.Functions.Functions;
import com.linkzone.linkzoneapp.R;
import com.linkzone.linkzoneapp.widget.RippleView;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowImageGall extends AppCompatActivity {

    @BindView(R.id.ParentLog)
    RelativeLayout ParenLay;
    @BindView(R.id.RefUploadImg)
    ImageView RefImage;
    @BindView(R.id.ArrowBack)
    ImageView ArrowBack;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<GetSetImg> getSetImgs = new ArrayList<>();

    ArrayList<Image> images = new ArrayList<>();
    int RequestCode = 1;
    File Uploadfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image_gall);
        ButterKnife.bind(this);
        APIWORK();

        ArrowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RefImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIWORK();
            }
        });

    }

    private void bindPackageList() {
        recyclerView = (RecyclerView) findViewById(R.id.UserImageRecyle);
        recyclerView.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Recycler_Cell_adapter(ShowImageGall.this, getSetImgs);
        recyclerView.setAdapter(adapter);
    }

    public void APIWORK() {
        Functions.showLoadingDialog(ShowImageGall.this, "Getting Details");
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_email", UserDetails.getUEmail());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.initialize(ShowImageGall.this);
        AndroidNetworking.post(ApiUrl.GetUserGallery)
                .setPriority(Priority.MEDIUM)
                .setTag("Token")
                .addJSONObjectBody(jsonObject)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        Log.e("Response", response.toString());
                        try {

                            if (response.getString("response").matches("success"))
                                try {
                                    getSetImgs.clear();
                                    JSONArray jsonarray = response.getJSONArray("userGallery");
                                    for (int i = 0; i < jsonarray.length(); i++) {
                                        GetSetImg getSetImg = new GetSetImg();
                                        getSetImg.setImagePath(jsonarray.getJSONObject(i).getString("image_name"));
                                        getSetImg.setImageId(jsonarray.getJSONObject(i).getString("id"));
                                        getSetImgs.add(getSetImg);
                                    }
                                    bindPackageList();

                                } catch (Exception e) {
                                    Log.e("Response", e.getMessage());
                                }
                            else {
                                if (response.getString("response").matches("no data found")) {
                                    Functions.showErrorMessage(ParenLay, "No Data Found", ShowImageGall.this);
                                }
                            }
                            Functions.closeLoadingDialog();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                    }

                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
                Intent intent = new Intent(ShowImageGall.this, CropDP.class);
                startActivity(intent);
                logout.dismiss();
            }
        });

        UpMoments = (RippleView) view.findViewById(R.id.no);
        UpMoments.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                Intent intent = new Intent(ShowImageGall.this, UploadImageToData.class);
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

    public void APIWorkForUpload() {
        Functions.showLoadingDialog(ShowImageGall.this, "Please Wait........");

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

    void initialize() {
        JSONObject object = new JSONObject();
        try {

            object.put("user_id", UserDetails.getUID());
            object.put("user_email", UserDetails.getUEmail());
            object.put("image_name", Uploadfile.getName().toString());

            Log.e("CoverData", object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AndroidNetworking.initialize(ShowImageGall.this);
        AndroidNetworking.post(ApiUrl.ProfileCoverImage)
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
                                Intent intent = new Intent(ShowImageGall.this, Home.class);
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

    //////////************************* IMAGE COMPRESSION START *************************////////////////////

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

}
