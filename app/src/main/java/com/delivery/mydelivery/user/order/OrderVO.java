package com.delivery.mydelivery.user.order;

public class OrderVO {

    private int orderId; // pk
    private int userId; // fk
    private int storeId; // fk
    private int menuId; // fk
    private String selectOption;
    private int amount;
    private int totalPrice;

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    @Override
    public String toString() {
        return "OrderVO{" +
                "orderId=" + orderId +
                ", userId=" + userId +
                ", storeId=" + storeId +
                ", menuId=" + menuId +
                ", selectOption='" + selectOption + '\'' +
                ", amount=" + amount +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
