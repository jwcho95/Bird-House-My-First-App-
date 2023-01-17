package com.example.android_project;

public class RefundListData extends DeliveryListData{
    String refundDate;
    String state;

    RefundListData(){}

    public String getRefundDate() {
        return refundDate;
    }

    public void setRefundDate(String refundDate) {
        this.refundDate = refundDate;
    }

    @Override
    public String getState() {
        return state;
    }

    @Override
    public void setState(String state) {
        this.state = state;
    }
}
