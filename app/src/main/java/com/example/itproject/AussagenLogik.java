package com.example.itproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class AussagenLogik extends AppCompatActivity {

    public Button z, v, klammerauf, klammerzu, mindestensein, f, komma, und, g, implikation, b, nicht, loeschen, bestaetigen, weiter, fueralle, x, y;
    TextView AussagenAufgabe, AussagenAntwort;

    private static final String FILE_NAME = "Auswertung.txt";

    private static String Benutzername = "Benutzername";
    private static String annehmer = "";

    public static String LogikFormelString;
    public static String LogikAussageString;
    public static String LogikPrädikateString;

    public static Date Anfangszeit;
    public static Date Endzeit;

    public static ArrayList<String> LogikFormeln = new ArrayList<>();
    public static ArrayList<String> AussageString = new ArrayList<>();
    public static ArrayList<String> LogikString = new ArrayList<>();

    public static int fragenzaehler = 0;
    public static int generiertePraedikate = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_aussagen_logik);

        Anfangszeit = null;
        Endzeit = null;

        Anfangszeit = Calendar.getInstance().getTime();
        generiertePraedikate = 0;
        generiertePraedikate++;

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

        ButtonBlaumacher();

        AussagenAufgabe = findViewById(R.id.Aussagenaufgabe);
        AussagenAntwort = findViewById(R.id.AussagenTerm);

        Aussagengeber();

        weiter.setVisibility(View.INVISIBLE);

        AussagenAufgabe.setText("Formalisieren Sie die folgende Aussage: \n" + AussageString.get(fragenzaehler) + "\nmit folgenden Prädikaten\n" + LogikString.get(fragenzaehler));

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
                    AussagenAntwort.setText("Die Musterlösung lautet: \n" + LogikFormeln.get(fragenzaehler));
                    weiter.setBackgroundColor(Color.BLUE);
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
                    generiertePraedikate++;
                    AussagenAufgabe.setText("Formalisieren Sie die folgende Aussage: \n" + AussageString.get(fragenzaehler) + "\nmit folgenden Prädikaten\n" + LogikString.get(fragenzaehler));
                    weiter.setVisibility(View.INVISIBLE);
                    bestaetigen.setVisibility(View.VISIBLE);

                } else {
                    fragenzaehler = 0;
                    Endzeit = Calendar.getInstance().getTime();
                    speichern();
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

        ArrayList<String> Relation = new ArrayList<>(); //Analog zu denen davor
        ArrayList<String> Interessiert = new ArrayList<>(); //Analog zu denen davor

        Formeln.clear();
        Aussagen.clear();
        Prädikate.clear();
        Namen.clear();
        Attribute.clear();
        Verben.clear();
        Relation.clear();
        Interessiert.clear();


        Formeln.add("∀x∀yv(x,y)");
        Formeln.add("∃x∀yv(x,y)");
        Formeln.add("∀x∃yv(x,y)");
        Formeln.add("∃y∀xv(x,y)");
        Formeln.add("∀y∃xv(x,y)");
        Formeln.add("∃x∃yv(x,y)");
        Formeln.add("∀xv(x,x)");
        Formeln.add("∃xv(x,x)");


        Formeln.add("∀x(v(x)->b(x))");
        Formeln.add("∀x(v(x)->(b(x)->f(x)))");
        //Formeln.add("∃xd(x)");//Eigenkreation

        //Formel aus Klausur

        Formeln.add("∀xv(x)(∃yv(y)(f(x,y)∧g(x)))->g(x)))");


        Aussagen.add("Jeder I1 jeden.");
        Aussagen.add("Es gibt mindestens einen, der jeden I1.");
        Aussagen.add("Jeder I1 mindestens einen.");
        Aussagen.add("Es gibt mindestens einen, der von jedem I1 wird.");
        Aussagen.add("Jeder wird von mindestens einem I1.");
        Aussagen.add("Es gibt mindestens einen, der mindestens einen I1.");
        Aussagen.add("Jeder I2 sich selbst.");
        Aussagen.add("Es gibt mindestens einen, der sich selbst I1.");

        Aussagen.add("Jeder X1 V1.");//https://people.umass.edu/partee/NZ_2006/More%20Answers%20for%20Practice%20in%20Logic%20and%20HW%201.pdf
        Aussagen.add("Jeder X1 welcher V1 V2.");
        //Aussagen.add("Es gibt mindestens einen welcher Z1 ist.");//Eigenkreation

        //Aussage aus Klausur

        //X Wesen, Z Eigenschaft, V tätigkeit, R Relation zueinander
        Aussagen.add("Ein X1 ist Z1, wenn er R1 mindestens eines Z1 X1 ist.");

        Prädikate.add("v(x,y) - x I1 y");
        Prädikate.add("v(x,y) - x I1 y");
        Prädikate.add("v(x,y) - x I1 y");
        Prädikate.add("v(x,y) - x I1 y");
        Prädikate.add("v(x,y) - x I1 y");
        Prädikate.add("v(x,y) - x I1 y");
        Prädikate.add("v(x,y) - x I1 y");
        Prädikate.add("v(x,y) - x I1 y");

        Prädikate.add("v(x) - x ist ein X1\nb(x) - x V1");
        Prädikate.add("v(x) - x ist ein X1\nb(x) - x V1\nf(x) - x V2");
        //Prädikate.add("d(x) - x ist Z1"); //Eigenkreation

        //Prädikate aus Klausur

        Prädikate.add("v(x) - x ist X1\nf(x,y) - x ist R1 von y\ng(x) - x ist Z1");

        Relation.add("Kind");
        Relation.add("Bruder");
        Relation.add("Onkel");
        Relation.add("Vater");
        Relation.add("Freund");

        Interessiert.add("(ge)liebt");
        Interessiert.add("verehrt");
        Interessiert.add("(ge)hasst");
        Interessiert.add("bewundert");
        Interessiert.add("verachtet");
        Interessiert.add("beneidet");
        Interessiert.add("beschützt");
        Interessiert.add("ignoriert");
        Interessiert.add("verachtet");
        Interessiert.add("verdammt");
        Interessiert.add("verachtet");

        Attribute.add("blond(en)");
        Attribute.add("traurig(en)");
        Attribute.add("schön(en)");
        Attribute.add("dumm(en)");
        Attribute.add("grün(en)");
        Attribute.add("verkorkst(en)");
        Attribute.add("wissbegierig(en)");
        Attribute.add("einsam(en)");
        Attribute.add("lustigen(en)");


        Verben.add("schwimmt");
        Verben.add("fliegt");
        Verben.add("taucht");
        Verben.add("tanzt");
        Verben.add("singt");
        Verben.add("schnarcht");
        Verben.add("stirbt");
        Verben.add("isst");
        Verben.add("trinkt");
        Verben.add("seniert");
        Verben.add("denkt");
        Verben.add("atmet");
        Verben.add("ertrinkt");


        Namen.add("Ork(s)");
        Namen.add("Champion(s)");
        Namen.add("Held(en)");
        Namen.add("Vampir(s)");
        Namen.add("Dinosaurier(s)");
        Namen.add("Butler(s)");
        Namen.add("Elf(en)");
        Namen.add("Roboter(s)");
        Namen.add("Werwoelf(es)");
        Namen.add("Krieger(s)");
        Namen.add("Paladin(s)");
        Namen.add("Jäger(s)");
        Namen.add("Schurke(n)");
        Namen.add("Priester(s)");
        Namen.add("Schamane(n)");
        Namen.add("Magier(s)");
        Namen.add("Hexenmeister(s)");
        Namen.add("Mönch(es)");
        Namen.add("Druide(n)");
        Namen.add("Dämonenjäger(s)");
        Namen.add("Todesritter(s)");
        Namen.add("Elch(es)");
        Namen.add("Assassine(n)");


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

        //Analog zu Name und Attribut
        int randomRelation1 = random.nextInt(Relation.size() - 0);
        String Relation1 = Relation.get(randomRelation1);

        int randomRelation2 = random.nextInt(Relation.size() - 0);
        while (randomRelation1 == randomRelation2) {
            randomRelation2 = random.nextInt(Relation.size() - 0);
        }
        String Relation2 = Relation.get(randomRelation2);

        //Analog zu Name und Attribut
        int randomInter1 = random.nextInt(Interessiert.size() - 0);
        String Inter1 = Interessiert.get(randomInter1);

        int randomInter2 = random.nextInt(Interessiert.size() - 0);
        while (randomInter1 == randomInter2) {
            randomInter2 = random.nextInt(Interessiert.size() - 0);
        }
        String Inter2 = Interessiert.get(randomInter2);


        //Hier wird das Zeug replaced mit den Namen die zufällig generiert wurden
        String AusgabeAnzeige = Aussage.replaceAll("X1", Name1).replaceAll("X2", Name2).replaceAll("Z1", Attribut1).replaceAll("Z2", Attribut2).replaceAll("V1", Verb1).replaceAll("V2", Verb2).replaceAll("R1", Relation1).replaceAll("R2", Relation2).replaceAll("I1", Inter1).replaceAll("I2", Inter2);
        String AnzeigePrädikate = Prädikat.replaceAll("X1", Name1).replaceAll("X2", Name2).replaceAll("Z1", Attribut1).replaceAll("Z2", Attribut2).replaceAll("V1", Verb1).replaceAll("V2", Verb2).replaceAll("R1", Relation1).replaceAll("R2", Relation2).replaceAll("I1", Inter1).replaceAll("I2", Inter2);
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

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.spiel_menu2, menu);
        return true;
    }

    //Für Neustart und Endscreen, es werden verschiedene Dinge resettet
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.cancel2:
                    fragenzaehler = 0;
                    activityWechsel();
                    finish();
                    break;

            }
        } catch (Exception e) {

        }
        return super.onOptionsItemSelected(item);
    }

    public void ButtonBlaumacher() {

        z.setBackgroundColor(Color.BLUE);
        v.setBackgroundColor(Color.BLUE);
        klammerzu.setBackgroundColor(Color.BLUE);
        klammerauf.setBackgroundColor(Color.BLUE);
        mindestensein.setBackgroundColor(Color.BLUE);
        f.setBackgroundColor(Color.BLUE);
        komma.setBackgroundColor(Color.BLUE);
        und.setBackgroundColor(Color.BLUE);
        g.setBackgroundColor(Color.BLUE);
        implikation.setBackgroundColor(Color.BLUE);
        b.setBackgroundColor(Color.BLUE);
        nicht.setBackgroundColor(Color.BLUE);
        loeschen.setBackgroundColor(Color.BLUE);
        weiter.setBackgroundColor(Color.BLUE);
        fueralle.setBackgroundColor(Color.BLUE);
        x.setBackgroundColor(Color.BLUE);
        y.setBackgroundColor(Color.BLUE);
        bestaetigen.setBackgroundColor(Color.BLUE);
    }

    //Die Methoden speichern() und laden() sind übernommen von https://gist.github.com/codinginflow/6c13bd0d08416115798f17d45b5d8056

    public void speichern() {
        laden();

        String text = annehmer + "UserID|" + Benutzername + ";Activity|Praedikatenlogik;Anfangszeit|" + Anfangszeit + ";Endzeit|" + Endzeit + ";\n";

        FileOutputStream fos = null;

        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(text.getBytes());


            //Toast.makeText(this, "Saved to " + getFilesDir() + "/" + FILE_NAME, Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void laden() {
        FileInputStream fis = null;

        try {
            fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }

            annehmer = sb.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}