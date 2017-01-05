package com.example.alexthomas.myapplication;

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
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.graphics.Color;
import org.w3c.dom.Text;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.os.Handler;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;




public class MainActivity extends AppCompatActivity {
    public static String genre = "null";
    public static TextView motivational_quote;
    public static TextView quoter;
    public static ImageView logo;
    public  PendingIntent pendingIntent;
    public static Intent alarm_intent;
    public static TextView alarm_confirmation;
    public static Button snooze_alarm;
    public static RelativeLayout content_main;



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);






        snooze_alarm = (Button) findViewById(R.id.alarm_off);
        content_main = (RelativeLayout) findViewById(R.id.content_main);




        //Captures font from previous session
        SharedPreferences sharedPref_font = getSharedPreferences("Font", MODE_PRIVATE);
        String last_font = sharedPref_font.getString("Message", "Default Font");


        //Captures background from previous session
        SharedPreferences sharedPref_background = getSharedPreferences("Backgrounds", MODE_PRIVATE);
        String last_background = sharedPref_background.getString("Message", "Default Background");

        //Sets last configured time
        alarm_confirmation = (TextView) findViewById(R.id.alarm_confirmation);
        alarm_confirmation.setText(getInput());
        snooze_alarm.setText(getAlarmButtonText());

        //sets quote/quoter from previous session
        motivational_quote = (TextView) findViewById(R.id.motivationalQuote);
        motivational_quote.setText(getQuote());
        quoter = (TextView) findViewById(R.id.quoter);
        quoter.setText(getQuoter());

        //Updates font
        font_changer(last_font); //invokes this class's font_changer

        //Updates background
        background_changer(last_background);


        //Adds Button Functionality
        Button set_alarm = (Button) findViewById(R.id.set_alarm);
        set_alarm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                launchSet_Alarm();
            }
        });


        ImageButton true_settings = (ImageButton) findViewById(R.id.settings_button);
        true_settings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                launchActivity();
            }
        });

        ImageButton share_facebook = (ImageButton) findViewById(R.id.share_button);
        share_facebook.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //launch share to facebook page


            }
        });



        Typeface blockFonts = Typeface.createFromAsset(getAssets(),"fonts/Lato-Black.ttf");
        TextClock clock = (TextClock) findViewById(R.id.textClock);
        clock.setTypeface(blockFonts);


        //make a listener on the snooze_alarm on user click
        snooze_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                boolean alarmUp = (PendingIntent.getBroadcast(MainActivity.this, 1,
                        new Intent(MainActivity.this, alarm_receiver.class),
                        PendingIntent.FLAG_NO_CREATE) != null);
                Log.e("alarmUP", String.valueOf(alarmUp));
                Log.e("isRunning", String.valueOf(alarm_service.isRunning));

                if (!alarmUp && !alarm_service.isRunning ){
                    Toast.makeText(MainActivity.this, "The alarm is already off!",Toast.LENGTH_SHORT).show();
                }
                else if (alarmUp && !alarm_service.isRunning)
                {
                    alarm_service.isRunning = true;
                    alarm_intent = new Intent(MainActivity.this, alarm_receiver.class);
                    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 1, alarm_intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    sendBroadcast(alarm_intent);
                    pendingIntent.cancel();
                    SharedPreferences sharedPref = getSharedPreferences("Alarm Time", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("Message", "Your alarm is unset.");
                    editor.apply();
                    alarm_confirmation.setText("Your alarm is unset.");
                    Toast.makeText(MainActivity.this, "Alarm unset.",Toast.LENGTH_SHORT).show();
                    Log.e("Cancelled for real", "Cancelled Intent");
                }
                else
                {
                    alarm_service.isRunning = true;
                    alarm_intent = new Intent(MainActivity.this, alarm_receiver.class);
                    pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 1, alarm_intent,
                            PendingIntent.FLAG_UPDATE_CURRENT);
                    sendBroadcast(alarm_intent);
                    Log.e("Cancel service", "Silenced");
                    snooze_alarm.setText("Alarm Off");
                }
            }
        });


        logo = (ImageView) findViewById(R.id.getWoke);

        //Animations!
        Animate_Text(motivational_quote,R.anim.fade_in,1000);
        Animate_Text(quoter,R.anim.fade_in,1250);
        Animate_Image(logo,R.anim.logo_rise);
    }



    //animation
    public void Animate_Text(final TextView text,int animation, int delay) {
        final Animation quoteRise = AnimationUtils.loadAnimation(this, animation);
        //zero alpha in the beginning
        text.setTextColor(Color.argb(0, 255, 0, 0));

        //apply the animation to the View
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                text.setTextColor(Color.argb(500, 255, 0, 0));
                text.startAnimation(quoteRise);
            }
        }, delay);
    }



    public void Animate_Image(final ImageView image,int animation) {
        final Animation imageRise = AnimationUtils.loadAnimation(this, animation);
        //apply the animation to the View
        image.startAnimation(imageRise);
    }









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

    //Gets previous time
    private String getInput(){
        SharedPreferences sharedPref = getSharedPreferences("Alarm Time", MODE_PRIVATE);
        String message = sharedPref.getString("Message", "Your alarm is unset");
        Log.e("message", message);
        return message;

    }




    ////DEFINITELY CHANGE THIS FOR OPTIMIZATION

    public void background_changer (String background) {
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
        Typeface font_roboto = Typeface.createFromAsset(getAssets(),"fonts/roboto-medium.ttf");
        Typeface font_cursive = Typeface.createFromAsset(getAssets(),"fonts/Otto.ttf");
        Typeface font_weird = Typeface.createFromAsset(getAssets(),"fonts/weird.otf");
        Log.e("yes","font changing to " + font);
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

    private String getQuote(){
        SharedPreferences sharedPref = getSharedPreferences("Quote", MODE_PRIVATE);
        String message = sharedPref.getString("Quote", "");
        Log.e("GetQuote()", message);
        return message;

    }

    private String getQuoter(){
        SharedPreferences sharedPref = getSharedPreferences("Quoter", MODE_PRIVATE);
        String message = sharedPref.getString("Quoter", "");
        return message;

    }

    private String getAlarmButtonText(){
        SharedPreferences sharedPref = getSharedPreferences("Alarm Unset", MODE_PRIVATE);
        String message = sharedPref.getString("Alarm Button Text", "Alarm Off");
        if (!alarm_service.isRunning) {
            message = "Alarm Off";
        }

        return message;
    }

    private String getBackground(){
        SharedPreferences sharedPref = getSharedPreferences("Backgrounds", MODE_PRIVATE);
        String message = sharedPref.getString("Message", "Default");
        return message;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (alarm_service.isRunning == false) {

            snooze_alarm.setText("Alarm Off");
        }
    }

}
