package com.team_vii.smsread;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
   static Switch mSwitch;
    Switch cSwitch;
    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = (TextView) findViewById(R.id.result);

        mSwitch = (Switch) findViewById(R.id.switch1);
        cSwitch = (Switch) findViewById(R.id.switch2);
        cSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cSwitch.isChecked())
                    callForward("on");
                else if(!cSwitch.isChecked())
                    callForward("off");
            }
        });
        /*mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mSwitch.isChecked())
                    messagesForward("0100162646","mohamed Kamel");

            }
        });*/


    }

    public void callForward(String switchState) {
        String phone = null;
        Intent intent = new Intent(Intent.ACTION_CALL);
        if (switchState.equals("on")) phone = "tel:" + "*21*01001626426";
        else if (switchState.equals("off")) phone = "tel:" + "#21";
        intent.setData(Uri.parse(phone+Uri.encode("#")));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);

    }

    /*public void messagesForward(String msgFrom,String msgBody) {
            ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<SMSModel> call = apiInterface.getMessages("incoming",msgFrom,msgBody);
            call.enqueue(new Callback<SMSModel>() {
                @Override
                public void onResponse(Call<SMSModel> call, Response<SMSModel> response) {
                    SMSModel smsModel = response.body();
                    String type = smsModel.getType(),
                            number = smsModel.getNumber(),
                            message = smsModel.getMessage();
                    Toast.makeText(MainActivity.this,type + "\n" + number + "\n" + message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<SMSModel> call, Throwable t) {
                    Log.d("my error",t.getMessage());
                }
            });

    }*/
}
