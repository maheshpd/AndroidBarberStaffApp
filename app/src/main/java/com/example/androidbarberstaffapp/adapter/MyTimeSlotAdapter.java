package com.example.androidbarberstaffapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidbarberstaffapp.Common.Common;
import com.example.androidbarberstaffapp.Interface.IRecycleItemSelectedListener;
import com.example.androidbarberstaffapp.R;
import com.example.androidbarberstaffapp.model.TimeSlot;

import java.util.ArrayList;
import java.util.List;

public class MyTimeSlotAdapter extends RecyclerView.Adapter<MyTimeSlotAdapter.MyTimeSlotViewHolder> {

    Context context;
    List<TimeSlot> timeSlotList;
    List<CardView> cardViewList;


    public MyTimeSlotAdapter(Context context) {
        this.context = context;
        this.timeSlotList = new ArrayList<>();
        cardViewList = new ArrayList<>();
    }

    public MyTimeSlotAdapter(Context context, List<TimeSlot> timeSlotList) {
        this.context = context;
        this.timeSlotList = timeSlotList;
        cardViewList = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyTimeSlotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate( R.layout.layout_time_slot,parent,false);
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

        }else //If have position is full (booked)
        {
            for (TimeSlot slotValue: timeSlotList)
            {
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

                }
            }
        }

        //Add all card to list (20 card because we have 20 time slot)
        //No add card already in cardViewList
        if (!cardViewList.contains(holder.card_time_slot))
            cardViewList.add(holder.card_time_slot);

        //Check if card time slot is available

        holder.setiRecycleItemSelectedListener(new IRecycleItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position) {
                //Loop all card in card List
                for (CardView cardView:cardViewList) {
                    if (cardView.getTag() == null) //Only available card time slot be change
                        cardView.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
                }
                //Our selected card will be change color
                holder.card_time_slot.setCardBackgroundColor(context.getResources()
                        .getColor(android.R.color.holo_orange_dark));

            }
        });
    }


    @Override
    public int getItemCount() {
        return Common.TIME_SLOT_TOTAL;
    }

    public class MyTimeSlotViewHolder extends RecyclerView.ViewHolder{
        TextView txt_time_slot,txt_time_slot_description;
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


        }


    }
}
