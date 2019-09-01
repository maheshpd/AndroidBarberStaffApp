package com.example.androidbarberstaffapp.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidbarberstaffapp.Common.Common;
import com.example.androidbarberstaffapp.Interface.IOnShoppingItemSelected;
import com.example.androidbarberstaffapp.Interface.IRecycleItemSelectedListener;
import com.example.androidbarberstaffapp.R;
import com.example.androidbarberstaffapp.model.ShoppingItem;
import com.squareup.picasso.Picasso;

import java.util.List;

class MyShoppingAdapter extends RecyclerView.Adapter<MyShoppingAdapter.MyViewHolder> {

    Context context;
    List<ShoppingItem> shoppingItemList;
    IOnShoppingItemSelected iOnShoppingItemSelected;


    public MyShoppingAdapter(Context context, List<ShoppingItem> shoppingItemList, IOnShoppingItemSelected iOnShoppingItemSelected) {
        this.context = context;
        this.shoppingItemList = shoppingItemList;
        this.iOnShoppingItemSelected = iOnShoppingItemSelected;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_shopping_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Picasso.get().load(shoppingItemList.get(position).getImage()).into(holder.img_shopping_item);
        holder.txt_shopping_item_name.setText(Common.formatShoppingItemName(shoppingItemList.get(position).getName()));
        holder.txt_shopping_item_prince.setText(new StringBuilder("$").append(shoppingItemList.get(position).getPrice()));


        //Add To Cart from Staff app
        holder.setiRecycleItemSelectedListener(new IRecycleItemSelectedListener() {
            @Override
            public void onItemSelected(View view, int position) {
                iOnShoppingItemSelected.onShoppingItemSelected(shoppingItemList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        return shoppingItemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txt_shopping_item_name, txt_shopping_item_prince, txt_add_to_cart;
        ImageView img_shopping_item;

        IRecycleItemSelectedListener iRecycleItemSelectedListener;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img_shopping_item = itemView.findViewById(R.id.img_shopping_item);
            txt_shopping_item_name = itemView.findViewById(R.id.txt_name_shopping_item);
            txt_shopping_item_prince = itemView.findViewById(R.id.txt_price_shopping_item);
            txt_add_to_cart = itemView.findViewById(R.id.txt_add_to_cart);

            txt_add_to_cart.setOnClickListener(this);
        }

        public void setiRecycleItemSelectedListener(IRecycleItemSelectedListener iRecycleItemSelectedListener) {
            this.iRecycleItemSelectedListener = iRecycleItemSelectedListener;
        }

        @Override
        public void onClick(View view) {
            iRecycleItemSelectedListener.onItemSelected(view, getAdapterPosition());
        }
    }
}