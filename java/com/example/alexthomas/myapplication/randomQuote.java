package com.example.alexthomas.myapplication;
/* Created by Kevin on 1/2/2017.*/

import android.app.Activity;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
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
import android.widget.Spinner;
import android.widget.Toast;
import android.graphics.Typeface;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.content.SharedPreferences;
import java.util.*;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import android.util.Log;
import android.app.Activity;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
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
import android.widget.Spinner;
import android.widget.Toast;
import android.graphics.Typeface;
import android.widget.TextView;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.content.SharedPreferences;
import java.util.Arrays;

public class randomQuote {



    String[][] Entrepreneur_quotes = {
            {"lol I'm rich","Bill Gates"},
            {"wow I'm an entrepreneur","Mark Zuckerberg"},
            {"fuck this country lol","Donald Trump"},
            {"do you jerk off???", "Elon Musk"},
            {"Fuck me", "Albert Einsten"},
            {"Mi papi.", "Pablo Picasso"}
    };

    String[][] Celebrity_quotes = {
            {"lolol fvck normal people I'm a celebrity","Kanye West"},
            {"wow I'm an celebrity","Jackie Chan"},
            {"do you jerk off 2 times a day?","Matthew McCoughenhay"},
            {"How is the weather up your asshole?", "Steven Carrell"}
    };


    String[][] Author_quotes = {
            {"lol writing is stupid","J.K. Rowling"},
            {"wow I'm an author","J.R.R Tolkien"},
            {"Anime is trash", "Hayao Miyazaki"},
            {"This is an insult to life itself...", "Hayao Miyazaki"}
    };



    public String[] quote_generator (String genre) {


        String[] quote_AuthorPair = new String[2];

        if (genre.equals("All Genres")) {
            int rnd4 = new Random().nextInt(Entrepreneur_quotes.length+Celebrity_quotes.length+Author_quotes.length);
            Log.e("All Genres playing!", "Fetching All Quotes");
            //create new String[][] for all quotes
            String[][] all_quotes1 = append(Entrepreneur_quotes, Celebrity_quotes);
            String[][] all_quotes = append(Author_quotes, all_quotes1);
            MainActivity.motivational_quote.setText(all_quotes[rnd4][0]);
            MainActivity.quoter.setText(all_quotes[rnd4][1]);
            quote_AuthorPair[0] = all_quotes[rnd4][0];
            quote_AuthorPair[1] = all_quotes[rnd4][1];
            return quote_AuthorPair;
        }


        if (genre.equals("Entrepreneur")) {
            int rnd = new Random().nextInt(Entrepreneur_quotes.length);
            MainActivity.motivational_quote.setText(Entrepreneur_quotes[rnd][0]);
            MainActivity.quoter.setText(Entrepreneur_quotes[rnd][1]);
            quote_AuthorPair[0] = Entrepreneur_quotes[rnd][0];
            quote_AuthorPair[1] = Entrepreneur_quotes[rnd][1];
            return quote_AuthorPair;
        }

        else if (genre.equals("Celebrity")) {
            int rnd1 = new Random().nextInt(Celebrity_quotes.length);
            MainActivity.motivational_quote.setText(Celebrity_quotes[rnd1][0]);
            MainActivity.quoter.setText(Celebrity_quotes[rnd1][1]);
            quote_AuthorPair[0] = Celebrity_quotes[rnd1][0];
            quote_AuthorPair[1] = Celebrity_quotes[rnd1][1];
            return quote_AuthorPair;
        }

        else if (genre.equals("Author")) {
            int rnd2 = new Random().nextInt(Author_quotes.length);
            MainActivity.motivational_quote.setText(Author_quotes[rnd2][0]);
            MainActivity.quoter.setText(Author_quotes[rnd2][1]);
            quote_AuthorPair[0] = Author_quotes[rnd2][0];
            quote_AuthorPair[1] = Author_quotes[rnd2][1];
            return quote_AuthorPair;
        }
        else {
            MainActivity.motivational_quote.setText("No genre found!");
            quote_AuthorPair[0] = "No genre found!";
            quote_AuthorPair[1] = "No author found!";
            return quote_AuthorPair;
        }
    }
    public static String[][] append(String[][] a, String[][] b) {
        String[][] result = new String[a.length + b.length][];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }

}

