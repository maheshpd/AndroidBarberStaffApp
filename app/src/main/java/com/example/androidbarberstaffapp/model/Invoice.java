package com.example.androidbarberstaffapp.model;

import java.util.List;

public class Invoice {

    private String salonId, salonName, salonAddress;
    private String barberId, barberName;
    private String customerName, customerPhone;
    private String imageUrl;
    private List<ShoppingItem> shoppingItemsList;
    private List<BarberServices> barberServices;
    private double finalPrice;

    public Invoice() {
    }

    /*public Invoice(String salonId, String salonName, String salonAddress, String barberId, String barberName, String customerName, String customerPhone, String imageUrl, List<ShoppingItem> shoppingItemsList, List<BarberServices> barberServicesList, double finalPrice) {
        this.salonId = salonId;
        this.salonName = salonName;
        this.salonAddress = salonAddress;
        this.barberId = barberId;
        this.barberName = barberName;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.imageUrl = imageUrl;
        this.shoppingItemsList = shoppingItemsList;
        this.barberServicesList = barberServicesList;
        this.finalPrice = finalPrice;
    }*/


    public String getSalonId() {
        return salonId;
    }

    public void setSalonId(String salonId) {
        this.salonId = salonId;
    }

    public String getSalonName() {
        return salonName;
    }

    public void setSalonName(String salonName) {
        this.salonName = salonName;
    }

    public String getSalonAddress() {
        return salonAddress;
    }

    public void setSalonAddress(String salonAddress) {
        this.salonAddress = salonAddress;
    }

    public String getBarberId() {
        return barberId;
    }

    public void setBarberId(String barberId) {
        this.barberId = barberId;
    }

    public String getBarberName() {
        return barberName;
    }

    public void setBarberName(String barberName) {
        this.barberName = barberName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<ShoppingItem> getShoppingItemsList() {
        return shoppingItemsList;
    }

    public void setShoppingItemsList(List<ShoppingItem> shoppingItemsList) {
        this.shoppingItemsList = shoppingItemsList;
    }

    public List<BarberServices> getBarberServices() {
        return barberServices;
    }

    public void setBarberServices(List<BarberServices> barberServices) {
        this.barberServices = barberServices;
    }

    public double getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(double finalPrice) {
        this.finalPrice = finalPrice;
    }
}
