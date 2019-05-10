package com.bono;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bono.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {

    private final int REGISTER_ID = 0x43f2;
    private final int LOCATION_ID = 0x4a2c;

    private MainActivityController mainActivityController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mainActivityController = new MainActivityController();
        mainActivityController.InitializeBinding(activityMainBinding);

        final Button startSessionButton = findViewById(R.id.startButton);
        //startSessionButton.setText(R.string.)

        startSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mainActivityController.isStarted()) {
                    mainActivityController.StartSession();
                    startSessionButton.setText(R.string.finish_session);
                }
                else {
                    try {
                        mainActivityController.FinishSession();
                        startSessionButton.setText(R.string.start_session);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        Context applicationContext = getApplicationContext();

        setupStepsDetector(applicationContext);
        setupVelocityDeterminer(applicationContext, true);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == LOCATION_ID) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupVelocityDeterminer(getApplicationContext(), false);
            }
        }
    }

    private void setupStepsDetector(Context context) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            Sensor stepsDetector = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);

            sensorManager.registerListener(mainActivityController, stepsDetector, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    private void setupVelocityDeterminer(Context context, boolean requestPermission) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if(requestPermission)
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_ID);

            return;
        }

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mainActivityController);
    }

    /*
    SIGNING IN AND REGISTERING
    CODE NOT IN USE CURRENTLY

    public void openRegisterIntent(View view) {
        List<AuthUI.IdpConfig> providers = Collections.singletonList(
                new AuthUI.IdpConfig.EmailBuilder().build());

        startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers).build(), REGISTER_ID);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REGISTER_ID) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
        }
    }

    */
}
