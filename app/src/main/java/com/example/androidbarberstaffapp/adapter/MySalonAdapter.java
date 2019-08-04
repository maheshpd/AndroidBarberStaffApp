package com.example.androidbarberstaffapp.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidbarberstaffapp.Common.Common;
import com.example.androidbarberstaffapp.Common.CustomeLoginDialog;
import com.example.androidbarberstaffapp.Interface.IDialogClickListener;
import com.example.androidbarberstaffapp.Interface.IRecyclerItemSelectedListener;
import com.example.androidbarberstaffapp.R;
import com.example.androidbarberstaffapp.activity.StaffHomeActivity;
import com.example.androidbarberstaffapp.model.Salon;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class MySalonAdapter extends RecyclerView.Adapter<MySalonAdapter.MyViewHolder> implements IDialogClickListener {

    Context context;
    List<Salon> salonList;
    List<CardView> itemViewList;
    LocalBroadcastManager localBroadcastManager;

    public MySalonAdapter(Context context, List<Salon> salonList) {
        this.context = context;
        this.salonList = salonList;
        this.itemViewList = new ArrayList<>();
        this.localBroadcastManager = LocalBroadcastManager.getInstance(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.layout_salon, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txt_salon_name.setText(salonList.get(position).getName());
        holder.txt_salon_address.setText(salonList.get(position).getAddress());
        if (!itemViewList.contains(holder.card_salon)) {
            itemViewList.add(holder.card_salon);
        }

        holder.setiRecycleItemSelectedListener(new IRecyclerItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position) {
                Common.selectedSalon = salonList.get(position);
                showLoginDialog();
            }
        });

    }

    private void showLoginDialog() {
        CustomeLoginDialog.getInstance()
                .showLoginDialog("STAFF LOGIN",
                        "LOGIN",
                        "CANCEL",
                        context, this);
    }

    @Override
    public int getItemCount() {
        return salonList.size();
    }

    @Override
    public void onClickPositiveButton(DialogInterface dialogInterface, String username, String password) {
//Show loading dialog
        AlertDialog loading = new SpotsDialog.Builder().setCancelable(false)
                .setContext(context).build();
        loading.show();

        FirebaseFirestore.getInstance()
                .collection("AllSalon")
                .document(Common.state_name)
                .collection("Branch")
                .document(Common.selectedSalon.getSalonId())
                .collection("Barber")
                .whereEqualTo("username", username)
                .whereEqualTo("password", password)
                .limit(1)
                .get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().size() > 0) {
                                dialogInterface.dismiss();
                                loading.dismiss();

                                //We will navigate Staff Home and clear all previous activity
                                Intent staffHome = new Intent(context, StaffHomeActivity.class);
                                staffHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                staffHome.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                context.startActivity(staffHome);

                            } else {
                                loading.dismiss();
                                Toast.makeText(context, "Wrong username / password or wrong salon", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    @Override
    public void onClickNegativeButton(DialogInterface diaogInterface) {
        diaogInterface.dismiss();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txt_salon_name, txt_salon_address;
        CardView card_salon;

        IRecyclerItemSelectedListener iRecycleItemSelectedListener;

        public void setiRecycleItemSelectedListener(IRecyclerItemSelectedListener iRecycleItemSelectedListener) {
            this.iRecycleItemSelectedListener = iRecycleItemSelectedListener;
        }

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            card_salon = itemView.findViewById(R.id.card_salon);
            txt_salon_address = itemView.findViewById(R.id.txt_salon_address);
            txt_salon_name = itemView.findViewById(R.id.salon_image);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            iRecycleItemSelectedListener.onItemSelected(view, getAdapterPosition());
        }
    }
}
