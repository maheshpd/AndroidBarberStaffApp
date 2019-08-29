package com.example.androidbarberstaffapp.Interface;

import com.example.androidbarberstaffapp.model.BookingInformation;

import java.util.List;

public interface ITimeSlotLoadListener {
     void onTimeSlotLoadSuccess(List<BookingInformation> timeSlots);
     void onTimeSlotLoadFailed(String message);
     void onTimeSlotLoadEmpty();
}
