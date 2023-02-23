package com.delivery.mydelivery.home;

// 카테고리 아이템
public class CategoryVO {

    private int categoryId; // pk
    private String categoryName; // 카테고리 이름

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
