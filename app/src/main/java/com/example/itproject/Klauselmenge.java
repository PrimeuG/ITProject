package com.example.itproject;

import android.bluetooth.BluetoothClass;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.logicng.datastructures.Tristate;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.io.parsers.ParserException;
import org.logicng.io.parsers.PropositionalParser;
import org.logicng.solvers.MiniSat;
import org.logicng.solvers.SATSolver;

import java.util.ArrayList;
import java.util.Collections;

public class Klauselmenge extends AppCompatActivity implements View.OnClickListener {

    Button erster, zweiter, dritter, vierter, fuenfter, erfuellbar, nichtErfuellbar, elf;
    TextView text, Teilkalkuel1, Teilkalkuel2, Teilkalkuel3, Teilkalkuel4, Teilkalkuel5;


    int fragenanzahl = 0;

    ArrayList<String> Endklausel = new ArrayList<>();
    ArrayList<String> ParserKlausel = new ArrayList<>();
    ArrayList<Integer> Klauselwaehler = new ArrayList<>();
    ArrayList<String> EinzelKlausel = new ArrayList<>();
    ArrayList<String> Mehrfachklausel = new ArrayList<>();

    public static boolean fuerT1 = true;
    public static boolean fuerT2 = true;
    public static boolean fuerT3 = true;
    public static boolean fuerT4 = true;
    public static boolean fuerT5 = true;
    public static boolean Beantwortet = false;

    public static String ParserText = "";
    public static String Klauselmenge = "";
    public static String ParserTexte = "";
    public static String warter = "";
    public static int farbe;
    public static int antwort = 0;
    public static int maxfragen = 10;

