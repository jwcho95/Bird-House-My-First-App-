package com.example.android_project;

public class OrderDetailListData {
    String productImage;
    String productName;
    int productPrice;
    int productQuantity;
    String orderName;
    String orderPhoneNum;
    String destination;
    String email;
    String bank;
    String bankNum;
    String howToPay;
    String reasonOfRefund;

    public OrderDetailListData(){};

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(int productPrice) {
        this.productPrice = productPrice;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderPhoneNum() {
        return orderPhoneNum;
    }

    public void setOrderPhoneNum(String orderPhoneNum) {
        this.orderPhoneNum = orderPhoneNum;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankNum() {
        return bankNum;
    }

    public void setBankNum(String bankNum) {
        this.bankNum = bankNum;
    }

    public String getHowToPay() {
        return howToPay;
    }

    public void setHowToPay(String howToPay) {
        this.howToPay = howToPay;
    }

    public String getReasonOfRefund() {
        return reasonOfRefund;
    }

    public void setReasonOfRefund(String reasonOfRefund) {
        this.reasonOfRefund = reasonOfRefund;
    }
}
