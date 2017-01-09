package com.example.alexthomas.myapplication;
/* Created by Kevin on 1/2/2017.*/

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;
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


public class randomQuote extends AppCompatActivity {
    public static int minlength = 0;
    public static int maxlength = 10000;
    public static int last_rand = -1;


    String[][] Entrepreneur_quotes = {
            {"Success is a lousy teacher. It seduces smart people into thinking they can't lose.", "Bill Gates"},
            {"The biggest risk is not taking any risk... In a world that changing really quickly, the only strategy that is guaranteed to fail is not taking risks.", "Mark Zuckerberg"},
            {"Patience is a virtue, and I'm learning patience. It's a tough lesson.", "Elon Musk"},
            {"Your time is limited, so don’t waste it living someone else’s life.", "Steve Jobs"},
            {"Failure is the opportunity to begin more intelligently", "Henry Ford"},
            {"Someone is sitting in the shade today because someone planted a tree a long time ago.", "Warren Buffett"},
            {"Failure is an option here. If things are not failing, you are not innovating enough.", "Elon Musk"},
            {"If something is important enough, even if the odds are against you, you should still do it.", "Elon Musk"},
            {"If you just work on stuff that you like and you’re passionate about, you don’t have to have a master plan with how things will play out.", "Mark Zuckerberg"},
            {"If you’re not a risk taker, you should get the hell out of business.", "Ray Kroc"},
            {"Always deliver more than expected.","Larry Page"},
            {"You shouldn’t focus on why you can’t do something, which is what most people do. You should focus on why perhaps you can, and be one of the exceptions.", "Steve Case"},
            {"I’m convinced that about half of what separates the successful entrepreneurs from the non-successful ones is pure perseverance.", "Steve Jobs"},
            {"The way to get started is to quit talking and begin doing.", "Walt Disney"}


    };

    String[][] Celebrity_quotes = {
            {"Cinema reflects culture and there is no harm in adapting technology, but not at the cost of losing your originality.", "Jackie Chan"},
            {"The best revenge is massive success", "Frank Sinatra"},
            {"Nothing to me feels as good as laughing incredibly hard.", "Steven Carrell"},
            {"Whether you think you can or you think you can’t, you’re right.", "Harrison Ford"},
            {"You don't learn from successes; you don't learn from awards; you don't learn from celebrity; you only learn from wounds and scars and mistakes and failures. And that's the truth.", "Jane Fonda"},
            {"As a member of the Greek community and a part of one of these organizations this is highly offensive. Sororities at UC Berkeley make it their goal to give women a place to feel comfortable as well as better the community. Comparing specific houses to characters from a movie about bullying is absurd and beyond inaccurate. Making the claim that sororities are cliques is demeaning the sisterhood and values they are founded on. This clearly is a stab at a community on campus that does nothing but support the rest of the student body.", "Sorority Chick"},
            {"As human beings we have unlimited potential and imagination. The worst thing you can do is be a conformist and buy into conformity. It’s the worst possible thing. It’s better to be outrageous. It’s better to hang out with the sages, the people open to possibilities, even the psychotics. You never know where you’ll find the geniuses of our society", "Deepak Chopra"},
            {"It’s cool to be a nerd. There’s a general understanding that smartphones didn’t come from jocks. The digital age was foreseen by a group of short-sleeved, buttoned-down, white-shirted guys and their female equivalents designing the very stuff that’s now ubiquitous.", "J.J. Abrams"},
            {"If someone’s not attacking you that means you’re not doing your job effectively.", "Sean Hannity"},
            {"There’s no such thing as overnight success. That’s my concern with a show like American Idol. It encourages the false belief that there’s a kind of magic, that you can be 'discovered.' That may be the way television works, but it’s not the way the world works. Rising to the top of any field requires an enormous amount of dedication, focus, drive, talent, and 99 factors that they don’t show on television. It’s not simply about being picked. Which, by the way, is why very few of the anointed winners on American Idol have gone on to true success. Most have flamed out and gone away. That should tell us something.", "Malcom Gladwell"},
            {"I think people sometimes don’t pay enough attention to what they do. I’ve done well, but the reason is pretty simple: I’ve worked my ass off. The toughest thing a performer can do is make it look as if it comes easy.", "Justin Timberlake"},
            {"I've been very lucky. All I wanted was to pay the rent. Then these characters took off and suddenly there were Hulk coffee mugs and Iron Man lunchboxes and The Avengers sweatshirts everywhere. Money's okay, but what I really like is working.", "Stan Lee"}

    };


