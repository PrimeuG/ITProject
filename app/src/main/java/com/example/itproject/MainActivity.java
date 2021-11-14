package com.example.itproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.logicng.datastructures.Tristate;
import org.logicng.formulas.Formula;
import org.logicng.formulas.FormulaFactory;
import org.logicng.io.parsers.ParserException;
import org.logicng.io.parsers.PropositionalParser;
import org.logicng.solvers.MiniSat;
import org.logicng.solvers.SATSolver;
import org.logicng.transformations.dnf.DNFFactorization;
import org.logicng.util.FormulaRandomizerConfig;
import org.mvel2.MVEL;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FormulaFactory f = new FormulaFactory();
        final FormulaRandomizerConfig config = FormulaRandomizerConfig.builder().seed(45).build();
        final PropositionalParser p = new PropositionalParser(f);
        final Formula formula;
        try {
            formula = p.parse("(~A<=>B) <=> ~C");
            final Formula nnf = formula.nnf();

            final Formula cnf = formula.cnf();
            final SATSolver miniSat = MiniSat.miniSat(f);
            miniSat.add(formula);
            final DNFFactorization dnfFactorization = new DNFFactorization();
            Formula test = dnfFactorization.apply(formula,true);
            System.out.println(test.toString());


            final Tristate result = miniSat.sat();
            System.out.println(result);
            System.out.println(nnf);
            System.out.println(cnf);



            String tes1 = test.toString();
            String finalParser = tes1.replaceAll("~", "!").replaceAll("\\|", "||").replaceAll("&", "&&").replaceAll("A", "false").replaceAll("B", "false").replaceAll("C","true");
            System.out.println(finalParser);
            String ergebnis = MVEL.evalToString(finalParser);
            System.out.println(ergebnis);
        } catch (ParserException e) {
            e.printStackTrace();
        }




        Button StartButton = findViewById(R.id.StartButton);

        StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityWechsel();
                finish();
            }
        });

    }

    private void activityWechsel(){
        Intent intent = new Intent(this, Themenauswahl.class);
        startActivity(intent);
    }
}