package com.example.androidbarberstaffapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidbarberstaffapp.Common.MyDiffCallBack;
import com.example.androidbarberstaffapp.R;
import com.example.androidbarberstaffapp.model.MyNotification;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyNotificationAdapter extends RecyclerView.Adapter<MyNotificationAdapter.MyNotificationViewHolder> {

    Context context;
    List<MyNotification> myNotificationList;

    public MyNotificationAdapter(Context context, List<MyNotification> myNotificationList) {
        this.context = context;
        this.myNotificationList = myNotificationList;
    }

    @NonNull
    @Override
    public MyNotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_notification_item,parent,false);
        return new MyNotificationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyNotificationViewHolder holder, int position) {
        holder.txt_notification_title.setText(myNotificationList.get(position).getTitle());
        holder.txt_notification_content.setText(myNotificationList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return myNotificationList.size();
    }

    public void updateList(List<MyNotification> newList) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new MyDiffCallBack(this.myNotificationList,newList));
        myNotificationList.addAll(newList);
        diffResult.dispatchUpdatesTo(this);
    }

    public class MyNotificationViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txt_notification_title)
        TextView txt_notification_title;
        @BindView(R.id.txt_notification_content)
        TextView txt_notification_content;

        public MyNotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
