package com.delivery.mydelivery.store;

// 매장 정보
public class StoreVO {

    private int storeId; // pk
    private String storeName; // 매장이름
    private String category; // 매장 카테고리
    private String storePhoneNum; // 매장 번호
    private String openTime; // 오픈시간
    private String closeTime; // 마감시간
    private String storeImageUrl; // 매장 이미지
    private String storeAddress; // 매장 주소
    private String deliveryTip; // 배달팁
    private int minimumDeliveryPrice; // 최소배달금액

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getStorePhoneNum() {
        return storePhoneNum;
    }

    public void setStorePhoneNum(String storePhoneNum) {
        this.storePhoneNum = storePhoneNum;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public String getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(String closeTime) {
        this.closeTime = closeTime;
    }

    public String getStoreImageUrl() {
        return storeImageUrl;
    }

    public void setStoreImageUrl(String storeImageUrl) {
        this.storeImageUrl = storeImageUrl;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getDeliveryTip() {
        return deliveryTip;
    }

    public void setDeliveryTip(String deliveryTip) {
        this.deliveryTip = deliveryTip;
    }

    public int getMinimumDeliveryPrice() {
        return minimumDeliveryPrice;
    }

    public void setMinimumDeliveryPrice(int minimumDeliveryPrice) {
        this.minimumDeliveryPrice = minimumDeliveryPrice;
    }

    @Override
    public String toString() {
        return "StoreVO{" +
                "storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", category='" + category + '\'' +
                ", storePhoneNum='" + storePhoneNum + '\'' +
                ", openTime='" + openTime + '\'' +
                ", closeTime='" + closeTime + '\'' +
                ", storeImageUrl='" + storeImageUrl + '\'' +
                ", storeAddress='" + storeAddress + '\'' +
                ", deliveryTip='" + deliveryTip + '\'' +
                ", minimumDeliveryPrice=" + minimumDeliveryPrice +
                '}';
    }
}
