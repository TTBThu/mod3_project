package com.myproject.dao;

import com.myproject.model.Product;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class ProductDAO {
    private Connection connection;

    public ProductDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Product> getAllProducts(int offset, int limit) throws SQLException {
        List<Product> productList = new ArrayList<>();
        String query = "SELECT * FROM PRODUCT LIMIT ? OFFSET ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, limit);
            preparedStatement.setInt(2, offset);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String productId = resultSet.getString("Product_Id");
                    String productName = resultSet.getString("Product_Name");
                    String manufacturer = resultSet.getString("Manufacturer");
                    byte batch = resultSet.getByte("Batch");
                    int quantity = resultSet.getInt("Quantity");
                    boolean productStatus = resultSet.getBoolean("Product_Status");
                    Date created = resultSet.getDate("Created");
                    Product product = new Product(productId, productName, manufacturer, batch, quantity, productStatus, created);
                    productList.add(product);
                }
            }
        }
        return productList;
    }

    public void addProduct(Product product) throws SQLException {
        String query = "INSERT INTO PRODUCT (Product_Id, Product_Name, Manufacturer, Batch, Product_Status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, product.getProductId());
            preparedStatement.setString(2, product.getProductName());
            preparedStatement.setString(3, product.getManufacturer());
            preparedStatement.setInt(4, product.getBatch());
            preparedStatement.setBoolean(5, true); // Mặc định là 1
            preparedStatement.executeUpdate();
        }
    }

    public void updateProduct(Product product) throws SQLException {
        String query = "UPDATE PRODUCT SET Product_Name = ?, Manufacturer = ?, Batch = ? WHERE Product_Id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, product.getProductName());
            preparedStatement.setString(2, product.getManufacturer());
            preparedStatement.setInt(3, product.getBatch());
            preparedStatement.setString(4, product.getProductId());
            preparedStatement.executeUpdate();
        }
    }

    public List<Product> searchProductByName(String productName) throws SQLException {
        List<Product> productList = new ArrayList<>();
        String query = "SELECT * FROM PRODUCT WHERE Product_Name LIKE ? LIMIT 10";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + productName + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String productId = resultSet.getString("Product_Id");
                    String manufacturer = resultSet.getString("Manufacturer");
                    byte batch = resultSet.getByte("Batch");
                    int quantity = resultSet.getInt("Quantity");
                    boolean productStatus = resultSet.getBoolean("Product_Status");
                    Date created = resultSet.getDate("Created");
                    Product product = new Product(productId, productName, manufacturer, batch, quantity, productStatus, created);
                    productList.add(product);
                }
            }
        }
        return productList;
    }

    public void updateProductStatus(String productId, boolean status) throws SQLException {
        String query = "UPDATE PRODUCT SET Product_Status = ? WHERE Product_Id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setBoolean(1, status);
            preparedStatement.setString(2, productId);
            preparedStatement.executeUpdate();
        }
    }

    public List<Product> searchProductByIdOrName(String productIdOrName) throws SQLException {
        List<Product> productList = new ArrayList<>();
        String query = "SELECT * FROM PRODUCT WHERE Product_Id = ? OR Product_Name LIKE ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, productIdOrName);
            preparedStatement.setString(2, "%" + productIdOrName + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    String productId = resultSet.getString("Product_Id");
                    String productName = resultSet.getString("Product_Name");
                    String manufacturer = resultSet.getString("Manufacturer");
                    byte batch = resultSet.getByte("Batch");
                    int quantity = resultSet.getInt("Quantity");
                    boolean productStatus = resultSet.getBoolean("Product_Status");
                    Date created = resultSet.getDate("Created");
                    Product product = new Product(productId, productName, manufacturer, batch, quantity, productStatus, created);
                    productList.add(product);
                }
            }
        }
        return productList;
    }
}

