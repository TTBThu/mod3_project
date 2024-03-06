package com.myproject.model;

public class Report {
    private String productName;
    private float totalPrice;

    public Report(String productName, float totalPrice) {
        this.productName = productName;
        this.totalPrice = totalPrice;
    }

    // Getter và setter cho các thuộc tính

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }


    @Override
    public String toString() {
        return "Report{" +
                "productName='" + productName + '\'' +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
