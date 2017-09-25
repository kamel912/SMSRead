package com.team_vii.smsread;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.team_vii.smsread.MainActivity.mSwitch;

/**
 * Created by MK on 9/22/2017.
 */

public class SMSBroadcastReceiver extends BroadcastReceiver {
    private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SMSBroadcastReceiver";
ResponseFromServer fromServer;
    @Override
    public void onReceive(final Context context, Intent intent) {
        if(intent.getAction().equals(SMS_RECEIVED)){
            Bundle bundle = intent.getExtras();           //---get the SMS message passed in---
            SmsMessage[] msgs;
            if (bundle != null){
                //---retrieve the SMS message received---
                Object[] pdus = (Object[]) bundle.get("pdus");
                assert pdus != null;
                msgs = new SmsMessage[pdus.length];
                String msgFrom,msgBody;
                for(int i=0; i<msgs.length; i++){
                    msgs[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                    msgFrom = msgs[i].getOriginatingAddress();
                    msgBody = msgs[i].getMessageBody();
                    if (mSwitch.isChecked()){
                        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                        Call<ResponseFromServer> call = apiInterface.getMessages("incoming",msgFrom,msgBody);
                        call.enqueue(new Callback<ResponseFromServer>() {
                            @Override
                            public void onResponse(Call<ResponseFromServer> call, Response<ResponseFromServer> response) {
                                fromServer = response.body();
                                String type =fromServer.getParams().getQuerystring().getType(),
                                       number = fromServer.getParams().getQuerystring().getNumber(),
                                       message = fromServer.getParams().getQuerystring().getMessage();
                                Toast.makeText(context,type + "\n" + number + "\n" + message, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<ResponseFromServer> call, Throwable t) {
                                Log.d("my error",t.getMessage());
                            }
                        });


                    }else if (!mSwitch.isChecked())
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    }
            }
        }
    }
}
