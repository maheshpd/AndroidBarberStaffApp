package com.example.androidbarberstaffapp.Interface;

import com.example.androidbarberstaffapp.model.City;

import java.util.List;

public interface IOnAllStateLoadListener {
    void onAllStateLoadSuccess(List<City> cityList);
    void onAllStateLoadFailed(String message);
}
