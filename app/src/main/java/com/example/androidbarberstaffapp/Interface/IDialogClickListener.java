package com.example.androidbarberstaffapp.Interface;

import android.content.DialogInterface;

public interface IDialogClickListener {
    void onClickPositiveButton(DialogInterface dialogInterface,String username,String password);
    void onClickNegativeButton(DialogInterface diaogInterface);
}
