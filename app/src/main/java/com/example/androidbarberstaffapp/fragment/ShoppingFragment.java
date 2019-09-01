package com.example.androidbarberstaffapp.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidbarberstaffapp.Common.SpacesItemDecoration;
import com.example.androidbarberstaffapp.Interface.IOnShoppingItemSelected;
import com.example.androidbarberstaffapp.Interface.IShoppingDataLoadListener;
import com.example.androidbarberstaffapp.R;
import com.example.androidbarberstaffapp.model.ShoppingItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingFragment extends BottomSheetDialogFragment implements IShoppingDataLoadListener, IOnShoppingItemSelected {

    private static ShoppingFragment instance;
    Unbinder unbinder;
    CollectionReference shoppingItemRef;
    IOnShoppingItemSelected callBackToActivity;
    IShoppingDataLoadListener iShoppingDataLoadListener;
    @BindView(R.id.chip_group)
    ChipGroup chip_group;
    @BindView(R.id.chip_wax)
    Chip chip_wax;
    @BindView(R.id.chip_spray)
    Chip chip_spray;
    @BindView(R.id.chip_hair_care)
    Chip chip_hair_care;
    @BindView(R.id.chip_body_care)
    Chip chip_body_care;
    @BindView(R.id.recycler_items)
    RecyclerView recyclerView;

    public ShoppingFragment(IOnShoppingItemSelected callBackToActivity) {
        this.callBackToActivity = callBackToActivity;
    }

    public ShoppingFragment() {
    }

    public static ShoppingFragment getInstance(IOnShoppingItemSelected iOnShoppingItemSelected) {
        return instance == null ? new ShoppingFragment(iOnShoppingItemSelected) : instance;
    }

    @OnClick(R.id.chip_wax)
    void waxLoadClick() {
        setSelectedChip(chip_wax);
        loadShoppingItem("Wax");

    }

    @OnClick(R.id.chip_spray)
    void sprayLoadClick() {
        setSelectedChip(chip_spray);
        loadShoppingItem("Spray");

    }

    @OnClick(R.id.chip_hair_care)
    void hairCareLoadClick() {
        setSelectedChip(chip_spray);
        loadShoppingItem("HairCare");

    }

    @OnClick(R.id.chip_body_care)
    void bodyCareLoadClick() {
        setSelectedChip(chip_spray);
        loadShoppingItem("BodyCare");

    }

    private void loadShoppingItem(String itemMenu) {
        shoppingItemRef = FirebaseFirestore.getInstance().collection("Shopping")
                .document(itemMenu)
                .collection("Item");

        //Get Data
        shoppingItemRef.get()
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        iShoppingDataLoadListener.onShoppingDataLoadFailed(e.getMessage());
                    }
                }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<ShoppingItem> shoppingItems = new ArrayList<>();
                for (DocumentSnapshot documentSnapshot : task.getResult()) {
                    ShoppingItem shoppingItem = documentSnapshot.toObject(ShoppingItem.class);
                    shoppingItem.setId(documentSnapshot.getId()); //Remember add it if you don't want toget null reference
                    shoppingItems.add(shoppingItem);
                }
                iShoppingDataLoadListener.onShoppingDataLoadSuccess(shoppingItems);
            }
        });
    }

    private void setSelectedChip(Chip chip) {
        //Set Color
        for (int i = 0; i < chip_group.getChildCount(); i++) {
            Chip chipItem = (Chip) chip_group.getChildAt(i);
            if (chipItem.getId() != chip.getId()) //If not selected
            {
                chipItem.setChipBackgroundColorResource(android.R.color.darker_gray);
                chipItem.setTextColor(getResources().getColor(android.R.color.white));
            } //If selected
            {
                chipItem.setChipBackgroundColorResource(android.R.color.holo_orange_dark);
                chipItem.setTextColor(getResources().getColor(android.R.color.black));
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemView = inflater.inflate(R.layout.fragment_shopping, container, false);
        unbinder = ButterKnife.bind(this, itemView);

        init();

        initView();

//        //Default load
        loadShoppingItem("Wax");
        return itemView;
    }

    private void initView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        recyclerView.addItemDecoration(new SpacesItemDecoration(8));
    }

    private void init() {
        iShoppingDataLoadListener = this;
    }

    @Override
    public void onShoppingDataLoadSuccess(List<ShoppingItem> shoppingItemList) {
        MyShoppingAdapter adapter = new MyShoppingAdapter(getContext(), shoppingItemList, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onShoppingDataLoadFailed(String message) {

    }

    @Override
    public void onShoppingItemSelected(ShoppingItem shoppingItem) {
        callBackToActivity.onShoppingItemSelected(shoppingItem);
    }
}