    String[][] Author_quotes = {
            {"We do not need magic to transform our world. We carry all of the power we need inside ourselves already.", "J.K. Rowling"},
            {"Not all those who wander are lost.", "J.R.R Tolkien"},
            {"Anime is a mistake, it's nothing but trash.", "Hayao Miyazaki"},
            {"This is an insult to life itself...", "Hayao Miyazaki"},
            {"Twenty years from now you will be more disappointed by the things that you didn’t do than by the ones you did do, so throw off the bowlines, sail away from safe harbor, catch the trade winds in your sails.  Explore, Dream, Discover.", "Mark Twain"},
            {"If you can tell stories, create characters, devise incidents, and have sincerity and passion, it doesn’t matter a damn how you write.", "Somerset Maugham"},
            {"Any man who keeps working is not a failure. He may not be a great writer, but if he applies the old-fashioned virtues of hard, constant labor, he’ll eventually make some kind of career for himself as writer.", "Ray Bradbury"},
            {"The secret of getting ahead is getting started.", "Mark Twain"},
            {"The best preparation for tomorrow is doing your best today.", "H. Jackson Brown Jr."},
            {"can't change the direction of the wind, but I can adjust my sails to always reach my destination.", "Jimmy Dean"},
            {"Put your heart, mind, and soul into even your smallest acts. This is the secret of success.", "Swami Sivananda"},
            {"Today I choose life. Every morning when I wake up I can choose joy, happiness, negativity, pain... To feel the freedom that comes from being able to continue to make mistakes and choices - today I choose to feel life, not to deny my humanity but embrace it.", "Walt Whitman"},
            {"You have brains in your head. You have feet in your shoes. You can steer yourself, any direction you choose.", "Dr. Seuss"},
            {"Hold fast to dreams, for if dreams die, life is a broken-winged bird that cannot fly.", "Langston Hughes"},
            {"I have discovered in life that there are ways of getting almost anywhere you want to go, if you really want to go.", "Langston Hughes"}


    };

    String[][] Athlete_quotes = {
            {"I've missed more than 9000 shots in my career. I've lost almost 300 games. 26 times I've been trusted to take the game winning shot and missed. I've failed over and over and over again in my life. And that is why I succeed.", "Michael Jordan"},
            {"Every strike brings me closer to the next home run.", "Babe Ruth"},
            {"You must expect great things of yourself before you can do them.", "Michael Jordan"},
            {"It's not whether you get knocked down; it's whether you get up.", "Vince Lombardi"},
            {"An athlete cannot run with money in his pockets. He must run with hope in his heart and dreams in his head.", "Emil Zatopek"},
            {"Most people never run far enough on their first wind to find out they’ve got a second.", "William James"},
            {"Do you know what my favorite part of the game is? The opportunity to play.", "Mike Singletary"},
            {"Just keep going. Everybody gets better if they keep at it.", "Ted Williams"},
            {"Push yourself again and again. Don’t give an inch until the final buzzer sounds.", "Larry Bird"},
            {"You can’t put a limit on anything. The more you dream, the farther you get.", "Michael Phelps"},
            {"I hated every minute of training, but I said, ‘Don’t quit. Suffer now and live the rest of your life as a champion.", " Muhammad Ali"},
            {"There are only two options regarding commitment. You’re either IN or you’re OUT. There is no such thing as life in-between.", "Pat Riley"},
            {"Gold medals aren’t really made of gold. They’re made of sweat, determination, and a hard-to-find alloy called guts.", "Dan Gable"}


    };

    String[][] Anime_quotes = {
            {"The difference between the novice and the master is that the master has failed more times than the novice has tried.", "Korosensei (Assassination Classroom)"},
            {"The world's not perfect, but it's there for us trying the best it can. That's what it makes it so damn beautiful.", "Roy Mustang (Full Metal Alchemist)"},
            {"A lesson without pain is meaningless. That's because no one can gain without sacrificing something. But by enduring that pain and overcoming it, he shall obtain a powerful, unmatched heart. A heart full-metal.", "Edward Elric (Full Metal Alchemist)"},
            {"Those who forgive themselves, and are able to accept their true nature...They are the strong ones!", "Itachi Uchiha (Naruto)"},
            {"You can't change the world without getting your hands dirty.", "Lelouch Lamperouge (Code Geass)"},
            {" We wondered what happiness would look like if we could give it a physical form. If I'm not mistaken, I think it was Suzaku that said that the shape of happiness might resemble glass. His reasoning made sense. He said that even though you don't usually notice it, it's still definitely there. You merely have to change your point of view slightly, and then that glass will sparkle when it reflects the light.", "Lelouch Lamperouge (Code Geass)"},
            {"To win, but not destroy; to conquer, but not humiliate: That is what true conquest is!", "Rider (Fate Zero)"},
            {"Before my eyes, it blocks my path. A high, high wall. What sort of scene is on the other side? What will I be able to see there? “The view from the top”. A scenery I will never be able to see on my own. But if I’m not alone, then... I might be able to see it.", "Shoyo Hinata, Haikyuu!!"}

    };

