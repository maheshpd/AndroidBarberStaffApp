package com.example.androidbarberstaffapp.Interface;

import com.example.androidbarberstaffapp.model.TimeSlot;

import java.util.List;

public interface ITimeSlotLoadListener {
     void onTimeSlotLoadSuccess(List<TimeSlot> timeSlots);
     void onTimeSlotLoadFailed(String message);
     void onTimeSlotLoadEmpty();
}
