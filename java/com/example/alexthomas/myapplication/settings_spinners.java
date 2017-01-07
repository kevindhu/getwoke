package com.example.alexthomas.myapplication;

/* Created by Kevin on 1/1/2017.*/

import android.app.Activity;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.app.AlarmManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.graphics.Typeface;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.CheckBox;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.content.SharedPreferences;

import org.w3c.dom.Text;


public class settings_spinners extends AppCompatActivity {
    private Spinner spinner_fonts;
    private Spinner spinner_quote_length;
    private Spinner spinner_genre;
    private Spinner spinner_backgrounds;
    private Spinner spinner_repeating_intervals;
    private Spinner spinner_alarm_schedule;
    private Spinner spinner_font_color;
    private CheckBox snooze_check;
    private TextView snooze_interval_title;

    private Button btnSubmit;

    private String[] fonts = {"Formal", "Roboto", "Weird"};
    private String[] genres ={"All Genres","Entrepreneur", "Celebrity", "Author", "Athlete", "Anime", "Great Minds", "Book Quotes","Meme Quotes"};
    private String[] quote_length = {"All Lengths","Medium", "Short", "Long"};
    private String[] backgrounds = {"Vanilla","Starry Clouds", "Galaxy", "Forest", "Crystal"};
    private String[] font_colors = {"White","Black", "Blue", "Red", "Yellow", "Green", "Gray"};
    private String[] repeating_intervals = {"30 Seconds", "1 Minute", "2 Minutes", "3 Minutes", "4 Minutes", "5 Minutes"};
    private String[] alarm_schedule = {"None", "15 Minutes", "30 Minutes", "1 Hour", "12 Hours", "24 Hours"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        getSpinnerEntries("Font", spinner_fonts, fonts, R.id.fonts_spinner);
        getSpinnerEntries("Genres", spinner_genre, genres, R.id.genre_spinner);
        getSpinnerEntries("Quote Length", spinner_quote_length, quote_length, R.id.quote_length_spinner);
        getSpinnerEntries("Backgrounds", spinner_backgrounds, backgrounds, R.id.spinner_backgrounds);
        getSpinnerEntries("Repeating Intervals", spinner_repeating_intervals, repeating_intervals, R.id.spinner_intervals);
        getSpinnerEntries("Alarm Schedule", spinner_alarm_schedule, alarm_schedule, R.id.spinner_alarm_schedule);
        getSpinnerEntries("Font Color",spinner_font_color, font_colors,R.id.spinner_font_colors);
        

        snooze_check = (CheckBox) findViewById(R.id.snooze_check);
        spinner_repeating_intervals = (Spinner) findViewById(R.id.spinner_intervals);
        spinner_fonts = (Spinner) findViewById(R.id.fonts_spinner);
        spinner_quote_length = (Spinner) findViewById(R.id.quote_length_spinner);
        spinner_genre = (Spinner) findViewById(R.id.genre_spinner);
        spinner_backgrounds = (Spinner) findViewById(R.id.spinner_backgrounds);
        spinner_alarm_schedule = (Spinner) findViewById(R.id.spinner_alarm_schedule);
        snooze_interval_title = (TextView) findViewById(R.id.types_of_intervals);


        getCheckBoxEntry();
        setVisibilitySpinner();

        addListenerOnCheckbox();
        addListenerOnButton();
    }



