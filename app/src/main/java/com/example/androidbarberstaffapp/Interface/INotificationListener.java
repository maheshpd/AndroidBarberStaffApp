package com.example.androidbarberstaffapp.Interface;

import com.example.androidbarberstaffapp.model.MyNotification;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.List;

public interface INotificationListener {
    void onNotificationLoadSuccess(List<MyNotification> myNotificationList, DocumentSnapshot lastDocument);
    void onNotificationLoadFailed(String message);
}
