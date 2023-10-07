package com.example.weather;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private AppCompatButton button;
    int count = 0;

    @Override
    protected void onStart() {
        super.onStart();
        try {
            File file = new File(getFilesDir(), "flag.txt"); // Use getFilesDir() to get the app's file directory
            if (file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));

                String line = br.readLine();
                if (line != null && !line.isEmpty()) {
                    Intent it = new Intent(MainActivity.this, Weather_Home.class);
                    startActivity(it);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.letsGoBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count++;
                try {
                    File file = new File(getFilesDir(), "flag.txt"); // Use getFilesDir() to get the app's file directory
                    BufferedWriter bw = new BufferedWriter(new FileWriter(file));
                    bw.write(String.valueOf(count));
                    bw.close(); // Make sure to close the BufferedWriter

                    Intent it = new Intent(MainActivity.this, Weather_Home.class);
                    startActivity(it);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
