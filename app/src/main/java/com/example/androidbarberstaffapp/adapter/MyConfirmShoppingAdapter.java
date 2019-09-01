package com.example.androidbarberstaffapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidbarberstaffapp.R;
import com.example.androidbarberstaffapp.model.ShoppingItem;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyConfirmShoppingAdapter extends RecyclerView.Adapter<MyConfirmShoppingAdapter.MyViewHolder> {

    Context context;
    List<ShoppingItem> shoppingItemList;

    public MyConfirmShoppingAdapter(Context context, List<ShoppingItem> shoppingItemList) {
        this.context = context;
        this.shoppingItemList = shoppingItemList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_confirm_shopping, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get()
                .load(shoppingItemList.get(position).getImage())
                .into(holder.item_image);
        holder.txt_name.setText(shoppingItemList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return shoppingItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_image)
        ImageView item_image;

        @BindView(R.id.txt_name)
        TextView txt_name;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
