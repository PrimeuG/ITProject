package com.example.itproject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.kyanogen.signatureview.SignatureView;

import org.logicng.datastructures.Tristate;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.io.parsers.ParserException;
import org.logicng.io.parsers.PropositionalParser;
import org.logicng.solvers.MiniSat;
import org.logicng.solvers.SATSolver;

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

public class Klauselmenge extends AppCompatActivity implements View.OnClickListener {

    Button erfuellbar, nichtErfuellbar, elf;
    TextView text;


    int fragenanzahl = 0;

    SignatureView signatureView;

    ArrayList<String> Endklausel = new ArrayList<>();
    ArrayList<String> ParserKlausel = new ArrayList<>();
    ArrayList<Integer> Klauselwaehler = new ArrayList<>();
    ArrayList<String> EinzelKlausel = new ArrayList<>();
    ArrayList<String> Mehrfachklausel = new ArrayList<>();
    ArrayList<String> Einzel4Klausel = new ArrayList<>();
    ArrayList<String> Mehrfach4klausel = new ArrayList<>();

    private static final String FILE_NAME = "Auswertung.txt";

    private static String Benutzername = "Benutzername";
    private static String annehmer = "";


    public static boolean Beantwortet = false;

    public static String ParserText = "";
    public static String Klauselmenge = "";
    public static String ParserTexte = "";

    public static int punkte = 0;
    public static int maxfragen = 10;

    public static int schwierigkeit = 5;
    public static int schwierigkeitspunkte1 = 0;
    public static int schwierigkeitspunkte2 = 0;
    public static int schwierigkeitspunkte3 = 0;
    public static int schwierigkeitsfrage1 = 0;
    public static int schwierigkeitsfrage2 = 0;
    public static int schwierigkeitsfrage3 = 0;

    public static Date Anfangszeit;
    public static Date Endzeit;

    int EinzelklauselStopper = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Landscape Modus ist deaktiviert.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //hier wird die Klauselmenge xml geladen
        setContentView(R.layout.activity_klauselmenge);

        //dies ist das Paint aehnliche Feld, die ID aus der xml wird zugewiesen
        signatureView = findViewById(R.id.signature_view);

        //hier wird die Zeiterfassung fuer die txt vorbereitet
        Anfangszeit = null;
        Endzeit = null;
        Anfangszeit = Calendar.getInstance().getTime();

        punkte = 0;

        //Textview wird aus xml eine ID zugeordnet
        text = findViewById(R.id.Text);

        //den Buttons wird eine ID aus dem xml zugeordnet
        erfuellbar = (Button) findViewById(R.id.button6);
        nichtErfuellbar = (Button) findViewById(R.id.button7);
        elf = (Button) findViewById(R.id.button11);

        //Methode um die Buttons nach dem Beantworten (gefaerbt durch beantworten) wieder blau gefaerbt
        ButtonBlaumacher();

        //Arraylisten werden gecleart wenn jemand die activity nacheinander uebt, dann haben die Listen nicht mehrmals das gleiche drin
        Klauselwaehler.clear();
        EinzelKlausel.clear();
        Mehrfachklausel.clear();
        Einzel4Klausel.clear();
        Mehrfach4klausel.clear();

        //Klauselwaehler wird genutzt um zu Entscheiden ob Einzelklauseln oder Mehrfachklauseln genutzt werden
        Klauselwaehler.add(0);
        Klauselwaehler.add(1);

        //Klauseln mit einer Variabel
        EinzelKlausel.add("{a}");
        EinzelKlausel.add("{??a}");
        EinzelKlausel.add("{b}");
        EinzelKlausel.add("{??b}");
        EinzelKlausel.add("{c}");
        EinzelKlausel.add("{??c}");

        Einzel4Klausel.add("{a}");
        Einzel4Klausel.add("{??a}");
        Einzel4Klausel.add("{b}");
        Einzel4Klausel.add("{??b}");
        Einzel4Klausel.add("{c}");
        Einzel4Klausel.add("{??c}");
        Einzel4Klausel.add("{d}");
        Einzel4Klausel.add("{??d}");

