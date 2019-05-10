package com.bono;

import android.databinding.ObservableField;
import android.databinding.ObservableFloat;

public class MainActivityModel {

    public ObservableField<String> ExpiredTime;
    public ObservableFloat TravelledDistance;
    public ObservableFloat CurrentSpeed;
    public ObservableFloat MaxSpeed;

    MainActivityModel() {
        ExpiredTime = new ObservableField<>("00:00:00");
        TravelledDistance = new ObservableFloat(0);
        CurrentSpeed = new ObservableFloat(0);
        MaxSpeed = new ObservableFloat(0);
    }
}
