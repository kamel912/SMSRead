package com.team_vii.smsread;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Date;

public class MainActivity extends AppCompatActivity {
    static boolean isSwitchChecked = false;
    Switch cSwitch,mSwitch;
    TextView result;
    static Date now;
    Intent startBroadcasting;


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
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mSwitch.isChecked()){
                    isSwitchChecked = true;
                    now = new Date();
                    startBroadcasting = new Intent(MainActivity.this, MyService.class);
                    startService(startBroadcasting);
                    getPackageManager().setComponentEnabledSetting(
                            new ComponentName(getApplicationContext(), MyService.class),
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                            PackageManager.DONT_KILL_APP);
                }else if (!mSwitch.isChecked()){
                    isSwitchChecked = false;
                    now = null;
                    startBroadcasting = new Intent(MainActivity.this, MyService.class);
                    stopService(startBroadcasting);
                    getPackageManager().setComponentEnabledSetting(
                            new ComponentName(getApplicationContext(), MyService.class),
                            PackageManager.COMPONENT_ENABLED_STATE_DEFAULT,
                            PackageManager.DONT_KILL_APP);
                }

            }
        });



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








}
