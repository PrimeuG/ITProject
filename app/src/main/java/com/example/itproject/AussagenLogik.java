package com.example.itproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class AussagenLogik extends AppCompatActivity {

    Button z, v, klammerauf, klammerzu, mindestensein, f, komma, und, g, implikation, b, nicht, loeschen, bestaetigen, weiter, fueralle, x, y;
    TextView AussagenAufgabe, AussagenAntwort;

    public static String LogikFormelString;
    public static String LogikAussageString;
    public static String LogikPrädikateString;

    public static ArrayList<String> LogikFormeln = new ArrayList<>();
    public static ArrayList<String> AussageString = new ArrayList<>();
    public static ArrayList<String> LogikString = new ArrayList<>();

    public static int fragenzaehler = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aussagen_logik);

        z = (Button) findViewById(R.id.button24);
        v = (Button) findViewById(R.id.button13);
        klammerauf = (Button) findViewById(R.id.button23);
        klammerzu = (Button) findViewById(R.id.button26);
        mindestensein = (Button) findViewById(R.id.button20);
        f = (Button) findViewById(R.id.button19);
        komma = (Button) findViewById(R.id.button27);
        und = (Button) findViewById(R.id.button12);
        g = (Button) findViewById(R.id.button22);
        implikation = (Button) findViewById(R.id.button17);
        b = (Button) findViewById(R.id.button25);
        nicht = (Button) findViewById(R.id.button16);
        loeschen = (Button) findViewById(R.id.button18);
        bestaetigen = (Button) findViewById(R.id.BestaetigenAussage);
        weiter = (Button) findViewById(R.id.WeitereAussage);
        fueralle = (Button) findViewById(R.id.button14);
        x = (Button) findViewById(R.id.button15);
        y = (Button) findViewById(R.id.button21);

        /*z.setOnClickListener(this);
        v.setOnClickListener(this);
        klammerzu.setOnClickListener(this);
        klammerauf.setOnClickListener(this);
        mindestensein.setOnClickListener(this);
        f.setOnClickListener(this);
        komma.setOnClickListener(this);

        g.setOnClickListener(this);
        implikation.setOnClickListener(this);
        b.setOnClickListener(this);
        nicht.setOnClickListener(this);
        loeschen.setOnClickListener(this);
        bestaetigen.setOnClickListener(this);
        weiter.setOnClickListener(this);
        fueralle.setOnClickListener(this);
        x.setOnClickListener(this);
        y.setOnClickListener(this);*/

        AussagenAufgabe = findViewById(R.id.Aussagenaufgabe);
        AussagenAntwort = findViewById(R.id.AussagenTerm);

        Aussagengeber();

        weiter.setVisibility(View.INVISIBLE);

        AussagenAufgabe.setText(AussageString.get(fragenzaehler) + "\n" + LogikString.get(fragenzaehler));

        und.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AussagenAntwort.setText(AussagenAntwort.getText() + "∧");
            }
        });

        nicht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AussagenAntwort.setText(AussagenAntwort.getText() + "¬");
            }
        });

        implikation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AussagenAntwort.setText(AussagenAntwort.getText() + "->");
            }
        });

        loeschen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    if (AussagenAntwort.getText().toString().valueOf(AussagenAntwort.getText().toString().charAt(AussagenAntwort.getText().toString().length() - 1)).equals(">")) {
                        AussagenAntwort.setText(AussagenAntwort.getText().toString().substring(0, AussagenAntwort.getText().length() - 2));
                        //AussagenAntwort.setText(AussagenAntwort.getText().toString().substring(0,AussagenAntwort.getText().length()-1));
                    } else {
                        AussagenAntwort.setText(AussagenAntwort.getText().toString().substring(0, AussagenAntwort.getText().length() - 1));
                    }
                } catch (Exception e) {

                }

            }
        });

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AussagenAntwort.setText(AussagenAntwort.getText() + "v");
            }
        });

        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AussagenAntwort.setText(AussagenAntwort.getText() + "f");
            }
        });

        g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AussagenAntwort.setText(AussagenAntwort.getText() + "g");
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AussagenAntwort.setText(AussagenAntwort.getText() + "b");
            }
        });

        fueralle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AussagenAntwort.setText(AussagenAntwort.getText() + "∀");
            }
        });

        mindestensein.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AussagenAntwort.setText(AussagenAntwort.getText() + "∃");
            }
        });

        klammerauf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AussagenAntwort.setText(AussagenAntwort.getText() + "(");
            }
        });

        klammerzu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AussagenAntwort.setText(AussagenAntwort.getText() + ")");
            }
        });

        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AussagenAntwort.setText(AussagenAntwort.getText() + "x");
            }
        });

        y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AussagenAntwort.setText(AussagenAntwort.getText() + "y");
            }
        });

        z.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AussagenAntwort.setText(AussagenAntwort.getText() + "z");
            }
        });

        komma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AussagenAntwort.setText(AussagenAntwort.getText() + ",");
            }
        });

        bestaetigen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AussagenAntwort.getText().toString().equals(LogikFormeln.get(fragenzaehler))) {
                    AussagenAntwort.setText("RICHTIG!");
                    weiter.setBackgroundColor(Color.GREEN);
                } else {
                    AussagenAntwort.setText("Die Antwort lautet: " + LogikFormeln.get(fragenzaehler));
                    weiter.setBackgroundColor(Color.RED);
                }
                bestaetigen.setVisibility(View.INVISIBLE);
                weiter.setVisibility(View.VISIBLE);

            }

        });

        weiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragenzaehler < 2) {
                    AussagenAntwort.setText("");
                    fragenzaehler++;
                    AussagenAufgabe.setText(AussageString.get(fragenzaehler) + "\n" + LogikString.get(fragenzaehler));
                    weiter.setVisibility(View.INVISIBLE);
                    bestaetigen.setVisibility(View.VISIBLE);

                }else {
                    activityWechsel();
                    finish();

                }

            }
        });


    }


    //Methode zum generieren der Aussage
    public static void LogikAussage() {


        //Einzelne ArrayListen
        ArrayList<String> Formeln = new ArrayList<>(); //Hier werden die Formeln welcher der Aufgabe zugrunde liegen gespeichert
        ArrayList<String> Aussagen = new ArrayList<>(); //Hier wird eine Aussage gespeichert welche zu der jeweiligen Formel passt
        ArrayList<String> Prädikate = new ArrayList<>();  //Hier werden die Prädikate welche zu der Aussage passen gespeichert

        ArrayList<String> Namen = new ArrayList<>();    //Hier wird eine Liste von Namen gespeichert welche Variabel implementiert werden in den Aussagen, die eigentlichen Formeln bleiben unberührt
        ArrayList<String> Attribute = new ArrayList<>();  //Hier werden variable Attribute gespeichert welche Analog zu den Namen random verwendet werden
        ArrayList<String> Verben = new ArrayList<>();   //Analog zu den anderen beiden

        Formeln.clear();
        Aussagen.clear();
        Prädikate.clear();
        Namen.clear();
        Attribute.clear();
        Verben.clear();


        Formeln.add("∀yv(y)((∀xf(x,y)->b(x))->g(y))");
        Formeln.add("∀xv(x)(∃yv(y)(f(x,y)∧g(x)))->g(x)))");
        Formeln.add("∀x(g(x)-> b(x))");
        Formeln.add("∀x((∀y(f(y, x)->g(y)))->b(x))"); //Sehr unsicher
        Formeln.add("∀x(b(x)->∃y(f(x,y)∧¬(∃z(n(z)∧k(z,y)))))"); //https://formal.kastel.kit.edu/~beckert/teaching/Logik-SS06/hklausur_lsg.pdf
        Formeln.add("∀x(v(x)->(∃yf(x,y))"); //debatierbar
        Formeln.add("∀x(f(x,y)");
        Formeln.add("∀x∀yf(x,y)");


        Aussagen.add("Jeder X1 ist Z1, wenn alle seine X2 Z2 sind.");
        Aussagen.add("Ein X1 ist Z1, wenn er X2 mindestens eines Z1 X1 ist.");
        Aussagen.add("Alle Z1 X1 können V1.");
        Aussagen.add("Jeder X1 ist Z1, wenn alle seine X2 V1 können.");
        Aussagen.add("Jedes Problem hat eine Lösung, die kein X1 versteht.");
        Aussagen.add("Für jeden X1 gibt es eine Lösung");
        Aussagen.add("Jeder X1 mag X2");
        Aussagen.add("Jeder mag jeden");

        Prädikate.add("v(x) - x ist X1\nf(x,y) - x ist X2 von y\ng(x) - x ist Z1\nb(x) - x ist Z2");
        Prädikate.add("v(x) - x ist X1\nf(x,y) - x ist X2 von y\ng(x) - x ist Z1");
        Prädikate.add("v(x) - x ist X1\ng(x) - x ist Z1\nb(x) - x kann V1");
        Prädikate.add("v(x) - x ist X1\nf(y,x) - y ist X2 von x\ng(y) y ist Z1\nb(x) - x ist V1");
        Prädikate.add("v(x) - x ist ein X1\nf(x,y) - y ist die Lösung von x\nb(x) - x ist ein Problem\nk(x,y) - x versteht y");
        Prädikate.add("v(x) - x ist ein X1\nf(x,y) - y ist die Lösung von x");
        Prädikate.add("v(x)- x ist ein X1\nf(x.y) x mag y\ng(y) - y ist X2");
        Prädikate.add("f(x,y) - x mag y");


        Attribute.add("blond");
        Attribute.add("traurig");
        Attribute.add("schön");
        Attribute.add("lecker");
        Attribute.add("dumm");
        Attribute.add("grün");
        Attribute.add("verkorkst");
        Attribute.add("wissbegierig(en)");
        Attribute.add("einsam");


        Verben.add("schwimmen");
        Verben.add("fliegen");
        Verben.add("tauchen");
        Verben.add("tanzen");
        Verben.add("singen");
        Verben.add("schnarchen");
        Verben.add("sterben");
        Verben.add("essen");
        Verben.add("trinken");
        Verben.add("senieren");
        Verben.add("denken");
        Verben.add("atmen");
        Verben.add("ertrinken");


        Namen.add("Ork(s)");
        Namen.add("Champion(s)");
        Namen.add("Held(en)");
        Namen.add("Vampir(e)");
        Namen.add("Dinosaurier");
        Namen.add("Butler");
        Namen.add("Elfen(s)");
        Namen.add("Roboter(s)");
        Namen.add("Alien(s)");
        Namen.add("Werwoelf(e)");
        Namen.add("Krieger");
        Namen.add("Paladin");
        Namen.add("Jäger");
        Namen.add("Schurke");
        Namen.add("Priester");
        Namen.add("Schamane");
        Namen.add("Magier");
        Namen.add("Hexenmeister");
        Namen.add("Mönch");
        Namen.add("Druide");
        Namen.add("Dämonenjäger");
        Namen.add("Todesritter");
        Namen.add("Elch");
        Namen.add("Assassinen");


        //Zufällige Zahl zwischen 0 und der size des Formel Arrays wird gebildet
        Random random = new Random();
        int randomNum = random.nextInt(Formeln.size() - 0);

        //Aus der Zufallszahl werden nun die Formel und die dazugehörige Aussage und die Prädikate ausgewählt
        String Formel = Formeln.get(randomNum);
        String Aussage = Aussagen.get(randomNum);
        String Prädikat = Prädikate.get(randomNum);

        //Möglichkeit zur Ausgabe der ausgewählten Sachen
        //System.out.println(Formel);
        //System.out.println(randomNum);


        //hier wird der erste zufällige Name ausgewählt
        int randomName1 = random.nextInt(Namen.size() - 0);
        String Name1 = Namen.get(randomName1);

        //hier wird der zweite zufällige Name ausgewählt, aber er wird solange neu ausgewürfelt bis es ein abweichender Name vom ersten ist
        int randomName2 = random.nextInt(Namen.size() - 0);
        while (randomName1 == randomName2) {
            randomName2 = random.nextInt(Namen.size() - 0);
        }
        String Name2 = Namen.get(randomName2);


        //hier wird das erste zufällige Attribut ausgewählt
        int randomAttribut1 = random.nextInt(Attribute.size() - 0);
        String Attribut1 = Attribute.get(randomAttribut1);

        //hier wird das zweite zufällige Attribut ausgewählt, aber es wird solange neu ausgewürfelt bis es ein abweichendes Attribut vom ersten ist
        int randomAttribut2 = random.nextInt(Attribute.size() - 0);
        while (randomAttribut1 == randomAttribut2) {
            randomAttribut2 = random.nextInt(Attribute.size() - 0);
        }
        String Attribut2 = Attribute.get(randomAttribut2);


        //Analog zu Name und Attribut
        int randomVerb1 = random.nextInt(Verben.size() - 0);
        String Verb1 = Verben.get(randomVerb1);

        int randomVerb2 = random.nextInt(Verben.size() - 0);
        while (randomVerb1 == randomVerb2) {
            randomVerb2 = random.nextInt(Verben.size() - 0);
        }
        String Verb2 = Verben.get(randomVerb2);


        //Hier wird das Zeug replaced mit den Namen die zufällig generiert wurden
        String AusgabeAnzeige = Aussage.replaceAll("X1", Name1).replaceAll("X2", Name2).replaceAll("Z1", Attribut1).replaceAll("Z2", Attribut2).replaceAll("V1", Verb1).replaceAll("V2", Verb2);
        String AnzeigePrädikate = Prädikat.replaceAll("X1", Name1).replaceAll("X2", Name2).replaceAll("Z1", Attribut1).replaceAll("Z2", Attribut2).replaceAll("V1", Verb1).replaceAll("V2", Verb2);

        /*System.out.println(randomName1);
        System.out.println(Name1);

        System.out.println(randomName2);
        System.out.println(Name2);

        System.out.println(Attribut1);
        System.out.println(Attribut2);*/


        //Hier werden die globalen Strings gesetzt
        LogikFormelString = Formel;
        LogikAussageString = AusgabeAnzeige;
        LogikPrädikateString = AnzeigePrädikate;


        /*System.out.println(AusgabeAnzeige);
        System.out.println(AnzeigePrädikate);*/


    }

    public void Aussagengeber() {

        LogikFormeln.clear();
        AussageString.clear();
        LogikString.clear();

        //Scanf einfach um eine Eingabe tätigen zu können
        try {


            for (int i = 0; i < 3; i++) {

                for (int k = 0; k < 3; k++) {
                    LogikAussage();
                    while (LogikFormeln.contains(LogikFormelString)) {
                        LogikAussage();
                    }
                    LogikFormeln.add(LogikFormelString);
                    AussageString.add(LogikAussageString);
                    LogikString.add(LogikPrädikateString);
                    k++;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void activityWechsel() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.spiel_menu,menu);
        return true;
    }
    //Für Neustart und Endscreen, es werden verschiedene Dinge resettet
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()){
                case R.id.cancel:
                    activityWechsel();
                    finish();
                    break;

            }
        }catch (Exception e){

        }
        return super.onOptionsItemSelected(item);
    }

}