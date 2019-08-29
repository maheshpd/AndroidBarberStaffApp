package com.example.androidbarberstaffapp.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import com.example.androidbarberstaffapp.Common.Common;
import com.example.androidbarberstaffapp.R;
import com.google.android.material.chip.ChipGroup;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DoneServicesActivity extends AppCompatActivity {

    @BindView(R.id.txt_customer_name)
    TextView txt_customer_name;

    @BindView(R.id.txt_customer_phone)
    TextView txt_customer_phone;

    @BindView(R.id.chip_group_services)
    ChipGroup chip_group_services;

    @BindView(R.id.chip_group_shopping)
    ChipGroup chip_group_shopping;

    @BindView(R.id.edt_services)
    AppCompatAutoCompleteTextView edt_services;

    @BindView(R.id.img_customer_hair)
    ImageView img_customer_hair;

    @BindView(R.id.btn_shopping)
    Button btn_shopping;

    @BindView(R.id.btn_finish)
    Button btn_finish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_services);

        ButterKnife.bind(this);

        setCustomerInformation();
    }

    private void setCustomerInformation() {
        txt_customer_name.setText(Common.currentBookingInformation.getCustomeName());
        txt_customer_phone.setText(Common.currentBookingInformation.getCustomerPhone());
    }
}
