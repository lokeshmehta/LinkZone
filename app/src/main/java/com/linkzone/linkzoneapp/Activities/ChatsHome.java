package com.linkzone.linkzoneapp.Activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.media.ExifInterface;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.androidnetworking.interfaces.StringRequestListener;
import com.linkzone.linkzoneapp.Adapters.ChatAdapter;
import com.linkzone.linkzoneapp.ApiDetails.ApiListners.ApiListener;
import com.linkzone.linkzoneapp.ApiDetails.ApiListners.ApiResponse;
import com.linkzone.linkzoneapp.ApiDetails.ApiUrls.ApiUrl;
import com.linkzone.linkzoneapp.Adapters.ChatEndDataBase;
import com.linkzone.linkzoneapp.DataHolders.ChatGetSetData;
import com.linkzone.linkzoneapp.DataHolders.GetUserDetails;
import com.linkzone.linkzoneapp.DataHolders.UserDetails;
import com.linkzone.linkzoneapp.FCMChat.ApiKeys;
import com.linkzone.linkzoneapp.FCMChat.IncomeChatHandel;
import com.linkzone.linkzoneapp.Functions.Functions;
import com.linkzone.linkzoneapp.R;
import com.linkzone.linkzoneapp.widget.MyBoldTextView;
import com.linkzone.linkzoneapp.widget.MyEditText;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.squareup.picasso.Picasso;
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
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatsHome extends AppCompatActivity implements ApiResponse {
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<ChatGetSetData> packageNames = new ArrayList<>();
    BroadcastReceiver broadcastReceiver;
    @BindView(R.id.TypeMessage)
    MyEditText TypeMsj;
    @BindView(R.id.SendMessage)
    RelativeLayout SendMsj;
    @BindView(R.id.ChatDp)
    CircleImageView ChatDp;
    @BindView(R.id.ChatName)
    MyBoldTextView ChatName;
    @BindView(R.id.PickImage)
    ImageView PickImg;
    @BindView(R.id.popUpMenu)
    ImageView popUpMenu;
    ArrayList<Image> images = new ArrayList<>();
    int RequestCode = 1;
    File Uploadfile;
    String MessageType = "";
    File ImageShareFile;
    @BindView(R.id.ParentLog)
    RelativeLayout PView;
    @BindView(R.id.ArrowBack)ImageView BackB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_home);
        ButterKnife.bind(this);
        bindPackageList();
        IncomeChatHandel.setChatCount(1);
        ChatEndDataBase chatEndDataBase = new ChatEndDataBase(getBaseContext());
        UserDetails.setUID(getIntent().getExtras().getString(ApiKeys.toUId));
        //Cursor c = chatEndDataBase.getChats(getIntent().getStringExtra("FUID"),UserDetails.getUID());
        // Toast.makeText(this, getIntent().getExtras().getString("FUID"), Toast.LENGTH_SHORT).show();
        parseMessages(chatEndDataBase.getChats(getIntent().getExtras().getString("FUID"), UserDetails.getUID()));

        chatEndDataBase.setAllSeen(getIntent().getExtras().getString("FUID"), UserDetails.getUID());


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                ChatGetSetData chatGetSetData = new ChatGetSetData();
                //chatGetSetData.setUserChatName(intent.getStringExtra(ApiKeys.USER_NAME));
                chatGetSetData.setUserChatFrmID(intent.getStringExtra(ApiKeys.fromUId));
                chatGetSetData.setUserChatToId(intent.getStringExtra(ApiKeys.toUId));
                chatGetSetData.setUserChatMsg(intent.getStringExtra(ApiKeys.msg));
                chatGetSetData.setUserChatMsjType(intent.getStringExtra(ApiKeys.messageType));
                chatGetSetData.setUserChatImage(intent.getStringExtra(ApiKeys.imageurl));
                chatGetSetData.setUserChatType(intent.getStringExtra(ApiKeys.messageType));
                //chatGetSetData.setUserChatTime(intent.getStringExtra(ApiKeys.MESSAGE_DATE_TIME));
                packageNames.add(chatGetSetData);
                adapter.notifyDataSetChanged();
                scrollToBottom();
            }
        };

        BackB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SendMsj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TypeMsj.getText().length() > 0) {
                    APIWork();
                }
            }
        });

        Picasso.with(ChatsHome.this)
                .load("http://xperiaindia.com/linkZone/userImages/" + getIntent().getStringExtra("FUID") + "/" + getIntent().getStringExtra("FImage"))
                .placeholder(R.drawable.dpplaceh)
                .into(ChatDp);
        ChatName.setText(getIntent().getExtras().getString(ApiKeys.USER_NAME));

        PickImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                images.clear();
                chooseImage();
            }
        });

        popUpMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(ChatsHome.this, popUpMenu);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getTitle().equals("Clear Chat")) {
                            ChatEndDataBase chatEndDataBase = new ChatEndDataBase(getBaseContext());
                            chatEndDataBase.deleteChat(getIntent().getStringExtra("FUID"), UserDetails.getUID());
                            packageNames.clear();
                            adapter.notifyDataSetChanged();
                            Toast.makeText(ChatsHome.this, "Data Clear", Toast.LENGTH_SHORT).show();
                        }
                        if (item.getTitle().equals("Block")) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("user_email", UserDetails.getUEmail());
                                jsonObject.put("friend_email", getIntent().getExtras().getString("FEmail"));
                                Log.e("Blockdata", jsonObject.toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            AndroidNetworking.initialize(ChatsHome.this);
                            AndroidNetworking.post(ApiUrl.BlockUser)
                                    .setPriority(Priority.MEDIUM)
                                    .setTag("Token")
                                    .addJSONObjectBody(jsonObject)
                                    .build()
                                    .getAsJSONObject(new JSONObjectRequestListener() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Log.e("res", response.toString());
                                            try {
                                                if (response.getString("response").equalsIgnoreCase("success")) {
                                                    Functions.showErrorMessage(PView, "User Blocked", ChatsHome.this);

                                                    Intent intent = new Intent(ChatsHome.this, Home.class);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
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

                        return true;
                    }
                });

                popup.show();//showing popup menu
            }


        });

        ChatName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                APIGetDetails();
            }
        });

    }

    private void bindPackageList() {
        recyclerView = (RecyclerView) findViewById(R.id.ChatRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ChatAdapter(ChatsHome.this, packageNames);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(ChatsHome.this).registerReceiver(broadcastReceiver, new IntentFilter("Chat"));
    }

    public void APIWork() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("msg", TypeMsj.getText().toString());
            jsonObject.put("fromUId", UserDetails.getUID());
            jsonObject.put("fromName", UserDetails.getUName());
            jsonObject.put("toUId", getIntent().getStringExtra("FUID"));
            jsonObject.put("mediaPath", "");
            jsonObject.put("fromEmail", UserDetails.getUEmail());
            jsonObject.put("toEmail", getIntent().getStringExtra("FEmail"));
            jsonObject.put("messageType", ApiKeys.MSG_TYP);
            jsonObject.put("ChatType", "1");
            MessageType = ApiKeys.MSG_TYP;
            Log.e("ChatData", jsonObject.toString());
            new ApiListener(ChatsHome.this, ApiUrl.ChatNoti, jsonObject).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void ImageSumitAPIWork() {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("msg", "");
            jsonObject.put("fromUId", UserDetails.getUID());
            jsonObject.put("fromName", UserDetails.getUName());
            jsonObject.put("toUId", getIntent().getStringExtra("FUID"));
            jsonObject.put("mediaPath", Uploadfile.getName());
            jsonObject.put("fromEmail", UserDetails.getUEmail());
            jsonObject.put("toEmail", getIntent().getStringExtra("FEmail"));
            jsonObject.put("messageType", ApiKeys.IMG_TYP);
            jsonObject.put("ChatType", "1");
            MessageType = ApiKeys.IMG_TYP;
            Log.e("ChatData", jsonObject.toString());
            new ApiListener(ChatsHome.this, ApiUrl.ChatNoti, jsonObject).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onApiResponse(String response) {
        Log.e("ChatResponse", response);

        if (response.equals("Submitted.....")) {

            if (MessageType.equalsIgnoreCase(ApiKeys.MSG_TYP)) {
                ChatGetSetData chatGetSetData = new ChatGetSetData();

                chatGetSetData.setUserChatMsg(TypeMsj.getText().toString());
                chatGetSetData.setUserChatType(ApiKeys.MSG_TYP);
                ChatEndDataBase chatEndDataBase = new ChatEndDataBase(getBaseContext());
                chatEndDataBase.insertChat(UserDetails.getUID(), getIntent().getStringExtra("FUID"), TypeMsj.getText().toString(), "", ApiKeys.MSG_TYP, "1", "");

                TypeMsj.setText("");

                packageNames.add(chatGetSetData);
                scrollToBottom();
                adapter.notifyDataSetChanged();
            } else {
                ChatGetSetData chatGetSetData = new ChatGetSetData();

                chatGetSetData.setUserChatMsg(TypeMsj.getText().toString());
                chatGetSetData.setUserChatType(ApiKeys.IMG_TYP);
                ChatEndDataBase chatEndDataBase = new ChatEndDataBase(getBaseContext());
                chatEndDataBase.insertChat(UserDetails.getUID(), getIntent().getStringExtra("FUID"), "", Uploadfile.getName(), ApiKeys.IMG_TYP, "1", "");
                chatGetSetData.setUserChatImage(Uploadfile.getName());
                TypeMsj.setText("");

                packageNames.add(chatGetSetData);
                scrollToBottom();
                adapter.notifyDataSetChanged();
            }


        } else {
            Toast.makeText(this, "Please Check Internet Connection", Toast.LENGTH_SHORT).show();
        }
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
                    .setAspectRatio(15, 16)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri fileUri = result.getUri();
            String file_path = fileUri.getPath();
            File f = new File(fileUri.getPath());

            compressImage(f.getAbsolutePath());
            ShareImage();
        }
    }

    //////////************************* IMAGE COMPRESSION START *************************////////////////////

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;


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


        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

        options.inJustDecodeBounds = false;

        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
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
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        Uploadfile = new File(uriSting);
        Log.e("UpFile", Uploadfile.getPath().toString());
        return uriSting;
    }

    private void parseMessages(Cursor c) {
        packageNames.clear();
        if (c != null & c.getCount() > 0) {
            c.moveToFirst();
            do {
                ChatGetSetData chatGetSetData = new ChatGetSetData();
                //chatGetSetData.setUserChatName(intent.getStringExtra(ApiKeys.USER_NAME));
                chatGetSetData.setUserChatFrmID(c.getString(1));
                chatGetSetData.setUserChatToId(c.getString(2));
                chatGetSetData.setUserChatMsg(c.getString(3));
                chatGetSetData.setUserChatImage(c.getString(4));
                chatGetSetData.setUserChatType(c.getString(5));
                //chatGetSetData.setUserChatTime(intent.getStringExtra(ApiKeys.MESSAGE_DATE_TIME));
                packageNames.add(chatGetSetData);
            } while (c.moveToNext());
            scrollToBottom();
        }
    }

    private void scrollToBottom() {
//        chatHeaderListAdapter.notifyDataSetChanged();
//        list.smoothScrollToPosition(chatingDetailses.size()-1);
        adapter.notifyDataSetChanged();
        if (adapter.getItemCount() > 1)
            // recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, adapter.getItemCount() - 1);
            recyclerView.scrollToPosition(packageNames.size() - 1);
    }


    public void ShareImage() {
        ImageShareFile = new File(Uploadfile.getPath());
        Functions.showLoadingDialog(ChatsHome.this, "Please Wait........");
        AndroidNetworking.upload("http://xperiaindia.com/linkZone/imageUpload.php")
                .setPriority(Priority.HIGH)
                .setTag("Up")
                .addMultipartFile("uploaded_file", ImageShareFile)
                .addMultipartParameter("UID", "USER12")
                .build()
                .getAsString(new StringRequestListener() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("ChatIm", response);
                        ImageSumitAPIWork();
                        Functions.closeLoadingDialog();
                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        IncomeChatHandel.setChatCount(0);
    }

    public void APIGetDetails() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_email", getIntent().getStringExtra("FEmail"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AndroidNetworking.initialize(ChatsHome.this);
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
                            if (response.getString("response").matches("success"))
                            {
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


                                Intent intent = new Intent(ChatsHome.this, SeeProfile.class);
                                intent.putExtra("Favorite","Yes");
                                intent.putExtra("Uname",ar.getJSONObject(0).getString("user_name"));
                                intent.putExtra("UEmail",ar.getJSONObject(0).getString("user_email"));
                                intent.putExtra("UMob",ar.getJSONObject(0).getString("user_mobile"));
                                intent.putExtra("UID",ar.getJSONObject(0).getString("id"));
                                intent.putExtra("UImage",ar.getJSONObject(0).getString("image"));
                                intent.putExtra("UDesignation",ar.getJSONObject(0).getString("working_as"));
                                intent.putExtra("UDob",ar.getJSONObject(0).getString("user_dob"));
                                intent.putExtra("UStatus",ar.getJSONObject(0).getString("timeline_status"));
                                intent.putExtra("UInterest",ar.getJSONObject(0).getString("interested_in"));
                                intent.putExtra("ULives",ar.getJSONObject(0).getString("lives_in"));
                                intent.putExtra("UWorkAS",ar.getJSONObject(0).getString("working_as"));
                                intent.putExtra("UWorkAT",ar.getJSONObject(0).getString("working_at"));
                                intent.putExtra("UHHC",ar.getJSONObject(0).getString("high_school"));
                                intent.putExtra("USSC",ar.getJSONObject(0).getString("intermediate"));
                                intent.putExtra("UGrad",ar.getJSONObject(0).getString("graduation"));
                                intent.putExtra("UPG",ar.getJSONObject(0).getString("Post_graduation"));
                                intent.putExtra("UFBPage",ar.getJSONObject(0).getString("facebook_page"));
                                intent.putExtra("UWebsite",ar.getJSONObject(0).getString("add_website"));
                                intent.putExtra("CheckFriend","yes");
                                intent.putExtra("user_gender",ar.getJSONObject(0).getString("user_gender"));
                                intent.putExtra("cover_pic",ar.getJSONObject(0).getString("cover_pic"));

                                startActivity(intent);


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

}
