package com.ppkhan.gps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    LocationFinder finder;
    int REQUEST_CODE = 401;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // check permission
        CheckPermission();

    }

    private void CheckPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            // reuqest for permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        } else {
            startServiceForLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE){
            if (grantResults.length > 0){
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Do the stuff that requires permission...
                    startServiceForLocation();
                }else if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        PleaseGivePermission();
                    }else{

                        Toast.makeText(MainActivity.this, "That's it, you afre a dead man walking, Ill come after you Nowhere to hide. " , Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }

    private void startServiceForLocation() {
        double longitude = 0.0, latitude = 0.0;
        finder = new LocationFinder(this);
        if (finder.canGetLocation()) {
            latitude = finder.getLatitude();
            longitude = finder.getLongitude();
            if(latitude==0.0){
                startServiceForLocation();
            }
            Toast.makeText(this, "lat-lng " + latitude + " — " + longitude, Toast.LENGTH_LONG).show();
        } else {
            finder.showSettingsAlert();
        }
    }

    public void PleaseGivePermission() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Dear User");
        alertDialog.setMessage("Im gonna ask you one more time..Can you or can you not give the damn permission?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                CheckPermission();
            }
        });
        alertDialog.setNegativeButton("Damn NO ", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
//                CheckPermission();
                Toast.makeText(MainActivity.this, "Fuck You!!! " , Toast.LENGTH_SHORT).show();
            }
        });
        alertDialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        CheckPermission();
    }
}