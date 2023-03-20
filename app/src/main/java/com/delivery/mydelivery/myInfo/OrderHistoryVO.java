package com.delivery.mydelivery.myInfo;

import java.sql.Timestamp;

public class OrderHistoryVO {

    private int orderHistoryId;
    private int recruitId;
    private int userId;
    private int storeId;
    private int paymentMoney;
    private int participantCount;
    private String deliveryCompletePicture;
    private Timestamp deliveryDate;

    public int getOrderHistoryId() {
        return orderHistoryId;
    }

    public void setOrderHistoryId(int orderHistoryId) {
        this.orderHistoryId = orderHistoryId;
    }

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

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public int getPaymentMoney() {
        return paymentMoney;
    }

    public void setPaymentMoney(int paymentMoney) {
        this.paymentMoney = paymentMoney;
    }

    public int getParticipantCount() {
        return participantCount;
    }

    public void setParticipantCount(int participantCount) {
        this.participantCount = participantCount;
    }

    public String getDeliveryCompletePicture() {
        return deliveryCompletePicture;
    }

    public void setDeliveryCompletePicture(String deliveryCompletePicture) {
        this.deliveryCompletePicture = deliveryCompletePicture;
    }

    public Timestamp getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Timestamp deliveryDate) {
        this.deliveryDate = deliveryDate;
    }
}
