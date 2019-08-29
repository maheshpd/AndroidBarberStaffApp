package com.example.androidbarberstaffapp.Common;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.androidbarberstaffapp.R;
import com.example.androidbarberstaffapp.model.Barber;
import com.example.androidbarberstaffapp.model.BookingInformation;
import com.example.androidbarberstaffapp.model.MyToken;
import com.example.androidbarberstaffapp.model.Salon;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import io.paperdb.Paper;

public class Common {
    public static final Object DISABLE_TAG = "DISABLE";
    public static final int TIME_SLOT_TOTAL = 20;
    public static final String LOGGED_KEY =  "LOGGED";
    public static final String STATE_KEY = "STATE";
    public static final String SALON_KEY = "SALON";
    public static final String BARBER_KEY = "BARBER";
    public static final String TITLE_KEY = "title";
    public static final String CONTENT_KEY = "content";
    public static final int MAX_NOTIFICATION_PER_LOAD = 10;
    public static String state_name="";
    public static Salon selected_salon;
    public static Barber currentBarber;
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");
    public static Calendar bookingDate = Calendar.getInstance() ;
    public static BookingInformation currentBookingInformation;


    public static String convertTimeSlotToString(int slot) {
        switch (slot)
        {
            case 0:
                return "9:0-9:30";
            case 1:
                return "9:30-10:00";
            case 2:
                return "10:00-10:30";
            case 3:
                return "10:30-11:00";
            case 4:
                return "11:00-11:30";
            case 5:
                return "11:30-12:00";
            case 6:
                return "12:00-12:30";
            case 7:
                return "12:30-01:00";
            case 8:
                return "01:00-01:30";
            case 9:
                return "01:30-2:00";
            case 10:
                return "2:00-02:30";
            case 11:
                return "02:30-03:00";
            case 12:
                return "03:00-03:30";
            case 13:
                return "3:30-04:00";
            case 14:
                return "04:00-04:30";
            case 15:
                return "04:30-05:00";
            case 16:
                return "05:00-05:30";
            case 17:
                return "05:30-06:00";
            case 18:
                return "06:00-06:30";
            case 19:
                return "06:30-07:00";
            default:
                return "Closed";
        }
    }

    public static void showNotification(Context context, int notification_id, String title, String content, Intent intent) {
        PendingIntent pendingIntent = null;
        if (intent != null)
            pendingIntent = PendingIntent.getActivity(context,
                    notification_id,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
        String NOTIFICATION_CHANNEL_ID = "edmt_barber_booking_channel_01";
        NotificationManager notificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    "EDMTV Barber Booking Staff App", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Staff app");
            notificationChannel.enableVibration(true);
            notificationChannel.enableLights(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context,NOTIFICATION_CHANNEL_ID);
        builder.setContentTitle(title)
                .setContentText(content)
                .setAutoCancel(false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),R.mipmap.ic_launcher));

        if (pendingIntent != null)
            builder.setContentIntent(pendingIntent);
        Notification notification = builder.build();
        notificationManager.notify(notification_id,notification);
    }

    public enum TOKEN_TYPE {
        CLIENT,
        BARBER,
        MANAGER
    }



    public static void updateToken(Context context, String token) {
        //First, we need check if user still login
        //Because, we need store token belonging user
        //So, we need user store data
        Paper.init(context );
        String user = Paper.book().read(Common.LOGGED_KEY);
        if (user != null)
        {
            if (!TextUtils.isEmpty(user))
            {
                MyToken myToken = new MyToken();
                myToken.setToken(token);
                myToken.setToken_type(TOKEN_TYPE.BARBER); //Because this vode run from Barber Staff app
                myToken.setUserPhone(user);

                //Submit on Firestore
                FirebaseFirestore.getInstance()
                        .collection("Tokens")
                        .document(user)
                        .set(myToken)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        });
            }
        }
    }
}
