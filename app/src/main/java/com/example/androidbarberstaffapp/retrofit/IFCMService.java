package com.example.androidbarberstaffapp.retrofit;

import com.example.androidbarberstaffapp.model.FCMResponse;
import com.example.androidbarberstaffapp.model.FCMSendData;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMService {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAX-uZsY0:APA91bGTfY8fPmQlwhTYhhu4-oq11LbBVvdK6HQepVs7vtA3w0ly-RSnKolIg8ZeOueRso1P-a1wx6F-WD-hquGZvJU6015gsMNE_ktRQrHmukZGwSsVUOTnEC45DoiVUmJC0IWqJu8X"
    })

    @POST("fcm/send")
    Observable<FCMResponse> sendNotification(@Body FCMSendData body);
}
