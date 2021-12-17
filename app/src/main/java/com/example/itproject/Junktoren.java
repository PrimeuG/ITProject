package com.example.itproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.io.parsers.ParserException;
import org.logicng.io.parsers.PropositionalParser;
import org.logicng.transformations.dnf.DNFFactorization;
import org.mvel2.MVEL;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class Junktoren extends AppCompatActivity {

    private static final String FILE_NAME = "Auswertung.txt";

    private static String Benutzername = "Benutzername";
    private static String annehmer = "";

    public static String Aussage;
    public static String ParserText = "";
    public static String ergebnis = "";

    public static Date Anfangszeit;
    public static Date Endzeit;

    public static int fragenanzahl = 0;

    public static int schwierigkeitsgrad = 0;
    public static int schwierigkeitsgradpunkte1 = 0;
    public static int schwierigkeitsgradpunkte2 = 0;
    public static int schwierigkeitsgradpunkte3 = 0;
    public static int schwierigkeitsgradfrage1 = 0;
    public static int schwierigkeitsgradfrage2 = 0;
    public static int schwierigkeitsgradfrage3 = 0;

    Button erfuellbar, nichterfuellbar, weiter;
    TextView Junktorenterm, AussagenTerm;

    ArrayList<Boolean> Booluebernehmer = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Landscape Modus wird deaktiviert
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Die activity_junktoren.xml wird als Layout verwendet
        setContentView(R.layout.activity_junktoren);

        //hier wird die Zeiterfassung fuer die txt vorbereitet
        Anfangszeit = null;
        Endzeit = null;
        Anfangszeit = Calendar.getInstance().getTime();



        //die Buttons werden zu den IDs aus dem xml zugewiesen
        erfuellbar = (Button) findViewById(R.id.button28);
        nichterfuellbar = (Button) findViewById(R.id.button29);
        weiter = (Button) findViewById(R.id.button30);

        //hier werden den Textviews den IDs aus dem xml zugewiesen
        Junktorenterm = findViewById(R.id.textView9);
        AussagenTerm = findViewById(R.id.textView10);

        //die Methode ist dafuer da, dass die Buttons nach dem Beantworten wieder Blau gefaerbt werden
        ButtonBlaumacher();

        fragenanzahl = 0;

        //Die Methode bekommt einen schwierigkeitsgrad hinein und erstellt dann einen Term der passend zum schwierigkeitsgrad ist
        schwierigkeit(schwierigkeitsgrad);

        //hier wird die Aufgabenstellung gesetzt
        Junktorenterm.setText("Ist die Aussage: \n" + Aussage + "\nin der unten beschriebenen Konstellation lösbar? ");

        //Der weiter button soll erst sichtbar werden, wenn die frage beantwortet wurde, somit wird er anfangs invisible gestellt
        weiter.setVisibility(View.INVISIBLE);

        /*
        der erfuellbar button wird gruen, sofern die antwort erfuellbar ist ansonsten rot - aequivalent zum nicht erfuellbar button
        nach dem betaetigen der erfuellbar oder nichterfuellbar buttons wird der weiter button sichtbar
        die punkte werden einzelnd fuer schwierigkeitsgrade einzelnd gezaehlt
        sofern die maximale anzahl an fragen erreicht ist, wird die zeit gespeichert, ebenso die punkte und wie viele fragen von welcher schwierigkeit
         */
        erfuellbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ergebnis.equals("true")) {
                    erfuellbar.setBackgroundColor(Color.GREEN);
                    nichterfuellbar.setBackgroundColor(Color.RED);
                    if (schwierigkeitsgrad == 0){
                        schwierigkeitsgradpunkte1++;
                    }else if (schwierigkeitsgrad == 1){
                        schwierigkeitsgradpunkte2++;
                    }else if (schwierigkeitsgrad == 2){
                        schwierigkeitsgradpunkte3++;
                    }
                    if (schwierigkeitsgrad < 2){
                        schwierigkeitsgrad++;
                    }
                } else {
                    erfuellbar.setBackgroundColor(Color.RED);
                    nichterfuellbar.setBackgroundColor(Color.GREEN);
                    if (schwierigkeitsgrad > 0){
                        schwierigkeitsgrad--;
                    }
                }

                weiter.setVisibility(View.VISIBLE);
            }
        });

        nichterfuellbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ergebnis.equals("true")) {
                    erfuellbar.setBackgroundColor(Color.GREEN);
                    nichterfuellbar.setBackgroundColor(Color.RED);
                    if (schwierigkeitsgrad > 0){
                        schwierigkeitsgrad--;
                    }
                } else {
                    erfuellbar.setBackgroundColor(Color.RED);
                    nichterfuellbar.setBackgroundColor(Color.GREEN);
                    if (schwierigkeitsgrad == 0){
                        schwierigkeitsgradpunkte1++;
                    }else if (schwierigkeitsgrad == 1){
                        schwierigkeitsgradpunkte2++;
                    }else if (schwierigkeitsgrad == 2){
                        schwierigkeitsgradpunkte3++;
                    }
                    if (schwierigkeitsgrad < 2){
                        schwierigkeitsgrad++;
                    }
                }
                weiter.setVisibility(View.VISIBLE);
            }
        });

        weiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fragenanzahl < 9) {
                    fragenanzahl++;
                    Booluebernehmer.clear();
                    AussagenTerm.setText("");
                    Junktorenterm.setText("");
                    Aussage = "";
                    ParserText = "";
                    erfuellbar.setBackgroundColor(Color.BLUE);
                    nichterfuellbar.setBackgroundColor(Color.BLUE);
                    schwierigkeit(schwierigkeitsgrad);
                 //   ueberpruefen();
                    Junktorenterm.setText("Ist die Aussage: \n" + Aussage + "\nin der unten beschriebenen Konstellation lösbar? ");
                   // AussagenTerm.setText("a = " + Booluebernehmer.get(0) + " b = " + Booluebernehmer.get(1) + " c = " + Booluebernehmer.get(2));
                    weiter.setVisibility(View.INVISIBLE);
                } else {
                    Endzeit = Calendar.getInstance().getTime();
                    speichern();
                    fragenanzahl = 0;
                    schwierigkeitsgrad = 0;
                    schwierigkeitsgradpunkte1 = 0;
                    schwierigkeitsgradpunkte2 = 0;
                    schwierigkeitsgradpunkte3 = 0;
                    schwierigkeitsgradfrage1 = 0;
                    schwierigkeitsgradfrage2 = 0;
                    schwierigkeitsgradfrage3 = 0;
                    activityWechsel();
                    finish();
                }

            }
        });


    }

    /*
    in dieser Methode wird ein Term mit 2 Variablen erstellt
    die junktoren werden als erstes so gewaehlt das es fuer die prueffunktion genutzt werden kann
    danach werden die Junktoren replaced in die Form wie es die Studenten kennen
     */
    public String zweierTerm() {

        ArrayList<String> LogikAussage = new ArrayList<String>();
        ArrayList<String> Junktoren = new ArrayList<String>();
        ArrayList<String> Negation = new ArrayList<String>();
        ArrayList<String> offeneKlammer = new ArrayList<String>();
        ArrayList<String> Variable = new ArrayList<String>();

        ParserText = "";

        LogikAussage.clear();
        Junktoren.clear();
        Negation.clear();
        Variable.clear();
        offeneKlammer.clear();

        Junktoren.add("=>");
        Junktoren.add("<=>");
        Junktoren.add("&");
        Junktoren.add("|");
        Negation.add("~");
        Negation.add("");
        offeneKlammer.add("(");
        offeneKlammer.add("");
        Variable.add("a");
        Variable.add("b");

        Collections.shuffle(Negation);
        Collections.shuffle(Variable);
        Collections.shuffle(Junktoren);

        LogikAussage.add(Negation.get(0));
        LogikAussage.add(Variable.get(0));
        LogikAussage.add(Junktoren.get(0));

        Collections.shuffle(Negation);

        LogikAussage.add(Negation.get(0));
        LogikAussage.add(Variable.get(1));

        for (int i = 0; i < LogikAussage.size(); i++) {
            ParserText += LogikAussage.get(i);
        }

        Aussage = ParserText;

        if (LogikAussage.contains("=>")) {
            Aussage = Aussage.replaceAll("=>", "->");
        }
        if (LogikAussage.contains("<=>")) {
            Aussage = Aussage.replaceAll("<=>", "<->");
        }
        if (LogikAussage.contains("&")) {
            Aussage = Aussage.replaceAll("&", "∧");
        }
        if (LogikAussage.contains("|")) {
            Aussage = Aussage.replaceAll("\\|", "∨");
        }
        if (LogikAussage.contains("~")) {
            Aussage = Aussage.replaceAll("~", "¬");
        }

        return Aussage;
    }


    /*
    in dieser Methode wird ein Term mit 3 Variablen erstellt
    die junktoren werden als erstes so gewaehlt das es fuer die prueffunktion genutzt werden kann
    danach werden die Junktoren replaced in die Form wie es die Studenten kennen
     */
    public void dreierTerm() {

        ParserText = "";

        ArrayList<String> LogikAussage = new ArrayList<String>();
        ArrayList<String> Junktoren = new ArrayList<String>();
        ArrayList<String> Negation = new ArrayList<String>();
        ArrayList<String> offeneKlammer = new ArrayList<String>();
        ArrayList<String> Variable = new ArrayList<String>();
        ArrayList<Integer> Klammergeber = new ArrayList<Integer>();

        LogikAussage.clear();
        Junktoren.clear();
        Negation.clear();
        Variable.clear();
        offeneKlammer.clear();
        Klammergeber.clear();

        Junktoren.add("=>");
        Junktoren.add("<=>");
        Junktoren.add("&");
        Junktoren.add("|");
        Negation.add("~");
        Negation.add("");
        offeneKlammer.add("(");
        offeneKlammer.add("");
        Variable.add("a");
        Variable.add("b");
        Variable.add("c");
        Klammergeber.add(0);
        Klammergeber.add(1);
        Klammergeber.add(2);

        Collections.shuffle(Junktoren);
        Collections.shuffle(Negation);
        Collections.shuffle(Variable);
        Collections.shuffle(offeneKlammer);
        Collections.shuffle(Klammergeber);


        Collections.shuffle(Negation);
        LogikAussage.add(Negation.get(0));          //0
        LogikAussage.add(Variable.get(0));          //1                     ~a|~b|~c
        Collections.shuffle(Junktoren);
        LogikAussage.add(Junktoren.get(0));          //2
        Collections.shuffle(Negation);
        LogikAussage.add(Negation.get(0));          //3
        LogikAussage.add(Variable.get(1));          //4
        Collections.shuffle(Junktoren);
        LogikAussage.add(Junktoren.get(0));          //5
        Collections.shuffle(Negation);
        LogikAussage.add(Negation.get(0));          //6
        LogikAussage.add(Variable.get(2));          //7

        if ((LogikAussage.get(2).equals("<=>") || LogikAussage.get(2).equals("=>")) && (LogikAussage.get(5).equals("<=>") || LogikAussage.get(5).equals("=>"))) {
            LogikAussage.add(3, "(");
            LogikAussage.add(")");
        } else if ((LogikAussage.get(2).equals("<=>") || LogikAussage.get(2).equals("=>"))) {
            LogikAussage.add(5, ")");
            LogikAussage.add(0, "(");
        } else if ((LogikAussage.get(5).equals("<=>") || LogikAussage.get(5).equals("=>"))) {
            LogikAussage.add(3, "(");
            LogikAussage.add(")");
        } else if (Klammergeber.get(0) == 0) {

        } else if (Klammergeber.get(0) == 1) {
            LogikAussage.add(")");
            LogikAussage.add(3, "(");
        } else if (Klammergeber.get(0) == 2) {
            LogikAussage.add(")");
            LogikAussage.add(5, "(");
        }


        for (int i = 0; i < LogikAussage.size(); i++) {
            ParserText += LogikAussage.get(i);
        }

        Aussage = ParserText;

        if (LogikAussage.contains("=>")) {
            Aussage = Aussage.replaceAll("=>", "->");
        }
        if (LogikAussage.contains("<=>")) {
            Aussage = Aussage.replaceAll("<=>", "<->");
        }
        if (LogikAussage.contains("&")) {
            Aussage = Aussage.replaceAll("&", "∧");
        }
        if (LogikAussage.contains("|")) {
            Aussage = Aussage.replaceAll("\\|", "∨");
        }
        if (LogikAussage.contains("~")) {
            Aussage = Aussage.replaceAll("~", "¬");
        }

    }

    /*
    in dieser Methode wird ein Term mit 4 Variablen erstellt
    die junktoren werden als erstes so gewaehlt das es fuer die prueffunktion genutzt werden kann
    danach werden die Junktoren replaced in die Form wie es die Studenten kennen
     */
    public void viererTerm() {

        ParserText = "";

        ArrayList<String> LogikAussage = new ArrayList<String>();
        ArrayList<String> Junktoren = new ArrayList<String>();
        ArrayList<String> Negation = new ArrayList<String>();
        ArrayList<String> offeneKlammer = new ArrayList<String>();
        ArrayList<String> Variable = new ArrayList<String>();
        ArrayList<String> GeschlosseneKlammer = new ArrayList<String>();

        LogikAussage.clear();
        Junktoren.clear();
        Negation.clear();
        Variable.clear();
        offeneKlammer.clear();
        GeschlosseneKlammer.clear();

        Junktoren.add("=>");
        Junktoren.add("<=>");
        Junktoren.add("&");
        Junktoren.add("|");
        Negation.add("~");
        Negation.add("");
        offeneKlammer.add("(");
        offeneKlammer.add("");
        Variable.add("a");
        Variable.add("b");
        Variable.add("c");
        Variable.add("d");
        GeschlosseneKlammer.add("");
        GeschlosseneKlammer.add(")");

        Collections.shuffle(Junktoren);
        Collections.shuffle(Negation);
        Collections.shuffle(Variable);
        Collections.shuffle(offeneKlammer);
        Collections.shuffle(GeschlosseneKlammer);

        LogikAussage.add(Negation.get(0));          //0
        LogikAussage.add(Variable.get(0));          //1
        Collections.shuffle(Junktoren);
        LogikAussage.add(Junktoren.get(0));         //2
        Collections.shuffle(Negation);
        LogikAussage.add(Negation.get(0));          //3
        LogikAussage.add(Variable.get(1));          //4
        Collections.shuffle(Junktoren);
        LogikAussage.add(Junktoren.get(0));         //5
        Collections.shuffle(Negation);
        LogikAussage.add(Negation.get(0));          //6
        LogikAussage.add(Variable.get(2));          //7
        Collections.shuffle(Junktoren);
        LogikAussage.add(Junktoren.get(0));         //8
        Collections.shuffle(Negation);
        LogikAussage.add(Negation.get(0));          //9
        LogikAussage.add(Variable.get(3));          //10

        if (LogikAussage.contains("<=>") || LogikAussage.contains("=>")) {

            if ((LogikAussage.get(8).equals("<=>") || LogikAussage.get(8).equals("=>")) && (LogikAussage.get(5).equals("<=>") || LogikAussage.get(5).equals("=>")) && (LogikAussage.get(2).equals("<=>") || LogikAussage.get(2).equals("=>"))) {
                LogikAussage.add(")");
                LogikAussage.add(")");
                LogikAussage.add(6, "(");
                LogikAussage.add(3, "(");
            } else if ((LogikAussage.get(8).equals("<=>") || LogikAussage.get(8).equals("=>")) && (LogikAussage.get(5).equals("<=>") || LogikAussage.get(5).equals("=>"))) {
                LogikAussage.add(")");
                LogikAussage.add(6, "(");
            } else if ((LogikAussage.get(2).equals("<=>") || LogikAussage.get(2).equals("=>")) && (LogikAussage.get(5).equals("<=>") || LogikAussage.get(5).equals("=>"))) {
                LogikAussage.add(")");
                LogikAussage.add(3, "(");
            } else if ((LogikAussage.get(2).equals("<=>") || LogikAussage.get(2).equals("=>")) && (LogikAussage.get(8).equals("<=>") || LogikAussage.get(8).equals("=>"))) {
                LogikAussage.add(5, ")");
                LogikAussage.add(0, "(");
            }
        }


        for (int i = 0; i < LogikAussage.size(); i++) {
            ParserText += LogikAussage.get(i);
        }

        Aussage = ParserText;

        if (LogikAussage.contains("=>")) {
            Aussage = Aussage.replaceAll("=>", "->");
        }
        if (LogikAussage.contains("<=>")) {
            Aussage = Aussage.replaceAll("<=>", "<->");
        }
        if (LogikAussage.contains("&")) {
            Aussage = Aussage.replaceAll("&", "∧");
        }
        if (LogikAussage.contains("|")) {
            Aussage = Aussage.replaceAll("\\|", "∨");
        }
        if (LogikAussage.contains("~")) {
            Aussage = Aussage.replaceAll("~", "¬");
        }

    }

    /*
    in dieser Methode ist eine Industrie Bibliothek eingearbeitet
    die Methode ueberprueft ob der gegebene Term aus der Aufgabenstellung erfuellbar ist oder nicht
     */

    public void ueberpruefen() {

        ArrayList<Boolean> Randombooler = new ArrayList<Boolean>();
        Randombooler.clear();

        Randombooler.add(true);
        Randombooler.add(false);

        try {
            final FormulaFactory f = new FormulaFactory();
            final PropositionalParser p = new PropositionalParser(f);
            final Formula formula = p.parse(ParserText);
            final DNFFactorization dnfFactorization = new DNFFactorization();
            Formula DNFFORM = dnfFactorization.apply(formula, true);
            String DNFinSTRING = DNFFORM.toString();
            if (DNFinSTRING.contains("~")) {
                DNFinSTRING = DNFinSTRING.replaceAll("~", "!");
            }
            if (DNFinSTRING.contains("|")) {
                DNFinSTRING = DNFinSTRING.replaceAll("\\|", "||");
            }
            if (DNFinSTRING.contains("&")) {
                DNFinSTRING = DNFinSTRING.replaceAll("&", "&&");
            }
            if (DNFinSTRING.contains("a")) {
                Collections.shuffle(Randombooler);
                Booluebernehmer.add(Randombooler.get(0));
                DNFinSTRING = DNFinSTRING.replaceAll("a", Randombooler.get(0).toString());
            }
            if (DNFinSTRING.contains("b")) {
                Collections.shuffle(Randombooler);
                Booluebernehmer.add(Randombooler.get(0));
                DNFinSTRING = DNFinSTRING.replaceAll("b", Randombooler.get(0).toString());
            }
            if (DNFinSTRING.contains("c")) {
                Collections.shuffle(Randombooler);
                Booluebernehmer.add(Randombooler.get(0));
                DNFinSTRING = DNFinSTRING.replaceAll("c", Randombooler.get(0).toString());
            }
            if (DNFinSTRING.contains("d")) {
                Collections.shuffle(Randombooler);
                Booluebernehmer.add(Randombooler.get(0));
                DNFinSTRING = DNFinSTRING.replaceAll("d", Randombooler.get(0).toString());
            }

            String finalParser = DNFinSTRING;
            ergebnis = MVEL.evalToString(finalParser);

        } catch (ParserException e) {
            e.printStackTrace();
        }

    }

    //nachdem die Maximale Fragen erreicht sind bzw. ein Student auf beenden gedrückt hat wechselt er mit dieser Methode wieder in die Main beziehungsweise in den Anfangsscreen
    private void activityWechsel() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    //hier wird ein Dropdownmenue erstellt
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
                    fragenanzahl = 0;
                    schwierigkeitsgrad = 0;
                    schwierigkeitsgradpunkte1 = 0;
                    schwierigkeitsgradpunkte2 = 0;
                    schwierigkeitsgradpunkte3 = 0;
                    schwierigkeitsgradfrage1 = 0;
                    schwierigkeitsgradfrage2 = 0;
                    schwierigkeitsgradfrage3 = 0;
                    activityWechsel();
                    finish();
                    break;

            }
        } catch (Exception e) {

        }
        return super.onOptionsItemSelected(item);
    }

    //macht die Buttons blau, nachdem sie durch das beantworten anders gefaerbt wurden
    public void ButtonBlaumacher() {

        erfuellbar.setBackgroundColor(Color.BLUE);
        nichterfuellbar.setBackgroundColor(Color.BLUE);
        weiter.setBackgroundColor(Color.BLUE);


    }

    //Die Methoden speichern() und laden() sind übernommen von https://gist.github.com/codinginflow/6c13bd0d08416115798f17d45b5d8056
    //Diese Methode speichert Dinge wie User, Activity, Anfangs und Endzeit sowie Punkte in eine txt Datei
    public void speichern() {
        laden();

        String text = annehmer + "UserID|" + Benutzername + ";Activity|Junktoren;Anfangszeit|" + Anfangszeit + ";Endzeit|" + Endzeit + ";PunkteS1|" + schwierigkeitsgradpunkte1 + " von" + schwierigkeitsgradfrage1 + ";PunkteS2|" + schwierigkeitsgradpunkte2 + " von" + schwierigkeitsgradfrage2 + ";PunkteS3|" + schwierigkeitsgradpunkte3 + " von" + schwierigkeitsgradfrage3 + ";\n";

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

    //die Methode laedt die txt Datei damit diese wieder hinzugefuegt wird um beim speichern in die txt alles davor nicht ueberschrieben wird
    public void laden(){
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

    //hier wird der Term passend zur Schwierigkeit erstellt
    public void schwierigkeit (int schwierigkeitsgrad){
        switch (schwierigkeitsgrad){
            case 0:
                zweierTerm();
                schwierigkeitsgradfrage1++;
                ueberpruefen();
                AussagenTerm.setText("a = " + Booluebernehmer.get(0) + " b = " + Booluebernehmer.get(1));
                break;
            case 1:
                dreierTerm();
                schwierigkeitsgradfrage2++;
                ueberpruefen();
                AussagenTerm.setText("a = " + Booluebernehmer.get(0) + " b = " + Booluebernehmer.get(1)  + " c = " + Booluebernehmer.get(2));
                break;
            case 2:
                viererTerm();
                schwierigkeitsgradfrage3++;
                ueberpruefen();
                AussagenTerm.setText("a = " + Booluebernehmer.get(0) + " b = " + Booluebernehmer.get(1)  + " c = " + Booluebernehmer.get(2)  + " d = " + Booluebernehmer.get(3));
                break;
        }
    }

}