        //Alle Klauseln die (ohne Tauschen z.B. {a,b}{b,a}) moeglich sind
        Mehrfachklausel.add("{a,b}");
        Mehrfachklausel.add("{a,??b}");
        Mehrfachklausel.add("{??a,b}");
        Mehrfachklausel.add("{??a,??b}");
        Mehrfachklausel.add("{a,c}");
        Mehrfachklausel.add("{a,??c}");
        Mehrfachklausel.add("{??a,c}");
        Mehrfachklausel.add("{??a,??c}");
        Mehrfachklausel.add("{b,c}");
        Mehrfachklausel.add("{b,??c}");
        Mehrfachklausel.add("{??b,c}");
        Mehrfachklausel.add("{??b,??c}");

        Mehrfachklausel.add("{a,b,c}");
        Mehrfachklausel.add("{a,b,??c}");
        Mehrfachklausel.add("{a,??b,c}");
        Mehrfachklausel.add("{a,??b,??c}");
        Mehrfachklausel.add("{??a,b,c}");
        Mehrfachklausel.add("{??a,b,??c}");
        Mehrfachklausel.add("{??a,??b,c}");
        Mehrfachklausel.add("{??a,??b,??c}");


        //Alle Klauseln die (ohne Tauschen z.B. {a,b}{b,a}) moeglich sind
        Mehrfach4klausel.add("{a,b}");
        Mehrfach4klausel.add("{a,??b}");
        Mehrfach4klausel.add("{??a,b}");
        Mehrfach4klausel.add("{??a,??b}");
        Mehrfach4klausel.add("{a,c}");
        Mehrfach4klausel.add("{a,??c}");
        Mehrfach4klausel.add("{??a,c}");
        Mehrfach4klausel.add("{??a,??c}");
        Mehrfach4klausel.add("{b,c}");
        Mehrfach4klausel.add("{b,??c}");
        Mehrfach4klausel.add("{??b,c}");
        Mehrfach4klausel.add("{??b,??c}");
        Mehrfach4klausel.add("{a,d}");
        Mehrfach4klausel.add("{??a,d}");
        Mehrfach4klausel.add("{a,??d}");
        Mehrfach4klausel.add("{??a,??d}");
        Mehrfach4klausel.add("{??a,??d}");
        Mehrfach4klausel.add("{b,d}");
        Mehrfach4klausel.add("{??b,d}");
        Mehrfach4klausel.add("{b,??d}");
        Mehrfach4klausel.add("{??b,??d}");
        Mehrfach4klausel.add("{c,d}");
        Mehrfach4klausel.add("{??c,d}");
        Mehrfach4klausel.add("{c,??d}");
        Mehrfach4klausel.add("{??c,??d}");


        Mehrfach4klausel.add("{a,b,c}");
        Mehrfach4klausel.add("{a,b,??c}");
        Mehrfach4klausel.add("{a,??b,c}");
        Mehrfach4klausel.add("{a,??b,??c}");
        Mehrfach4klausel.add("{??a,b,c}");
        Mehrfach4klausel.add("{??a,b,??c}");
        Mehrfach4klausel.add("{??a,??b,c}");
        Mehrfach4klausel.add("{??a,??b,??c}");
        Mehrfach4klausel.add("{a,b,d}");
        Mehrfach4klausel.add("{a,b,??d}");
        Mehrfach4klausel.add("{a,??b,d}");
        Mehrfach4klausel.add("{a,??b,??d}");
        Mehrfach4klausel.add("{??a,b,d}");
        Mehrfach4klausel.add("{??a,b,??d}");
        Mehrfach4klausel.add("{??a,??b,d}");
        Mehrfach4klausel.add("{??a,??b,??d}");
        Mehrfach4klausel.add("{a,c,d}");
        Mehrfach4klausel.add("{a,c,??d}");
        Mehrfach4klausel.add("{a,??c,d}");
        Mehrfach4klausel.add("{a,??c,??d}");
        Mehrfach4klausel.add("{??a,c,d}");
        Mehrfach4klausel.add("{??a,c,??d}");
        Mehrfach4klausel.add("{??a,??c,d}");
        Mehrfach4klausel.add("{??a,??c,??d}");
        Mehrfach4klausel.add("{b,c,d}");
        Mehrfach4klausel.add("{b,c,??d}");
        Mehrfach4klausel.add("{b,??c,d}");
        Mehrfach4klausel.add("{b,??c,??d}");
        Mehrfach4klausel.add("{??b,c,d}");
        Mehrfach4klausel.add("{??b,c,??d}");
        Mehrfach4klausel.add("{??b,??c,d}");
        Mehrfach4klausel.add("{??b,??c,??d}");

