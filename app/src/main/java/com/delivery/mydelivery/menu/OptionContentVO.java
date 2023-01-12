package com.delivery.mydelivery.menu;

public class OptionContentVO {

    private int menuOptionContentId; // pk
    private int menuOptionId; // fk
    private int menuId; // fk
    private String optionContentName; // 옵션 내용 이름
    private int optionPrice; // 옵션 가격

    public int getMenuOptionContentId() {
        return menuOptionContentId;
    }

    public void setMenuOptionContentId(int menuOptionContentId) {
        this.menuOptionContentId = menuOptionContentId;
    }

    public int getMenuOptionId() {
        return menuOptionId;
    }

    public void setMenuOptionId(int menuOptionId) {
        this.menuOptionId = menuOptionId;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getOptionContentName() {
        return optionContentName;
    }

    public void setOptionContentName(String optionContentName) {
        this.optionContentName = optionContentName;
    }

    public int getOptionPrice() {
        return optionPrice;
    }

    public void setOptionPrice(int optionPrice) {
        this.optionPrice = optionPrice;
    }

}
