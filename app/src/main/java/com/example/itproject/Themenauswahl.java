package com.example.itproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Themenauswahl extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_themenauswahl);

        Button Klauselmenge = findViewById(R.id.button8);
        Klauselmenge.setText("Klauselmengen");

        Klauselmenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KlauselWechsel();
                finish();
            }
        });

        Button KNFmenge = findViewById(R.id.button9);
        KNFmenge.setText("Normalformen");

        KNFmenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KNFWechsel();
                finish();
            }
        });

        Button Aussagenlogik = findViewById(R.id.button10);
        Aussagenlogik.setText("Aussagenlogik");

        Aussagenlogik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logikaussage();
                finish();
            }
        });

        TextView text = findViewById(R.id.textView6);
        text.setText("Bitte wählen Sie das Themengebiet aus!");

        Button Junktoren = findViewById(R.id.button31);
        Junktoren.setText("Junktorenübung");

        Junktoren.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JunktorenWechsel();
                finish();
            }
        });


    }

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