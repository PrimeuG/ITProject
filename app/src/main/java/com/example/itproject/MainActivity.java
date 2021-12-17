package com.example.itproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;



import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String FILE_NAME = "example.txt";
    private static final int STORAGE_PERMISSION_CODE = 101;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Aufgrund dessen, das die App den Darkmode uebernimmt, wird der hintergrund dunkel, wodurch man die schrift nicht mehr richtig erkennen kann, deshalb blockiert diese Zeile den Darkmode fuer die App
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        //der Landscape Mode ist fuer diese App deaktiviert
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);


        //IDs aus der Main xml werden den Buttons zugewiesen
        Button StartButton = findViewById(R.id.StartButton);
        Button Storage = findViewById(R.id.storage);

        //Buttonfarbe auf Blau aendern, damit die App eine einheitliche Buttonfarbe haben
        StartButton.setBackgroundColor(Color.BLUE);
        Storage.setBackgroundColor(Color.BLUE);

        //der Button wird fuer das IT Projekt noch nicht gebraucht
        Storage.setVisibility(View.INVISIBLE);

        //der Button ist dafuer um in die Themenauswahl zu kommen
        StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityWechsel();
                finish();
            }
        });

        //noch nicht benoetigt
        Storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


    }

    //Wenn der StartButton betaetigt wird, wird diese Methode betaetigt und man wechselt in die Activity "Themenauswahl"
    private void activityWechsel() {
        Intent intent = new Intent(this, Themenauswahl.class);
        startActivity(intent);
    }

}

