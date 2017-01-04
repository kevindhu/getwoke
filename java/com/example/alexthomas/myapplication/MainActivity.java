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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;
import android.graphics.Color;
import org.w3c.dom.Text;



public class MainActivity extends AppCompatActivity {
    public static String genre = "null";
    public static TextView motivational_quote;
    public static TextView quoter;
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

        Button set_alarm = (Button) findViewById(R.id.set_alarm);
        set_alarm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                launchSet_Alarm();
            }
        });
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {

            case R.id.action_settings:
                launchActivity();
                return true;
            case R.id.action_home:
                Toast.makeText(getApplicationContext(), "You're already at 'Home'!",
                        Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        return message;
    }


}
