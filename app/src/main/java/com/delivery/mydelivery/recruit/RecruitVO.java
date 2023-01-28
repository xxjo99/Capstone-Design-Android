package com.delivery.mydelivery.recruit;

public class RecruitVO {

    private int recruitId;

    private int userId;
    private String registrantPlace;
    private int storeId;
    private String place;
    private String deliveryTime;
    private int person;

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

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public int getPerson() {
        return person;
    }

    public void setPerson(int person) {
        this.person = person;
    }

    @Override
    public String toString() {
        return "RecruitVO{" +
                "recruitId=" + recruitId +
                ", userId=" + userId +
                ", registrantPlace='" + registrantPlace + '\'' +
                ", storeId=" + storeId +
                ", place='" + place + '\'' +
                ", deliveryTime='" + deliveryTime + '\'' +
                ", person=" + person +
                '}';
    }
}
