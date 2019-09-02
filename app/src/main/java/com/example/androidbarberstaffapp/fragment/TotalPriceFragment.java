package com.example.androidbarberstaffapp.fragment;


import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidbarberstaffapp.Common.Common;
import com.example.androidbarberstaffapp.Interface.IBottomSheetDialogOnDismissListener;
import com.example.androidbarberstaffapp.R;
import com.example.androidbarberstaffapp.adapter.MyConfirmShoppingAdapter;
import com.example.androidbarberstaffapp.model.BarberServices;
import com.example.androidbarberstaffapp.model.FCMResponse;
import com.example.androidbarberstaffapp.model.FCMSendData;
import com.example.androidbarberstaffapp.model.Invoice;
import com.example.androidbarberstaffapp.model.MyToken;
import com.example.androidbarberstaffapp.model.ShoppingItem;
import com.example.androidbarberstaffapp.retrofit.IFCMService;
import com.example.androidbarberstaffapp.retrofit.RetrofitClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dmax.dialog.SpotsDialog;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class TotalPriceFragment extends BottomSheetDialogFragment {
    private static TotalPriceFragment instancce;
    Unbinder unbinder;
    @BindView(R.id.chip_group_services)
    ChipGroup chip_group_services;
    @BindView(R.id.recycler_view_shopping)
    RecyclerView recycler_view_shopping;
    @BindView(R.id.txt_salon_name)
    TextView txt_salon_name;
    @BindView(R.id.txt_barber_name)
    TextView txt_barber_name;
    @BindView(R.id.txt_time)
    TextView txt_time;
    @BindView(R.id.txt_customer_name)
    TextView txt_customer_name;
    @BindView(R.id.txt_customer_phone)
    TextView txt_customer_phone;
    @BindView(R.id.txt_total_price)
    TextView txt_total_price;
    @BindView(R.id.btn_confirm)
    Button btn_confirm;
    HashSet<BarberServices> servicesAdded;
    List<ShoppingItem> shoppingItemList;
    IFCMService ifcmService;

    IBottomSheetDialogOnDismissListener iBottomSheetDialogOnDismissListener;
    AlertDialog dialog;

    String image_url;
    public TotalPriceFragment() {
        // Required empty public constructor
    }

    public TotalPriceFragment(IBottomSheetDialogOnDismissListener iBottomSheetDialogOnDismissListener) {
        this.iBottomSheetDialogOnDismissListener = iBottomSheetDialogOnDismissListener;
    }

    public static TotalPriceFragment getInstance(IBottomSheetDialogOnDismissListener iBottomSheetDialogOnDismissListener) {
        return instancce == null ? new TotalPriceFragment(iBottomSheetDialogOnDismissListener) : instancce;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_total_price, container, false);

        unbinder = ButterKnife.bind(this, view);

        init();

        initView();

        getBundle(getArguments());

        setInformation();


        return view;
    }

    private void setInformation() {
        txt_salon_name.setText(Common.selected_salon.getName());
        txt_barber_name.setText(Common.currentBarber.getName());
        txt_time.setText(Common.convertTimeSlotToString(Common.currentBookingInformation.getSlot().intValue()));
        txt_customer_name.setText(Common.currentBookingInformation.getCustomeName());
        txt_customer_phone.setText(Common.currentBookingInformation.getCustomerPhone());


        if (servicesAdded.size() > 0) {
            //Add to chip Group
            int i = 0;
            for (BarberServices services : servicesAdded) {
                Chip chip = (Chip) getLayoutInflater().inflate(R.layout.chip_item, null);
                chip.setText(services.getName());
                chip.setTag(i);
                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        servicesAdded.remove(view.getTag());
                        chip_group_services.removeView(view);

                        calculatePrice();
                    }
                });
                chip_group_services.addView(chip);
                i++;
            }
        }

        if (shoppingItemList.size() > 0) {
            MyConfirmShoppingAdapter adapter = new MyConfirmShoppingAdapter(getContext(), shoppingItemList);
            recycler_view_shopping.setAdapter(adapter);
        }

        calculatePrice();

    }

    private double calculatePrice() {
        double price = Common.DEFAULT_PRICE;
        for (BarberServices services : servicesAdded)
            price += services.getPrice();
        for (ShoppingItem shoppingItem : shoppingItemList)
            price += shoppingItem.getPrice();

        txt_total_price.setText(new StringBuilder(Common.MONEY_SIGN)
                .append(price));

        return price;
    }

    private void getBundle(Bundle arguments) {
        this.servicesAdded = new Gson()
                .fromJson(arguments.getString(Common.SERVICES_ADDED),
                        new TypeToken<HashSet<BarberServices>>() {
                        }.getType());

        this.shoppingItemList = new Gson()
                .fromJson(arguments.getString(Common.SHOPPING_LIST),
                        new TypeToken<List<ShoppingItem>>() {
                        }.getType());

        image_url = arguments.getString(Common.IMAGE_DOWNLOADABLE_URL);
    }

    private void initView() {
        recycler_view_shopping.setHasFixedSize(true);
        recycler_view_shopping.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();

                //Update bookingInformation , set done = true
                DocumentReference bookingSet = FirebaseFirestore.getInstance()
                        .collection("AllSalon")
                        .document(Common.state_name)
                        .collection("Branch")
                        .document(Common.selected_salon.getSalonId())
                        .collection("Barber")
                        .document(Common.currentBarber.getBarberId())
                        .collection(Common.simpleDateFormat.format(Common.bookingDate.getTime()))
                        .document(Common.currentBookingInformation.getBookingId());

                bookingSet.get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().exists()) {
                                        //Update
                                        Map<String, Object> dataUpdate = new HashMap<>();
                                        dataUpdate.put("done", true);
                                        bookingSet.update(dataUpdate)
                                                .addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        dialog.dismiss();
                                                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    //If update is done , create invoice
                                                    createInvoice();
                                                }
                                            }
                                        });
                                    }
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        dialog.dismiss();
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void createInvoice() {
        //Create invoice
        CollectionReference invoiceRef = FirebaseFirestore.getInstance()
                .collection("AllSalon")
                .document(Common.state_name)
                .collection("Branch")
                .document(Common.selected_salon.getSalonId())
                .collection("Invoices");

        Invoice invoice = new Invoice();
        invoice.setBarberId(Common.currentBarber.getBarberId());
        invoice.setBarberName(Common.currentBarber.getName());

        invoice.setSalonId(Common.selected_salon.getSalonId());
        invoice.setSalonName(Common.selected_salon.getName());
        invoice.setSalonAddress(Common.selected_salon.getAddress());

        invoice.setCustomerName(Common.currentBookingInformation.getCustomeName());
        invoice.setCustomerPhone(Common.currentBookingInformation.getCustomerPhone());

        invoice.setImageUrl(image_url);

        invoice.setBarberServices(new ArrayList<>(servicesAdded));
        invoice.setShoppingItemsList(shoppingItemList);
        invoice.setFinalPrice(calculatePrice());

        invoiceRef.document()
                .set(invoice)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    sendNotificationUpdateToUser(Common.currentBookingInformation.getCustomerPhone());
                }
            }
        });
    }

    private void sendNotificationUpdateToUser(String customerPhone) {
        //Get Toke of user first
        FirebaseFirestore.getInstance()
                .collection("Token")
                .whereEqualTo("userPhone", customerPhone)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful() && task.getResult().size() > 0) {
                            MyToken myToken = new MyToken();
                            for (DocumentSnapshot tokenSnapShot : task.getResult())
                                myToken = tokenSnapShot.toObject(MyToken.class);

                            //Create notification to send
                            FCMSendData fcmSendData = new FCMSendData();
                            Map<String, String> dataSend = new HashMap<>();
                            dataSend.put("update_done", "true");

                            fcmSendData.setTo(myToken.getToken());
                            fcmSendData.setData(dataSend);

                            ifcmService.sendNotification(fcmSendData)
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(Schedulers.newThread())
                                    .subscribe(new Consumer<FCMResponse>() {
                                        @Override
                                        public void accept(FCMResponse fcmResponse) throws Exception {
                                            dialog.dismiss();
                                            dismiss();
                                            iBottomSheetDialogOnDismissListener.onDismissBottomSheetDialog(true);
                                        }
                                    }, new Consumer<Throwable>() {
                                        @Override
                                        public void accept(Throwable throwable) throws Exception {
                                            Toast.makeText(getContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                });
    }

    private void init() {
        dialog = new SpotsDialog.Builder().setContext(getContext())
                .setCancelable(false).build();
        ifcmService = RetrofitClient.getInstance().create(IFCMService.class);
    }

}
