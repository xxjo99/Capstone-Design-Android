package com.delivery.mydelivery.recruit;

import java.sql.Timestamp;

public class RecruitVO {

    private int recruitId;

    private int userId;
    private String registrantPlace;
    private int storeId;
    private String place;
    private Timestamp deliveryTime;
    private int person;
    private int receiptState;

    public int getRecruitId() {
        return recruitId;
    }

    public void setRecruitId(int recruitId) {
        this.recruitId = recruitId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getRegistrantPlace() {
        return registrantPlace;
    }

    public void setRegistrantPlace(String registrantPlace) {
        this.registrantPlace = registrantPlace;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public Timestamp getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Timestamp deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public int getPerson() {
        return person;
    }

    public void setPerson(int person) {
        this.person = person;
    }

    public int getReceiptState() {
        return receiptState;
    }

    public void setReceiptState(int receiptState) {
        this.receiptState = receiptState;
    }
}
