package com.example.androidbarberstaffapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidbarberstaffapp.Common.Common;
import com.example.androidbarberstaffapp.Interface.IRecycleItemSelectedListener;
import com.example.androidbarberstaffapp.R;
import com.example.androidbarberstaffapp.activity.DoneServicesActivity;
import com.example.androidbarberstaffapp.model.BookingInformation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MyTimeSlotAdapter extends RecyclerView.Adapter<MyTimeSlotAdapter.MyTimeSlotViewHolder> {

    Context context;
    List<BookingInformation> timeSlotList;
    List<CardView> cardViewList;


    public MyTimeSlotAdapter(Context context) {
        this.context = context;
        this.timeSlotList = new ArrayList<>();
        cardViewList = new ArrayList<>();
    }

    public MyTimeSlotAdapter(Context context, List<BookingInformation> timeSlotList) {
        this.context = context;
        this.timeSlotList = timeSlotList;
        cardViewList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyTimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_time_slot, parent, false);
        return new MyTimeSlotViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTimeSlotViewHolder holder, int position) {
        holder.txt_time_slot.setText(new StringBuilder(Common.convertTimeSlotToString(position)).toString());
        if (timeSlotList.size() == 0) //If all position is available, just show list
        {
            holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
            holder.txt_time_slot_description.setText("Available");
            holder.txt_time_slot_description.setTextColor(context.getResources().getColor(android.R.color.black));
            holder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.black));
//            holder.card_time_slot.setEnabled(true);

            holder.setiRecycleItemSelectedListener(new IRecycleItemSelectedListener() {
                @Override
                public void onItemSelected(View view, int position) {

                }
            });
        } else //If have position is full (booked)
        {
            for (BookingInformation slotValue : timeSlotList) {
                //Loop all time slot from server and set different color
                int slot = Integer.parseInt(slotValue.getSlot().toString());
                if (slot == position) //If slot == position
                {
                    //We will set tag for all time slot is full
                    //So base on tag, we set all remain card background without change full time slot
                    holder.card_time_slot.setTag(Common.DISABLE_TAG);
                    holder.card_time_slot.setCardBackgroundColor(context.getResources().getColor(android.R.color.holo_blue_bright));
                    holder.txt_time_slot_description.setText("Full");
                    holder.txt_time_slot_description.setTextColor(context.getResources().getColor(android.R.color.white));
                    holder.txt_time_slot.setTextColor(context.getResources().getColor(android.R.color.white));

//                    holder.card_time_slot.setEnabled(true);

                    holder.setiRecycleItemSelectedListener(new IRecycleItemSelectedListener() {
                        @Override
                        public void onItemSelected(View view, int position) {
                            // Only add for gray time slot
                            //Here we will get Booking Information and store in Common.currentBookingInformation
                            //After that, start DoneServicesActivity

                            FirebaseFirestore.getInstance()
                                    .collection("AllSalon")
                                    .document(Common.state_name)
                                    .collection("Branch")
                                    .document(Common.selected_salon.getSalonId())
                                    .collection("Barbers")
                                    .document(Common.currentBarber.getBarberId())
                                    .collection(Common.simpleDateFormat.format(Common.bookingDate.getTime()))
                                    .document(slotValue.getSlot().toString())
                                    .get()
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        if (task.getResult().exists()) {
                                            Common.currentBookingInformation = task.getResult().toObject(BookingInformation.class);
                                            context.startActivity(new Intent(context, DoneServicesActivity.class));
                                        }
                                    }
                                }
                            });

                        }
                    });
                } else {
                    holder.setiRecycleItemSelectedListener(new IRecycleItemSelectedListener() {
                        @Override
                        public void onItemSelected(View view, int position) {

                        }
                    });
                }
            }
        }

        //Add all card to list (20 card because we have 20 time slot)
        //No add card already in cardViewList
        if (!cardViewList.contains(holder.card_time_slot))
            cardViewList.add(holder.card_time_slot);

        //Check if card time slot is available
//        if (!timeSlotList.contains(position)) {
//            holder.setiRecycleItemSelectedListener(new IRecycleItemSelectedListener() {
//                @Override
//                public void onItemSelected(View view, int position) {
//                    //Loop all card in card List
//                    for (CardView cardView : cardViewList) {
//                        if (cardView.getTag() == null) //Only available card time slot be change
//                            cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
//                    }
//                    //Our selected card will be change color
//                    holder.card_time_slot.setCardBackgroundColor(context.getResources()
//                            .getColor(android.R.color.holo_orange_dark));
//
//                }
//            });
//        }
    }


    @Override
    public int getItemCount() {
        return Common.TIME_SLOT_TOTAL;
    }

    public class MyTimeSlotViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_time_slot, txt_time_slot_description;
        CardView card_time_slot;

        IRecycleItemSelectedListener iRecycleItemSelectedListener;

        public void setiRecycleItemSelectedListener(IRecycleItemSelectedListener iRecycleItemSelectedListener) {
            this.iRecycleItemSelectedListener = iRecycleItemSelectedListener;
        }

        public MyTimeSlotViewHolder(@NonNull View itemView) {
            super(itemView);
            card_time_slot = itemView.findViewById(R.id.card_time_slot);
            txt_time_slot = itemView.findViewById(R.id.txt_time_slot);
            txt_time_slot_description = itemView.findViewById(R.id.txt_time_slot_description);


            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            iRecycleItemSelectedListener.onItemSelected(itemView, getAdapterPosition());
        }
    }
}