    public void addListenerOnCheckbox() {
        snooze_check.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setVisibilitySpinner();
            }
        });
    }


    public void setVisibilitySpinner() {
        if (!snooze_check.isChecked()) {
            spinner_repeating_intervals.setVisibility(View.GONE);
            snooze_interval_title.setVisibility(View.GONE);
        }
        else {
            spinner_repeating_intervals.setVisibility(View.VISIBLE);
            getSpinnerEntries("Repeating Intervals", spinner_repeating_intervals, repeating_intervals, R.id.spinner_intervals);
            snooze_interval_title.setVisibility(View.VISIBLE);
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_aboutus, menu);
        return true;
    }






    public void getSpinnerEntries(String item, Spinner spinner, String[] args, int XMLSpinner) {
        //shared preferences
        SharedPreferences sharedPref = getSharedPreferences(item, MODE_PRIVATE);
        String message = sharedPref.getString("Message", "--Choose your " + item+"--");
        spinner = (Spinner) findViewById(XMLSpinner);

        List<String> new_item = new ArrayList<String>();

        new_item.add(message);
        for (int i = 0; i<args.length;i++){
            new_item.add(args[i]);
        }
        for (int i = 0; i < new_item.size(); i++) {
            if (i != 0 && new_item.get(i).equals(message)) {
                new_item.remove(i);
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, new_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }





    public void storeValue(String value,Spinner spinner) {
        SharedPreferences sharedPref = getSharedPreferences(value, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Message",String.valueOf(spinner.getSelectedItem()));
        editor.apply();
    }




    public void getCheckBoxEntry() {
        SharedPreferences checkbox_boolean = getSharedPreferences("Snooze Boolean",MODE_PRIVATE);
        String message = checkbox_boolean.getString("Message", "false");
        alarm_service.if_AlarmSchedule = Boolean.getBoolean(message);
        snooze_check.setChecked(Boolean.valueOf(message));
    }











    // get the selected dropdown list value when button is clicked
    public void addListenerOnButton() {
        Log.e("Hi", "Listener on Duty!");
        spinner_fonts = (Spinner) findViewById(R.id.fonts_spinner);
        spinner_quote_length = (Spinner) findViewById(R.id.quote_length_spinner);
        spinner_genre = (Spinner) findViewById(R.id.genre_spinner);
        spinner_backgrounds = (Spinner) findViewById(R.id.spinner_backgrounds);
        spinner_repeating_intervals = (Spinner) findViewById(R.id.spinner_intervals);
        spinner_alarm_schedule = (Spinner) findViewById(R.id.spinner_alarm_schedule);
        spinner_font_color = (Spinner) findViewById(R.id.spinner_font_colors);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //checks if snooze_checkbox is checked
                if (snooze_check.isChecked()) {
                    alarm_service.if_RepeatingAlarm = Boolean.valueOf("true");
                }
                else {
                    alarm_service.if_RepeatingAlarm = Boolean.valueOf("false");
                }

                SharedPreferences shared_snooze  = getSharedPreferences("Snooze Boolean", MODE_PRIVATE);
                SharedPreferences.Editor editor = shared_snooze.edit();
                editor.putString("Message",String.valueOf(alarm_service.if_RepeatingAlarm));
                Log.e("Changed","boolean to " + String.valueOf(alarm_service.if_RepeatingAlarm));
                editor.apply();
                snooze_check.setChecked(Boolean.valueOf(String.valueOf(alarm_service.if_RepeatingAlarm)));









                //Stores Values//
                storeValue("Font",spinner_fonts);
                storeValue("Quote Length",spinner_quote_length);
                storeValue("Genres",spinner_genre);
                storeValue("Backgrounds",spinner_backgrounds);
                storeValue("Font Color", spinner_font_color);
                storeValue("Repeating Intervals",spinner_repeating_intervals);
                storeValue("Alarm Schedule",spinner_alarm_schedule);

                
                Toast.makeText(getApplicationContext(), "Settings Updated!",
                        Toast.LENGTH_LONG).show();

                //Changes Fonts
                font_changer(String.valueOf(spinner_fonts.getSelectedItem()));

                //Font Color changer
                color_changer(String.valueOf(spinner_font_color.getSelectedItem()));


                randomQuote Quote = new randomQuote();
                //Chooses size of quotes from quotes_length_spinner
                Log.e("new","min and max length set");

                switch (String.valueOf(spinner_quote_length.getSelectedItem())) {
                    case "Medium":
                        randomQuote.minlength = 101;
                        randomQuote.maxlength = 200;
                        break;
                    case "Long":
                        randomQuote.minlength = 201;
                        randomQuote.maxlength = 1000;
                        break;
                    case "Short":
                        randomQuote.minlength = 0;
                        randomQuote.maxlength = 100;
                        break;
                    case "All Lengths":
                        randomQuote.minlength = 0;
                        randomQuote.maxlength = 10000;
                        break;
                    default:
                        break;
                }

                switch (String.valueOf(spinner_repeating_intervals.getSelectedItem())){
                    case "30 Seconds":
                        alarm_service.interval = 30000;
                        break;
                    case "1 Minute":
                        alarm_service.interval = 60000;
                        break;
                    case "2 Minutes":
                        alarm_service.interval = 120000;
                        break;
                    case "3 Minutes":
                        alarm_service.interval = 180000;
                        break;
                    case "4 Minutes":
                        alarm_service.interval = 240000;
                        break;
                    case "5 Minutes":
                        alarm_service.interval = 300000;
                        break;
                    default:
                        break;

                }

                switch (String.valueOf(spinner_alarm_schedule.getSelectedItem())){
                    case "None":
                        alarm_service.alarm_schedule = 0;
                        alarm_service.if_AlarmSchedule = false;
                        break;
                    case "12 Hours":
                        alarm_service.alarm_schedule = AlarmManager.INTERVAL_HALF_DAY;
                        alarm_service.if_AlarmSchedule = true;
                        break;
                    case "24 Hours":
                        alarm_service.alarm_schedule = AlarmManager.INTERVAL_DAY;
                        alarm_service.if_AlarmSchedule = true;
                        break;
                    default:
                        break;
                }



                //Chooses genre
                Log.e("Whoa!", "Genre right now is " +MainActivity.genre);
                //MainActivity.motivational_quote.setText(""); //makes quote blank
                if (MainActivity.genre != String.valueOf(spinner_genre.getSelectedItem())) {
                    Log.e("Whoa!", "New Genre Detected Called " +String.valueOf(spinner_genre.getSelectedItem()));
                    MainActivity.genre = String.valueOf(spinner_genre.getSelectedItem());
                }

                //Chooses background
                Log.e("wow","the selected background is " + String.valueOf(spinner_backgrounds.getSelectedItem()));
                background_changer(String.valueOf(spinner_backgrounds.getSelectedItem()));
            }
        });
    }


    public void background_changer (String background) {
        switch (background) {
            case "Starry Clouds":
                //set background as starry clouds
                MainActivity.content_main.setBackgroundResource(R.drawable.stars_clouds);
                break;
            case "Galaxy":
                //set background as starry sky
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

    //Changes font color
    public void color_changer (String color) {
        switch (color) {
            case "White":
                //set background as starry clouds
                MainActivity.motivational_quote.setTextColor(Color.WHITE);
                break;
            case "Black":
                //set background as starry sky
                MainActivity.motivational_quote.setTextColor(Color.BLACK);
                MainActivity.quoter.setTextColor(Color.BLACK);
                break;
            case "Red":
                MainActivity.motivational_quote.setTextColor(Color.RED);
                MainActivity.quoter.setTextColor(Color.RED);
                break;
            case "Blue":
                MainActivity.motivational_quote.setTextColor(Color.BLUE);
                MainActivity.quoter.setTextColor(Color.BLUE);
                break;
            case "Yellow":
                MainActivity.motivational_quote.setTextColor(Color.YELLOW);
                MainActivity.quoter.setTextColor(Color.YELLOW);
                break;
            case "Green":
                MainActivity.motivational_quote.setTextColor(Color.GREEN);
                MainActivity.quoter.setTextColor(Color.GREEN);
                break;
            case "Gray":
                MainActivity.motivational_quote.setTextColor(Color.GRAY);
                MainActivity.quoter.setTextColor(Color.GRAY);
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
                MainActivity.motivational_quote.setTypeface(font_cursive);
                break;
            case "Roboto":
                MainActivity.motivational_quote.setTypeface(font_roboto);
                break;
            case "Weird":
                MainActivity.motivational_quote.setTypeface(font_weird);
                break;
            default:
                break;
        }
    }


    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()){
            case R.id.action_home:
                finish();
                return true;
            case R.id.about_us:
                Intent about_us = new Intent(settings_spinners.this, MainActivity_aboutus.class);
                startActivity(about_us);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}