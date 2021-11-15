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

import java.util.ArrayList;
import java.util.Collections;

public class NormalFormen extends AppCompatActivity {

    public static String Aussage;
    public static String ParserText = "";
    public static String Form = "";
    public static String test = "";
    public static int fragenanzahl = 0;
    public static String texti = "";

    ArrayList<Boolean> DNFKNFGenerator = new ArrayList<>();
    ArrayList<String> pruefListeErgebnis = new ArrayList<>();


    public static int schwierigkeitsgrad;
    public TextView text;
    public TextView unterertext;


    Button klammerauf, klammerzu, d, weiter, a, b, c, loeschen, bestaetigen, und, oder, nicht;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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


        ButtonBlaumacher();

        schwierigkeitsgrad = 0;
        fragenanzahl = 0;
        weiter.setVisibility(View.INVISIBLE);

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
                        bestaetigen.setBackgroundColor(Color.RED);
                    } else {
                        schwierigkeitsgrad = 1;
                        bestaetigen.setBackgroundColor(Color.GREEN);
                    }
                } else if (schwierigkeitsgrad == 1) {
                    if (pruefListeErgebnis.contains("false")) {
                        schwierigkeitsgrad = 0;
                        bestaetigen.setBackgroundColor(Color.RED);
                    } else {
                        schwierigkeitsgrad = 2;
                        bestaetigen.setBackgroundColor(Color.GREEN);
                    }
                } else {
                    if (pruefListeErgebnis.contains("false")) {
                        schwierigkeitsgrad = 1;
                        bestaetigen.setBackgroundColor(Color.RED);
                    } else {
                        bestaetigen.setBackgroundColor(Color.GREEN);
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
                    if (DNFKNFGenerator.get(0)) {
                        text.setText("Geben Sie für folgenden Term die DNF an! \n" + Aussage.toString());
                    } else {
                        text.setText("Geben Sie für folgenden Term die KNF an! \n" + Aussage.toString());
                    }

                    weiter.setVisibility(View.INVISIBLE);
                } else {
                    fragenanzahl = 0;
                    schwierigkeitsgrad = 0;
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

    public void ButtonBlaumacher(){

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

    }

}