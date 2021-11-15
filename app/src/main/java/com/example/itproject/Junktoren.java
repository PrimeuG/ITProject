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
import org.logicng.transformations.dnf.DNFFactorization;
import org.mvel2.MVEL;

import java.util.ArrayList;
import java.util.Collections;

public class Junktoren extends AppCompatActivity {

    public static String Aussage;
    public static String ParserText = "";
    public static String ergebnis = "";

    public static int fragenanzahl = 0;

    Button erfuellbar, nichterfuellbar, weiter;

    ArrayList<Boolean> Booluebernehmer = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_junktoren);


        TextView Junktorenterm, AussagenTerm;

        erfuellbar = (Button) findViewById(R.id.button28);
        nichterfuellbar = (Button) findViewById(R.id.button29);
        weiter = (Button) findViewById(R.id.button30);

        Junktorenterm = findViewById(R.id.textView9);
        AussagenTerm = findViewById(R.id.textView10);

        ButtonBlaumacher();

        fragenanzahl = 0;
        dreierTerm();
        ueberpruefen();

        Junktorenterm.setText("Ist die Aussage: \n" + Aussage + "\nin der unten beschriebenen Konstellation lösbar? ");
        AussagenTerm.setText("a = "+ Booluebernehmer.get(0) +" b = "+ Booluebernehmer.get(1) +" c = "+ Booluebernehmer.get(2) );

        weiter.setVisibility(View.INVISIBLE);

        erfuellbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ergebnis.equals("true")){
                    erfuellbar.setBackgroundColor(Color.GREEN);
                    nichterfuellbar.setBackgroundColor(Color.RED);
                }else {
                    erfuellbar.setBackgroundColor(Color.RED);
                    nichterfuellbar.setBackgroundColor(Color.GREEN);
                }

                weiter.setVisibility(View.VISIBLE);
            }
        });

        nichterfuellbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ergebnis.equals("true")){
                    erfuellbar.setBackgroundColor(Color.GREEN);
                    nichterfuellbar.setBackgroundColor(Color.RED);
                }else {
                    erfuellbar.setBackgroundColor(Color.RED);
                    nichterfuellbar.setBackgroundColor(Color.GREEN);
                }
                weiter.setVisibility(View.VISIBLE);
            }
        });

        weiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fragenanzahl < 9){
                    fragenanzahl++;
                    Booluebernehmer.clear();
                    AussagenTerm.setText("");
                    Junktorenterm.setText("");
                    Aussage = "";
                    ParserText = "";
                    erfuellbar.setBackgroundColor(Color.BLUE);
                    nichterfuellbar.setBackgroundColor(Color.BLUE);
                    dreierTerm();
                    ueberpruefen();
                    Junktorenterm.setText("Ist die Aussage: \n" + Aussage + "\nin der unten beschriebenen Konstellation lösbar? ");
                    AussagenTerm.setText("a = "+ Booluebernehmer.get(0) +" b = "+ Booluebernehmer.get(1) +" c = "+ Booluebernehmer.get(2) );
                    weiter.setVisibility(View.INVISIBLE);
                }else {
                    fragenanzahl = 0;
                    activityWechsel();
                    finish();
                }

            }
        });


    }

    public void dreierTerm(){

        ParserText = "";

        ArrayList<String> LogikAussage = new ArrayList<String>();
        ArrayList<String> Junktoren = new ArrayList<String>();
        ArrayList<String> Negation = new ArrayList<String>();
        ArrayList<String> offeneKlammer = new ArrayList<String>();
        ArrayList<String> Variable = new ArrayList<String>();

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
        Variable.add("c");

        Collections.shuffle(Junktoren);
        Collections.shuffle(Negation);
        Collections.shuffle(Variable);
        Collections.shuffle(offeneKlammer);

        LogikAussage.add(Negation.get(0));
        LogikAussage.add(offeneKlammer.get(0));
        if (LogikAussage.contains("(")) {
            Collections.shuffle(Negation);
            LogikAussage.add(Negation.get(0));
        }
        LogikAussage.add(Variable.get(0));
        LogikAussage.add(Junktoren.get(0));
        Collections.shuffle(Negation);
        LogikAussage.add(Negation.get(0));
        if(!LogikAussage.contains("(")){
            Collections.shuffle(offeneKlammer);
            LogikAussage.add(offeneKlammer.get(0));
            if (LogikAussage.contains("(")){
                Collections.shuffle(Negation);
                LogikAussage.add(Negation.get(0));
            }
        }
        LogikAussage.add(Variable.get(1));
        if (LogikAussage.contains("(")){
            if(LogikAussage.indexOf("(")==1){
                LogikAussage.add(")");
            }
        }
        Collections.shuffle(Junktoren);
        LogikAussage.add(Junktoren.get(0));
        Collections.shuffle(Negation);
        LogikAussage.add(Negation.get(0));
        LogikAussage.add(Variable.get(2));
        if(LogikAussage.contains("(")){
            if (LogikAussage.indexOf("(") > 1){
                LogikAussage.add(")");
            }
        }

        for(int i = 0 ; i < LogikAussage.size(); i++){
            ParserText+=LogikAussage.get(i);
        }

        Aussage = ParserText;

        if(LogikAussage.contains("=>")){
            Aussage = Aussage.replaceAll("=>","->");
        }
        if (LogikAussage.contains("<=>")){
            Aussage = Aussage.replaceAll("<=>","<->");
        }
        if (LogikAussage.contains("&")){
            Aussage = Aussage.replaceAll("&","∧");
        }
        if (LogikAussage.contains("|")){
            Aussage = Aussage.replaceAll("\\|","∨");
        }
        if (LogikAussage.contains("~")){
            Aussage = Aussage.replaceAll("~","¬");
        }

    }

    public void ueberpruefen(){

        ArrayList<Boolean> Randombooler = new ArrayList<Boolean>();
        Randombooler.clear();

        Randombooler.add(true);
        Randombooler.add(false);

        try {
            final FormulaFactory f = new FormulaFactory();
            final PropositionalParser p = new PropositionalParser(f);
            final Formula formula = p.parse(ParserText);
            final DNFFactorization dnfFactorization = new DNFFactorization();
            Formula DNFFORM = dnfFactorization.apply(formula,true);
            String DNFinSTRING = DNFFORM.toString();
            if(DNFinSTRING.contains("~")){
                DNFinSTRING=DNFinSTRING.replaceAll("~", "!");
            }
            if(DNFinSTRING.contains("|")){
                DNFinSTRING=DNFinSTRING.replaceAll("\\|", "||");
            }
            if(DNFinSTRING.contains("&")){
                DNFinSTRING=DNFinSTRING.replaceAll("&", "&&");
            }
            if(DNFinSTRING.contains("a")){
                Collections.shuffle(Randombooler);
                Booluebernehmer.add(Randombooler.get(0));
                DNFinSTRING=DNFinSTRING.replaceAll("a", Randombooler.get(0).toString());
            }
            if(DNFinSTRING.contains("b")){
                Collections.shuffle(Randombooler);
                Booluebernehmer.add(Randombooler.get(0));
                DNFinSTRING=DNFinSTRING.replaceAll("b",Randombooler.get(0).toString());
            }
            if(DNFinSTRING.contains("c")){
                Collections.shuffle(Randombooler);
                Booluebernehmer.add(Randombooler.get(0));
                DNFinSTRING=DNFinSTRING.replaceAll("c",Randombooler.get(0).toString());
            }

            String finalParser = DNFinSTRING;
            ergebnis = MVEL.evalToString(finalParser);

        } catch (ParserException e) {
            e.printStackTrace();
        }

    }

    private void activityWechsel() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.spiel_menu2,menu);
        return true;
    }
    //Für Neustart und Endscreen, es werden verschiedene Dinge resettet
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            switch (item.getItemId()){
                case R.id.cancel2:
                    fragenanzahl = 0;
                    activityWechsel();
                    finish();
                    break;

            }
        }catch (Exception e){

        }
        return super.onOptionsItemSelected(item);
    }

    public void ButtonBlaumacher(){

        erfuellbar.setBackgroundColor(Color.BLUE);
        nichterfuellbar.setBackgroundColor(Color.BLUE);
        weiter.setBackgroundColor(Color.BLUE);



    }

}