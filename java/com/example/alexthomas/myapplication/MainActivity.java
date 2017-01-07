package com.example.alexthomas.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Debug;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.util.Log;
import android.widget.DigitalClock;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.graphics.Color;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.os.Handler;

import java.util.Calendar;

import static java.lang.StrictMath.abs;


public class MainActivity extends AppCompatActivity {
    public static String genre = "null";
    public static TextView motivational_quote;
    public static TextView quoter;
    public static ImageView logo;
    public PendingIntent pendingIntent;
    public static Intent alarm_intent;
    public static TextView alarm_confirmation;
    public static Button snooze_alarm;
    public static RelativeLayout content_main;
    public static Context context;
    public static Button powerButton;
    public static Boolean powerButton_on = false;
    private static Boolean lastTimerisNull = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();

        powerButton = (Button) findViewById(R.id.alarm_off);
        snooze_alarm = (Button) findViewById(R.id.alarm_off);
        powerButton = (Button) findViewById(R.id.powerbutton);
        content_main = (RelativeLayout) findViewById(R.id.content_main);


        final ImageView settings_feedback = (ImageView) findViewById(R.id.settings_feedback);
        settings_feedback.setAlpha(0f);


        //Sets font on Clock
        Typeface blockFonts = Typeface.createFromAsset(getAssets(), "fonts/Lato-Black.ttf");
        DigitalClock clock = (DigitalClock) findViewById(R.id.textClock);
        clock.setTypeface(blockFonts);


        /////SHAREDPREF GETTERS/////

        //Captures font from previous session
        SharedPreferences sharedPref_font = getSharedPreferences("Font", MODE_PRIVATE);
        String last_font = sharedPref_font.getString("Message", "--Choose your Font--");


        //Captures background from previous session
        SharedPreferences sharedPref_background = getSharedPreferences("Backgrounds", MODE_PRIVATE);
        String last_background = sharedPref_background.getString("Message", "--Choose your Background--");

        //Sets last configured time
        alarm_confirmation = (TextView) findViewById(R.id.alarm_confirmation);
        alarm_confirmation.setText(getInput());
        snooze_alarm.setText(getAlarmButtonText());
        if (get_PowerButtonBoolean()) {
            powerButton.setText("Turn Off");
        } else {
            powerButton.setText("Turn On");
        }

        //sets quote/quoter from previous session
        motivational_quote = (TextView) findViewById(R.id.motivationalQuote);
        motivational_quote.setText(getQuote());
        quoter = (TextView) findViewById(R.id.quoter);
        quoter.setText(getQuoter());
        setRandInt();
        setMaxMin();

        //Updates font
        font_changer(last_font); //invokes this class's font_changer

        //Updates background
        background_changer(last_background);


        /////LISTENERS/////

