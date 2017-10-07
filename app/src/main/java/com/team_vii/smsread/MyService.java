package com.team_vii.smsread;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.team_vii.smsread.MainActivity.isSwitchChecked;
import static com.team_vii.smsread.MainActivity.now;

public class MyService extends Service {
    ResponseFromServer fromServer;
    static String lastId ;

    public MyService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "start", Toast.LENGTH_SHORT).show();

        Bundle bundle = intent.getExtras();
        if (bundle != null)
            isSwitchChecked = bundle.getBoolean("isSwitchChecked");
        SendSmsObserver smsObeserver = (new SendSmsObserver(new Handler()));
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        Uri uri = Uri.parse("content://sms");
        contentResolver.registerContentObserver(uri,true, smsObeserver);

        class SMSBroadcastReceiver extends BroadcastReceiver {
            private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

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
                           if(isSwitchChecked) forward("incomming",msgFrom,msgBody);


                        }
                    }
                }
            }

        }
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "destroy", Toast.LENGTH_SHORT).show();
    }

    public class SendSmsObserver extends ContentObserver {

        public SendSmsObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {

            super.onChange(selfChange);
            // save the message to the SD card here
            if(isSwitchChecked){
                Activity activity = new Activity();
                Uri mSmsinboxQueryUri = Uri.parse("content://sms/sent");
                Cursor cursor1 = getContentResolver().query(mSmsinboxQueryUri,
                        new String[]{"_id", "thread_id", "address", "person", "date",
                        "body", "type"}, null, null, null);
                activity.startManagingCursor(cursor1);
                String[] columns = new String[]{"address", "person", "date", "body", "type","_id"};
                if (cursor1.getCount() > 0) {
                    String count = Integer.toString(cursor1.getCount());
                    Log.e("Count", count);
                    while (cursor1.moveToNext()) {
                        String address = cursor1.getString(cursor1.getColumnIndex(columns[0]));
                        String name = cursor1.getString(cursor1.getColumnIndex(columns[1]));
                        String date = cursor1.getString(cursor1.getColumnIndex(columns[2]));
                        Long timestamp = Long.parseLong(date);
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(timestamp);
                        Date finaldate = calendar.getTime();
                        String msg = cursor1.getString(cursor1.getColumnIndex(columns[3]));
                        int type = cursor1.getInt(cursor1.getColumnIndex(columns[4]));
                        String id = cursor1.getString(cursor1.getColumnIndex(columns[5]));
                        if (finaldate.after(now) && !id.equals(lastId)) {
                            forward("outgoing",address,msg);
                            now = finaldate;
                            lastId = id;
                        }
                        break;
                    }
                        Log.d("sent sms", "one text send");

                }
            }
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        Intent restartServiceTask = new Intent(getApplicationContext(),this.getClass());
        Bundle bundle = new Bundle();
        bundle.putBoolean("isSwichChecked",isSwitchChecked);
        restartServiceTask.putExtras(bundle);
        restartServiceTask.setPackage(getPackageName());
        PendingIntent restartPendingIntent =PendingIntent.getService(getApplicationContext(), 1,restartServiceTask, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager myAlarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        myAlarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartPendingIntent);



        super.onTaskRemoved(rootIntent);
    }

    void forward(String type, String number, String message){
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<ResponseFromServer> call = apiInterface.getMessages("testmohamed",type,number,message);
        call.enqueue(new Callback<ResponseFromServer>() {
            @Override
            public void onResponse(Call<ResponseFromServer> call, Response<ResponseFromServer> response) {
                fromServer = response.body();
                String result = fromServer.getRespons();
                Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseFromServer> call, Throwable t) {
                Log.d("my error",t.getMessage());
            }
        });
    }

}
