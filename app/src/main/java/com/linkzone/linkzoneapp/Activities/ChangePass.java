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

public class ChangePass extends AppCompatActivity implements ApiResponse {

    @BindView(R.id.OldPass)MyEditText OldP;
    @BindView(R.id.NewPass)MyEditText NewP;
    @BindView(R.id.ConfirmPass)MyEditText CPas;
    @BindView(R.id.ChangeP)RippleView ChangeP;
    @BindView(R.id.CancelP)RippleView CancelP;
    @BindView(R.id.ParentLog)LinearLayout Pl;
    @BindView(R.id.ArrowBack)ImageView backArrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        ButterKnife.bind(ChangePass.this);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
            }

        });

        CancelP.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {
                onBackPressed();
                overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
            }
        });

        ChangeP.setOnRippleCompleteListener(new RippleView.OnRippleCompleteListener() {
            @Override
            public void onComplete(RippleView rippleView) {

                if(OldP.getText().toString().length()<=0 && NewP.getText().toString().length()<=0
                        && CPas.getText().toString().length()<=0)
                {
                    Functions.showErrorMessage(Pl,"All Field Mandatory",ChangePass.this);
                }
                else
                {
                    if(NewP.getText().toString().trim().matches(CPas.getText().toString().trim()))
                    {
                        try
                        {
                            JSONObject o=new JSONObject();
                            o.put("old_password",OldP.getText().toString());
                            o.put("new_password",NewP.getText().toString());
                            o.put("user_email", UserDetails.getUEmail());
                            Functions.showLoadingDialog(ChangePass.this,"Please Wait.......");
                            new ApiListener(ChangePass.this, ApiUrl.ChangePassword,o).execute();
                        }
                        catch (Exception e){Functions.closeLoadingDialog();}
                    }
                    else
                    {
                        Functions.showErrorMessage(Pl, "New and Confirm Password not Matched", ChangePass.this);
                    }
                }

            }
        });

    }

    @Override
    public void onApiResponse(String response) {

        Log.e("Response",response);
            Functions.closeLoadingDialog();

            try {
                JSONObject jsonObject = new JSONObject(response);

            if (jsonObject.getString("response").matches("success"))
            {
                Functions.showMessage(Pl,"Password Successfully Changed",ChangePass.this);
                OldP.setText("");
                NewP.setText("");
                CPas.setText("");
            }
            else
            {
                if(jsonObject.getString("response").matches("invalid_old_password"))
                {
                    Functions.showErrorMessage(Pl,"Invalid Old Password",ChangePass.this);
                }
                else
                {
                    Functions.showErrorMessage(Pl,"Something Went Wrong",ChangePass.this);
                }

            }
        } catch (JSONException e) {

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);
    }

}
