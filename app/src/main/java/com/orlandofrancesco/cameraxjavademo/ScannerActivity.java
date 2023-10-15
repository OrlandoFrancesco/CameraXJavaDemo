package com.orlandofrancesco.cameraxjavademo;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ScannerActivity extends AppCompatActivity {

    PreviewView cameraPreview;
    CameraSelector cameraSelector;
    ImageCapture imageCapture = null;

//    boolean test = false;
    CountDownTimer countDownTimer;

    ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    void bindPreview(@NonNull ProcessCameraProvider cameraProvider) {
        Preview preview = new Preview.Builder().build();
        preview.setSurfaceProvider(cameraPreview.getSurfaceProvider());

        cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        preview.setSurfaceProvider(cameraPreview.getSurfaceProvider());
        cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview);


        imageCapture = new ImageCapture.Builder().build();

        try {
            cameraProvider = cameraProviderFuture.get();
        } catch (Exception e) { }

        cameraProvider.bindToLifecycle((LifecycleOwner) this,
                cameraSelector,
                imageCapture,
                preview);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        cameraPreview = findViewById(R.id.cameraPreview);

        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
                bindPreview(cameraProvider);
            } catch (ExecutionException | InterruptedException e) { }
        }, ContextCompat.getMainExecutor(this));

        //barcode
        BarcodeScannerOptions barcodeScannerOptions =new BarcodeScannerOptions.Builder()
                .setBarcodeFormats( Barcode.FORMAT_QR_CODE, Barcode.FORMAT_AZTEC)
                .build();

        BarcodeScanner scanner = BarcodeScanning.getClient(barcodeScannerOptions);

//        countDownTimer = new CountDownTimer(1000,3000) {
//            @Override
//            public void onTick(long millisUntilFinished) { }
//
//            @Override
//            public void onFinish() {
//                InputImage inputImage = InputImage.fromBitmap(cameraPreview.getBitmap(), 0);
//                scanner.process(inputImage).addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
//                    @Override
//                    public void onSuccess(List<Barcode> barcodes) {
//                        try {
//                            Toast.makeText(ScannerActivity.this, barcodes.get(0).getRawValue().toString(), Toast.LENGTH_SHORT).show();
//                            test = true;
//                        } catch (Exception e){
//                            Toast.makeText(ScannerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//
////                if (!test){
////                    countDownTimer.start();
////                }
//
//                countDownTimer.start();
//            }
//        };
//        countDownTimer.start();

        Button scanButton = findViewById(R.id.scanButton);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    InputImage inputImage = InputImage.fromBitmap(cameraPreview.getBitmap(), 0);
                    scanner.process(inputImage).addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                        @Override
                        public void onSuccess(List<Barcode> barcodes) {
                            try {
                                Toast.makeText(ScannerActivity.this, barcodes.get(0).getRawValue().toString(), Toast.LENGTH_SHORT).show();
                            } catch (Exception e){
                                Toast.makeText(ScannerActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } catch (Exception e){
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}