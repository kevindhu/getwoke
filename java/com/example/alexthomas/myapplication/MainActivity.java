package com.example.alexthomas.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
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
    public static DigitalClock digitalClock;
    public static Boolean powerButton_on = false;
    private static Boolean lastTimerisNull = true;
    public static boolean alarmUp;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();

        digitalClock = (DigitalClock) findViewById(R.id.textClock);
        powerButton = (Button) findViewById(R.id.alarm_off);
        snooze_alarm = (Button) findViewById(R.id.alarm_off);
        powerButton = (Button) findViewById(R.id.powerbutton);
        content_main = (RelativeLayout) findViewById(R.id.content_main);

        final ImageView settings_feedback = (ImageView) findViewById(R.id.settings_feedback);
        settings_feedback.setAlpha(0f);



        //Sets font on Clock
        Typeface blockFonts = Typeface.createFromAsset(getAssets(), "fonts/Lato-Black.ttf");
        digitalClock.setTypeface(blockFonts);


        //Sets last configured time
        load_timer_null();
        setInput_bottomText(); //Sets "alarm set" text in bottom
        //Sets quotes/quoter
        setQuote();
        setQuoter();
        //Updates font
        font_changer(); //invokes this class's font_changer
        //Updates background
        background_changer();
        //Updates color
        adjustColor();


        snooze_alarm.setText(getAlarmButtonText());
        if (get_PowerButtonBoolean()) {
            powerButton.setText("Turn Off");
        } else {
            powerButton.setText("Turn On");
        }


        //sets quote/quoter from previous session

        setRandInt();
        setMaxMin();



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
                powerButton_on = !powerButton_on;
                Log.e("Power button is on?????", String.valueOf(powerButton_on));

                SharedPreferences sharedPref = getSharedPreferences("Alarm Time", MODE_PRIVATE);
                int hour = sharedPref.getInt("Hour", -1);
                int minute = sharedPref.getInt("Minute", -1);
                Log.e("Time", String.valueOf(hour) + ":" + String.valueOf(minute));
                if (powerButton_on) {
                    powerButton.setText("Turn Off");
                    store_PowerButtonBoolean(true);
                } else {
                    powerButton.setText("Turn On");
                    store_PowerButtonBoolean(false);
                }

                //Checks if pending intent with int 1 is still around
                        alarmUp = (PendingIntent.getBroadcast(MainActivity.this, 1,
                        new Intent(MainActivity.this, alarm_receiver.class),
                        PendingIntent.FLAG_NO_CREATE) != null);
                //Checks if repeating intent is still around
                boolean repeating_alarm = (PendingIntent.getBroadcast(MainActivity.this, 2,
                        new Intent(MainActivity.this, alarm_receiver.class),
                        PendingIntent.FLAG_NO_CREATE) != null);
                Log.e("Conditionals", String.valueOf(repeating_alarm) + " " + String.valueOf(alarmUp));

                if ((repeating_alarm && alarm_service.isRunning && !powerButton_on) || (alarmUp && alarm_service.isRunning && !powerButton_on)){
                    Log.e("Conditional", "0");
                    alarm_service.isRunning = true;
                    alarm_intent = new Intent(MainActivity.this, alarm_receiver.class);
                    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 2, alarm_intent,
                            PendingIntent.FLAG_CANCEL_CURRENT);
                    sendBroadcast(alarm_intent);
                    pendingIntent.cancel();

                    alarm_intent = new Intent(MainActivity.this, alarm_receiver.class);
                    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 1, alarm_intent,
                            PendingIntent.FLAG_CANCEL_CURRENT);
                    pendingIntent.cancel();
                    lastTimerisNull = true;
                    store_timer_null(true);
                    snooze_alarm.setText("Alarm Off");
                    store_snoozeText("Alarm Off");
                    alarm_confirmation.setText("Your alarm is unset.");



                }
                else{
                //Checks whether the user sets an alarm
                if ((hour == -1) && (minute == -1)) {
                    Log.e("Conditional", "1");
                    Toast.makeText(MainActivity.this, "You need to set an alarm first!", Toast.LENGTH_SHORT).show();
                }
                //
                else if (!alarmUp && !powerButton_on && !alarm_service.isRunning && !repeating_alarm) {
                    Log.e("Conditional", "2");
                    Toast.makeText(MainActivity.this, "The alarm is already off!", Toast.LENGTH_SHORT).show();
                }
                //When alarm schedule has no pending alarm and user turns power on
                else if (!alarmUp && powerButton_on) {
                    Log.e("Conditional", "3");
                    alarm_intent = new Intent(MainActivity.this, alarm_receiver.class);
                    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 1, alarm_intent,
                            PendingIntent.FLAG_CANCEL_CURRENT);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);
                    long time = calendar.getTimeInMillis();
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, adjustTime(time), pendingIntent);
                    lastTimerisNull = false;
                    store_timer_null(false);
                }
                //When alarm has a pending alarm and user turns it off
                else if (alarmUp && !powerButton_on) {
                    Log.e("Conditional", "4");
                    alarm_intent = new Intent(MainActivity.this, alarm_receiver.class);
                    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 1, alarm_intent,
                            PendingIntent.FLAG_CANCEL_CURRENT);
                    pendingIntent.cancel();

                    alarm_intent = new Intent(MainActivity.this, alarm_receiver.class);
                    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 2, alarm_intent,
                            PendingIntent.FLAG_CANCEL_CURRENT);
                    pendingIntent.cancel();
                    lastTimerisNull = true;
                    store_timer_null(true);
                    alarm_confirmation.setText("Your alarm is unset.");
                    store_Alarm_bottomText("Your alarm is unset.");
                    snooze_alarm.setText("Alarm Off");
                    store_snoozeText("Alarm Off");
                    Toast.makeText(MainActivity.this, "Alarm unset.", Toast.LENGTH_SHORT).show();
                    Log.e("Cancelled for real", "Cancelled Intent");
                }
                setInput_bottomText();

            }}
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
                boolean x = snooze_alarm.getText().toString() == "I'm Woke!";
                Log.e("Snooze Alarm Text", String.valueOf(x));

                if (repeating_alarm && (snooze_alarm.getText().toString().equals("I'm Woke!"))) {
                    Log.e("Conditional", "1");
                    Intent repeating_alarm_intent = new Intent(MainActivity.this, alarm_receiver.class);
                    PendingIntent repeating_pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 2, repeating_alarm_intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    alarm_service.control_RepeatingAlarm = false;
                    repeating_pendingIntent.cancel();
                    snooze_alarm.setText("Alarm Off");
                    store_snoozeText("Alarm Off");
                } else {
                    if (!alarm_service.isRunning && !repeating_alarm) {
                        Log.e("Conditional", "2");
                        Toast.makeText(MainActivity.this, "The alarm is already off!", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("Conditional", "3");
                        //Silences Alarm
                        alarm_service.isRunning = true;
                        alarm_intent = new Intent(MainActivity.this, alarm_receiver.class);
                        pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 2, alarm_intent,
                                PendingIntent.FLAG_CANCEL_CURRENT);
                        sendBroadcast(alarm_intent);
                        //Starts periodic Alarm
                        SharedPreferences sharedPref = getSharedPreferences("Repeating Intervals", MODE_PRIVATE);
                        long interval = sharedPref.getLong("Interval", -1);
                        Log.e("Interval", String.valueOf(interval));
                        SharedPreferences checkbox_boolean = getSharedPreferences("Snooze Boolean",MODE_PRIVATE);
                        String ifRepeatingOn = checkbox_boolean.getString("Message", "false");
                        lastTimerisNull = false;
                        store_timer_null(false);

                        if(Boolean.valueOf(ifRepeatingOn) && (interval != -1)){
                            snooze_alarm.setText("I'm Woke!");
                            store_snoozeText("I'm Woke!");
                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + interval, pendingIntent);
                            Log.e("Repeats", "This repeats");
                        }
                        else{
                            pendingIntent.cancel();
                            snooze_alarm.setText("Alarm Off");
                            store_snoozeText("Alarm Off");
                            Log.e("Cancel service", "Doesn't repeat");
                        }

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
        text.setVisibility(View.GONE);

        //apply the animation to the View
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text.setVisibility(View.VISIBLE);
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

    public void background_changer () {
        SharedPreferences sharedPref_background = getSharedPreferences("Backgrounds", MODE_PRIVATE);
        String last_background = sharedPref_background.getString("Message", "--Choose your Background--");
        switch (last_background) {
            case "Starry Clouds":
                MainActivity.content_main.setBackgroundResource(R.drawable.stars_clouds);
                break;
            case "Galaxy":
                MainActivity.content_main.setBackgroundResource(R.drawable.galaxy);
                break;
            case "Vanilla":
                MainActivity.content_main.setBackgroundResource(R.drawable.vanilla);
                break;
            case "Forest":
                MainActivity.content_main.setBackgroundResource(R.drawable.forest);
                break;
            case "Crystal":
                MainActivity.content_main.setBackgroundResource(R.drawable.crystal);
                break;
            default:
                break;
        }
    }



    public void font_changer() {
        SharedPreferences sharedPref_font = getSharedPreferences("Font", MODE_PRIVATE);
        String last_font = sharedPref_font.getString("Message", "--Choose your Font--");
        Typeface font_roboto = Typeface.createFromAsset(getAssets(), "fonts/roboto-medium.ttf");
        Typeface font_cursive = Typeface.createFromAsset(getAssets(), "fonts/Otto.ttf");
        Typeface font_weird = Typeface.createFromAsset(getAssets(), "fonts/weird.otf");
        Log.e("yes", "font changing to " + last_font);
        switch (last_font) {
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

    public void clock_color_changer (String color) {
        switch (color) {
            case "White":
                //set background as starry clouds
                MainActivity.digitalClock.setTextColor(Color.WHITE);
                break;
            case "Black":
                //set background as starry sky
                MainActivity.digitalClock.setTextColor(Color.BLACK);
                break;
            case "Red":
                MainActivity.digitalClock.setTextColor(Color.RED);
                break;
            case "Blue":
                MainActivity.digitalClock.setTextColor(Color.BLUE);
                break;
            case "Yellow":
                MainActivity.digitalClock.setTextColor(Color.YELLOW);
                break;
            case "Green":
                MainActivity.digitalClock.setTextColor(Color.GREEN);
                break;
            case "Gray":
                MainActivity.digitalClock.setTextColor(Color.GRAY);
                break;
            default:
                break;
        }
    }

    public void color_changer (String color) {
        switch (color) {
            case "White":
                motivational_quote.setTextColor(Color.WHITE);
                quoter.setTextColor(Color.WHITE);
                break;
            case "Black":
                motivational_quote.setTextColor(Color.BLACK);
                quoter.setTextColor(Color.BLACK);
                break;
            case "Red":
                motivational_quote.setTextColor(Color.RED);
                quoter.setTextColor(Color.RED);
                break;
            case "Blue":
                motivational_quote.setTextColor(Color.BLUE);
                quoter.setTextColor(Color.BLUE);
                break;
            case "Yellow":
                motivational_quote.setTextColor(Color.YELLOW);
                quoter.setTextColor(Color.YELLOW);
                Log.e("color", "Yellow for sure");
                break;
            case "Green":
                motivational_quote.setTextColor(Color.GREEN);
                quoter.setTextColor(Color.GREEN);
                break;
            case "Gray":
                motivational_quote.setTextColor(Color.GRAY);
                quoter.setTextColor(Color.GRAY);
                break;
            default:
                break;
        }
    }


    /////GETTERS/////

    //Gets previous time

    private String getAlarmButtonText() {
        boolean repeating_alarm = (PendingIntent.getBroadcast(MainActivity.this, 2,
                new Intent(MainActivity.this, alarm_receiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);
        SharedPreferences sharedPref = getSharedPreferences("Alarm Unset", MODE_PRIVATE);
        String message = sharedPref.getString("Alarm Button Text", "Alarm Off");
        if (!alarm_service.isRunning && !repeating_alarm) {
            message = "Alarm Off";
        }
        return message;
    }

    private String getBackground() {
        SharedPreferences sharedPref = getSharedPreferences("Backgrounds", MODE_PRIVATE);
        String message = sharedPref.getString("Message", "--Choose your Background--");
        return message;
    }

    private void adjustColor() {
        SharedPreferences sharedPref = getSharedPreferences("Font Color", MODE_PRIVATE);
        String message = sharedPref.getString("Message", "--Choose your Color--");


        SharedPreferences clock_sharedPref = getSharedPreferences("Clock Color", MODE_PRIVATE);
        String message_clock = clock_sharedPref.getString("Message", "--Choose your Color--");
        Log.e("Color", message);
        color_changer(message);
        clock_color_changer(message_clock);
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


    //Get values

    public void load_timer_null() {
        SharedPreferences sharedPref = getSharedPreferences("Last Timer", MODE_PRIVATE);
        lastTimerisNull = sharedPref.getBoolean("Message",true);
        Log.e("boolean set to",String.valueOf(lastTimerisNull));
    }


    private Boolean get_PowerButtonBoolean() {
        SharedPreferences sharedPref = getSharedPreferences("Power Button", MODE_PRIVATE);
        Boolean message = sharedPref.getBoolean("Message", false);
        powerButton_on = message;
        alarmUp = message;
        Log.e("the power button start",String.valueOf(powerButton_on));
        return message;
    }

    private void setInput_bottomText() {
        alarm_confirmation = (TextView) findViewById(R.id.alarm_confirmation);
        if (!lastTimerisNull) {
            SharedPreferences sharedPref = getSharedPreferences("Alarm Time", MODE_PRIVATE);
            String message = sharedPref.getString("Message", "Your alarm is unset");
            Log.e("Setting previous alarm", message);
            alarm_confirmation.setText(message);
        } else {
             alarm_confirmation.setText("Your alarm is unset.");
        }
    }


    private void setQuote() {
        motivational_quote = (TextView) findViewById(R.id.motivationalQuote);
        SharedPreferences sharedPref = getSharedPreferences("Quote", MODE_PRIVATE);
        String message = sharedPref.getString("Quote", "");
        Log.e("GetQuote()", message);
        motivational_quote.setText(message);

    }

    private void setQuoter() {
        quoter = (TextView) findViewById(R.id.quoter);
        SharedPreferences sharedPref = getSharedPreferences("Quoter", MODE_PRIVATE);
        String message = sharedPref.getString("Quoter", "");
        quoter.setText(message);

    }



    //Stores values
    private void store_snoozeText(String message){

        SharedPreferences sharedPref_alarm_unset = getSharedPreferences("Alarm Unset", MODE_PRIVATE);
        SharedPreferences.Editor editor_alarm_unset = sharedPref_alarm_unset.edit();
        editor_alarm_unset.putString("Alarm Button Text", message);
        editor_alarm_unset.apply();
    }

    private void store_Alarm_bottomText(String message){

        SharedPreferences sharedPref_alarm_unset = getSharedPreferences("Alarm Time", MODE_PRIVATE);
        SharedPreferences.Editor editor_alarm_unset = sharedPref_alarm_unset.edit();
        editor_alarm_unset.putString("Message", message);
        editor_alarm_unset.apply();
    }

    public void store_timer_null(Boolean message) {
        SharedPreferences sharedPref = getSharedPreferences("Last Timer", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("Message", message);
        editor.apply();
    }


    public void store_PowerButtonBoolean(Boolean message) {

        SharedPreferences sharedPref = getSharedPreferences("Power Button", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("Message", message);
        editor.apply();
    }
}
