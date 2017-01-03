package com.example.alexthomas.myapplication;

import android.app.PendingIntent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.content.Intent;
import android.widget.Button;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {
    public static String genre = "null";
    MainActivity_set_alarm instance = new MainActivity_set_alarm();
    public static TextView motivational_quote;
    public  PendingIntent pendingIntent;
    public static Intent alarm_intent;
    public static TextView alarm_confirmation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button snooze_alarm = (Button) findViewById(R.id.alarm_off);


        //Makes text_view static
        motivational_quote = (TextView) findViewById(R.id.motivationalQuote);
        alarm_confirmation = (TextView) findViewById(R.id.alarm_confirmation);
        alarm_confirmation.setText(getInput());

        Button set_alarm = (Button) findViewById(R.id.set_alarm);
        set_alarm.setOnClickListener(new View.OnClickListener() {

                                          public void onClick(View view) {
                                              launchSet_Alarm();
                                          }
                                      }

        );
        //make a listener on the snooze_alarm on user click
        snooze_alarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                alarm_service.isRunning = true;
                alarm_intent = new Intent(MainActivity.this, alarm_receiver.class );
                alarm_intent.putExtra("Alarm_off", true);
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 1, alarm_intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                sendBroadcast(alarm_intent);
                Log.e("Cancel service", "Cancelled");
                pendingIntent.cancel();
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

    //launch the alarm
    private void launchActivity() {
        Intent intent = new Intent(MainActivity.this, settings_spinners.class);
        startActivity(intent);
    }

    private void launchSet_Alarm() {
        Intent intent = new Intent(MainActivity.this, MainActivity_set_alarm.class);
        startActivity(intent);
    }

    private String getInput(){
        SharedPreferences sharedPref = getSharedPreferences("Alarm Time", MODE_PRIVATE);
        String message = sharedPref.getString("Message", "Your alarm is unset");
        Log.e("message", message);
        return message;

    }

}
