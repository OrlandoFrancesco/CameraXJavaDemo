package com.orlandofrancesco.cameraxjavademo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.security.Permission;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button photoNavButton = findViewById(R.id.photoNavButton);
        Button videoNavButton = findViewById(R.id.videoNavButton);
        Button scannerNavActivity = findViewById(R.id.scannerNavButton);

        Intent photoNanIntent = new Intent( getApplicationContext(), PhotoActivity.class);
        Intent videoNavIntent = new Intent( getApplicationContext(), VideoActivity.class);
        Intent scannerNanIntent = new Intent( getApplicationContext(), ScannerActivity.class);

        photoNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(photoNanIntent);
            }
        });

        videoNavButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(videoNavIntent);
            }
        });

        scannerNavActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(scannerNanIntent);
            }
        });

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
    }
}