package com.example.alexthomas.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

/* Created by alexthomas on 12/31/16. */

public class alarm_service extends Service {

    public static Boolean isRunning = false;
    private MediaPlayer mediasong;
    String[] quote;
    public static String genre;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("onStartCommand", "Initated");

        if (isRunning){
            mediasong.stop();
            mediasong.reset();
            Log.e("cancel", "cancelled");
            isRunning = false;
        }
        else {
            alarm_service.isRunning = true;
            MainActivity.snooze_alarm.setText("Silence Alarm");
            //displays new quote
            randomQuote newQuote = new randomQuote();
            genre = MainActivity.genre;
            Log.e("alright","Generating new quote with genre set to "+genre);
            quote = newQuote.quote_generator(genre);


            mediasong = MediaPlayer.create(alarm_service.this, R.raw.getup);
            //Plays song , for testing
            mediasong.start();

            //Notification manager
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            //Intent to MainActivity


            //Notification
            Intent intent_mainactivity = new Intent(this, MainActivity.class);

            intent_mainactivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent pendingIntent_mainactivity = PendingIntent.getActivity(
                    getApplicationContext(),
                    2,
                    intent_mainactivity,
                    PendingIntent.FLAG_UPDATE_CURRENT);


            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.gw_logo)
                            .setVisibility(0)
                            .setAutoCancel(true)
                            .setContentIntent(pendingIntent_mainactivity)
                            .setContentTitle("Get Woke")
                            .setContentText(quote[0] + " - " + quote[1]);

            notificationManager.notify(0, mBuilder.build());
            }

            return START_NOT_STICKY;

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "On destroy called",Toast.LENGTH_SHORT).show();
    }
}
