package com.delivery.mydelivery.recruit;

public class ParticipantOrderVO {

    private int participantOrderId;
    private int userId;
    private int storeId;
    private int menuId;
    private String selectOption;
    private int amount;
    private int totalPrice;

    public int getParticipantOrderId() {
        return participantOrderId;
    }

    public void setParticipantOrderId(int participantOrderId) {
        this.participantOrderId = participantOrderId;
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

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getSelectOption() {
        return selectOption;
    }

    public void setSelectOption(String selectOption) {
        this.selectOption = selectOption;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
