package com.team_vii.smsread;

import android.Manifest;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.crash.FirebaseCrash;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity {
    boolean isSwitchChecked = false;
    Switch cSwitch, mSwitch;
    TextView result;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Intent broadcasting;
    String[] permissions = {
            Manifest.permission.READ_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
            Manifest.permission.RECEIVE_BOOT_COMPLETED
    };


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseCrash.report(new Exception("My first Android non-fatal error"));
        preferences = getSharedPreferences("SwitchState", MODE_PRIVATE);
        editor = preferences.edit();

        result = findViewById(R.id.result);
        mSwitch = findViewById(R.id.switch1);
        cSwitch = findViewById(R.id.switch2);
        cSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cSwitch.isChecked())
                    callForward("on");
                else if (!cSwitch.isChecked())
                    callForward("off");
            }
        });
        broadcasting = new Intent(getApplicationContext(), MyService.class);
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            PackageManager pm = getPackageManager();

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mSwitch.isChecked()) {
                    isSwitchChecked = true;
                    pm.setComponentEnabledSetting(new ComponentName(getApplicationContext(), com.team_vii.smsread.MyService.class),
                            PackageManager.COMPONENT_ENABLED_STATE_ENABLED, PackageManager.DONT_KILL_APP);
                    startService(broadcasting);
                } else if (!mSwitch.isChecked()) {
                    isSwitchChecked = false;
                    pm.setComponentEnabledSetting(new ComponentName(getApplicationContext(), com.team_vii.smsread.MyService.class),
                            PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);
                    stopService(broadcasting);
                }
                editor.putBoolean("state", isSwitchChecked);
                editor.apply();
            }
        });
        if (preferences != null) {
            isSwitchChecked = preferences.getBoolean("state", false);
            mSwitch.setChecked(isSwitchChecked);
        }


        for (int i = 0; i < permissions.length; i++) {
            if (ActivityCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        permissions[i])) {

                    // Show an explanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    toast("permission must have been granted");

                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{permissions[i]},
                            1);
                }
            }
        }

    }

    public void callForward(String switchState) {
        String phone = null;
        Intent intent = new Intent(Intent.ACTION_CALL);
        if (switchState.equals("on")) phone = "tel:" + "*21*01001626426";
        else if (switchState.equals("off")) phone = "tel:" + "#21";
        intent.setData(Uri.parse(phone + Uri.encode("#")));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    toast("Permission Granted");
                } else {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{permissions[0]},
                            1);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void toast (String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }
}