    String[][] GreatMinds_quotes = {
            {"My mother said to me, 'If you are a soldier, you will become a general. If you are a monk, you will become the Pope.' Instead, I was a painter, and became Picasso.", "Pablo Picasso"},
            {"The most beautiful thing we can experience is the mysterious. It is the source of all true art and science.", "Albert Einsten"},
            {"An unexamined life is not worth living.", "Socrates"},
            {"To find yourself, think for yourself.", "Socrates"},
            {"Live as if you were to die tomorrow, Learn as if you were to live forever", "Mahatma Gandhi"},
            {"And those who were seen dancing were thought to be insane by those who could not hear the music", "Friedrich Nietzche"},
            {"I love those who can smile in trouble, who can gather strength from distress, and grow brave by reflection. 'Tis the business of little minds to shrink, but they whose heart is firm, and whose conscience approves their conduct, will pursue their principles unto death.", "Leonardo DaVinci"},
            {"A person, who no matter how desperate the situation, gives others hope, is a true leader.", "Daisaku Ikeda"},
            {"You must not for one instant give up the effort to build new lives for yourselves. Creativity means to push open the heavy, groaning doorway to life.", "Daisaku Ikeda"},
            {"The determination to win is the better part of winning.", "Daisaku Ikeda"},
            {"It always seems impossible until it's done.", "Nelson Mandela"},
            {"The will to win, the desire to succeed, the urge to reach your full potential... these are the keys that will unlock the door to personal excellence.", "Confucius"},
            {"Look up at the stars and not down at your feet. Try to make sense of what you see, and wonder about what makes the universe exist. Be curious.", "Stephen Hawking"},
            {"Genius is one percent inspiration and ninety–nine percent perspiration.", "Thomas Edison"},
            {"To the young people in particular, I hope you will hear this. I have...spent my entire adult life fighting for what I believe in. I have had successes and I have had setbacks. Sometimes really painful ones. Many of you are at the beginning of your professional public and political careers. You will have successes and setbacks, too. This loss hurts but please never stop believing that fighting for what's right is worth it.", "Hilary Clinton"},



    };

    String[][] Book_quotes = {
            {"Twenty years from now you will be more disappointed by the things that you didn’t do than by the ones you did do.", "H. Jackson Brown, P.S. I Love You"},
            {"Every human life is worth the same, and worth saving.", "J.K. Rowling, Harry Potter: The Deathly Hallows"},
            {"Get busy living, or get busy dying.", "Stephen Kings, Different Seasons"},
            {"There is some good in this world, and it’s worth fighting for.", "J.R.R. Tolkien, The Two Towers"},
            {"It is our choices that show what we truly are, far more than our abilities.", "J.K. Rowling, Harry Potter and the Chamber of Secrets"},
            {"I don’t want to die without any scars.", "Chuck Palahniuk, Fight Club"},
            {"The worst enemy to creativity is self-doubt.", "Sylvia Plath, The Unabridged Journals of Sylvia Plath"},
            {"Some infinities are bigger than other infinities.", "John Green, The Fault in Our Stars"},
            {"Appear weak when you are strong, and strong when you are weak.", "Sun Tzu, The Art of War"},
            {"All we have to decide is what to do with the time that is given to us.", "J.R.R. Tolkien, The Fellowship of the Ring"},
            {"There’s only one rule you need to remember: laugh at everything and forget everybody else! It sound egotistical, but it’s actually the only cure for those suffering from self-pity.", "Anne Frank, The Diary of Anne Frank"},
            {"The saddest people I’ve ever met in life are the ones who don’t care deeply about anything at all. Passion and satisfaction go hand in hand, and without them, any happiness is only temporary, because there’s nothing to make it last.", "Nicholas Sparks, Dear"},
            {"You can’t stay in your corner of the Forest waiting for others to come to you. You have to go to them sometimes.", "A.A. Milne, Winnie-the-Pooh"},
            {"The point is not to pay back kindness but to pass it on.", "Julia Alvarez"},
            {"We can never give up longing and wishing while we are thoroughly alive. There are certain things we feel to be beautiful and good, and we must hunger after them.", "George Eliot, The Mill on the Floss"},
            {"We must have ideals and try to live up to them, even if we never quite succeed. Life would be a sorry business without them. With them it's grand and great.", "Lucy Maude Montgomery, Ann of Avonlea"},
            {"If you are interested in something, no matter what it is, go at it at full speed ahead. Embrace it with both arms, hug it, love it and above all become passionate about it. Lukewarm is no good. Hot is no good either. White hot and passionate is the only thing to be.", "Roald Dahl, My Uncle Oswald"},
            {"It does not do to dwell on dreams and forget to live, remember that.", "J.K. Rowling, Harry Potter and the Sorcerer's Stone"},
            {"Cherish your friends, stay true to your principles, live passionately and fully and well. Experience new things. Love and be loved, if you ever get the chance.", "David Nicholls, One Day"}

    };

