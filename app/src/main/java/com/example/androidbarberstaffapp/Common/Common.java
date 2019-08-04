package com.example.androidbarberstaffapp.Common;

import com.example.androidbarberstaffapp.model.Barber;
import com.example.androidbarberstaffapp.model.Salon;

import java.io.Console;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Common {
    public static final Object DISABLE_TAG = "DISABLE";
    public static final int TIME_SLOT_TOTAL = 20;
    public static final String LOGGED_KEY =  "LOGGED";
    public static final String STATE_KEY = "STATE";
    public static final String SALON_KEY = "SALON";
    public static final String BARBER_KEY = "BARBER";
    public static String state_name="";
    public static Salon selected_salon;
    public static Barber currentBarber;
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd_MM_yyyy");
    public static Calendar bookingDate = Calendar.getInstance() ;


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
}
