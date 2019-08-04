package com.example.androidbarberstaffapp.Interface;

import com.example.androidbarberstaffapp.model.Salon;

import java.util.List;

public interface IBranchLoadListener {
    void onBranchLoadSuccess(List<Salon> branchList);
    void onBranchLoadFailed(String message);
}
