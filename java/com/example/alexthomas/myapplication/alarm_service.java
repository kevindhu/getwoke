package com.example.alexthomas.myapplication;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
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
    public static boolean if_AlarmSchedule = false;
    public static int ringtone = R.raw.motivationalmusic;
    public static boolean fromAlarmStart = false;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("onStartCommand", "Initiated alarm service (for starting and stopping music and generating new quotes");
        SharedPreferences sharedRingtone = getSharedPreferences("Ringtones", MODE_PRIVATE);
        String message = sharedRingtone.getString("Message", "Default Ringtones");
        ringtone_changer(message);
        //String.valueOf(intent.getExtras().getBoolean("From Main Alarm"));
        Log.e("isRunning", String.valueOf(isRunning));
        Log.e("Alarm Schedule", "FromMainAlarm: " + String.valueOf(fromMainAlarm));
        Log.e("Alarm Schedule", "fromAlarmStart: " + String.valueOf(fromAlarmStart));


        if (isRunning) {
            try {
                mediasong.stop();
                mediasong.reset();
            } catch (NullPointerException e) {
                Log.e("NullpointException", "e");
            }
            Log.e("cancel", "Cancelled in alarm_service");
            isRunning = false;
        } else {
            alarm_service.isRunning = true;
            //Sets whether alarm is repeating
            control_RepeatingAlarm = if_RepeatingAlarm;
            //Last Genre
            SharedPreferences sharedPref_genres = getSharedPreferences("Genres", MODE_PRIVATE);
            String genre = sharedPref_genres.getString("Message", "All Genres");



            //displays new quote
            randomQuote newQuote = new randomQuote();
            Log.e("Alarm Service","Old last_rand is "+String.valueOf(newQuote.last_rand));


            Log.e("alright", "Generating new quote with genre set to " + genre);


            quote = newQuote.quote_generator(genre);
            saverandInt();



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
            } catch (NullPointerException e) {
                Log.e("MainActivity is closed", "Stored in sharedPref");
            }


            //Starts Playing Music
            mediasong = MediaPlayer.create(alarm_service.this, ringtone);
            mediasong.start();

            //Starts Main Alarm for alarm schedule
            if (fromMainAlarm || fromAlarmStart) {
                start_Alarm();
            }


            //Catches whether MainActivity is closed when changing text to 'silence alarm', if so, stores in sharedpref
            try {
                MainActivity.snooze_alarm.setText("Silence Alarm");
                SharedPreferences sharedPref_alarm_unset = getSharedPreferences("Alarm Unset", MODE_PRIVATE);
                SharedPreferences.Editor editor_alarm_unset = sharedPref_alarm_unset.edit();
                editor_alarm_unset.putString("Alarm Button Text", "Silence Alarm");
                editor_alarm_unset.apply();
            } catch (NullPointerException e) {
                SharedPreferences sharedPref_alarm_unset = getSharedPreferences("Alarm Unset", MODE_PRIVATE);
                SharedPreferences.Editor editor_alarm_unset = sharedPref_alarm_unset.edit();
                editor_alarm_unset.putString("Alarm Button Text", "Silence Alarm");
                editor_alarm_unset.apply();
            }


            //Alarm snoozes automatically after song stops if snooze feature is on
            mediasong.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    SharedPreferences checkbox_boolean = getSharedPreferences("Snooze Boolean",MODE_PRIVATE);
                    String ifRepeatingOn = checkbox_boolean.getString("Message", "false");
                    if (Boolean.valueOf(ifRepeatingOn)) {
                        //Log.e("Setting", "Text Automatically to Alarm Off");
                        try {
                            MainActivity.snooze_alarm.setText("I'm Woke!");
                            SharedPreferences sharedPref_alarm_unset1 = getSharedPreferences("Alarm Unset", MODE_PRIVATE);
                            SharedPreferences.Editor editor_alarm_unset = sharedPref_alarm_unset1.edit();
                            editor_alarm_unset.putString("Alarm Button Text", "I'm Woke!");
                            editor_alarm_unset.apply();
                            alarm_service.isRunning = false;
                            snooze_restart();
                        } catch (NullPointerException e) {
                            SharedPreferences sharedPref_alarm_unset2 = getSharedPreferences("Alarm Unset", MODE_PRIVATE);
                            SharedPreferences.Editor editor_alarm_unset = sharedPref_alarm_unset2.edit();
                            editor_alarm_unset.putString("Alarm Button Text", "I'm Woke!");
                            editor_alarm_unset.apply();
                            alarm_service.isRunning = false;
                            snooze_restart();
                        }


                    } else {
                        try {
                            MainActivity.snooze_alarm.setText("Get Quotes");
                            Log.e("Not repeating", "This alarm does not repeat");
                            control_RepeatingAlarm = if_RepeatingAlarm;
                            already_Pressed = false;
                            SharedPreferences sharedPref_alarm_unset3 = getSharedPreferences("Alarm Unset", MODE_PRIVATE);
                            SharedPreferences.Editor editor_alarm_unset = sharedPref_alarm_unset3.edit();
                            editor_alarm_unset.putString("Alarm Button Text", "Alarm Off");
                            editor_alarm_unset.apply();
                        } catch (NullPointerException e) {
                            SharedPreferences sharedPref_alarm_unset3 = getSharedPreferences("Alarm Unset", MODE_PRIVATE);
                            SharedPreferences.Editor editor_alarm_unset = sharedPref_alarm_unset3.edit();
                            editor_alarm_unset.putString("Alarm Button Text", "Get Quotes");
                            editor_alarm_unset.apply();
                            control_RepeatingAlarm = if_RepeatingAlarm;
                            already_Pressed = false;
                        }
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
        Toast.makeText(this, "On destroy called", Toast.LENGTH_SHORT).show();
    }

    public void saverandInt() {
        SharedPreferences sharedPref_alarm_unset = getSharedPreferences("Random Int", MODE_PRIVATE);
        SharedPreferences.Editor editor_alarm_unset = sharedPref_alarm_unset.edit();
        editor_alarm_unset.putInt("Int", randomQuote.last_rand);
        Log.e("Last rand","saved as "+String.valueOf(randomQuote.last_rand));
        editor_alarm_unset.apply();
    }

    public void ringtone_changer(String input) {
        switch (input) {
            case "Haikyuu":
                ringtone = R.raw.motivationalmusic;
                break;
            case "Believe It":
                ringtone = R.raw.believeit;
                break;
            case "Get Up":
                ringtone = R.raw.getup;
                break;
        }
    }


    public void snooze_restart() {
        //snooze restart
        fromMainAlarm = false;
        fromAlarmStart = false;

        SharedPreferences sharedPreferences = getSharedPreferences("Repeating Intervals", MODE_PRIVATE);
        long interval = sharedPreferences.getLong("Interval", 0);

        Log.e("Snooze", "Start new repeating alarm");
        Log.e("Interval", String.valueOf(interval));

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent alarm_intent = new Intent(alarm_service.this, alarm_receiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(alarm_service.this, 2, alarm_intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, pendingIntent);
        }
        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, pendingIntent);
            }
            else{
                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, pendingIntent);

            }
        }
    }


    public void store_fromMainAlarm(Boolean message) {
        SharedPreferences sharedPref = getSharedPreferences("Main Alarm", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("Message", message);
        editor.apply();
    }


    public void start_Alarm() {
        //starts alarm again periodically
        fromMainAlarm = false;
        fromAlarmStart = false;
        SharedPreferences sharedPreferences = getSharedPreferences("Alarm Schedule", MODE_PRIVATE);
        Boolean schedule_Enabled = sharedPreferences.getBoolean("Schedule Enabled", false);
        long alarm_schedule = sharedPreferences.getLong("Interval", AlarmManager.INTERVAL_DAY);

        if (schedule_Enabled) {
            Log.e("Alarm Schedule", "Alarm Schedule ON");
            Log.e("Alarm Schedule", String.valueOf(alarm_schedule));
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent alarm_intent = new Intent(alarm_service.this, alarm_receiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(alarm_service.this, 1, alarm_intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + alarm_schedule, pendingIntent);
            }
            else{
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + alarm_schedule, pendingIntent);
                }
                else{
                    alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + alarm_schedule, pendingIntent);
                }
            }
        }
        else {
            Log.e("Alarm Schedule", "Off");
            MainActivity.alarm_confirmation.setText("Your alarm is unset.");
            MainActivity.powerButton_on = false;
            MainActivity.on_off_boolean(false);
            store_PowerButtonBoolean(false);
            store_timer_null(true);
        }

    }

    private void store_Alarm_bottomText(String message){

        SharedPreferences sharedPref_alarm_unset = getSharedPreferences("Alarm Time", MODE_PRIVATE);
        SharedPreferences.Editor editor_alarm_unset = sharedPref_alarm_unset.edit();
        editor_alarm_unset.putString("Message", message);
        editor_alarm_unset.apply();
    }

    public void store_PowerButtonBoolean(Boolean message) {

        SharedPreferences sharedPref = getSharedPreferences("Power Button", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("Message", message);
        editor.apply();
    }

    public void store_timer_null(Boolean message) {
        SharedPreferences sharedPref = getSharedPreferences("Last Timer", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("Message", message);
        editor.apply();
    }


}
