package com.myproject.dao;

import com.myproject.model.Bill;
import com.myproject.model.Product;
import com.myproject.model.Report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReportDAO {

    private final Connection connection;

    public ReportDAO(Connection connection) {
        this.connection = connection;
    }

    // Thống kê chi phí theo ngày, tháng, năm
    public List<Report> getExpenseReportByDate(int year, int month, int day) throws SQLException {
        List<Report> reports = new ArrayList<>();
        String query = "SELECT p.Product_Name, bd.Quantity * bd.Price AS Total_Price " +
                "FROM PRODUCT p " +
                "JOIN BILL_DETAIL bd ON p.Product_Id = bd.Product_Id " +
                "JOIN BILL b ON bd.Bill_Id = b.Bill_id " +
                "WHERE b.Bill_Type = 0 " +
                "AND YEAR(b.Created) = ? " +
                "AND MONTH(b.Created) = ? " +
                "AND DAY(b.Created) = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, year);
            preparedStatement.setInt(2, month);
            preparedStatement.setInt(3, day);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String productName = resultSet.getString("Product_Name");
                float totalPrice = resultSet.getFloat("Total_Price");
                Report report = new Report(productName, totalPrice);
                reports.add(report);
            }
        }
        return reports;
    }

    // Thống kê chi phí theo khoảng thời gian
    public List<Report> getExpenseReportByTimeRange(String startDate, String endDate) throws SQLException {
        List<Report> reports = new ArrayList<>();
        String query = "SELECT p.Product_Name, bd.Quantity * bd.Price AS Total_Price " +
                "FROM PRODUCT p " +
                "JOIN BILL_DETAIL bd ON p.Product_Id = bd.Product_Id " +
                "JOIN BILL b ON bd.Bill_Id = b.Bill_id " +
                "WHERE b.Bill_Type = 0 " +
                "AND b.Created BETWEEN ? AND ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, startDate);
            preparedStatement.setString(2, endDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String productName = resultSet.getString("Product_Name");
                float totalPrice = resultSet.getFloat("Total_Price");
                Report report = new Report(productName, totalPrice);
                reports.add(report);
            }
        }
        return reports;
    }

    // Thống kê doanh thu theo ngày, tháng, năm
    public List<Report> getRevenueReportByDate(int year, int month, int day) throws SQLException {
        List<Report> reports = new ArrayList<>();
        String query = "SELECT p.Product_Name, bd.Quantity * bd.Price AS Total_Price " +
                "FROM PRODUCT p " +
                "JOIN BILL_DETAIL bd ON p.Product_Id = bd.Product_Id " +
                "JOIN BILL b ON bd.Bill_Id = b.Bill_id " +
                "WHERE b.Bill_Type = 1 " +
                "AND YEAR(b.Created) = ? " +
                "AND MONTH(b.Created) = ? " +
                "AND DAY(b.Created) = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, year);
            preparedStatement.setInt(2, month);
            preparedStatement.setInt(3, day);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String productName = resultSet.getString("Product_Name");
                float totalPrice = resultSet.getFloat("Total_Price");
                Report report = new Report(productName, totalPrice);
                reports.add(report);
            }
        }
        return reports;
    }

    // Thống kê doanh thu theo khoảng thời gian
    public List<Report> getRevenueReportByTimeRange(String startDate, String endDate) throws SQLException {
        List<Report> reports = new ArrayList<>();
        String query = "SELECT p.Product_Name, bd.Quantity * bd.Price AS Total_Price " +
                "FROM PRODUCT p " +
                "JOIN BILL_DETAIL bd ON p.Product_Id = bd.Product_Id " +
                "JOIN BILL b ON bd.Bill_Id = b.Bill_id " +
                "WHERE b.Bill_Type = 1 " +
                "AND b.Created BETWEEN ? AND ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, startDate);
            preparedStatement.setString(2, endDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String productName = resultSet.getString("Product_Name");
                float totalPrice = resultSet.getFloat("Total_Price");
                Report report = new Report(productName, totalPrice);
                reports.add(report);
            }
        }
        return reports;
    }

    // Thống kê số nhân viên theo từng trạng thái
    public int[] getEmployeeCountByStatus() throws SQLException {
        int[] counts = new int[2];
        String query = "SELECT Emp_Status, COUNT(*) AS Count " +
                "FROM EMPLOYEE " +
                "GROUP BY Emp_Status";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int status = resultSet.getInt("Emp_Status");
                int count = resultSet.getInt("Count");
                if (status == 0) {
                    counts[0] = count;
                } else {
                    counts[1] = count;
                }
            }
        }
        return counts;
    }

    // Thống kê sản phẩm nhập nhiều nhất trong khoảng thời gian
    public Product getProductWithMaxImportQuantity(String startDate, String endDate) throws SQLException {
        Product product = null;
        String query = "SELECT p.* " +
                "FROM PRODUCT p " +
                "JOIN BILL_DETAIL bd ON p.Product_Id = bd.Product_Id " +
                "JOIN BILL b ON bd.Bill_Id = b.Bill_id " +
                "WHERE b.Bill_Type = 0 " +
                "AND b.Created BETWEEN ? AND ? " +
                "GROUP BY p.Product_Id " +
                "ORDER BY SUM(bd.Quantity) DESC " +
                "LIMIT 1";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, startDate);
            preparedStatement.setString(2, endDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                product = new Product();
                product.setProductId(resultSet.getString("Product_Id"));
                product.setProductName(resultSet.getString("Product_Name"));
                product.setManufacturer(resultSet.getString("Manufacturer"));
                product.setCreated(resultSet.getDate("Created"));
                product.setBatch(resultSet.getByte("Batch"));
                product.setQuantity(resultSet.getInt("Quantity"));
                product.setProductStatus(resultSet.getBoolean("Product_Status"));
            }
        }
        return product;
    }

    // Thống kê sản phẩm nhập ít nhất trong khoảng thời gian
    public Product getProductWithMinImportQuantity(String startDate, String endDate) throws SQLException {
        Product product = null;
        String query = "SELECT p.* " +
                "FROM PRODUCT p " +
                "JOIN BILL_DETAIL bd ON p.Product_Id = bd.Product_Id " +
                "JOIN BILL b ON bd.Bill_Id = b.Bill_id " +
                "WHERE b.Bill_Type = 0 " +
                "AND b.Created BETWEEN ? AND ? " +
                "GROUP BY p.Product_Id " +
                "ORDER BY SUM(bd.Quantity) ASC " +
                "LIMIT 1";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, startDate);
            preparedStatement.setString(2, endDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                product = new Product();
                product.setProductId(resultSet.getString("Product_Id"));
                product.setProductName(resultSet.getString("Product_Name"));
                product.setManufacturer(resultSet.getString("Manufacturer"));
                product.setCreated(resultSet.getDate("Created"));
                product.setBatch(resultSet.getByte("Batch"));
                product.setQuantity(resultSet.getInt("Quantity"));
                product.setProductStatus(resultSet.getBoolean("Product_Status"));
            }
        }
        return product;
    }

    // Thống kê sản phẩm xuất nhiều nhất trong khoảng thời gian
    public Product getProductWithMaxExportQuantity(String startDate, String endDate) throws SQLException {
        Product product = null;
        String query = "SELECT p.* " +
                "FROM PRODUCT p " +
                "JOIN BILL_DETAIL bd ON p.Product_Id = bd.Product_Id " +
                "JOIN BILL b ON bd.Bill_Id = b.Bill_id " +
                "WHERE b.Bill_Type = 1 " +
                "AND b.Created BETWEEN ? AND ? " +
                "GROUP BY p.Product_Id " +
                "ORDER BY SUM(bd.Quantity) DESC " +
                "LIMIT 1";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, startDate);
            preparedStatement.setString(2, endDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                product = new Product();
                product.setProductId(resultSet.getString("Product_Id"));
                product.setProductName(resultSet.getString("Product_Name"));
                product.setManufacturer(resultSet.getString("Manufacturer"));
                product.setCreated(resultSet.getDate("Created"));
                product.setBatch(resultSet.getByte("Batch"));
                product.setQuantity(resultSet.getInt("Quantity"));
                product.setProductStatus(resultSet.getBoolean("Product_Status"));
            }
        }
        return product;
    }

    // Thống kê sản phẩm xuất ít nhất trong khoảng thời gian
    public Product getProductWithMinExportQuantity(String startDate, String endDate) throws SQLException {
        Product product = null;
        String query = "SELECT p.* " +
                "FROM PRODUCT p " +
                "JOIN BILL_DETAIL bd ON p.Product_Id = bd.Product_Id " +
                "JOIN BILL b ON bd.Bill_Id = b.Bill_id " +
                "WHERE b.Bill_Type = 1 " +
                "AND b.Created BETWEEN ? AND ? " +
                "GROUP BY p.Product_Id " +
                "ORDER BY SUM(bd.Quantity) ASC " +
                "LIMIT 1";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, startDate);
            preparedStatement.setString(2, endDate);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                product = new Product();
                product.setProductId(resultSet.getString("Product_Id"));
                product.setProductName(resultSet.getString("Product_Name"));
                product.setManufacturer(resultSet.getString("Manufacturer"));
                product.setCreated(resultSet.getDate("Created"));
                product.setBatch(resultSet.getByte("Batch"));
                product.setQuantity(resultSet.getInt("Quantity"));
                product.setProductStatus(resultSet.getBoolean("Product_Status"));
            }
        }
        return product;
    }
}
