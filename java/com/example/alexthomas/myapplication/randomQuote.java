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
            {"Success is a lousy teacher. It seduces smart people into thinking they can't lose.", "Bill Gates"},
            {"The biggest risk is not taking any risk... In a world that changing really quickly, the only strategy that is guaranteed to fail is not taking risks.", "Mark Zuckerberg"},
            {"Patience is a virtue, and I'm learning patience. It's a tough lesson.", "Elon Musk"},
            {"The most beautiful thing we can experience is the mysterious. It is the source of all true art and science.", "Albert Einsten"},
            {"My mother said to me, 'If you are a soldier, you will become a general. If you are a monk, you will become the Pope.' Instead, I was a painter, and became Picasso.", "Pablo Picasso"},
            {"Your time is limited, so don’t waste it living someone else’s life.", "Steve Jobs"}
    };

    String[][] Celebrity_quotes = {
            {"lolol fvck normal people I'm a celebrity","Kanye West"},
            {"Cinema reflects culture and there is no harm in adapting technology, but not at the cost of losing your originality.","Jackie Chan"},
            {"The best revenge is massive success","Frank Sinatra"},
            {"Nothing to me feels as good as laughing incredibly hard.", "Steven Carrell"},
            {"Whether you think you can or you think you can’t, you’re right.", "Harrison Ford"}
    };


    String[][] Author_quotes = {
            {"We do not need magic to transform our world. We carry all of the power we need inside ourselves already.","J.K. Rowling"},
            {"Not all those who wander are lost.","J.R.R Tolkien"},
            {"Anime is a mistake, it's nothing but trash.", "Hayao Miyazaki"},
            {"This is an insult to life itself...", "Hayao Miyazaki"},
            {"Twenty years from now you will be more disappointed by the things that you didn’t do than by the ones you did do, so throw off the bowlines, sail away from safe harbor, catch the trade winds in your sails.  Explore, Dream, Discover.", "Mark Twain"},

    };

    String[][] Athlete_quotes = {
            {"I've missed more than 9000 shots in my career. I've lost almost 300 games. 26 times I've been trusted to take the game winning shot and missed. I've failed over and over and over again in my life. And that is why I succeed.", "Michael Jordan"},
            {"Every strike brings me closer to the next home run.", "Babe Ruth"},
            {"You must expect great things of yourself before you can do them.", "Michael Jordan"}
    };

    String[][] Anime_quotes = {
            {"The difference between the novice and the master is that the master has failed more times than the novice has tried." , "Korosensei (Assassination Classroom)"},
            {"The world's not perfect, but it's there for us trying the best it can. That's what it makes it so damn beautiful.", "Roy Mustang (Full Metal Alchemist)"},
            {"A lesson without pain is meaningless. That's because no one can gain without sacrificing something. But by enduring that pain and overcoming it, he shall obtain a powerful, unmatched heart. A heart full-metal.", "Edward Elric (Full Metal Alchemist)"},
            {"Those who forgive themselves, and are able to accept their true nature...They are the strong ones!", "Itachi Uchiha (Naruto)"},
            {"You can't change the world without getting your hands dirty.", "Lelouch Lamperouge (Code Geass)"},
            {" We wondered what happiness would look like if we could give it a physical form. If I'm not mistaken, I think it was Suzaku that said that the shape of happiness might resemble glass. His reasoning made sense. He said that even though you don't usually notice it, it's still definitely there. You merely have to change your point of view slightly, and then that glass will sparkle when it reflects the light.", "Lelouch Lamperouge (Code Geass)"}
    };



    public String[] quote_generator (String genre) {


        String[] quote_AuthorPair = new String[2];

        if (genre.equals("All Genres")) {
            int rnd4 = new Random().nextInt(Entrepreneur_quotes.length+Celebrity_quotes.length+Author_quotes.length);
            Log.e("All Genres playing!", "Fetching All Quotes");
            //create new String[][] for all quotes
            String[][] all_quotes1 = append(Entrepreneur_quotes, Celebrity_quotes);
            String[][] all_quotes = append(Author_quotes, all_quotes1);
            quote_AuthorPair[0] = all_quotes[rnd4][0];
            quote_AuthorPair[1] = all_quotes[rnd4][1];
            return quote_AuthorPair;
        }

        if (genre.equals("Entrepreneur")) {
            int rnd = new Random().nextInt(Entrepreneur_quotes.length);
            quote_AuthorPair[0] = Entrepreneur_quotes[rnd][0];
            quote_AuthorPair[1] = Entrepreneur_quotes[rnd][1];
            return quote_AuthorPair;
        }

        else if (genre.equals("Celebrity")) {
            int rnd1 = new Random().nextInt(Celebrity_quotes.length);
            quote_AuthorPair[0] = Celebrity_quotes[rnd1][0];
            quote_AuthorPair[1] = Celebrity_quotes[rnd1][1];
            return quote_AuthorPair;
        }

        else if (genre.equals("Author")) {
            int rnd2 = new Random().nextInt(Author_quotes.length);
            quote_AuthorPair[0] = Author_quotes[rnd2][0];
            quote_AuthorPair[1] = Author_quotes[rnd2][1];
            return quote_AuthorPair;
        }

        else if (genre.equals("Athlete")) {
            int rnd2 = new Random().nextInt(Athlete_quotes.length);
            quote_AuthorPair[0] = Athlete_quotes[rnd2][0];
            quote_AuthorPair[1] = Athlete_quotes[rnd2][1];
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

