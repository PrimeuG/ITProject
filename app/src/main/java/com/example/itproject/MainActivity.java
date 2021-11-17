package com.example.itproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import com.opencsv.CSVWriter;

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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);


        Button StartButton = findViewById(R.id.StartButton);
        Button Storage = findViewById(R.id.storage);
        StartButton.setBackgroundColor(Color.BLUE);
        Storage.setBackgroundColor(Color.BLUE);

        StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityWechsel();
                finish();
            }
        });

        Storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
            }
        });


    }

    private void activityWechsel() {
        Intent intent = new Intent(this, Themenauswahl.class);
        startActivity(intent);
    }

   /* public void checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission }, requestCode);
        }
        else {
            Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();

          try {
              String name1 = "Hallo";
              String no1 = "bioer";
              File file = new File("/sdcard/test/");
              file.mkdirs();
              String csv = "/sdcard/test/a.csv";
              CSVWriter csvWriter = new CSVWriter(new FileWriter(csv,true));
              String row[] = new String[]{name1,no1};
              csvWriter.writeNext(row);
              csvWriter.close();
              Toast.makeText(MainActivity.this, "WURDE ERSTELLT", Toast.LENGTH_SHORT).show();
          }catch (Exception e){

          }

        }
    }*/

  /*  public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(MainActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }*/



}

