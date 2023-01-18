package com.delivery.mydelivery.menu;

public class OptionVO {

    private int menuOptionId; // pk
    private int storeId; // fk
    private int menuId; // fk
    private String optionName; // 옵션 이름
    private int minimumSelection; // 최소 선택 개수
    private int maximumSelection; // 최대 선택 개수

    public int getMenuOptionId() {
        return menuOptionId;
    }

    public void setMenuOptionId(int menuOptionId) {
        this.menuOptionId = menuOptionId;
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

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public int getMinimumSelection() {
        return minimumSelection;
    }

    public void setMinimumSelection(int minimumSelection) {
        this.minimumSelection = minimumSelection;
    }

    public int getMaximumSelection() {
        return maximumSelection;
    }

    public void setMaximumSelection(int maximumSelection) {
        this.maximumSelection = maximumSelection;
    }
}