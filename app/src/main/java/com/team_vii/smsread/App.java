package com.team_vii.smsread;

import android.app.Application;
import android.content.Intent;

/**
 * Created by MK on 10/9/2017.
 */

public class App extends Application {
    Intent startBroadcasting;

    @Override
    public void onCreate() {
        super.onCreate();
        startBroadcasting = new Intent(getApplicationContext(), MyService.class);
        startService(startBroadcasting);
    }
}