        Mehrfach4klausel.add("{a,b,c,d}");
        Mehrfach4klausel.add("{a,b,c,??d}");
        Mehrfach4klausel.add("{a,b,??c,d}");
        Mehrfach4klausel.add("{a,b,??c,??d}");
        Mehrfach4klausel.add("{a,??b,c,d}");
        Mehrfach4klausel.add("{a,??b,c,??d}");
        Mehrfach4klausel.add("{a,??b,??c,d}");
        Mehrfach4klausel.add("{a,??b,??c,??d}");
        Mehrfach4klausel.add("{??a,b,c,d}");
        Mehrfach4klausel.add("{??a,b,c,??d}");
        Mehrfach4klausel.add("{??a,b,??c,d}");
        Mehrfach4klausel.add("{??a,b,??c,??d}");


        //Buttons werden Blau gefaerbt
        erfuellbar.setBackgroundColor(Color.BLUE);
        nichtErfuellbar.setBackgroundColor(Color.BLUE);
        elf.setBackgroundColor(Color.BLUE);

        //elf bzw. weiter wird unsichtbar gemacht, da er erst gebraucht wird wenn man die erfuellbar oder nichterfuellbar betaetigt hat
        elf.setVisibility(View.INVISIBLE);

        //OnClickListener fuer die Buttons erstellt
        erfuellbar.setOnClickListener(this);
        nichtErfuellbar.setOnClickListener(this);
        elf.setOnClickListener(this);

        Beantwortet = false;

        //die Methode erstellt eine Klausel
        Klauselerstellen();

        //die Methode stellt die Aufgabenstellung mit der Klauselmenge
        textersteller();

