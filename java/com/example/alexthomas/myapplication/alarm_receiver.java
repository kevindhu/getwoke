package com.example.alexthomas.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/* Created by alexthomas on 12/31/16. */

public class alarm_receiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        Log.e("Hi", "hi");

        Boolean if_alarm = intent.getExtras().getBoolean("Alarm_off");

        Intent service_intent = new Intent(context, alarm_service.class);

        if (if_alarm) {
            Log.e("If/else", "sucess");
            service_intent.putExtra("Alarm_off", true);
        }
        else {
            service_intent.putExtra("Alarm_off", false);
        }


        context.startService(service_intent);
        Log.e("Hi", "bye");
    }

}