    int EinzelklauselStopper = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klauselmenge);

        if (savedInstanceState == null) {
            fragenanzahl = 0;
        } else {
            fragenanzahl = (int) savedInstanceState.get("Frage");
        }

        text = findViewById(R.id.Text);
        Teilkalkuel1 = findViewById(R.id.textView);
        Teilkalkuel2 = findViewById(R.id.textView2);
        Teilkalkuel3 = findViewById(R.id.textView3);
        Teilkalkuel4 = findViewById(R.id.textView4);
        Teilkalkuel5 = findViewById(R.id.textView5);


        erster = (Button) findViewById(R.id.button);
        zweiter = (Button) findViewById(R.id.button2);
        dritter = (Button) findViewById(R.id.button3);
        vierter = (Button) findViewById(R.id.button4);
        fuenfter = (Button) findViewById(R.id.button5);
        erfuellbar = (Button) findViewById(R.id.button6);
        nichtErfuellbar = (Button) findViewById(R.id.button7);
        elf = (Button) findViewById(R.id.button11);

        Klauselwaehler.clear();
        EinzelKlausel.clear();
        Mehrfachklausel.clear();

        Klauselwaehler.add(0);
        Klauselwaehler.add(1);

        EinzelKlausel.add("{a}");
        EinzelKlausel.add("{¬a}");
        EinzelKlausel.add("{b}");
        EinzelKlausel.add("{¬b}");
        EinzelKlausel.add("{c}");
        EinzelKlausel.add("{¬c}");

        Mehrfachklausel.add("{a,b}");
        Mehrfachklausel.add("{a,¬b}");
        Mehrfachklausel.add("{¬a,b}");
        Mehrfachklausel.add("{¬a,¬b}");
        Mehrfachklausel.add("{a,c}");
        Mehrfachklausel.add("{a,¬c}");
        Mehrfachklausel.add("{¬a,c}");
        Mehrfachklausel.add("{¬a,¬c}");
        Mehrfachklausel.add("{b,c}");
        Mehrfachklausel.add("{b,¬c}");
        Mehrfachklausel.add("{¬b,c}");
        Mehrfachklausel.add("{¬b,¬c}");

        Mehrfachklausel.add("{a,b,c}");
        Mehrfachklausel.add("{a,b,¬c}");
        Mehrfachklausel.add("{a,¬b,c}");
        Mehrfachklausel.add("{a,¬b,¬c}");
        Mehrfachklausel.add("{¬a,b,c}");
        Mehrfachklausel.add("{¬a,b,¬c}");
        Mehrfachklausel.add("{¬a,¬b,c}");
        Mehrfachklausel.add("{¬a,¬b,¬c}");


        erfuellbar.setBackgroundColor(Color.BLUE);
        nichtErfuellbar.setBackgroundColor(Color.BLUE);
        elf.setBackgroundColor(Color.BLUE);
        elf.setVisibility(View.INVISIBLE);

        erster.setBackgroundColor(Color.RED);
        zweiter.setBackgroundColor(Color.RED);
        dritter.setBackgroundColor(Color.RED);
        vierter.setBackgroundColor(Color.RED);
        fuenfter.setBackgroundColor(Color.RED);


        erster.setOnClickListener(this);
        zweiter.setOnClickListener(this);
        dritter.setOnClickListener(this);
        vierter.setOnClickListener(this);
        fuenfter.setOnClickListener(this);
        erfuellbar.setOnClickListener(this);
        nichtErfuellbar.setOnClickListener(this);
        elf.setOnClickListener(this);

        Beantwortet = false;

        Klauselerstellen();
        textersteller();
        try {
            Logikueberpruefer(ParserTexte);
        } catch (ParserException e) {
            e.printStackTrace();
        }


    }

    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.button:
                if (fuerT1) {
                    erster.setBackgroundColor(Color.GREEN);
                    Teilkalkuel1.setVisibility(View.INVISIBLE);
                    fuerT1 = false;
                } else {
                    erster.setBackgroundColor(Color.RED);
                    Teilkalkuel1.setVisibility(View.VISIBLE);
                    fuerT1 = true;
                }
                Beantwortet = false;
                break;

            case R.id.button2:
                if (fuerT2) {
                    zweiter.setBackgroundColor(Color.GREEN);
                    Teilkalkuel2.setVisibility(View.INVISIBLE);
                    fuerT2 = false;
                } else {
                    zweiter.setBackgroundColor(Color.RED);
                    Teilkalkuel2.setVisibility(View.VISIBLE);
                    fuerT2 = true;
                }
                Beantwortet = false;
                break;

            case R.id.button3:
                if (fuerT3) {
                    dritter.setBackgroundColor(Color.GREEN);
                    Teilkalkuel3.setVisibility(View.INVISIBLE);
                    fuerT3 = false;
                } else {
                    dritter.setBackgroundColor(Color.RED);
                    Teilkalkuel3.setVisibility(View.VISIBLE);
                    fuerT3 = true;
                }
                Beantwortet = false;
                break;

            case R.id.button4:
                if (fuerT4) {
                    vierter.setBackgroundColor(Color.GREEN);
                    Teilkalkuel4.setVisibility(View.INVISIBLE);
                    fuerT4 = false;
                } else {
                    vierter.setBackgroundColor(Color.RED);
                    Teilkalkuel4.setVisibility(View.VISIBLE);
                    fuerT4 = true;
                }
                Beantwortet = false;
                break;

            case R.id.button5:
                if (fuerT5) {
                    fuenfter.setBackgroundColor(Color.GREEN);
                    Teilkalkuel5.setVisibility(View.INVISIBLE);
                    fuerT5 = false;
                } else {
                    fuenfter.setBackgroundColor(Color.RED);
                    Teilkalkuel5.setVisibility(View.VISIBLE);
                    fuerT5 = true;
                }
                Beantwortet = false;
                break;


            case R.id.button6:
                try {
                    if (Logikueberpruefer(ParserTexte).equals("TRUE")) {

                        erfuellbar.setBackgroundColor(Color.GREEN);
                        nichtErfuellbar.setBackgroundColor(Color.RED);

                        // Buttonzuruecksteller();

                    } else {
                        erfuellbar.setBackgroundColor(Color.RED);
                        nichtErfuellbar.setBackgroundColor(Color.GREEN);
                    }
                } catch (ParserException e) {
                    e.printStackTrace();
                }
                elf.setVisibility(View.VISIBLE);
                break;

            case R.id.button7:
                try {
                    if(Logikueberpruefer(ParserTexte).equals("TRUE")){

                        erfuellbar.setBackgroundColor(Color.GREEN);
                        nichtErfuellbar.setBackgroundColor(Color.RED);
                    }else {
                        erfuellbar.setBackgroundColor(Color.RED);
                        nichtErfuellbar.setBackgroundColor(Color.GREEN);
                    }
                } catch (ParserException e) {
                    e.printStackTrace();
                }
                elf.setVisibility(View.VISIBLE);
                break;

            case R.id.button11:
                try {
                    fragenanzahl++;
                    if (fragenanzahl < maxfragen){
                        ParserTexte = "";
                        Klauselerstellen();
                        textersteller();
                        Buttonzuruecksteller();
                        elf.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception e) {
                }
                break;
        }

        if(fragenanzahl >= maxfragen){
            activityWechsel();
            finish();
        }


    }


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

    public void Klauselerstellen() {
        Endklausel.clear();
        ParserKlausel.clear();

        for (int i = 0; i < 5; i++) {

            String ueberpruefer;
            boolean kommageber = false;
            String uebernehmer = "";

            Collections.shuffle(Klauselwaehler);

            if (Klauselwaehler.get(0) == 0 && EinzelklauselStopper == 0) {
                Collections.shuffle(EinzelKlausel);
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
                if (uebernehmer.contains("¬")) {
                    uebernehmer = uebernehmer.replaceAll("¬", "~");
                }
                ParserKlausel.add(uebernehmer);
                EinzelklauselStopper++;
                kommageber = true;
            } else {

                Collections.shuffle(Mehrfachklausel);

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
                    if (uebernehmer.contains("¬")) {
                        uebernehmer = uebernehmer.replaceAll("¬", "~");
                    } else if (uebernehmer.contains(",")) {
                        uebernehmer = uebernehmer.replaceAll(",", "|");
                    }

                    ParserKlausel.add(uebernehmer);
                    if (i < 4) {
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

    public void textersteller() {

        text.setText("Ist diese Klauselmenge erfüllbar?\n" + Klauselmenge);

        erster.setText(Endklausel.get(0));
        zweiter.setText(Endklausel.get(2));
        dritter.setText(Endklausel.get(4));
        vierter.setText(Endklausel.get(6));
        fuenfter.setText(Endklausel.get(8));

        Teilkalkuel1.setText(Endklausel.get(0));
        Teilkalkuel2.setText(Endklausel.get(2));
        Teilkalkuel3.setText(Endklausel.get(4));
        Teilkalkuel4.setText(Endklausel.get(6));
        Teilkalkuel5.setText(Endklausel.get(8));
    }

    public void Buttonzuruecksteller() {

        fuerT1 = true;
        fuerT2 = true;
        fuerT3 = true;
        fuerT4 = true;
        fuerT5 = true;

        erster.setBackgroundColor(Color.RED);
        zweiter.setBackgroundColor(Color.RED);
        dritter.setBackgroundColor(Color.RED);
        vierter.setBackgroundColor(Color.RED);
        fuenfter.setBackgroundColor(Color.RED);

        Teilkalkuel1.setVisibility(View.VISIBLE);
        Teilkalkuel2.setVisibility(View.VISIBLE);
        Teilkalkuel3.setVisibility(View.VISIBLE);
        Teilkalkuel4.setVisibility(View.VISIBLE);
        Teilkalkuel5.setVisibility(View.VISIBLE);

        erfuellbar.setBackgroundColor(Color.BLUE);
        nichtErfuellbar.setBackgroundColor(Color.BLUE);

    }

    private void activityWechsel(){
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
