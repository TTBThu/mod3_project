package com.myproject.dao.util;

import com.myproject.controller.manager.Bill;
import com.myproject.controller.manager.BillDetails;
import com.myproject.dao.BillDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class BillDAOImpl implements BillDAO {
    private Connection connection;

    public BillDAOImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<Bill> getAllReceipts() {
        List<Bill> billList = new ArrayList<>();
        String sql = "SELECT * FROM bill";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Bill bill = extractBillFromResultSet(rs);
                billList.add(bill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return billList;
    }

    private Bill extractBillFromResultSet(ResultSet rs) throws SQLException {
        long billId = rs.getLong("Bill_Id");
        String billCode = rs.getString("Bill_Code");
        boolean billType = rs.getBoolean("Bill_Type");
        String empIdCreated = rs.getString("Emp_Id_Created");
        Date created = rs.getDate("Created");
        String empIdAuth = rs.getString("Emp_Id_Auth");
        Date authDate = rs.getDate("Auth_date");
        byte billStatus = rs.getByte("Bill_Status");

        return new Bill(billId, billCode, billType, empIdCreated, created, empIdAuth, authDate, billStatus);
    }


    @Override
    public boolean create(Bill bill) {
        String sql = "INSERT INTO bill (Bill_Code, Bill_Type, Emp_Id_Created, Created, Emp_Id_Auth, Bill_Status) " +
                "VALUES (?, ?, ?, ?, '', ?)"; // Sử dụng giá trị rỗng cho Emp_Id_Auth
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, bill.getBillCode());
            ps.setBoolean(2, bill.isBillType());
            ps.setString(3, bill.getEmpIdCreated());
            ps.setDate(4, new java.sql.Date(bill.getCreated().getTime()));
            ps.setByte(5, bill.getBillStatus());

            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                return false;
            }
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                long billId = generatedKeys.getLong(1);
                bill.setBillId(billId);
                return insertBillDetails(bill);
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    private boolean insertBillDetails(Bill bill) {
        // Thêm chi tiết phiếu nhập vào cơ sở dữ liệu
        String sql = "INSERT INTO bill_detail (Bill_Id, Product_Id, Quantity, Price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (BillDetails details : bill.getDetails()) {
                ps.setLong(1, bill.getBillId());
                ps.setString(2, details.getProductId());
                ps.setInt(3, details.getQuantity());
                ps.setFloat(4, details.getPrice());
                ps.addBatch();
            }
            int[] result = ps.executeBatch();
            for (int i : result) {
                if (i == PreparedStatement.EXECUTE_FAILED) {
                    return false;
                }
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateReceipt(Bill bill) {
        String sql = "UPDATE bill SET Emp_Id_Created = ?, Created = ?, Emp_Id_Auth = ?, Bill_Status = ? WHERE Bill_Code = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, bill.getEmpIdCreated());
            ps.setDate(2, new java.sql.Date(bill.getCreated().getTime()));
            ps.setString(3, bill.getEmpIdAuth());
            ps.setByte(4, bill.getBillStatus());
            ps.setString(5, bill.getBillCode());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean employeeExists(String empId) {
        String sql = "SELECT COUNT(*) FROM employee WHERE Emp_Id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, empId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    @Override
    public Bill getBillById(String billIdOrCode) {
        String sql = "SELECT * FROM bill LEFT JOIN bill_detail ON bill.Bill_Id = bill_detail.Bill_Id WHERE bill.Bill_Id = ? OR bill.Bill_Code = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            List<BillDetails> billDetails = new ArrayList<>();
            // Try parsing input as long for Bill_Id
            try {
                long billId = Long.parseLong(billIdOrCode);
                ps.setLong(1, billId);
                ps.setString(2, ""); // Set an empty string as placeholder for Bill_Code
            } catch (NumberFormatException e) {
                // If parsing as long fails, treat input as Bill_Code
                ps.setLong(1, 0); // Set 0 as placeholder for Bill_Id
                ps.setString(2, billIdOrCode);
            }
            ResultSet rs = ps.executeQuery();
            Bill bill = null; // Initialize bill to null
            while (rs.next()) {
                if (bill == null) { // Create bill object only once
                    bill = extractBillFromResultSet(rs);
                }
                // Retrieve data from ResultSet and create BillDetails objects
                BillDetails detail = new BillDetails();
                detail.setBillDetailId(rs.getLong("Bill_Detail_Id"));
                detail.setBillId(rs.getLong("Bill_Id"));
                detail.setProductId(rs.getString("Product_Id"));
                detail.setQuantity(rs.getInt("Quantity"));
                detail.setPrice(rs.getFloat("Price"));
                bill.getDetails().add(detail);
                billDetails.add(detail);
            }
            return bill; // Return the bill object with populated details
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean updateProductQuantity(String productId, int quantity) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT Quantity FROM product WHERE Product_Id = ?");
            ps.setString(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int currentQuantity = rs.getInt("Quantity");
                int newQuantity = currentQuantity - quantity;
                // Cập nhật số lượng mới của sản phẩm trong kho
                PreparedStatement updatePs = connection.prepareStatement("UPDATE product SET Quantity = ? WHERE Product_Id = ?");
                updatePs.setInt(1, newQuantity);
                updatePs.setString(2, productId);
                updatePs.executeUpdate();
            }
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Bill> getBillsByStatus(byte status) {
        List<Bill> billList = new ArrayList<>();
        String sql = "SELECT * FROM bill WHERE Bill_Status = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setByte(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                // Retrieve data from ResultSet and create Bill objects
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return billList;
    }
}