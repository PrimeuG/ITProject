package com.example.itproject;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

    Button erster, zweiter, dritter, vierter, fuenfter, erfuellbar, nichtErfuellbar, elf, a, b, c;
    TextView text, Teilkalkuel1, Teilkalkuel2, Teilkalkuel3, Teilkalkuel4, Teilkalkuel5;


    int fragenanzahl = 0;

    ArrayList<String> Endklausel = new ArrayList<>();
    ArrayList<String> ParserKlausel = new ArrayList<>();
    ArrayList<Integer> Klauselwaehler = new ArrayList<>();
    ArrayList<String> EinzelKlausel = new ArrayList<>();
    ArrayList<String> Mehrfachklausel = new ArrayList<>();

    private static final String FILE_NAME = "Auswertung.txt";

    private static String Benutzername = "Benutzername";
    private static String annehmer = "";

    public static boolean fuerT1 = false;
    public static boolean fuerT2 = false;
    public static boolean fuerT3 = false;
    public static boolean fuerT4 = false;
    public static boolean fuerT5 = false;
    public static boolean Beantwortet = false;

    public static String ParserText = "";
    public static String Klauselmenge = "";
    public static String ParserTexte = "";

    public static int punkte = 0;
    public static int maxfragen = 10;

    public static Date Anfangszeit;
    public static Date Endzeit;

    int EinzelklauselStopper = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_klauselmenge);

        Anfangszeit = null;
        Endzeit = null;
        Anfangszeit = Calendar.getInstance().getTime();

        punkte = 0;

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
        a = (Button) findViewById(R.id.buttona);
        b = (Button) findViewById(R.id.buttonb);
        c = (Button) findViewById(R.id.buttonc);

        ButtonBlaumacher();

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
        a.setOnClickListener(this);
        b.setOnClickListener(this);
        c.setOnClickListener(this);

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
                    erster.setBackgroundColor(Color.RED);
                    fuerT1 = false;
                } else {
                    erster.setBackgroundColor(Color.GREEN);
                    fuerT1 = true;
                }
                Beantwortet = false;
                break;

            case R.id.button2:
                if (fuerT2) {
                    zweiter.setBackgroundColor(Color.RED);
                    fuerT2 = false;
                } else {
                    zweiter.setBackgroundColor(Color.GREEN);
                    fuerT2 = true;
                }
                Beantwortet = false;
                break;

            case R.id.button3:
                if (fuerT3) {
                    dritter.setBackgroundColor(Color.RED);
                    fuerT3 = false;
                } else {
                    dritter.setBackgroundColor(Color.GREEN);
                    fuerT3 = true;
                }
                Beantwortet = false;
                break;

            case R.id.button4:
                if (fuerT4) {
                    vierter.setBackgroundColor(Color.RED);
                    fuerT4 = false;
                } else {
                    vierter.setBackgroundColor(Color.GREEN);
                    fuerT4 = true;
                }
                Beantwortet = false;
                break;

            case R.id.button5:
                if (fuerT5) {
                    fuenfter.setBackgroundColor(Color.RED);
                    fuerT5 = false;
                } else {
                    fuenfter.setBackgroundColor(Color.GREEN);
                    fuerT5 = true;
                }
                Beantwortet = false;
                break;

            case R.id.buttona:
                if (fuerT1){
                    String entferner;
                    entferner = Teilkalkuel1.getText().toString();
                    try {
                        entferner = entferner.replaceAll("¬a","");
                        entferner = entferner.replaceAll("a","");
                        Teilkalkuel1.setText(entferner);
                        erster.setText(entferner);
                        entferner="";
                    }catch (Exception e){

                    }
                }
                if (fuerT2){
                    String entferner;
                    entferner = Teilkalkuel2.getText().toString();
                    try {
                        entferner = entferner.replaceAll("¬a","");
                        entferner = entferner.replaceAll("a","");
                        Teilkalkuel2.setText(entferner);
                        zweiter.setText(entferner);
                        entferner="";
                    }catch (Exception e){

                    }
                }
                if (fuerT3){
                    String entferner;
                    entferner = Teilkalkuel3.getText().toString();
                    try {
                        entferner = entferner.replaceAll("¬a","");
                        entferner = entferner.replaceAll("a","");
                        Teilkalkuel3.setText(entferner);
                        dritter.setText(entferner);
                        entferner="";
                    }catch (Exception e){

                    }
                }
                if (fuerT4){
                    String entferner;
                    entferner = Teilkalkuel4.getText().toString();
                    try {
                        entferner = entferner.replaceAll("¬a","");
                        entferner = entferner.replaceAll("a","");
                        Teilkalkuel4.setText(entferner);
                        vierter.setText(entferner);
                        entferner="";
                    }catch (Exception e){

                    }
                }
                if (fuerT5){
                    String entferner;
                    entferner = Teilkalkuel5.getText().toString();
                    try {
                        entferner = entferner.replaceAll("¬a","");
                        entferner = entferner.replaceAll("a","");
                        Teilkalkuel5.setText(entferner);
                        fuenfter.setText(entferner);
                        entferner="";
                    }catch (Exception e){

                    }
                }

                erster.setBackgroundColor(Color.RED);
                fuerT1 = false;

                zweiter.setBackgroundColor(Color.RED);
                fuerT2 = false;

                dritter.setBackgroundColor(Color.RED);
                fuerT3 = false;

                vierter.setBackgroundColor(Color.RED);
                fuerT4 = false;

                fuenfter.setBackgroundColor(Color.RED);
                fuerT5 = false;

                break;

            case R.id.buttonb:
                if (fuerT1){
                    String entferner;
                    entferner = Teilkalkuel1.getText().toString();
                    try {
                        entferner = entferner.replaceAll("¬b","");
                        entferner = entferner.replaceAll("b","");
                        Teilkalkuel1.setText(entferner);
                        erster.setText(entferner);
                        entferner="";
                    }catch (Exception e){

                    }
                }
                if (fuerT2){
                    String entferner;
                    entferner = Teilkalkuel2.getText().toString();
                    try {
                        entferner = entferner.replaceAll("¬b","");
                        entferner = entferner.replaceAll("b","");
                        Teilkalkuel2.setText(entferner);
                        zweiter.setText(entferner);
                        entferner="";
                    }catch (Exception e){

                    }
                }
                if (fuerT3){
                    String entferner;
                    entferner = Teilkalkuel3.getText().toString();
                    try {
                        entferner = entferner.replaceAll("¬b","");
                        entferner = entferner.replaceAll("b","");
                        Teilkalkuel3.setText(entferner);
                        dritter.setText(entferner);
                        entferner="";
                    }catch (Exception e){

                    }
                }
                if (fuerT4){
                    String entferner;
                    entferner = Teilkalkuel4.getText().toString();
                    try {
                        entferner = entferner.replaceAll("¬b","");
                        entferner = entferner.replaceAll("b","");
                        Teilkalkuel4.setText(entferner);
                        vierter.setText(entferner);
                        entferner="";
                    }catch (Exception e){

                    }
                }
                if (fuerT5){
                    String entferner;
                    entferner = Teilkalkuel5.getText().toString();
                    try {
                        entferner = entferner.replaceAll("¬b","");
                        entferner = entferner.replaceAll("b","");
                        Teilkalkuel5.setText(entferner);
                        fuenfter.setText(entferner);
                        entferner="";
                    }catch (Exception e){

                    }
                }

                erster.setBackgroundColor(Color.RED);
                fuerT1 = false;

                zweiter.setBackgroundColor(Color.RED);
                fuerT2 = false;

                dritter.setBackgroundColor(Color.RED);
                fuerT3 = false;

                vierter.setBackgroundColor(Color.RED);
                fuerT4 = false;

                fuenfter.setBackgroundColor(Color.RED);
                fuerT5 = false;

                break;

            case R.id.buttonc:
                if (fuerT1){
                    String entferner;
                    entferner = Teilkalkuel1.getText().toString();
                    try {
                        entferner = entferner.replaceAll("¬c","");
                        entferner = entferner.replaceAll("c","");
                        Teilkalkuel1.setText(entferner);
                        erster.setText(entferner);
                        entferner="";
                    }catch (Exception e){

                    }
                }
                if (fuerT2){
                    String entferner;
                    entferner = Teilkalkuel2.getText().toString();
                    try {
                        entferner = entferner.replaceAll("¬c","");
                        entferner = entferner.replaceAll("c","");
                        Teilkalkuel2.setText(entferner);
                        zweiter.setText(entferner);
                        entferner="";
                    }catch (Exception e){

                    }
                }
                if (fuerT3){
                    String entferner;
                    entferner = Teilkalkuel3.getText().toString();
                    try {
                        entferner = entferner.replaceAll("¬c","");
                        entferner = entferner.replaceAll("c","");
                        Teilkalkuel3.setText(entferner);
                        dritter.setText(entferner);
                        entferner="";
                    }catch (Exception e){

                    }
                }
                if (fuerT4){
                    String entferner;
                    entferner = Teilkalkuel4.getText().toString();
                    try {
                        entferner = entferner.replaceAll("¬c","");
                        entferner = entferner.replaceAll("c","");
                        Teilkalkuel4.setText(entferner);
                        vierter.setText(entferner);
                        entferner="";
                    }catch (Exception e){

                    }
                }
                if (fuerT5){
                    String entferner;
                    entferner = Teilkalkuel5.getText().toString();
                    try {
                        entferner = entferner.replaceAll("¬c","");
                        entferner = entferner.replaceAll("c","");
                        Teilkalkuel5.setText(entferner);
                        fuenfter.setText(entferner);
                        entferner="";
                    }catch (Exception e){

                    }
                }

                erster.setBackgroundColor(Color.RED);
                fuerT1 = false;

                zweiter.setBackgroundColor(Color.RED);
                fuerT2 = false;

                dritter.setBackgroundColor(Color.RED);
                fuerT3 = false;

                vierter.setBackgroundColor(Color.RED);
                fuerT4 = false;

                fuenfter.setBackgroundColor(Color.RED);
                fuerT5 = false;

                break;

            case R.id.button6:
                try {
                    if (Logikueberpruefer(ParserTexte).equals("TRUE")) {

                        erfuellbar.setBackgroundColor(Color.GREEN);
                        nichtErfuellbar.setBackgroundColor(Color.RED);
                        punkte++;

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
                    if (Logikueberpruefer(ParserTexte).equals("TRUE")) {

                        erfuellbar.setBackgroundColor(Color.GREEN);
                        nichtErfuellbar.setBackgroundColor(Color.RED);
                    } else {
                        erfuellbar.setBackgroundColor(Color.RED);
                        nichtErfuellbar.setBackgroundColor(Color.GREEN);
                        punkte++;
                    }
                } catch (ParserException e) {
                    e.printStackTrace();
                }
                elf.setVisibility(View.VISIBLE);
                break;

            case R.id.button11:
                try {
                    fragenanzahl++;
                    if (fragenanzahl < maxfragen) {
                        ParserTexte = "";
                        Endzeit = Calendar.getInstance().getTime();
                        Klauselerstellen();
                        textersteller();
                        Buttonzuruecksteller();
                        elf.setVisibility(View.INVISIBLE);
                    }
                } catch (Exception e) {
                }
                break;
        }

        if (fragenanzahl >= maxfragen) {
            fragenanzahl=0;
            Endzeit = Calendar.getInstance().getTime();
            speichern();
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

        fuerT1 = false;
        fuerT2 = false;
        fuerT3 = false;
        fuerT4 = false;
        fuerT5 = false;

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

    private void activityWechsel() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.spiel_menu, menu);
        return true;
    }

    //Für Neustart und Endscreen, es werden verschiedene Dinge resettet
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()) {
                case R.id.cancel:
                    fragenanzahl = 0;
                    activityWechsel();
                    finish();
                    break;
                case R.id.reset:

                    Teilkalkuel1.setText(Endklausel.get(0));
                    Teilkalkuel2.setText(Endklausel.get(2));
                    Teilkalkuel3.setText(Endklausel.get(4));
                    Teilkalkuel4.setText(Endklausel.get(6));
                    Teilkalkuel5.setText(Endklausel.get(8));

                    erster.setText(Endklausel.get(0));
                    zweiter.setText(Endklausel.get(2));
                    dritter.setText(Endklausel.get(4));
                    vierter.setText(Endklausel.get(6));
                    fuenfter.setText(Endklausel.get(8));

                    erster.setBackgroundColor(Color.RED);
                    fuerT1 = false;

                    zweiter.setBackgroundColor(Color.RED);
                    fuerT2 = false;

                    dritter.setBackgroundColor(Color.RED);
                    fuerT3 = false;

                    vierter.setBackgroundColor(Color.RED);
                    fuerT4 = false;

                    fuenfter.setBackgroundColor(Color.RED);
                    fuerT5 = false;

                    break;
            }
        } catch (Exception e) {

        }
        return super.onOptionsItemSelected(item);
    }

    public void ButtonBlaumacher(){
        erfuellbar.setBackgroundColor(Color.BLUE);
        nichtErfuellbar.setBackgroundColor(Color.BLUE);
        elf.setBackgroundColor(Color.BLUE);
        a.setBackgroundColor(Color.BLUE);
        b.setBackgroundColor(Color.BLUE);
        c.setBackgroundColor(Color.BLUE);
    }

    //Die Methoden speichern() und laden() sind übernommen von https://gist.github.com/codinginflow/6c13bd0d08416115798f17d45b5d8056

    public void speichern() {
        laden();

        String text = annehmer + "UserID|" + Benutzername + ";Activity|Klauselmengen;Anfangszeit|" + Anfangszeit + ";Endzeit|" + Endzeit + ";Punkte|" + punkte + "von10;\n";

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
