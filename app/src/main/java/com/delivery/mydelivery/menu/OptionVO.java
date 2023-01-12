package com.delivery.mydelivery.menu;

public class OptionVO {

    private int menuOptionId; // pk
    private int menuId; // fk
    private String optionName; // 옵션 이름
    private Integer maximumSelection; // 최대 선택 개수

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

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public Integer getMaximumSelection() {
        return maximumSelection;
    }

    public void setMaximumSelection(Integer maximumSelection) {
        this.maximumSelection = maximumSelection;
    }
}
