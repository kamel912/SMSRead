package com.team_vii.smsread;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    static Switch mSwitch;
    Switch cSwitch;
    TextView result;
    ResponseFromServer fromServer;


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

        if (mSwitch.isChecked()){

            SendSmsObserver smsObeserver = (new SendSmsObserver(new Handler()));
            ContentResolver contentResolver = this.getContentResolver();
            contentResolver.registerContentObserver(Uri.parse("content://sms"),true, smsObeserver);
            /*ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
            Call<ResponseFromServer> call = apiInterface.getMessages("testmohamed","outcomming","","");
            call.enqueue(new Callback<ResponseFromServer>() {
                @Override
                public void onResponse(Call<ResponseFromServer> call, Response<ResponseFromServer> response) {
                    fromServer = response.body();
                    String result = fromServer.getRespons();
                    Toast.makeText(MainActivity.this,result, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<ResponseFromServer> call, Throwable t) {
                    Log.d("my error",t.getMessage());
                }
            });*/
        }


    }

    public void callForward(String switchState) {
        String phone = null;
        Intent intent = new Intent(Intent.ACTION_CALL);
        if (switchState.equals("on")) phone = "tel:" + "*21*01001626426";
        else if (switchState.equals("off")) phone = "tel:" + "#21";
        intent.setData(Uri.parse(phone+Uri.encode("#")));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);

    }





    public class SendSmsObserver extends ContentObserver {

        public SendSmsObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            // save the message to the SD card here

            Log.d("sent sms", "one text send");

        }
    }
}
