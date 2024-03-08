package com.myproject.model;

import java.util.Date;

public class Product {
    private String productId;
    private String productName;
    private String manufacturer;
    private byte batch;
    private int quantity;
    private boolean productStatus;
    private Date created;

    public Product() {}

    public Product(String productId, String productName, String manufacturer, byte batch, int quantity, boolean productStatus, Date created) {
        this.productId = productId;
        this.productName = productName;
        this.manufacturer = manufacturer;
        this.batch = batch;
        this.quantity = quantity;
        this.productStatus = productStatus;
        this.created = created;
    }
    public Product(String productId, String productName, String manufacturer, byte batch, boolean productStatus, Date created) {
        this.productId = productId;
        this.productName = productName;
        this.manufacturer = manufacturer;
        this.batch = batch;
        this.productStatus = productStatus;
        this.created = created;
    }

    // Getters and setters
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getBatch() {
        return batch;
    }

    public void setBatch(byte batch) {
        this.batch = batch;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public boolean isProductStatus() {
        return productStatus;
    }

    public void setProductStatus(boolean productStatus) {
        this.productStatus = productStatus;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}

