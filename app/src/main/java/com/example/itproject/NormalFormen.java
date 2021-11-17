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

import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.io.parsers.ParserException;
import org.logicng.io.parsers.PropositionalParser;
import org.logicng.solvers.MiniSat;
import org.logicng.solvers.SATSolver;
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

public class NormalFormen extends AppCompatActivity {

    private static final String FILE_NAME = "Auswertung.txt";

    private static String Benutzername = "Benutzername";
    private static String annehmer = "";

    public static String Aussage;
    public static String ParserText = "";
    public static String Form = "";
    public static String test = "";
    public static int fragenanzahl = 0;
    public static String texti = "";

    ArrayList<Boolean> DNFKNFGenerator = new ArrayList<>();
    ArrayList<String> pruefListeErgebnis = new ArrayList<>();

    public static Date Anfangszeit;
    public static Date Endzeit;

    public static int punkte = 0;


    public static int schwierigkeitsgrad;
    public TextView text;
    public TextView unterertext;


    Button klammerauf, klammerzu, d, weiter, a, b, c, loeschen, bestaetigen, und, oder, nicht, Buttonnochmal, ButtonLoesung;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_normal_formen);

        Anfangszeit = null;
        Endzeit = null;
        punkte = 0;

        Anfangszeit = Calendar.getInstance().getTime();

        TextView text = findViewById(R.id.textView7);
        TextView unterertext = findViewById(R.id.textView8);


        klammerauf = findViewById(R.id.klammerauf);
        klammerzu = findViewById(R.id.klammerzu);
        a = findViewById(R.id.A);
        b = findViewById(R.id.B);
        c = findViewById(R.id.C);
        d = findViewById(R.id.D);
        loeschen = findViewById(R.id.delete);
        bestaetigen = findViewById(R.id.bestätigen);
        und = findViewById(R.id.und);
        oder = findViewById(R.id.oder);
        nicht = findViewById(R.id.nicht);
        weiter = findViewById(R.id.weiter);
        Buttonnochmal = findViewById(R.id.Nochmal);
        ButtonLoesung = findViewById(R.id.Loesung);
        test = "";


        ButtonBlaumacher();

        schwierigkeitsgrad = 0;
        fragenanzahl = 0;
        weiter.setVisibility(View.INVISIBLE);
        ButtonLoesung.setVisibility(View.INVISIBLE);
        Buttonnochmal.setVisibility(View.INVISIBLE);

        schwierigkeit(schwierigkeitsgrad);
        Modus();

        if (DNFKNFGenerator.get(0)) {
            text.setText("Geben Sie für folgenden Term die DNF an! \n" + Aussage.toString());
        } else {
            text.setText("Geben Sie für folgenden Term die KNF an! \n" + Aussage.toString());
        }


        a.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unterertext.setText(unterertext.getText() + "a");
            }
        });

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unterertext.setText(unterertext.getText() + "b");
            }
        });

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unterertext.setText(unterertext.getText() + "c");
            }
        });

        d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unterertext.setText(unterertext.getText() + "d");
            }
        });

        nicht.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unterertext.setText(unterertext.getText() + "¬");
            }
        });

        klammerauf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unterertext.setText(unterertext.getText() + "(");
            }
        });

        klammerzu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unterertext.setText(unterertext.getText() + ")");
            }
        });

        oder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unterertext.setText(unterertext.getText() + "∨");
            }
        });

        und.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unterertext.setText(unterertext.getText() + "∧");
            }
        });

        loeschen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    unterertext.setText(unterertext.getText().toString().substring(0, unterertext.getText().length() - 1));
                } catch (Exception e) {

                }
            }
        });

        bestaetigen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                texti = "";
                texti = unterertext.getText().toString();


                ueberpruefer(schwierigkeitsgrad);

                if (schwierigkeitsgrad == 0) {
                    if (pruefListeErgebnis.contains("false")) {
                        schwierigkeitsgrad = 0;
                        weiter.setBackgroundColor(Color.RED);
                        Buttonnochmal.setVisibility(View.VISIBLE);
                        ButtonLoesung.setVisibility(View.VISIBLE);
                    } else if (pruefListeErgebnis.contains("true")) {
                        schwierigkeitsgrad = 1;
                        weiter.setBackgroundColor(Color.GREEN);
                        punkte++;
                    } else {
                        schwierigkeitsgrad = 0;
                        weiter.setBackgroundColor(Color.RED);
                        Buttonnochmal.setVisibility(View.VISIBLE);
                        ButtonLoesung.setVisibility(View.VISIBLE);
                    }
                } else if (schwierigkeitsgrad == 1) {
                    if (pruefListeErgebnis.contains("false")) {
                        schwierigkeitsgrad = 0;
                        weiter.setBackgroundColor(Color.RED);
                        Buttonnochmal.setVisibility(View.VISIBLE);
                        ButtonLoesung.setVisibility(View.VISIBLE);
                    } else if (pruefListeErgebnis.contains("true")) {
                        schwierigkeitsgrad = 2;
                        punkte++;
                        weiter.setBackgroundColor(Color.GREEN);
                    } else {
                        schwierigkeitsgrad = 0;
                        weiter.setBackgroundColor(Color.RED);
                        Buttonnochmal.setVisibility(View.VISIBLE);
                        ButtonLoesung.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (pruefListeErgebnis.contains("false")) {
                        schwierigkeitsgrad = 1;
                        weiter.setBackgroundColor(Color.RED);
                        Buttonnochmal.setVisibility(View.VISIBLE);
                        ButtonLoesung.setVisibility(View.VISIBLE);
                    } else if (pruefListeErgebnis.contains("true")) {
                        weiter.setBackgroundColor(Color.GREEN);
                        punkte++;
                    } else {
                        schwierigkeitsgrad = 1;
                        weiter.setBackgroundColor(Color.RED);
                        Buttonnochmal.setVisibility(View.VISIBLE);
                        ButtonLoesung.setVisibility(View.VISIBLE);
                    }

                }
                weiter.setVisibility(View.VISIBLE);
                a.setVisibility(View.INVISIBLE);
                b.setVisibility(View.INVISIBLE);
                c.setVisibility(View.INVISIBLE);
                d.setVisibility(View.INVISIBLE);
                loeschen.setVisibility(View.INVISIBLE);
                und.setVisibility(View.INVISIBLE);
                oder.setVisibility(View.INVISIBLE);
                nicht.setVisibility(View.INVISIBLE);
                klammerauf.setVisibility(View.INVISIBLE);
                klammerzu.setVisibility(View.INVISIBLE);
                bestaetigen.setVisibility(View.INVISIBLE);
            }
        });

        weiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fragenanzahl < 5) {
                    text.setText("");
                    unterertext.setText("");
                    bestaetigen.setBackgroundColor(Color.BLUE);
                    fragenanzahl++;
                    schwierigkeit(schwierigkeitsgrad);
                    Modus();
                    if (DNFKNFGenerator.get(0)) {
                        text.setText("Geben Sie für folgenden Term die DNF an! \n" + Aussage.toString());
                    } else {
                        text.setText("Geben Sie für folgenden Term die KNF an! \n" + Aussage.toString());
                    }

                    weiter.setVisibility(View.INVISIBLE);
                    Buttonnochmal.setVisibility(View.INVISIBLE);
                    ButtonLoesung.setVisibility(View.INVISIBLE);

                    a.setVisibility(View.VISIBLE);
                    b.setVisibility(View.VISIBLE);
                    c.setVisibility(View.VISIBLE);
                    d.setVisibility(View.VISIBLE);
                    loeschen.setVisibility(View.VISIBLE);
                    bestaetigen.setVisibility(View.VISIBLE);
                    und.setVisibility(View.VISIBLE);
                    oder.setVisibility(View.VISIBLE);
                    nicht.setVisibility(View.VISIBLE);
                    klammerauf.setVisibility(View.VISIBLE);
                    klammerzu.setVisibility(View.VISIBLE);
                    bestaetigen.setVisibility(View.VISIBLE);
                } else {
                    fragenanzahl = 0;
                    schwierigkeitsgrad = 0;
                    Endzeit = Calendar.getInstance().getTime();
                    speichern();
                    activityWechsel();
                    finish();
                }
            }
        });

        Buttonnochmal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                unterertext.setText("");
                bestaetigen.setBackgroundColor(Color.BLUE);

                weiter.setVisibility(View.INVISIBLE);
                Buttonnochmal.setVisibility(View.INVISIBLE);
                ButtonLoesung.setVisibility(View.INVISIBLE);

                a.setVisibility(View.VISIBLE);
                b.setVisibility(View.VISIBLE);
                c.setVisibility(View.VISIBLE);
                d.setVisibility(View.VISIBLE);
                loeschen.setVisibility(View.VISIBLE);
                bestaetigen.setVisibility(View.VISIBLE);
                und.setVisibility(View.VISIBLE);
                oder.setVisibility(View.VISIBLE);
                nicht.setVisibility(View.VISIBLE);
                klammerauf.setVisibility(View.VISIBLE);
                klammerzu.setVisibility(View.VISIBLE);

            }
        });

        ButtonLoesung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String formuebernehmer = Form;

                try {
                    formuebernehmer = formuebernehmer.replaceAll("~", "¬").replaceAll("&", "∧").replaceAll("\\|", "∨").replaceAll("\\s+","");
                    unterertext.setText(formuebernehmer);
                } catch (Exception e) {

                }

            }
        });

    }

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

        /*LogikAussage.add(Negation.get(0));
        LogikAussage.add(offeneKlammer.get(0));
        if (LogikAussage.contains("(")) {
            Collections.shuffle(Negation);
            LogikAussage.add(Negation.get(0));
        }
        LogikAussage.add(Variable.get(0));
        LogikAussage.add(Junktoren.get(0));
        Collections.shuffle(Negation);
        LogikAussage.add(Negation.get(0));
        if (!LogikAussage.contains("(")) {
            Collections.shuffle(offeneKlammer);
            LogikAussage.add(offeneKlammer.get(0));
            if (LogikAussage.contains("(")) {
                Collections.shuffle(Negation);
                LogikAussage.add(Negation.get(0));
            }
        }
        LogikAussage.add(Variable.get(1));
        if (LogikAussage.contains("(")) {
            if (LogikAussage.indexOf("(") == 1) {
                LogikAussage.add(")");
            }
        }
        Collections.shuffle(Junktoren);
        LogikAussage.add(Junktoren.get(0));
        Collections.shuffle(Negation);
        LogikAussage.add(Negation.get(0));
        LogikAussage.add(Variable.get(2));
        if (LogikAussage.contains("(")) {
            if (LogikAussage.indexOf("(") > 1) {
                LogikAussage.add(")");
            }
        }*/

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

        /*LogikAussage.add(Negation.get(0));                                          //0
        LogikAussage.add(offeneKlammer.get(0));                                     //1
        if (LogikAussage.contains("(")) {                                           //2
            Collections.shuffle(Negation);
            LogikAussage.add(Negation.get(0));
        } else {                                                                    //2
            LogikAussage.add("");
        }
        LogikAussage.add(Variable.get(0));                                          //3
        LogikAussage.add(Junktoren.get(0));                                         //4
        Collections.shuffle(Negation);
        LogikAussage.add(Negation.get(0));                                          //5
        if (LogikAussage.contains("(")){                                            //6
            LogikAussage.add("");
        }else if (LogikAussage.contains("<=>") || LogikAussage.contains("=>")){     //6
            LogikAussage.add("(");
        }else {                                                                     //6
            Collections.shuffle(offeneKlammer);
            LogikAussage.add(offeneKlammer.get(0));
        }
        if (LogikAussage.get(6).equals("(")){                                       //7
            Collections.shuffle(Negation);
            LogikAussage.add(Negation.get(0));
        }else {                                                                     //7
            LogikAussage.add("");
        }
        LogikAussage.add(Variable.get(1));                                          //8
        if (LogikAussage.get(6).equals("(")){                                       //9
            LogikAussage.add("");
        }else if (LogikAussage.get(4).equals("<=>")||LogikAussage.get(4).equals("=>")){ //9
            LogikAussage.set(1,"(");
            LogikAussage.add(")");
        }else if (LogikAussage.get(1).equals("(")){                                 //9
            Collections.shuffle(GeschlosseneKlammer);
            LogikAussage.add(GeschlosseneKlammer.get(0));
        }else {                                                                     //9
            LogikAussage.add("");
        }
        Collections.shuffle(Junktoren);
        LogikAussage.add(Junktoren.get(0));                                         //10
        Collections.shuffle(Negation);
        LogikAussage.add(Negation.get(0));                                          //11

        if (LogikAussage.contains("(")){
            if (LogikAussage.get(6).equals("(")){                                   //12
                Collections.shuffle(offeneKlammer);
                LogikAussage.add(offeneKlammer.get(0));
            }else if (LogikAussage.get(1).equals("(")){                             //12
                LogikAussage.add("");
            }
        }else {                                                                     //12
            Collections.shuffle(offeneKlammer);
            LogikAussage.add(offeneKlammer.get(0));
        }

        if (LogikAussage.get(12).equals("(")){                                      //13
            Collections.shuffle(Negation);
            LogikAussage.add(Negation.get(0));
        }else {                                                                     //13
            LogikAussage.add("");
        }
        LogikAussage.add(Variable.get(2));                                          //14

        if (LogikAussage.contains("(")){
            if (LogikAussage.get(12).equals("(")){
                LogikAussage.add("");
            }else if (LogikAussage.get(6).equals("(")){
                if (LogikAussage.get(1).equals("(")){
                    LogikAussage.add(")");
                }else if (LogikAussage.get(10).equals("<=>")||LogikAussage.get(10).equals("=>")){
                    LogikAussage.add(")");
                }else {
                    Collections.shuffle(GeschlosseneKlammer);
                    LogikAussage.add(GeschlosseneKlammer.get(0));
                }

            }else if (LogikAussage.get(1).equals("(")){
                if (LogikAussage.contains(")")){
                    LogikAussage.add("");
                }else {
                    LogikAussage.add(")");
                }
            }
        }else {                                                                     //15
            LogikAussage.add("");
        }
        Collections.shuffle(Junktoren);
        LogikAussage.add(Junktoren.get(0));                                         //16
        Collections.shuffle(Negation);
        LogikAussage.add(Negation.get(0));                                          //17
        LogikAussage.add(Variable.get(3));                                          //18
        if (LogikAussage.get(12).equals("(")){
            LogikAussage.add(")");
        }
        if (LogikAussage.get(6).equals("(")||LogikAussage.get(1).equals("(")){
            if(LogikAussage.get(9).equals(")")||LogikAussage.get(15).equals(")")){
                LogikAussage.add("");
            }else {
                LogikAussage.add(")");
            }
        }*/


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

    public String KNFpruefer(String Parsertext) throws ParserException {
        Form = "";
        final FormulaFactory f = new FormulaFactory();
        final PropositionalParser p = new PropositionalParser(f);
        final Formula formula = p.parse(Parsertext);
        // final Formula nnf = formula.nnf();
        final Formula cnf = formula.cnf();
        final SATSolver miniSat = MiniSat.miniSat(f);
        miniSat.add(formula);
        // final Tristate result = miniSat.sat();

        Form = cnf.toString();

        return Form;
        //Logikboolean = result.toString();
    }

    public String DNFpruefer(String Parsertext) throws ParserException {
        Form = "";
        final FormulaFactory f = new FormulaFactory();
        final PropositionalParser p = new PropositionalParser(f);
        final Formula formula = p.parse(Parsertext);

        final SATSolver miniSat = MiniSat.miniSat(f);
        miniSat.add(formula);

        final DNFFactorization dnfFactorization = new DNFFactorization();
        Formula test = dnfFactorization.apply(formula, true);
        // System.out.println(test.toString());
        Form = test.toString();
        return Form.toString();
    }

    public void schwierigkeit(int Schwierigkeit) {
        if (Schwierigkeit == 0) {
            zweierTerm();
        } else if (Schwierigkeit == 1) {
            dreierTerm();
        } else if (Schwierigkeit == 2) {
            viererTerm();
        }
    }

    public void Modus() {


        DNFKNFGenerator.clear();
        //text.setText("");

        DNFKNFGenerator.add(true);
        DNFKNFGenerator.add(false);

        Collections.shuffle(DNFKNFGenerator);

        if (DNFKNFGenerator.get(0)) {
            try {
                DNFpruefer(ParserText);
            } catch (ParserException e) {
                e.printStackTrace();
            }
        } else {
            try {
                KNFpruefer(ParserText);
            } catch (ParserException e) {
                e.printStackTrace();
            }
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
                    fragenanzahl = 0;
                    schwierigkeitsgrad = 0;
                    activityWechsel();
                    finish();
                    break;

            }
        } catch (Exception e) {

        }
        return super.onOptionsItemSelected(item);
    }


    public void ueberpruefer(int schwierigkeit) {
        try {
            ArrayList<String> geschrListe = new ArrayList<>();
            ArrayList<String> formListe = new ArrayList<>();

            geschrListe.clear();
            formListe.clear();
            pruefListeErgebnis.clear();

            String geschriebenerText = "";
            String formText = "";

            geschriebenerText = texti;
            formText = Form.replaceAll("\\s+", "");

            if (geschriebenerText.contains("∧")) {
                geschriebenerText = geschriebenerText.replaceAll("∧", "&&");
            }
            if (geschriebenerText.contains("∨")) {
                geschriebenerText = geschriebenerText.replaceAll("∨", "\\||");
            }
            if (geschriebenerText.contains("¬")) {
                geschriebenerText = geschriebenerText.replaceAll("¬", "!");
            }

            if (formText.contains("&")) {
                formText = formText.replaceAll("&", "&&");
            }
            if (formText.contains("|")) {
                formText = formText.replaceAll("\\|", "||");
            }
            if (formText.contains("~")) {
                formText = formText.replaceAll("~", "!");
            }


            switch (schwierigkeit) {
                case 0:
                    geschrListe.add(geschriebenerText.replaceAll("a", "false").replaceAll("b", "false")); // 00
                    geschrListe.add(geschriebenerText.replaceAll("a", "false").replaceAll("b", "true"));  // 01
                    geschrListe.add(geschriebenerText.replaceAll("a", "true").replaceAll("b", "false"));  // 10
                    geschrListe.add(geschriebenerText.replaceAll("a", "true").replaceAll("b", "true"));   // 11


                    formListe.add(formText.replaceAll("a", "false").replaceAll("b", "false")); // 00
                    formListe.add(formText.replaceAll("a", "false").replaceAll("b", "true"));  // 01
                    formListe.add(formText.replaceAll("a", "true").replaceAll("b", "false"));  // 10
                    formListe.add(formText.replaceAll("a", "true").replaceAll("b", "true"));   // 11

                    for (int i = 0; i < formListe.size(); i++) {
                        String linkervergleich = "";
                        String rechtervergleich = "";

                        linkervergleich = MVEL.evalToString(geschrListe.get(i));
                        rechtervergleich = MVEL.evalToString(formListe.get(i));

                        if (linkervergleich == rechtervergleich) {
                            pruefListeErgebnis.add("true");
                        } else {
                            pruefListeErgebnis.add("false");
                        }
                    }

                    break;

                case 1:

                    geschrListe.add(geschriebenerText.replaceAll("a", "false").replaceAll("b", "false").replaceAll("c", "false")); // 000
                    geschrListe.add(geschriebenerText.replaceAll("a", "false").replaceAll("b", "false").replaceAll("c", "true"));  // 001
                    geschrListe.add(geschriebenerText.replaceAll("a", "false").replaceAll("b", "true").replaceAll("c", "false"));  // 010
                    geschrListe.add(geschriebenerText.replaceAll("a", "false").replaceAll("b", "true").replaceAll("c", "true"));   // 011
                    geschrListe.add(geschriebenerText.replaceAll("a", "true").replaceAll("b", "false").replaceAll("c", "false")); // 100
                    geschrListe.add(geschriebenerText.replaceAll("a", "true").replaceAll("b", "false").replaceAll("c", "true"));  // 101
                    geschrListe.add(geschriebenerText.replaceAll("a", "true").replaceAll("b", "true").replaceAll("c", "false"));  // 110
                    geschrListe.add(geschriebenerText.replaceAll("a", "true").replaceAll("b", "true").replaceAll("c", "true"));   // 111


                    formListe.add(formText.replaceAll("a", "false").replaceAll("b", "false").replaceAll("c", "false")); // 000
                    formListe.add(formText.replaceAll("a", "false").replaceAll("b", "false").replaceAll("c", "true"));  // 001
                    formListe.add(formText.replaceAll("a", "false").replaceAll("b", "true").replaceAll("c", "false"));  // 010
                    formListe.add(formText.replaceAll("a", "false").replaceAll("b", "true").replaceAll("c", "true"));   // 011
                    formListe.add(formText.replaceAll("a", "true").replaceAll("b", "false").replaceAll("c", "false")); // 100
                    formListe.add(formText.replaceAll("a", "true").replaceAll("b", "false").replaceAll("c", "true"));  // 101
                    formListe.add(formText.replaceAll("a", "true").replaceAll("b", "true").replaceAll("c", "false"));  // 110
                    formListe.add(formText.replaceAll("a", "true").replaceAll("b", "true").replaceAll("c", "true"));   // 111

                    for (int i = 0; i < formListe.size(); i++) {
                        String linkervergleich = "";
                        String rechtervergleich = "";

                        linkervergleich = MVEL.evalToString(geschrListe.get(i));
                        rechtervergleich = MVEL.evalToString(formListe.get(i));

                        if (linkervergleich == rechtervergleich) {
                            pruefListeErgebnis.add("true");
                        } else {
                            pruefListeErgebnis.add("false");
                        }
                    }
                    break;

                case 2:

                    geschrListe.add(geschriebenerText.replaceAll("a", "false").replaceAll("b", "false").replaceAll("c", "false").replaceAll("d", "false"));   // 0000
                    geschrListe.add(geschriebenerText.replaceAll("a", "false").replaceAll("b", "false").replaceAll("c", "false").replaceAll("d", "true"));    // 0001
                    geschrListe.add(geschriebenerText.replaceAll("a", "false").replaceAll("b", "false").replaceAll("c", "true").replaceAll("d", "false"));    // 0010
                    geschrListe.add(geschriebenerText.replaceAll("a", "false").replaceAll("b", "false").replaceAll("c", "true").replaceAll("d", "true"));     // 0011
                    geschrListe.add(geschriebenerText.replaceAll("a", "false").replaceAll("b", "true").replaceAll("c", "false").replaceAll("d", "false"));   // 0100
                    geschrListe.add(geschriebenerText.replaceAll("a", "false").replaceAll("b", "true").replaceAll("c", "false").replaceAll("d", "true"));    // 0101
                    geschrListe.add(geschriebenerText.replaceAll("a", "false").replaceAll("b", "true").replaceAll("c", "true").replaceAll("d", "false"));    // 0110
                    geschrListe.add(geschriebenerText.replaceAll("a", "false").replaceAll("b", "true").replaceAll("c", "true").replaceAll("d", "true"));     // 0111
                    geschrListe.add(geschriebenerText.replaceAll("a", "true").replaceAll("b", "false").replaceAll("c", "false").replaceAll("d", "false"));   // 1000
                    geschrListe.add(geschriebenerText.replaceAll("a", "true").replaceAll("b", "false").replaceAll("c", "false").replaceAll("d", "true"));    // 1001
                    geschrListe.add(geschriebenerText.replaceAll("a", "true").replaceAll("b", "false").replaceAll("c", "true").replaceAll("d", "false"));    // 1010
                    geschrListe.add(geschriebenerText.replaceAll("a", "true").replaceAll("b", "false").replaceAll("c", "true").replaceAll("d", "true"));     // 1011
                    geschrListe.add(geschriebenerText.replaceAll("a", "true").replaceAll("b", "true").replaceAll("c", "false").replaceAll("d", "false"));   // 1100
                    geschrListe.add(geschriebenerText.replaceAll("a", "true").replaceAll("b", "true").replaceAll("c", "false").replaceAll("d", "true"));    // 1101
                    geschrListe.add(geschriebenerText.replaceAll("a", "true").replaceAll("b", "true").replaceAll("c", "true").replaceAll("d", "false"));    // 1110
                    geschrListe.add(geschriebenerText.replaceAll("a", "true").replaceAll("b", "true").replaceAll("c", "true").replaceAll("d", "true"));     // 1111


                    formListe.add(formText.replaceAll("a", "false").replaceAll("b", "false").replaceAll("c", "false").replaceAll("d", "false"));   // 0000
                    formListe.add(formText.replaceAll("a", "false").replaceAll("b", "false").replaceAll("c", "false").replaceAll("d", "true"));    // 0001
                    formListe.add(formText.replaceAll("a", "false").replaceAll("b", "false").replaceAll("c", "true").replaceAll("d", "false"));    // 0010
                    formListe.add(formText.replaceAll("a", "false").replaceAll("b", "false").replaceAll("c", "true").replaceAll("d", "true"));     // 0011
                    formListe.add(formText.replaceAll("a", "false").replaceAll("b", "true").replaceAll("c", "false").replaceAll("d", "false"));   // 0100
                    formListe.add(formText.replaceAll("a", "false").replaceAll("b", "true").replaceAll("c", "false").replaceAll("d", "true"));    // 0101
                    formListe.add(formText.replaceAll("a", "false").replaceAll("b", "true").replaceAll("c", "true").replaceAll("d", "false"));    // 0110
                    formListe.add(formText.replaceAll("a", "false").replaceAll("b", "true").replaceAll("c", "true").replaceAll("d", "true"));     // 0111
                    formListe.add(formText.replaceAll("a", "true").replaceAll("b", "false").replaceAll("c", "false").replaceAll("d", "false"));   // 1000
                    formListe.add(formText.replaceAll("a", "true").replaceAll("b", "false").replaceAll("c", "false").replaceAll("d", "true"));    // 1001
                    formListe.add(formText.replaceAll("a", "true").replaceAll("b", "false").replaceAll("c", "true").replaceAll("d", "false"));    // 1010
                    formListe.add(formText.replaceAll("a", "true").replaceAll("b", "false").replaceAll("c", "true").replaceAll("d", "true"));     // 1011
                    formListe.add(formText.replaceAll("a", "true").replaceAll("b", "true").replaceAll("c", "false").replaceAll("d", "false"));   // 1100
                    formListe.add(formText.replaceAll("a", "true").replaceAll("b", "true").replaceAll("c", "false").replaceAll("d", "true"));    // 1101
                    formListe.add(formText.replaceAll("a", "true").replaceAll("b", "true").replaceAll("c", "true").replaceAll("d", "false"));    // 1110
                    formListe.add(formText.replaceAll("a", "true").replaceAll("b", "true").replaceAll("c", "true").replaceAll("d", "true"));     // 1111

                    for (int i = 0; i < formListe.size(); i++) {
                        String linkervergleich = "";
                        String rechtervergleich = "";

                        linkervergleich = MVEL.evalToString(geschrListe.get(i));
                        rechtervergleich = MVEL.evalToString(formListe.get(i));

                        if (linkervergleich == rechtervergleich) {
                            pruefListeErgebnis.add("true");
                        } else {
                            pruefListeErgebnis.add("false");
                        }
                    }
                    break;

            }
        } catch (Exception e) {

        }


    }

    public void ButtonBlaumacher() {

        klammerauf.setBackgroundColor(Color.BLUE);
        klammerzu.setBackgroundColor(Color.BLUE);
        d.setBackgroundColor(Color.BLUE);
        weiter.setBackgroundColor(Color.BLUE);
        a.setBackgroundColor(Color.BLUE);
        b.setBackgroundColor(Color.BLUE);
        c.setBackgroundColor(Color.BLUE);
        loeschen.setBackgroundColor(Color.BLUE);
        bestaetigen.setBackgroundColor(Color.BLUE);
        und.setBackgroundColor(Color.BLUE);
        oder.setBackgroundColor(Color.BLUE);
        nicht.setBackgroundColor(Color.BLUE);
        ButtonLoesung.setBackgroundColor(Color.BLUE);
        Buttonnochmal.setBackgroundColor(Color.BLUE);

    }

    //Die Methoden speichern() und laden() sind übernommen von https://gist.github.com/codinginflow/6c13bd0d08416115798f17d45b5d8056

    public void speichern() {
        laden();

        String text = annehmer + "UserID|" + Benutzername + ";Activity|NormalFormen;Anfangszeit|" + Anfangszeit + ";Endzeit|" + Endzeit + ";Punkte|" + punkte + "von5;\n";

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

}