        //die Methode ueberprueft ob die gegebene Klauselmenge erfuellbar oder nicht erfuellbar ist
        try {
            Logikueberpruefer(ParserTexte);
        } catch (ParserException e) {
            e.printStackTrace();
        }


    }

    public void onClick(View v) {


        switch (v.getId()) {

            /*
            wenn die Klauselmenge erfuellbar ist und man den erfuellbarbutton bestaetigt wird dieser gruen ansonsten rot
            ebenso wird der nichterfuellbar button rot, sofern die Klauselmenge erfuellbar ist ansonsten wird er gruen
            wenn der richtige Button betaetigt wird, so wird die schwierigkeit + 2 gerechnet
            die schwierigkeit entspricht die anzahl der einzelnen Klauseln der Klauselmenge
            die punkte werden entsprechend dem schwierigkeitsgrad zugeordnet
            beim beantworten wird der weiter Button sichtbar gemacht
             */
            case R.id.button6:
                try {
                    if (Logikueberpruefer(ParserTexte).equals("TRUE")) {

                        erfuellbar.setBackgroundColor(Color.GREEN);
                        nichtErfuellbar.setBackgroundColor(Color.RED);
                        punkte++;

                        if (schwierigkeit == 5) {
                            schwierigkeitspunkte1++;
                            schwierigkeitsfrage1++;
                        } else if (schwierigkeit == 7) {
                            schwierigkeitspunkte2++;
                            schwierigkeitsfrage2++;
                        } else if (schwierigkeit == 9) {
                            schwierigkeitspunkte3++;
                            schwierigkeitsfrage3++;
                        }

                        if (schwierigkeit < 9) {
                            schwierigkeit += 2;
                        }

                    } else {
                        erfuellbar.setBackgroundColor(Color.RED);
                        nichtErfuellbar.setBackgroundColor(Color.GREEN);

                        if (schwierigkeit == 5) {
                            schwierigkeitsfrage1++;
                        } else if (schwierigkeit == 7) {
                            schwierigkeitsfrage2++;
                        } else if (schwierigkeit == 9) {
                            schwierigkeitsfrage3++;
                        }

                        if (schwierigkeit > 5) {
                            schwierigkeit -= 2;
                        }
                    }
                } catch (ParserException e) {
                    e.printStackTrace();
                }

                elf.setVisibility(View.VISIBLE);
                break;

            case R.id.button7:
                try {
                    if (Logikueberpruefer(ParserTexte).equals("TRUE")) {

                        erfuellbar.setBackgroundColor(Color.GREEN);
                        nichtErfuellbar.setBackgroundColor(Color.RED);

                        if (schwierigkeit == 5) {
                            schwierigkeitsfrage1++;
                        } else if (schwierigkeit == 7) {
                            schwierigkeitsfrage2++;
                        } else if (schwierigkeit == 9) {
                            schwierigkeitsfrage3++;
                        }

                        if (schwierigkeit > 5) {
                            schwierigkeit -= 2;
                        }
                    } else {
                        erfuellbar.setBackgroundColor(Color.RED);
                        nichtErfuellbar.setBackgroundColor(Color.GREEN);
                        punkte++;

                        if (schwierigkeit == 5) {
                            schwierigkeitspunkte1++;
                            schwierigkeitsfrage1++;
                        } else if (schwierigkeit == 7) {
                            schwierigkeitspunkte2++;
                            schwierigkeitsfrage2++;
                        } else if (schwierigkeit == 9) {
                            schwierigkeitspunkte3++;
                            schwierigkeitsfrage3++;
                        }

                        if (schwierigkeit < 9) {
                            schwierigkeit += 2;
                        }
                    }
                } catch (ParserException e) {
                    e.printStackTrace();
                }
                elf.setVisibility(View.VISIBLE);
                break;

                /*
                sofern der weiter Button betaetigt wird und die maxfragen noch nicht erreicht sind, dann wird der weiter button wieder unsichtbar gemacht
                eine neue Klausel wird erstellt und die Endzeit wird genommen
                die SignatureView wird gecleart
                 */
            case R.id.button11:
                try {
                    fragenanzahl++;
                    if (fragenanzahl < maxfragen) {
                        ParserTexte = "";
                        Endzeit = Calendar.getInstance().getTime();
                        Klauselerstellen();
                        textersteller();
                        signatureView.clearCanvas();
                        signatureView.setPenColor(Color.BLACK);
                        signatureView.setPenSize(10);
                        Buttonzuruecksteller();
                        elf.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception e) {
                }
                break;
        }

        /*
        sofern die Maxfragen erreicht worden sind wird die endzeit gespeichert, sowie punkte, aktivity, anzahl an fragen
        danach wird alles zurueck gesetzt damit man bei einer weiteren nutzung des Themengebietes wieder neue Klauseln erstellt und die Punkte nicht weiter gezaehlt werden
         */
        if (fragenanzahl >= maxfragen) {
            fragenanzahl = 0;
            schwierigkeit = 5;
            Endzeit = Calendar.getInstance().getTime();
            speichern();
            signatureView.clearCanvas();
            schwierigkeitspunkte1 = 0;
            schwierigkeitspunkte2 = 0;
            schwierigkeitspunkte3 = 0;
            schwierigkeitsfrage1 = 0;
            schwierigkeitsfrage2 = 0;
            schwierigkeitsfrage3 = 0;
            signatureView.setPenColor(Color.BLACK);
            signatureView.setPenSize(10);
            activityWechsel();
            finish();
        }


    }

    //dies ist eine Methode aus einer Industrie Bibliothek und ueberprueft ob die erstellte Klauselmenge loesbar ist
    public String Logikueberpruefer(String Parsertext) throws ParserException {
        final FormulaFactory f = new FormulaFactory();
        final PropositionalParser p = new PropositionalParser(f);
        final Formula formula = p.parse(Parsertext);
        final Formula nnf = formula.nnf();
        final Formula cnf = formula.cnf();
        final SATSolver miniSat = MiniSat.miniSat(f);
        miniSat.add(formula);
        final Tristate result = miniSat.sat();

        return result.toString();
    }

    /*
    in dieser Methode werden die Klauselmengen erstellt
    hier wird dabei unterschieden zwischen einer Klauselmenge mit den Zeichen die die User aus den Theoretisch Informatik Vorlesung kennengelernt haben
    sowie eine Klauselmenge die fuer die Pruefmethode ist, denn diese kann bestimmte zeichen nicht arbeiten
     */
    public void Klauselerstellen() {
        Endklausel.clear();
        ParserKlausel.clear();

        if (schwierigkeit == 5 || schwierigkeit == 7) {
            for (int i = 0; i < schwierigkeit; i++) {

                String ueberpruefer;
                boolean kommageber = false;
                String uebernehmer = "";

                Collections.shuffle(Klauselwaehler);

                if (Klauselwaehler.get(0) == 0 && EinzelklauselStopper == 0) {
                    Collections.shuffle(EinzelKlausel);
                    while (Endklausel.contains(Mehrfachklausel.get(0))) {
                        Collections.shuffle(Mehrfachklausel);
                    }
                    Endklausel.add(EinzelKlausel.get(0));
                    ueberpruefer = EinzelKlausel.get(0);
                    for (int x = 0; x < ueberpruefer.length(); x++) {
                        if (ueberpruefer.charAt(x) == 123) {
                            uebernehmer = "(";
                        } else if (ueberpruefer.charAt(x) == 125) {
                            uebernehmer += ")";
                        } else if (ueberpruefer.charAt(x) == 172) {
                            uebernehmer += "~";
                        } else if (ueberpruefer.charAt(x) != 123) {
                            uebernehmer += String.valueOf(ueberpruefer.charAt(x));
                        }
                    }
                    if (uebernehmer.contains("??")) {
                        uebernehmer = uebernehmer.replaceAll("??", "~");
                    }
                    ParserKlausel.add(uebernehmer);
                    EinzelklauselStopper++;
                    kommageber = true;
                } else {

                    Collections.shuffle(Mehrfachklausel);
                    while (Endklausel.contains(Mehrfachklausel.get(0))) {
                        Collections.shuffle(Mehrfachklausel);
                    }
                    if (Endklausel.contains(Mehrfachklausel.get(0))) {
                        i--;
                        kommageber = false;
                    } else {
                        Endklausel.add(Mehrfachklausel.get(0));
                        ueberpruefer = Mehrfachklausel.get(0);
                        for (int x = 0; x < ueberpruefer.length(); x++) {
                            if (ueberpruefer.charAt(x) == 123) {
                                uebernehmer = "(";
                            } else if (ueberpruefer.charAt(x) == 125) {
                                uebernehmer += ")";
                            } else if (ueberpruefer.charAt(x) == 172) {
                                uebernehmer += "~";
                            } else if (ueberpruefer.charAt(x) != 123) {
                                uebernehmer += String.valueOf(ueberpruefer.charAt(x));
                            }
                        }

                        //uebernehmer = uebernehmer.replaceAll("}",")");
                        if (uebernehmer.contains("??")) {
                            uebernehmer = uebernehmer.replaceAll("??", "~");
                        } else if (uebernehmer.contains(",")) {
                            uebernehmer = uebernehmer.replaceAll(",", "|");
                        }

                        ParserKlausel.add(uebernehmer);
                        if (i < schwierigkeit - 1) {
                            kommageber = true;
                        } else {
                            kommageber = false;
                        }
                    }

                }

                if (kommageber) {
                    Endklausel.add(",");
                    ParserKlausel.add("&");
                }
            }
        } else if (schwierigkeit == 9) {
            for (int i = 0; i < 6; i++) {

                String ueberpruefer;
                boolean kommageber = false;
                String uebernehmer = "";

                Collections.shuffle(Klauselwaehler);

                if (Klauselwaehler.get(0) == 0 && EinzelklauselStopper == 0) {
                    Collections.shuffle(Einzel4Klausel);
                    while (Endklausel.contains(Mehrfach4klausel.get(0))) {
                        Collections.shuffle(Mehrfach4klausel);
                    }
                    Endklausel.add(Einzel4Klausel.get(0));
                    ueberpruefer = Einzel4Klausel.get(0);
                    for (int x = 0; x < ueberpruefer.length(); x++) {
                        if (ueberpruefer.charAt(x) == 123) {
                            uebernehmer = "(";
                        } else if (ueberpruefer.charAt(x) == 125) {
                            uebernehmer += ")";
                        } else if (ueberpruefer.charAt(x) == 172) {
                            uebernehmer += "~";
                        } else if (ueberpruefer.charAt(x) != 123) {
                            uebernehmer += String.valueOf(ueberpruefer.charAt(x));
                        }
                    }
                    if (uebernehmer.contains("??")) {
                        uebernehmer = uebernehmer.replaceAll("??", "~");
                    }
                    ParserKlausel.add(uebernehmer);
                    EinzelklauselStopper++;
                    kommageber = true;
                } else {

                    Collections.shuffle(Mehrfach4klausel);
                    while (Endklausel.contains(Mehrfach4klausel.get(0))) {
                        Collections.shuffle(Mehrfach4klausel);
                    }
                    if (Endklausel.contains(Mehrfach4klausel.get(0))) {
                        i--;
                        kommageber = false;
                    } else {
                        Endklausel.add(Mehrfach4klausel.get(0));
                        ueberpruefer = Mehrfach4klausel.get(0);
                        for (int x = 0; x < ueberpruefer.length(); x++) {
                            if (ueberpruefer.charAt(x) == 123) {
                                uebernehmer = "(";
                            } else if (ueberpruefer.charAt(x) == 125) {
                                uebernehmer += ")";
                            } else if (ueberpruefer.charAt(x) == 172) {
                                uebernehmer += "~";
                            } else if (ueberpruefer.charAt(x) != 123) {
                                uebernehmer += String.valueOf(ueberpruefer.charAt(x));
                            }
                        }

                        //uebernehmer = uebernehmer.replaceAll("}",")");
                        if (uebernehmer.contains("??")) {
                            uebernehmer = uebernehmer.replaceAll("??", "~");
                        } else if (uebernehmer.contains(",")) {
                            uebernehmer = uebernehmer.replaceAll(",", "|");
                        }

                        ParserKlausel.add(uebernehmer);
                        if (i < 6 - 1) {
                            kommageber = true;
                        } else {
                            kommageber = false;
                        }
                    }

                }

                if (kommageber) {
                    Endklausel.add(",");
                    ParserKlausel.add("&");
                }
            }
        }


        String Klauselmengen = "";

        for (int y = 0; y < ParserKlausel.size(); y++) {
            ParserText += ParserKlausel.get(y);
            Klauselmengen += Endklausel.get(y);

        }
        Klauselmenge = Klauselmengen;
        ParserTexte = ParserText;
        Klauselmengen = "";
        ParserText = "";
        EinzelklauselStopper = 0;
    }

    //die Methode erstellt die Aufgabenstellung und setzt diese im textview
    public void textersteller() {

        text.setText("Ist diese Klauselmenge erf??llbar?\n{" + Klauselmenge+"}");

    }

    //diese Methode macht die Buttons wieder Blau
    public void Buttonzuruecksteller() {


        erfuellbar.setBackgroundColor(Color.BLUE);
        nichtErfuellbar.setBackgroundColor(Color.BLUE);

    }

    //nach dem Beenden bzw. durch das erreichen der Maxfragen gelangt man mit dieser Methode wieder in d
    private void activityWechsel() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.spiel_menu3, menu);
        return true;
    }

    //F??r Neustart und Endscreen, es werden verschiedene Dinge resettet
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.cancel:
                    fragenanzahl = 0;
                    schwierigkeit = 5;
                    schwierigkeitspunkte1 = 0;
                    schwierigkeitspunkte2 = 0;
                    schwierigkeitspunkte3 = 0;
                    schwierigkeitsfrage1 = 0;
                    schwierigkeitsfrage2 = 0;
                    schwierigkeitsfrage3 = 0;
                    signatureView.setPenColor(Color.BLACK);
                    signatureView.setPenSize(10);
                    activityWechsel();
                    finish();
                    break;
                case R.id.reset:
                    signatureView.clearCanvas();
                    signatureView.setPenColor(Color.BLACK);
                    signatureView.setPenSize(10);
                    break;
                case R.id.eraser:
                    signatureView.setPenColor(Color.WHITE);
                    signatureView.setPenSize(50);
                    break;
                case R.id.pen:
                    signatureView.setPenColor(Color.BLACK);
                    signatureView.setPenSize(10);
                    break;
            }
        } catch (Exception e) {

        }
        return super.onOptionsItemSelected(item);
    }

    public void ButtonBlaumacher() {
        erfuellbar.setBackgroundColor(Color.BLUE);
        nichtErfuellbar.setBackgroundColor(Color.BLUE);
        elf.setBackgroundColor(Color.BLUE);
    }

    //Die Methoden speichern() und laden() sind ??bernommen von https://gist.github.com/codinginflow/6c13bd0d08416115798f17d45b5d8056

    public void speichern() {
        laden();

        String text = annehmer + "UserID|" + Benutzername + ";Activity|Klauselmengen;Anfangszeit|" + Anfangszeit + ";Endzeit|" + Endzeit + ";PunkteS1|" + schwierigkeitspunkte1 + " von" + schwierigkeitsfrage1 + ";PunkteS2|" + schwierigkeitspunkte2 + " von" + schwierigkeitsfrage2 + ";PunkteS3|" + schwierigkeitspunkte3 + " von" + schwierigkeitsfrage3 + ";\n";

        FileOutputStream fos = null;

        try {
            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);
            fos.write(text.getBytes());

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

    //Die Methode ist dafuer da, dass bei der Methode speichern nicht die txt ueberschrieben wird, denn sie wird vorher geladen und dann bei der speicher methode mit uebernommen
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
