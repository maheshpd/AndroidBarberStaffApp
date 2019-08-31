package com.example.androidbarberstaffapp.Interface;

import com.example.androidbarberstaffapp.model.BarberServices;

import java.util.List;

public interface IBarberServicesLoadListener {
    void onBarberServicesLoadSuccess(List<BarberServices> barberServicesList);

    void onBarberServicesLoadFailed(String message);
}