        //Sets Listener on Set Alarm Button
        Button set_alarm = (Button) findViewById(R.id.set_alarm);
        set_alarm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                launchSet_Alarm();
            }
        });


        //Sets Listener on Settings Button
        ImageButton true_settings = (ImageButton) findViewById(R.id.settings_button);
        true_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                settings_feedback.setAlpha(1f);
                launchActivity();
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        settings_feedback.setAlpha(0f);
                    }
                }, 1000);
            }

        });


        //Sets Listener on Share Button
        ImageButton share_facebook = (ImageButton) findViewById(R.id.share_button);
        share_facebook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //launch share to facebook page
            }
        });


        //make a listener on the Power Button
        powerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checks whether MAIN alarm is pending
                Log.e("Test", String.valueOf(powerButton_on));
                powerButton_on = !powerButton_on;

                SharedPreferences sharedPref = getSharedPreferences("Alarm Time", MODE_PRIVATE);
                int hour = sharedPref.getInt("Hour", -1);
                int minute = sharedPref.getInt("Minute", -1);
                Log.e("Time", String.valueOf(hour) + "" + String.valueOf(minute));
                if (powerButton_on) {
                    powerButton.setText("Turn Off");
                    store_PowerButtonBoolean(true);
                } else {
                    powerButton.setText("Turn On");
                    store_PowerButtonBoolean(false);
                }
                //Checks if pending intent with int 1 is still around
                boolean alarmUp = (PendingIntent.getBroadcast(MainActivity.this, 1,
                        new Intent(MainActivity.this, alarm_receiver.class),
                        PendingIntent.FLAG_NO_CREATE) != null);

                //Checks whether the user sets an alarm
                if ((hour == -1) && (minute == -1)) {
                    Log.e("Conditional", "1");
                    Toast.makeText(MainActivity.this, "You need to set an alarm first!", Toast.LENGTH_SHORT).show();
                }
                //
                else if (!alarmUp && !powerButton_on && !alarm_service.isRunning) {
                    Log.e("Conditional", "2");
                    Toast.makeText(MainActivity.this, "The alarm is already off!", Toast.LENGTH_SHORT).show();
                }
                //When alarm schedule has no pending alarm and user turns power on
                else if (!alarmUp && powerButton_on) {
                    Log.e("Conditional", "3");
                    Log.e("getInput", getInput());
                    alarm_intent = new Intent(MainActivity.this, alarm_receiver.class);
                    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 1, alarm_intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);
                    long time = calendar.getTimeInMillis();
                    alarm_confirmation.setText(getInput());
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, adjustTime(time), pendingIntent);
                    lastTimerisNull = false;
                }
                //When alarm has a pending alarm and user turns it off
                else if (alarmUp && !powerButton_on) {
                    Log.e("Conditional", "4");
                    alarm_intent = new Intent(MainActivity.this, alarm_receiver.class);
                    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 1, alarm_intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    pendingIntent.cancel();
                    lastTimerisNull = true;
                    alarm_confirmation.setText("Your alarm is unset.");
                    Toast.makeText(MainActivity.this, "Alarm unset.", Toast.LENGTH_SHORT).show();
                    Log.e("Cancelled for real", "Cancelled Intent");
                }

            }
        });


        //make a listener on the Snooze Button
        snooze_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Checks whether REPEATING alarm is on
                boolean repeating_alarm = (PendingIntent.getBroadcast(MainActivity.this, 2,
                        new Intent(MainActivity.this, alarm_receiver.class),
                        PendingIntent.FLAG_NO_CREATE) != null);

                Log.e("isRunning", String.valueOf(alarm_service.isRunning));
                Log.e("repeating_alarm", String.valueOf(repeating_alarm));

                alarm_service.already_Pressed = true;

                if (repeating_alarm && snooze_alarm.getText() == "I'm Woke!") {
                    Log.e("Conditional", "1");
                    Intent repeating_alarm_intent = new Intent(MainActivity.this, alarm_receiver.class);
                    PendingIntent repeating_pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 2, repeating_alarm_intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    alarm_service.control_RepeatingAlarm = false;
                    repeating_pendingIntent.cancel();
                    snooze_alarm.setText("Alarm Off");
                } else {
                    if (!alarm_service.isRunning) {
                        Log.e("Conditional", "2");
                        Toast.makeText(MainActivity.this, "The alarm is already off!", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("Conditional", "3");
                        //Silences Alarm
                        alarm_service.isRunning = true;
                        alarm_intent = new Intent(MainActivity.this, alarm_receiver.class);
                        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 2, alarm_intent,
                                PendingIntent.FLAG_UPDATE_CURRENT);
                        sendBroadcast(alarm_intent);
                        snooze_alarm.setText("I'm Woke!");
                        //Starts periodic Alarm
                        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, 6000, pendingIntent);
                        Log.e("Cancel service", "Silenced");
                    }
                }
            }

        });


        /////START ANIMATION/////
        logo = (ImageView) findViewById(R.id.getWoke);

        //Animate the Quote/Quoter and Logo
        Animate_Text(motivational_quote, R.anim.fade_in, 1000);
        Animate_Text(quoter, R.anim.fade_in, 1250);
        Animate_Image(logo, R.anim.logo_rise);
    }


    /////ANIMATION METHODS/////
    public void Animate_Text(final TextView text, int animation, int delay) {
        final Animation quoteRise = AnimationUtils.loadAnimation(this, animation);
        //zero alpha in the beginning
        text.setTextColor(Color.argb(0, 255, 255, 255));

        //apply the animation to the View
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text.setTextColor(Color.argb(1000, 255, 255, 255));
                text.startAnimation(quoteRise);
            }
        }, delay);
    }


    public void Animate_Image(final ImageView image, int animation) {
        final Animation imageRise = AnimationUtils.loadAnimation(this, animation);
        //apply the animation to the View
        image.startAnimation(imageRise);
    }

    ////LAUNCHERS/////

    //launch the settings
    private void launchActivity() {
        Intent intent = new Intent(MainActivity.this, settings_spinners.class);
        startActivity(intent);
    }

    //launch the 'set alarm' page
    private void launchSet_Alarm() {
        Intent intent = new Intent(MainActivity.this, MainActivity_set_alarm.class);
        startActivity(intent);
    }


    /////Feature Changers/////

    public void background_changer(String background) {
        switch (background) {
            case "Starry Clouds":
                //set background as starry clouds
                content_main.setBackgroundResource(R.drawable.stars_clouds);
                break;
            case "Starry Sky":
                //set background as starry sky
                content_main.setBackgroundResource(R.drawable.stars_wallpaper);
                break;
            case "Vanilla":
                content_main.setBackgroundResource(R.drawable.vanilla);
                break;
            default:
                break;
        }
    }


    public void font_changer(String font) {
        Typeface font_roboto = Typeface.createFromAsset(getAssets(), "fonts/roboto-medium.ttf");
        Typeface font_cursive = Typeface.createFromAsset(getAssets(), "fonts/Otto.ttf");
        Typeface font_weird = Typeface.createFromAsset(getAssets(), "fonts/weird.otf");
        Log.e("yes", "font changing to " + font);
        switch (font) {
            case "Formal":
                motivational_quote.setTypeface(font_cursive);
                break;
            case "Roboto":
                motivational_quote.setTypeface(font_roboto);
                break;
            case "Weird":
                motivational_quote.setTypeface(font_weird);
                break;
            default:
                break;
        }
    }


    /////GETTERS/////

    //Gets previous time
    private String getInput() {
        if (lastTimerisNull) {
            SharedPreferences sharedPref = getSharedPreferences("Alarm Time", MODE_PRIVATE);
            String message = sharedPref.getString("Message", "Your alarm is unset");
            Log.e("message", message);
            return message;
        } else {
            return "Your alarm is unset.";
        }
    }


    private String getQuote() {
        SharedPreferences sharedPref = getSharedPreferences("Quote", MODE_PRIVATE);
        String message = sharedPref.getString("Quote", "");
        Log.e("GetQuote()", message);
        return message;

    }

    private String getQuoter() {
        SharedPreferences sharedPref = getSharedPreferences("Quoter", MODE_PRIVATE);
        String message = sharedPref.getString("Quoter", "");
        return message;

    }

    private String getAlarmButtonText() {
        SharedPreferences sharedPref = getSharedPreferences("Alarm Unset", MODE_PRIVATE);
        String message = sharedPref.getString("Alarm Button Text", "Alarm Off");
        if (!alarm_service.isRunning) {
            message = "Alarm Off";
        }

        return message;
    }

    private String getBackground() {
        SharedPreferences sharedPref = getSharedPreferences("Backgrounds", MODE_PRIVATE);
        String message = sharedPref.getString("Message", "--Choose your Background--");
        return message;
    }

    private Boolean get_PowerButtonBoolean() {
        SharedPreferences sharedPref = getSharedPreferences("Power Button", MODE_PRIVATE);
        Boolean message = sharedPref.getBoolean("Message", false);
        return message;
    }


    public void setRandInt() {
        SharedPreferences sharedPref_alarm_unset = getSharedPreferences("Random Int", MODE_PRIVATE);
        int new_int = sharedPref_alarm_unset.getInt("Int", -1);
        randomQuote.last_rand = new_int;
    }

    public void setMaxMin() {
        SharedPreferences sharedPref_max = getSharedPreferences("Max", MODE_PRIVATE);
        SharedPreferences sharedPref_min = getSharedPreferences("Min", MODE_PRIVATE);
        int new_min = sharedPref_min.getInt("Max", 0);
        int new_max = sharedPref_max.getInt("Min", 10000);
        randomQuote.maxlength = new_max;
        randomQuote.minlength = new_min;
    }

    private long adjustTime(long time) {
        if ((abs(System.currentTimeMillis() - time) < 60000)
                && (System.currentTimeMillis() > time)) {
            return time;
        } else if (System.currentTimeMillis() > time) {

            return 86400000 + System.currentTimeMillis();
        } else {

            return time;
        }
    }

    public void store_PowerButtonBoolean(Boolean message) {

        SharedPreferences sharedPref = getSharedPreferences("Power Button", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("Message", message);
        editor.apply();
    }


}
