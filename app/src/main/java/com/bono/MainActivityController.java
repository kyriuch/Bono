package com.bono;

import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.SystemClock;

import com.bono.databinding.ActivityMainBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivityController implements SensorEventListener, LocationListener {

    private MainActivityModel mainActivityModel;
    private List<IntervalData> intervalDatas;

    private boolean started;
    private float startTimestamp;
    private int steps;

    private Location lastLocation;

    private Thread timerTask;
    private Thread dataTask;

    MainActivityController() {
        mainActivityModel = new MainActivityModel();
        started = false;
    }

    void InitializeBinding(ActivityMainBinding dataBinding) {
        dataBinding.setMainActivityModel(mainActivityModel);
    }

    void StartSession() {
        started = true;

        // reset values
        steps = 0;
        mainActivityModel.ExpiredTime.set("00:00:00");
        mainActivityModel.TravelledDistance.set(0f);
        mainActivityModel.MaxSpeed.set(0f);
        mainActivityModel.CurrentSpeed.set(0f);
        intervalDatas = new ArrayList<>();

        startTimestamp = SystemClock.elapsedRealtime() / 1000f;

        timerTask = new Thread() {

            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                while(started) {
                    float expiredTime = (SystemClock.elapsedRealtime() / 1000f) - startTimestamp;

                    int hours = (int)(expiredTime / 3600);
                    int minutes = (int)(expiredTime / 60);
                    int seconds = (int)(expiredTime % 60);

                    mainActivityModel.ExpiredTime.set(String.format("%02d:%02d:%02d", hours, minutes, seconds));
                }
            }
        };

        timerTask.start();

        dataTask = new Thread() {

            @SuppressLint("DefaultLocale")
            @Override
            public void run() {
                while(started) {
                    try {
                        Thread.sleep(60000);

                        float expiredTime = (SystemClock.elapsedRealtime() / 1000f) - startTimestamp;

                        int hours = (int)(expiredTime / 3600);
                        int minutes = (int)(expiredTime / 60);

                        IntervalData intervalData = new IntervalData();

                        intervalData.setCurrentTime(String.format("%02d:%02d:00", hours, minutes));
                        intervalData.setCurrentDistance(mainActivityModel.TravelledDistance.get());
                        intervalData.setCurrentAltitude((int) lastLocation.getAltitude());
                        intervalData.setCurrentSpeed(lastLocation.getSpeed());
                        intervalData.setCurrentSteps(steps);

                        intervalDatas.add(intervalData);
                    } catch (InterruptedException e) {

                    }
                }
            }
        };

        dataTask.start();
    }

    @SuppressLint("DefaultLocale")
    void FinishSession() throws InterruptedException {
        started = false;
        timerTask.join();
        dataTask.interrupt();

        float expiredTime = (SystemClock.elapsedRealtime() / 1000f) - startTimestamp;

        int hours = (int)(expiredTime / 3600);
        int minutes = (int)(expiredTime / 60);
        int seconds = (int)(expiredTime % 60);

        IntervalData intervalData = new IntervalData();

        String sExpiredTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);

        intervalData.setCurrentTime(sExpiredTime);
        intervalData.setCurrentDistance(mainActivityModel.TravelledDistance.get());
        intervalData.setCurrentAltitude((int) lastLocation.getAltitude());
        intervalData.setCurrentSpeed(lastLocation.getSpeed());
        intervalData.setCurrentSteps(steps);

        intervalDatas.add(intervalData);

        SessionData sessionData = new SessionData();
        sessionData.setExpiredTime(sExpiredTime);
        sessionData.setMaxSpeed(mainActivityModel.MaxSpeed.get());
        sessionData.setTravelledDistance(mainActivityModel.TravelledDistance.get());
        sessionData.setIntervalDatas(intervalDatas);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference sessionsReference = database.getReference("sessions");
        DatabaseReference newSessionReference = sessionsReference.push();
        newSessionReference.setValue(sessionData);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(!started)
            return;

        if(event.sensor.getType() != Sensor.TYPE_STEP_DETECTOR)
            return;

        steps++;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onLocationChanged(Location location) {
        if(!started)
            return;

        float currentVelocity = location.getSpeed() * 3600f / 1000f;

        mainActivityModel.CurrentSpeed.set(currentVelocity);
        mainActivityModel.MaxSpeed.set(Math.max(mainActivityModel.MaxSpeed.get(), currentVelocity));

        if(lastLocation != null) {
            mainActivityModel.TravelledDistance.set(mainActivityModel.TravelledDistance.get() +
                    (location.distanceTo(lastLocation) / 1000f));
        }

        lastLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public boolean isStarted() {
        return started;
    }
}
