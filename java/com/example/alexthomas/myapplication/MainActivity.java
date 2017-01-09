package com.example.alexthomas.myapplication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
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
    public static DigitalClock digitalClock;
    public static Button imagepowerButton;
    public static Boolean powerButton_on = false;
    private static Boolean lastTimerisNull = true;
    public static boolean alarmUp;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        content_main = (RelativeLayout) findViewById(R.id.content_main);


        digitalClock = (DigitalClock) findViewById(R.id.textClock);
        snooze_alarm = (Button) findViewById(R.id.alarm_off);
        imagepowerButton = (Button) findViewById(R.id.powerbutton);



        //Sets font on Clock
        Typeface blockFonts = Typeface.createFromAsset(getAssets(), "fonts/Lato-Black.ttf");
        digitalClock.setTypeface(blockFonts);
        //Sets last configured time
        load_timer_null();
        //Sets "alarm set" text in bottom
        setInput_bottomText();
        //Sets quotes/quoter
        setQuote();
        setQuoter();
        //Sets font
        font_changer(); //invokes this class's font_changer
        //Sets background
        background_changer();
        //Sets color
        adjustColor();
        //Sets Power Button Text
        snooze_alarm.setText(getAlarmButtonText());

        //Load Power Button Boolean
        load_PowerButtonBoolean();
        //Load Power Button Image
        on_off_boolean(powerButton_on);
        //Sets Quote Numbers
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
        final Button true_settings = (Button) findViewById(R.id.settings_button);
        true_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                true_settings.setBackgroundResource(R.drawable.settings_iconcopy);
                launchActivity();
                Handler handler1 = new Handler();
                handler1.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        true_settings.setBackgroundResource(R.drawable.settings_icon);
                    }
                }, 100);
            }
        });


        //Sets Listener on Share Button
        ImageButton share_facebook = (ImageButton) findViewById(R.id.share_button);
        share_facebook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //launch share to facebook page
            }
        });




        //make a listener on the image Power Button
        imagepowerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Checks whether MAIN alarm is pending
                SharedPreferences sharedPref = getSharedPreferences("Alarm Time", MODE_PRIVATE);
                int hour = sharedPref.getInt("Hour", -1);
                int minute = sharedPref.getInt("Minute", -1);


                if ((hour == -1) && (minute == -1)) {
                    Log.e("Conditional", "1");
                    Toast.makeText(MainActivity.this, "You need to set an alarm first!", Toast.LENGTH_SHORT).show();
                }
                else if (!powerButton_on && (snooze_alarm.getText().toString().equals("Silence Alarm"))){
                        Log.e("Conditional", "7");
                        Toast.makeText(MainActivity.this, "You need to silence the alarm first!", Toast.LENGTH_SHORT).show();

                }
                else{

                powerButton_on = !powerButton_on;
                Log.e("Time", String.valueOf(hour) + ":" + String.valueOf(minute));
                if (powerButton_on) {
                    on_off_boolean(true);
                    Log.e("power is on", "now");
                    store_PowerButtonBoolean(true);
                } else {
                    on_off_boolean(false);
                    Log.e("power is off", "now");
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

                    /////when there is no alarm set ever before and power button off
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
                    snooze_alarm.setText("Get Quotes");
                    store_snoozeText("Get Quotes");
                    alarm_confirmation.setText("Your alarm is unset.");

                }
                else{
                //Checks whether the user sets an alarm
                 if (!alarmUp && !powerButton_on && !alarm_service.isRunning && !repeating_alarm) {
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
                     alarm_service.fromAlarmStart = true;
                     AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                         alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, adjustTime(time), pendingIntent);
                     }
                     else{
                         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                             alarmManager.setExact(AlarmManager.RTC_WAKEUP, adjustTime(time), pendingIntent);
                         }
                         else{
                             alarmManager.set(AlarmManager.RTC_WAKEUP, adjustTime(time), pendingIntent);

                         }

                     }
                     lastTimerisNull = false;
                    store_timer_null(false);
                    getInput_bottomText();

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
                    alarm_confirmation.setText("Your alarm is unset");
                    snooze_alarm.setText("Get Quotes");
                    store_snoozeText("Get Quotes");
                    Toast.makeText(MainActivity.this, "Alarm unset.", Toast.LENGTH_SHORT).show();
                    Log.e("Cancelled for real", "Cancelled Intent");
                }
                setInput_bottomText();
            }}}
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
                if (snooze_alarm.getText().toString().equals("Get Quotes")) {
                    //execute new quote
                    get_quotes();
                }

                if (repeating_alarm && (snooze_alarm.getText().toString().equals("I'm Woke!"))) {
                    Log.e("Conditional", "1");
                    Intent repeating_alarm_intent = new Intent(MainActivity.this, alarm_receiver.class);
                    PendingIntent repeating_pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 2, repeating_alarm_intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    alarm_service.control_RepeatingAlarm = false;
                    repeating_pendingIntent.cancel();
                    snooze_alarm.setText("Get Quotes");
                    store_snoozeText("Get Quotes");
                } else {
                    if (!alarm_service.isRunning && !repeating_alarm) {
                        Log.e("Conditional", "2");
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

                        if(Boolean.valueOf(ifRepeatingOn)){
                            snooze_alarm.setText("I'm Woke!");
                            store_snoozeText("I'm Woke!");
                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
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
                            Log.e("Repeats", "This repeats");
                        }
                        else{

                            if(snooze_alarm.getText().equals("Get Quotes")){
                                Toast.makeText(MainActivity.this, "The alarm is already fucked!", Toast.LENGTH_SHORT).show();
                            }
                            pendingIntent.cancel();
                            snooze_alarm.setText("Get Quotes");
                            store_snoozeText("Get Quotes");
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
                //set background as starry clouds
                MainActivity.content_main.setBackgroundResource(R.drawable.stars_clouds);
                break;
            case "Vanilla":
                MainActivity.content_main.setBackgroundResource(R.drawable.vanilla);
                break;
            case "Sky":
                //set background as starry clouds
                MainActivity.content_main.setBackgroundResource(R.drawable.sky);
                break;
            case "Mountain":
                MainActivity.content_main.setBackgroundResource(R.drawable.mountain);
                break;
            case "Water":
                MainActivity.content_main.setBackgroundResource(R.drawable.water);
                break;
            case "Sunset":
                MainActivity.content_main.setBackgroundResource(R.drawable.sunset);
                break;
            case "Golden Gate":
                MainActivity.content_main.setBackgroundResource(R.drawable.golden_gate);
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
        Typeface font_scifi = Typeface.createFromAsset(getAssets(), "fonts/scifi.ttf");
        Typeface font_personal = Typeface.createFromAsset(getAssets(), "fonts/personal.ttf");
        Typeface font_thick = Typeface.createFromAsset(getAssets(), "fonts/samurai.otf");
        Typeface font_samurai = Typeface.createFromAsset(getAssets(), "fonts/Lato-Black.ttf");
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
            case "Sci-Fi":
                MainActivity.motivational_quote.setTypeface(font_scifi);
                break;
            case "Samurai":
                MainActivity.motivational_quote.setTypeface(font_samurai);
                break;
            case "Personal":
                MainActivity.motivational_quote.setTypeface(font_personal);
                break;
            case "Thick":
                MainActivity.motivational_quote.setTypeface(font_thick);
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
            case "Red":
                MainActivity.digitalClock.setTextColor(Color.parseColor("#ffafaf"));
                break;
            case "Blue":
                MainActivity.digitalClock.setTextColor(Color.parseColor("#a8c9ff"));
                break;
            case "Yellow":
                MainActivity.digitalClock.setTextColor(Color.parseColor("#fffad8"));
                break;
            case "Green":
                MainActivity.digitalClock.setTextColor(Color.parseColor("#defcd6"));
                break;
            default:
                break;
        }
    }

    public void color_changer (String color) {
        switch (color) {
            case "White":
                MainActivity.motivational_quote.setTextColor(Color.WHITE);
                MainActivity.quoter.setTextColor(Color.WHITE);
                break;
            case "Red":
                MainActivity.motivational_quote.setTextColor(Color.parseColor("#ffafaf"));
                MainActivity.quoter.setTextColor(Color.parseColor("#ffafaf"));
                break;
            case "Blue":
                MainActivity.motivational_quote.setTextColor(Color.parseColor("#a8c9ff"));
                MainActivity.quoter.setTextColor(Color.parseColor("#a8c9ff"));
                break;
            case "Yellow":
                MainActivity.motivational_quote.setTextColor(Color.parseColor("#fffad8"));
                MainActivity.quoter.setTextColor(Color.parseColor("#fffad8"));
                break;
            case "Green":
                MainActivity.motivational_quote.setTextColor(Color.parseColor("#defcd6"));
                MainActivity.quoter.setTextColor(Color.parseColor("#defcd6"));
                break;
            default:
                break;
        }
    }


    /////GETTERS/////

    public void saverandInt() {
        SharedPreferences sharedPref_alarm_unset = getSharedPreferences("Random Int", MODE_PRIVATE);
        SharedPreferences.Editor editor_alarm_unset = sharedPref_alarm_unset.edit();
        editor_alarm_unset.putInt("Int", randomQuote.last_rand);
        Log.e("Last rand","saved as "+String.valueOf(randomQuote.last_rand));
        editor_alarm_unset.apply();
    }


    private String getAlarmButtonText() {
        boolean repeating_alarm = (PendingIntent.getBroadcast(MainActivity.this, 2,
                new Intent(MainActivity.this, alarm_receiver.class),
                PendingIntent.FLAG_NO_CREATE) != null);
        SharedPreferences sharedPref = getSharedPreferences("Alarm Unset", MODE_PRIVATE);
        String message = sharedPref.getString("Alarm Button Text", "Get Quotes");
        if (!alarm_service.isRunning && !repeating_alarm) {
            message = "Get Quotes";
        }
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
        int new_max = sharedPref_max.getInt("Maximum", 10000);
        int new_min = sharedPref_min.getInt("Minimum", 0);
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




    /////Get values/////

    public static void on_off_boolean(Boolean bool) {
        if (bool == true) {
            imagepowerButton.setBackgroundResource(R.drawable.on_button_on);
        }
        else {
            imagepowerButton.setBackgroundResource(R.drawable.on_button);
        }
    }



    public void load_timer_null() {
        SharedPreferences sharedPref = getSharedPreferences("Last Timer", MODE_PRIVATE);
        lastTimerisNull = sharedPref.getBoolean("Message", true);
        Log.e("boolean set to", String.valueOf(lastTimerisNull));
    }


    private void load_PowerButtonBoolean() {
        SharedPreferences sharedPref = getSharedPreferences("Power Button", MODE_PRIVATE);
        Boolean message = sharedPref.getBoolean("Message", false);
        powerButton_on = message;
        alarmUp = message;
        Log.e("the power button start",String.valueOf(powerButton_on));
    }

    private void setInput_bottomText() {
        alarm_confirmation = (TextView) findViewById(R.id.alarm_confirmation);
        if (!lastTimerisNull) {
            SharedPreferences sharedPref = getSharedPreferences("Alarm Time", MODE_PRIVATE);
            String message = sharedPref.getString("Message", "Your alarm is unset.");
            Log.e("Setting previous alarm", message);
            alarm_confirmation.setText(message);
        } else {
            alarm_confirmation.setText("Your alarm is unset.");
        }
    }

    private String getInput_bottomText() {
        alarm_confirmation = (TextView) findViewById(R.id.alarm_confirmation);
        if (!lastTimerisNull) {
            SharedPreferences sharedPref = getSharedPreferences("Alarm Time", MODE_PRIVATE);
            String message = sharedPref.getString("Message", "Your alarm is unset.");
            Log.e("Setting previous alarm", message);
            return message;
        } else {
            return "Your alarm is unset.";
        }
    }



    private void setQuote() {
        SharedPreferences sharedPreferences = getSharedPreferences("Font", MODE_PRIVATE);
        int size = sharedPreferences.getInt("Size", 20);
        motivational_quote = (TextView) findViewById(R.id.motivationalQuote);
        SharedPreferences sharedPref = getSharedPreferences("Quote", MODE_PRIVATE);
        String message = sharedPref.getString("Quote", "");
        Log.e("GetQuote()", message);
        motivational_quote.setText(message);
        motivational_quote.setTextSize(size);

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

    public void get_quotes() {
        //execute new quote
        SharedPreferences sharedPref_genres = getSharedPreferences("Genres", MODE_PRIVATE);
        String genre = sharedPref_genres.getString("Message", "All Genres");
        randomQuote newQuote = new randomQuote();
        Log.e("Alarm Service","Old last_rand is "+String.valueOf(newQuote.last_rand));
        Log.e("alright", "Generating new quote with genre set to " + genre);


        String[] quote = newQuote.quote_generator(genre);
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

        //sets text
        motivational_quote.setText(quote[0]);
        quoter.setText(quote[1]);
        //Animation ghetto version
        final Animation quoteRise1 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
        final Animation quoteRise2 = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);
        MainActivity.motivational_quote.startAnimation(quoteRise1);
        MainActivity.quoter.startAnimation(quoteRise2);
    }
}
