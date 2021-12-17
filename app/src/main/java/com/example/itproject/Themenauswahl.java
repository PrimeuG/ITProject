package com.example.itproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Themenauswahl extends AppCompatActivity {

    Button KNFmenge, Aussagenlogik, Junktoren, Klauselmenge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Landscape Modus deaktiviert
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //die Themenauswahl xml wird geladen
        setContentView(R.layout.activity_themenauswahl);

        //der Button Klauselmenge wird einer ID aus der xml zugewiesen sowie der farbe Blau
        Button Klauselmenge = findViewById(R.id.button8);
        Klauselmenge.setBackgroundColor(Color.BLUE);
        Klauselmenge.setText("Klausel-\nmengen");

        //dem Button wird die Methode zugewiesen zur Activity zu wechseln
        Klauselmenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KlauselWechsel();
                finish();
            }
        });

        //der Button KNFmenge wird einer ID aus der xml zugewiesen sowie der farbe Blau
        Button KNFmenge = findViewById(R.id.button9);
        KNFmenge.setBackgroundColor(Color.BLUE);
        KNFmenge.setText("Normal-\nformen");

        //dem Button wird die Methode zugewiesen zur Activity zu wechseln
        KNFmenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KNFWechsel();
                finish();
            }
        });

        //der Button Aussagenlogik wird einer ID aus der xml zugewiesen sowie der farbe Blau
        Button Aussagenlogik = findViewById(R.id.button10);
        Aussagenlogik.setBackgroundColor(Color.BLUE);
        Aussagenlogik.setText("Prädikaten-\nlogik");

        //dem Button wird die Methode zugewiesen zur Activity zu wechseln
        Aussagenlogik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logikaussage();
                finish();
            }
        });

        //die Textview ID aus der xml wird zugewiesen und ein Text wird im Textview gesetzt
        TextView text = findViewById(R.id.textView6);
        text.setText("Bitte wählen Sie das Themengebiet aus!");

        //der Button Junktoren wird einer ID aus der xml zugewiesen sowie der farbe Blau
        Button Junktoren = findViewById(R.id.button31);
        Junktoren.setBackgroundColor(Color.BLUE);
        Junktoren.setText("Junktoren-\nübung");

        //dem Button wird die Methode zugewiesen zur Activity zu wechseln
        Junktoren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JunktorenWechsel();
                finish();
            }
        });

    }

    /*
    die Methoden sind dafuer da, um in die jeweiligen activities zu wechseln
     */
    private void KlauselWechsel(){
        Intent intent = new Intent(this, Klauselmenge.class);
        startActivity(intent);
    }

    private void KNFWechsel(){
        Intent intent = new Intent(this, NormalFormen.class);
        startActivity(intent);
    }

    private void Logikaussage(){
        Intent intent = new Intent(this, AussagenLogik.class);
        startActivity(intent);
    }

    private void JunktorenWechsel(){
        Intent intent = new Intent(this, Junktoren.class);
        startActivity(intent);
    }

}