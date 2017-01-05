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
import android.app.Activity;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;


public class randomQuote extends AppCompatActivity{



    String[][] Entrepreneur_quotes = {
            {"Success is a lousy teacher. It seduces smart people into thinking they can't lose.", "Bill Gates"},
            {"The biggest risk is not taking any risk... In a world that changing really quickly, the only strategy that is guaranteed to fail is not taking risks.", "Mark Zuckerberg"},
            {"Patience is a virtue, and I'm learning patience. It's a tough lesson.", "Elon Musk"},
            {"The most beautiful thing we can experience is the mysterious. It is the source of all true art and science.", "Albert Einsten"},
            {"My mother said to me, 'If you are a soldier, you will become a general. If you are a monk, you will become the Pope.' Instead, I was a painter, and became Picasso.", "Pablo Picasso"},
            {"Your time is limited, so don’t waste it living someone else’s life.", "Steve Jobs"}
    };

    String[][] Celebrity_quotes = {
            {"George Bush doesn't care about black people","Kanye West"},
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
            {" We wondered what happiness would look like if we could give it a physical form. If I'm not mistaken, I think it was Suzaku that said that the shape of happiness might resemble glass. His reasoning made sense. He said that even though you don't usually notice it, it's still definitely there. You merely have to change your point of view slightly, and then that glass will sparkle when it reflects the light.", "Lelouch Lamperouge (Code Geass)"},
            {"To win, but not destroy; to conquer, but not humiliate: That is what true conquest is!", "Rider (Fate Zero)"},
            {"A prison, you say? You think you'll be free if you go to the world above? A girl like you can never be free no matter where you go. After all, we humans are apes put in a cage called Earth. There is no difference between the world above and the world below. The only difference is which is bigger or smaller. People who pout about how confining the cage is can never be happy. They live their lives only seeing the iron bars. A true lack of freedom is when you cage your soul.", "Gintama (Gintama)"}
    };

    String[] error = {"No genre found!","No author found!"};



    public String[] quote_generator (String genre) {


        String[] answer = {};
        String[][][] original = {Entrepreneur_quotes, Celebrity_quotes, Author_quotes, Athlete_quotes, Anime_quotes};
        String[][] sum = two_d_summer(original);

        switch (genre) {
            case "All Genres":
                answer = solver(sum);
                return answer;
            case "Entrepreneur":
                answer = solver(Entrepreneur_quotes);
                return answer;
            case "Celebrity":
                answer = solver(Celebrity_quotes);
                return answer;
            case "Author":
                answer = solver(Author_quotes);
                return answer;
            case "Athlete":
                answer = solver(Athlete_quotes);
                return answer;
            case "Anime":
                answer = solver(Anime_quotes);
                return answer;
            default:
                break;
            } return answer;

        }










    public static String[][] append(String[][] a, String[][] b) {
        String[][] result = new String[a.length + b.length][];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }



    public String[][] two_d_summer(String[][][] args) {

        String[][] sum = {};
        for(int i=0; i<args.length; i++) {
            sum = append(sum,args[i]);
        }
        return sum;
    }



    public String[] solver(String[][] quote_array){
        SharedPreferences quote_id1 = getSharedPreferences("same", MODE_PRIVATE);
        String message1 = quote_id1.getString("Message", "999999");


        int rnd = new Random().nextInt(quote_array.length);
        if (rnd != Integer.parseInt(message1)) {
            SharedPreferences.Editor editor = quote_id1.edit();
            editor.putString("Message",String.valueOf(rnd));
            Log.e("new quote is index ", String.valueOf(rnd));
            editor.apply();

            String[] quote_AuthorPair = new String[2];
            quote_AuthorPair[0] = quote_array[rnd][0];
            quote_AuthorPair[1] = quote_array[rnd][1];
            return quote_AuthorPair;
        }
        else {
            return solver(quote_array);
        }
    }

}




