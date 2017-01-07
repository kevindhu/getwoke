package com.example.alexthomas.myapplication;

import android.app.AlarmManager;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

/* Created by alexthomas on 12/31/16. */

public class alarm_service extends Service {

    public static Boolean isRunning = false;
    private MediaPlayer mediasong;
    String[] quote;
    public static String genre;
    public static boolean control_RepeatingAlarm = true; //change constantly
    public static boolean if_RepeatingAlarm = true; //wants to repeat
    public static boolean already_Pressed = false;
    public static boolean fromMainAlarm = false;
    public static long interval =
            0;
    public static long alarm_schedule = 0;
    public static boolean if_AlarmSchedule = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("onStartCommand", "Initiated alarm service (for starting and stopping music and generating new quotes");

        if (isRunning){
            try {
                mediasong.stop();
                mediasong.reset();
            }
            catch (NullPointerException e){
                Log.e("NullpointException", "e");
            }
            Log.e("cancel", "cancelled");
            isRunning = false;
        }


        else {
            alarm_service.isRunning = true;
            //Sets whether alarm is repeating
            control_RepeatingAlarm = if_RepeatingAlarm;
            //Last Genre
            SharedPreferences sharedPref_genres = getSharedPreferences("Genres", MODE_PRIVATE);
            String genre = sharedPref_genres.getString("Message", "All Genres");

            //displays new quote
            randomQuote newQuote = new randomQuote();
            Log.e("alright","Generating new quote with genre set to "+genre);
            quote = newQuote.quote_generator(genre);
            saverandInt();
            saveMaxMin();

            //Instantiates sharedPrefs and saves quote/quoter
            SharedPreferences sharedPref_quote = getSharedPreferences("Quote", MODE_PRIVATE);
            SharedPreferences sharedPref_quoter = getSharedPreferences("Quoter", MODE_PRIVATE);
            SharedPreferences.Editor editor_quote = sharedPref_quote.edit();
            SharedPreferences.Editor editor_quoter = sharedPref_quoter.edit();
            editor_quote.putString("Quote", quote[0]);
            editor_quoter.putString("Quoter", quote[1]);
            editor_quote.apply();
            editor_quoter.apply();

            //Sets the text to the new quote generated and Catches whether MainActivity is closed when changing quote/quoter text
            try {
                //sets text
                MainActivity.motivational_quote.setText(quote[0]);
                MainActivity.quoter.setText(quote[1]);
                //Animation ghetto version
                final Animation quoteRise1 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
                final Animation quoteRise2 = AnimationUtils.loadAnimation(this, R.anim.fade_in);
                MainActivity.motivational_quote.startAnimation(quoteRise1);
                MainActivity.quoter.startAnimation(quoteRise2);
            }
            catch(NullPointerException e) {
                    Log.e("MainActivity is closed", "Stored in sharedPref");
            }



            //Starts Playing Music
            mediasong = MediaPlayer.create(alarm_service.this, R.raw.believeit);
            mediasong.start();

            //Starts Main Alarm
            start_Alarm();






            //Catches whether MainActivity is closed when changing text to 'silence alarm', if so, stores in sharedpref
            try {
                MainActivity.snooze_alarm.setText("Silence Alarm");
                SharedPreferences sharedPref_alarm_unset = getSharedPreferences("Alarm Unset", MODE_PRIVATE);
                SharedPreferences.Editor editor_alarm_unset = sharedPref_alarm_unset.edit();
                editor_alarm_unset.putString("Alarm Button Text", "Silence Alarm");
                editor_alarm_unset.apply();
            }
            catch(NullPointerException e) {
                SharedPreferences sharedPref_alarm_unset = getSharedPreferences("Alarm Unset", MODE_PRIVATE);
                SharedPreferences.Editor editor_alarm_unset = sharedPref_alarm_unset.edit();
                editor_alarm_unset.putString("Alarm Button Text", "Silence Alarm");
                editor_alarm_unset.apply();

            }





            //Alarm snoozes automatically after song stops if snooze feature is on
            mediasong.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if(control_RepeatingAlarm){
                    //Log.e("Setting", "Text Automatically to Alarm Off");
                    MainActivity.snooze_alarm.setText("I'm Woke!");
                    alarm_service.isRunning = false;
                    snooze_restart();
                    }
                    else {
                        MainActivity.snooze_alarm.setText("Alarm Off");
                        Log.e("Not repeating", "This alarm does not repeat");
                        control_RepeatingAlarm = if_RepeatingAlarm;
                        already_Pressed = false;
                    }
                }

            });




            //Notification manager
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Intent intent_mainactivity = new Intent(this, MainActivity.class);
            intent_mainactivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                    | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent pendingIntent_mainactivity = PendingIntent.getActivity(
                    getApplicationContext(), 2, intent_mainactivity, PendingIntent.FLAG_UPDATE_CURRENT);

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

    public void saverandInt() {
        SharedPreferences sharedPref_alarm_unset = getSharedPreferences("Random Int", MODE_PRIVATE);
        SharedPreferences.Editor editor_alarm_unset = sharedPref_alarm_unset.edit();
        editor_alarm_unset.putInt("Int", randomQuote.last_rand);
        editor_alarm_unset.apply();
    }

    public void saveMaxMin() {
        SharedPreferences sharedPref_max = getSharedPreferences("Max", MODE_PRIVATE);
        SharedPreferences sharedPref_min = getSharedPreferences("Min", MODE_PRIVATE);
        SharedPreferences.Editor editor_max = sharedPref_max.edit();
        SharedPreferences.Editor editor_min = sharedPref_min.edit();
        editor_max.putInt("Max", randomQuote.maxlength);
        editor_min.putInt("Min", randomQuote.minlength);
        editor_max.apply();
        editor_min.apply();
    }


    public void snooze_restart() {
        //snooze restart
        Log.e("alarm","Start new repeating alarm");

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent alarm_intent = new Intent(alarm_service.this, alarm_receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(alarm_service.this, 2, alarm_intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ this.interval ,pendingIntent);
    }



    public void start_Alarm() {
        //starts alarm again periodically
        if(this.if_AlarmSchedule){
        Log.e("alarm","Start new alarm");
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent alarm_intent = new Intent(alarm_service.this, alarm_receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(alarm_service.this, 1, alarm_intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+ alarm_schedule,pendingIntent);
        }
        Log.e("Alarm Schedule", "Off");

    }

}
