package com.delivery.mydelivery.menu;

public class MenuVO {

    private int menuId; // pk
    private int storeId; // fk
    private String menuName; // 메뉴 이름
    private int menuPrice; // 메뉴 가격
    private String menuPicUrl; // 메뉴 이미지 주소
    private String menuInfo; // 메뉴 설명

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(int menuPrice) {
        this.menuPrice = menuPrice;
    }

    public String getMenuPicUrl() {
        return menuPicUrl;
    }

    public void setMenuPicUrl(String menuPicUrl) {
        this.menuPicUrl = menuPicUrl;
    }

    public String getMenuInfo() {
        return menuInfo;
    }

    public void setMenuInfo(String menuInfo) {
        this.menuInfo = menuInfo;
    }

}