    String[][] meme_quotes = {
            {"I have never seen a thin person drinking  Diet Coke.", "Donald Trump"},
            {"You know, it really doesn’t matter what the media write as long as you’ve got a young, and beautiful, piece of ass.", "Donald Trump"},
            {"The beauty of me is that I’m very rich.", "Donald Trump"},
            {"I can't belive my grand mothers making me take Out the garbage I'm rich fuck this I'm going home I don't need this shit", "50 Cent"},
            {"I always lick my lips when I see a child look at me because they need to realize there are bad people in this world", "Jordan the Twitter guy"},
            {"I STRAIGHT UP HAVE NO IDEA HOW PORCUPINES FUCK EACH OTHER", "Bill Nye Tho"},
            {"Don't let what other people think of you stop you from doing what you love", "Adolf Hitler"},
            {"George Bush doesn't care about black people", "Kanye West"}

    };


    String[] error = {"No genre found!", "No author found!"};


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public String[] quote_generator(String genre) {


        String[] answer = {};
        String[][][] original = {
                Entrepreneur_quotes, Celebrity_quotes, Author_quotes, Athlete_quotes,
                Anime_quotes, GreatMinds_quotes, Book_quotes};

        String[][] sum = two_d_summer(original);


        if (genre.equals("--Choose your Genres--")) {
            genre = "All Genres";
            Log.e("Default", "changed to All genres");
        }


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
            case "Great Minds":
                answer = solver(GreatMinds_quotes);
                return answer;
            case "Book Quotes":
                answer = solver(Book_quotes);
                return answer;
            case "Meme Quotes":
                answer = solver(meme_quotes);
            default:
                break;
        }
        return answer;

    }


    public static String[][] append(String[][] a, String[][] b) {
        String[][] result = new String[a.length + b.length][];
        System.arraycopy(a, 0, result, 0, a.length);
        System.arraycopy(b, 0, result, a.length, b.length);
        return result;
    }


    public String[][] two_d_summer(String[][][] args) {

        String[][] sum = {};
        for (int i = 0; i < args.length; i++) {
            sum = append(sum, args[i]);
        }
        return sum;
    }


    public String[] solver(String[][] quote_array) {


        int rnd = new Random().nextInt(quote_array.length);

        Log.e("Last_rand right now is ", String.valueOf(last_rand));
        Log.e("quote", "max length is " + maxlength);
        Log.e("quote", "min length is " + minlength);
        Log.e("quote", "length of rnd is " + quote_array[rnd][0].length());


        if (rnd != last_rand && quote_array[rnd][0].length() < maxlength && quote_array[rnd][0].length() > minlength) {

            String[] quote_AuthorPair = new String[2];
            quote_AuthorPair[0] = quote_array[rnd][0];
            Log.e("first", "value is set to " + String.valueOf(quote_array[rnd][0]));
            quote_AuthorPair[1] = quote_array[rnd][1];
            Log.e("last_rand is ",String.valueOf(rnd));
            randomQuote.last_rand = rnd;
            return quote_AuthorPair;
        } else {
            return solver(quote_array);
        }


    }

}




