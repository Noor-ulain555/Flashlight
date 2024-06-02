 package com.noorulaain.flashlight;
 import android.Manifest;
 import android.annotation.SuppressLint;
 import android.content.pm.PackageManager;
 import android.hardware.camera2.CameraAccessException;
 import android.hardware.camera2.CameraManager;
 import android.os.Bundle;
 import android.view.View;
 import android.widget.Button;
 import android.widget.ImageButton;
 import android.widget.ImageView;
 import android.widget.Toast;

 import androidx.annotation.NonNull;
 import androidx.appcompat.app.AppCompatActivity;
 import androidx.constraintlayout.widget.ConstraintLayout;
 import androidx.core.app.ActivityCompat;
 import androidx.core.content.ContextCompat;

 public class MainActivity extends AppCompatActivity {

     private ImageView buttonToggle;
     private CameraManager cameraManager;
     private String cameraId;

     private ConstraintLayout constraintLayout;
     private boolean isFlashOn = false;

     private static final int CAMERA_REQUEST = 50;

     @SuppressLint("MissingInflatedId")
     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_main);

         buttonToggle = findViewById(R.id.button_toggle);
         cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
         constraintLayout=findViewById(R.id.cl);

         try {
             cameraId = cameraManager.getCameraIdList()[0];
         } catch (CameraAccessException e) {
             e.printStackTrace();
         }

         buttonToggle.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                         == PackageManager.PERMISSION_GRANTED) {
                     toggleFlashlight();
                 } else {
                     ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
                 }
             }
         });
     }

     private void toggleFlashlight() {
         try {
             if (isFlashOn) {
                 cameraManager.setTorchMode(cameraId, false);
                 isFlashOn = false;
                 buttonToggle.setImageResource(R.drawable.torchoff);
                 Toast.makeText(this, "FlashLight is Off", Toast.LENGTH_SHORT).show();
                 constraintLayout.setBackgroundColor(ContextCompat.getColor
                         (MainActivity.this, android.R.color.holo_red_light)); // or any default color


             } else {
                 cameraManager.setTorchMode(cameraId, true);
                 isFlashOn = true;
                 buttonToggle.setImageResource(R.drawable.torchon);
                 Toast.makeText(this, "FlashLight is On", Toast.LENGTH_SHORT).show();
                 constraintLayout.setBackgroundColor(ContextCompat.getColor
                         (MainActivity.this, android.R.color.holo_purple)); // or any default color



             }
         } catch (CameraAccessException e) {
             e.printStackTrace();
         }
     }

     @Override
     public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults);
         if (requestCode == CAMERA_REQUEST) {
             if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                 toggleFlashlight();
             } else {
                 Toast.makeText(this, "Camera permission is required to use flashlight", Toast.LENGTH_SHORT).show();
             }
         }
     }
 }