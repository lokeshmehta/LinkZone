package com.linkzone.linkzoneapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.linkzone.linkzoneapp.ApiDetails.ApiListners.ApiListener;
import com.linkzone.linkzoneapp.ApiDetails.ApiListners.ApiResponse;
import com.linkzone.linkzoneapp.ApiDetails.ApiUrls.ApiUrl;
import com.linkzone.linkzoneapp.DataHolders.UserDetails;
import com.linkzone.linkzoneapp.Functions.Functions;
import com.linkzone.linkzoneapp.R;
import com.linkzone.linkzoneapp.widget.MyEditText;
import com.linkzone.linkzoneapp.widget.RippleView;
import org.json.JSONException;
import org.json.JSONObject;
import butterknife.BindView;
import butterknife.ButterKnife;

public class FeedbackLay extends AppCompatActivity implements ApiResponse {
    @BindView(R.id.LeftArrow)ImageView backArrow;
    @BindView(R.id.SubmitFeed)RippleView SubmitF;
    @BindView(R.id.EditSubmit)MyEditText EditFeed;
    @BindView(R.id.ParentLog)LinearLayout Pl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_lay);
        ButterKnife.bind(this);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
            }
        });

        SubmitF.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                  if(EditFeed.getText().length()>0){
                      APIWork();
                  }
            }
        });
    }

    public void APIWork()
    {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("user_email", UserDetails.getUEmail());
            jsonObject.put("user_id", UserDetails.getUID());
            jsonObject.put("feedback",EditFeed.getText().toString());
            Functions.showLoadingDialog(FeedbackLay.this,"Please wait......");
            new ApiListener(FeedbackLay.this, ApiUrl.GiveFeedback, jsonObject).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
    }

    @Override
    public void onApiResponse(String response) {
        Log.e("Response",response);
        Functions.closeLoadingDialog();

        try {
            JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.getString("response").matches("success"))
            {
                Functions.showMessage(Pl,"Feedback Submitted Successfully",FeedbackLay.this);
                EditFeed.setText("");
            }
            else
            {
                Functions.showErrorMessage(Pl,"Something Went Wrong",FeedbackLay.this);
            }
        } catch (JSONException e) {

        }

    }
}
