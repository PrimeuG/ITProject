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

import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.io.parsers.ParserException;
import org.logicng.io.parsers.PropositionalParser;
import org.logicng.solvers.MiniSat;
import org.logicng.solvers.SATSolver;
import org.logicng.transformations.dnf.DNFFactorization;

import java.util.ArrayList;
import java.util.Collections;

public class NormalFormen extends AppCompatActivity {

    public static String Aussage;
    public static String ParserText = "";
    public static String Form = "";
    public static String test = "";
    public static int fragenanzahl = 0;

    ArrayList<Boolean> DNFKNFGenerator = new ArrayList<>();


    public static int schwierigkeitsgrad;
    public TextView text;
    public TextView unterertext;


    Button klammerauf, klammerzu, d, weiter, a, b, c, loeschen, bestaetigen, und, oder, nicht;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_formen);

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
        test = "";


        schwierigkeitsgrad = 0;
        fragenanzahl = 0;
        weiter.setVisibility(View.INVISIBLE);

        schwierigkeit(schwierigkeitsgrad);
        Modus();

        if(DNFKNFGenerator.get(0)){
            text.setText("Geben Sie für folgenden Term die DNF an! " + Aussage.toString());
        }else {
            text.setText("Geben Sie für folgenden Term die KNF an! " + Aussage.toString());
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
                }catch (Exception e){

                }
            }
        });

        bestaetigen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uebernehmer = "";
                uebernehmer = unterertext.getText().toString();

                if (uebernehmer.contains("∧")) {
                    uebernehmer = uebernehmer.replaceAll("∧", "&");
                }
                if (uebernehmer.contains("∨")) {
                    uebernehmer = uebernehmer.replaceAll("∨", "\\|");
                }
                if (uebernehmer.contains("¬")) {
                    uebernehmer = uebernehmer.replaceAll("¬", "~");
                }



                if (schwierigkeitsgrad == 0) {
                    Form = Form.replaceAll("\\s+", "");
                    if (Form.equals(uebernehmer)) {
                        schwierigkeitsgrad = 1;
                        bestaetigen.setBackgroundColor(Color.GREEN);
                    } else {
                        bestaetigen.setBackgroundColor(Color.RED);
                    }
                } else if (schwierigkeitsgrad == 1) {
                    Form = Form.replaceAll("\\s+", "");
                    if (Form.equals(uebernehmer)) {
                        schwierigkeitsgrad = 2;
                        bestaetigen.setBackgroundColor(Color.GREEN);
                    } else {
                        schwierigkeitsgrad = 0;
                        bestaetigen.setBackgroundColor(Color.RED);
                    }
                } else {
                    Form = Form.replaceAll("\\s+", "");
                    if (Form.equals(uebernehmer)) {
                        bestaetigen.setBackgroundColor(Color.GREEN);
                    } else {
                        schwierigkeitsgrad = 1;
                        bestaetigen.setBackgroundColor(Color.RED);
                    }
                }
                weiter.setVisibility(View.VISIBLE);
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
                    if(DNFKNFGenerator.get(0)){
                        text.setText("Geben Sie für folgenden Term die DNF an! " + Aussage.toString());
                    }else {
                        text.setText("Geben Sie für folgenden Term die KNF an! " + Aussage.toString());
                    }

                    weiter.setVisibility(View.INVISIBLE);
                }else {
                    fragenanzahl = 0;
                    activityWechsel();
                    finish();
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

    public void viererTerm() {

        ParserText = "";

        ArrayList<String> LogikAussage = new ArrayList<String>();
        ArrayList<String> Junktoren = new ArrayList<String>();
        ArrayList<String> Negation = new ArrayList<String>();
        ArrayList<String> offeneKlammer = new ArrayList<String>();
        ArrayList<String> Variable = new ArrayList<String>();
        ArrayList<Boolean> GeschlosseneKlammer = new ArrayList<Boolean>();

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
        GeschlosseneKlammer.add(true);
        GeschlosseneKlammer.add(false);

        Collections.shuffle(Junktoren);
        Collections.shuffle(Negation);
        Collections.shuffle(Variable);
        Collections.shuffle(offeneKlammer);
        Collections.shuffle(GeschlosseneKlammer);

        LogikAussage.add(Negation.get(0));              //0
        LogikAussage.add(offeneKlammer.get(0));         //1
        if (LogikAussage.contains("(")) {
            Collections.shuffle(Negation);
            LogikAussage.add(Negation.get(0));          //2
        } else {
            LogikAussage.add("");                       //2
        }
        LogikAussage.add(Variable.get(0));              //3
        LogikAussage.add(Junktoren.get(0));             //4
        Collections.shuffle(Negation);
        LogikAussage.add(Negation.get(0));              //5
        Collections.shuffle(offeneKlammer);
        LogikAussage.add(offeneKlammer.get(0));         //6
        Collections.shuffle(Negation);
        LogikAussage.add(Negation.get(0));              //7
        LogikAussage.add(Variable.get(1));              //8

        if (LogikAussage.contains("(")) {
            if (LogikAussage.get(6).equals("(")) {
                LogikAussage.add("");                   //9
            } else {
                LogikAussage.add(")");                  //9
            }
        } else {
            LogikAussage.add("");                       //9
        }

        Collections.shuffle(Junktoren);
        LogikAussage.add(Junktoren.get(0));             //10
        Collections.shuffle(Negation);
        LogikAussage.add(Negation.get(0));              //11
        Collections.shuffle(offeneKlammer);
        if (LogikAussage.get(6).equals("(")) {
            LogikAussage.add("");                       //12
        } else {
            LogikAussage.add(offeneKlammer.get(0));     //12
        }
        if (LogikAussage.get(12).equals("(")) {
            Collections.shuffle(Negation);
            LogikAussage.add(Negation.get(0));          //13
        } else {
            LogikAussage.add("");                       //13
        }
        LogikAussage.add(Variable.get(2));              //14
        if (LogikAussage.get(6).equals("(")) {
            Collections.shuffle(GeschlosseneKlammer);
            if (GeschlosseneKlammer.get(0)) {
                LogikAussage.add(")");                  //15
            } else {
                LogikAussage.add("");                   //15
            }
        } else if (LogikAussage.get(12).equals("(")) {
            LogikAussage.add("");                       //15
        }
        Collections.shuffle(Junktoren);
        LogikAussage.add(Junktoren.get(0));             //16
        Collections.shuffle(Negation);
        LogikAussage.add(Negation.get(0));              //17
        LogikAussage.add(Variable.get(3));              //18
        if (LogikAussage.get(12).equals("(")) {
            LogikAussage.add(")");                      //19
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

}