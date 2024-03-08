package com.myproject.dao;

import com.myproject.controller.manager.Bill;
import com.myproject.controller.manager.BillDetails;
import com.myproject.dao.util.Manager;
import com.myproject.dao.*;
import com.myproject.view.ConsoleView;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReceiptDAO{
    private Connection connection;

    public ReceiptDAO(Connection connection) {
        this.connection = connection;
    }

    public Bill getBillById(String keyword) {
        // Kiểm tra xem billIdOrCode có phải là một số hay không
        try {
            long billId = Long.parseLong(keyword);
            // Lấy thông tin của một phiếu nhập dựa trên mã phiếu nhập
            String sql = "SELECT * FROM bill WHERE Bill_Id = ?";
            String getDetailSQL = "SELECT * FROM bill_detail WHERE Bill_Id = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                PreparedStatement getListDetail = connection.prepareStatement(getDetailSQL);
                List<BillDetails> billDetails = new ArrayList<>();
                ps.setLong(1, billId);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    String billCode = rs.getString("Bill_Code");
                    boolean billType = rs.getBoolean("Bill_Type");
                    String empIdCreated = rs.getString("Emp_Id_Created");
                    java.util.Date created = rs.getDate("Created");
                    String empIdAuth = rs.getString("Emp_id_auth");
                    byte billStatus = rs.getByte("Bill_Status");

                    // Lấy chi tiết của phiếu nhập từ bảng bill_detail
                    getListDetail.setLong(1, billId);
                    ResultSet detailsResultSet = getListDetail.executeQuery();
                    while (detailsResultSet.next()) {
                        long detailId = detailsResultSet.getLong(" Bill_Detail_Id");
                        String productId = detailsResultSet.getString("Product_Id");
                        int quantity = detailsResultSet.getInt("Quantity");
                        float price = detailsResultSet.getFloat("Price");
                        // Tạo đối tượng BillDetails từ dữ liệu lấy từ bảng bill_detail
                        BillDetails detail = new BillDetails(detailId, billId, productId, quantity, price);
                        billDetails.add(detail);
                    }

                    // Tạo đối tượng Bill và set thông tin chi tiết
                    Bill bill = new Bill(billId, billCode, billType, empIdCreated, created, empIdAuth, null, billStatus);
                    bill.setDetails(billDetails);

                    return bill;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } catch (NumberFormatException e) {
            String sql = "SELECT * FROM bill WHERE Bill_Code = ?";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setString(1, keyword);
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    long billId = rs.getLong("Bill_Id");
                    String billCode = rs.getString("Bill_Code");
                    boolean billType = rs.getBoolean("Bill_Type");
                    String empIdCreated = rs.getString("Emp_Id_Created");
                    java.util.Date created = rs.getDate("Created");
                    String empIdAuth = rs.getString("Emp_id_auth");
                    byte billStatus = rs.getByte("Bill_Status");

                    // Lấy chi tiết của phiếu nhập từ bảng bill_detail
                    String getDetailSQL = "SELECT * FROM bill_detail WHERE Bill_id = ?";
                    try (PreparedStatement getListDetail = connection.prepareStatement(getDetailSQL)) {
                        List<BillDetails> billDetails = new ArrayList<>();
                        getListDetail.setLong(1, billId);
                        ResultSet detailsResultSet = getListDetail.executeQuery();
                        while (detailsResultSet.next()) {
                            long detailId = detailsResultSet.getLong("Bill_Detail_Id");
                            String productId = detailsResultSet.getString("Product_Id");
                            int quantity = detailsResultSet.getInt("Quantity");
                            float price = detailsResultSet.getFloat("Price");
                            // Tạo đối tượng BillDetails từ dữ liệu lấy từ bảng bill_detail
                            BillDetails detail = new BillDetails(detailId, billId, productId, quantity, price);
                            billDetails.add(detail);
                        }

                        // Tạo đối tượng Bill và set thông tin chi tiết
                        Bill bill = new Bill(billId, billCode, billType, empIdCreated, created, empIdAuth, null, billStatus);
                        bill.setDetails(billDetails);

                        return bill;
                    }
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public boolean employeeExists(String empId) {
        // Kiểm tra xem một nhân viên có tồn tại trong cơ sở dữ liệu không
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

    public List<Bill> getAllReceipts() {
        List<Bill> receiptList = new ArrayList<>();
        String sql = "SELECT * FROM bill";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                long billId = rs.getLong("Bill_Id");
                String billCode = rs.getString("Bill_Code");
                boolean billType = rs.getBoolean("Bill_Type");
                String empIdCreated = rs.getString("Emp_Id_Created");
                java.util.Date created = rs.getDate("Created");
                String empIdAuth = rs.getString("Emp_id_auth");
                java.util.Date authDate = rs.getDate("Auth_date");
                byte billStatus = rs.getByte("Bill_Status");
                Bill bill = new Bill(billId, billCode, billType, empIdCreated, created, empIdAuth, authDate, billStatus);
                receiptList.add(bill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return receiptList;
    }

    public boolean createReceipt(Bill bill) {
        // Tạo mới một phiếu nhập trong cơ sở dữ liệu
        String sql = "INSERT INTO bill (Bill_Code, Bill_Type, Emp_Id_Created, Created, Emp_id_auth, Bill_Status) VALUES (?, ?, ?, ?, '', ?)";
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

    public boolean updateReceipt(Bill bill) {
        String sqlGetBillId = "SELECT Bill_Id FROM bill WHERE Bill_Code = ?";
        String sqlUpdateBill = "UPDATE bill SET Bill_Type = ?, Emp_Id_Created = ?, Created = ?, Emp_id_auth = ? WHERE Bill_Code = ?";
        String sqlUpdateBillDetail = "UPDATE bill_detail SET Product_Id = ?, Quantity = ?, Price = ? WHERE Bill_Id = ?";

        try (PreparedStatement psGetBillId = connection.prepareStatement(sqlGetBillId);
             PreparedStatement psUpdateBill = connection.prepareStatement(sqlUpdateBill);
             PreparedStatement psUpdateBillDetail = connection.prepareStatement(sqlUpdateBillDetail)) {

            connection.setAutoCommit(false);

            // Lấy Bill_Id dựa trên Bill_Code
            psGetBillId.setString(1, bill.getBillCode());
            ResultSet rs = psGetBillId.executeQuery();
            if (rs.next()) {
                long billId = rs.getLong("Bill_Id");

                // Cập nhật thông tin của phiếu nhập trong bảng bill
                psUpdateBill.setBoolean(1, bill.isBillType());
                psUpdateBill.setString(2, bill.getEmpIdCreated());
                psUpdateBill.setDate(3, new java.sql.Date(bill.getCreated().getTime()));
                psUpdateBill.setString(4, bill.getEmpIdAuth());
                psUpdateBill.setString(5, bill.getBillCode());
                psUpdateBill.executeUpdate();

                // Cập nhật thông tin chi tiết phiếu nhập trong bảng bill_detail
                for (BillDetails details : bill.getDetails()) {
                    psUpdateBillDetail.setString(1, details.getProductId());
                    psUpdateBillDetail.setInt(2, details.getQuantity());
                    psUpdateBillDetail.setFloat(3, details.getPrice());
                    psUpdateBillDetail.setLong(4, billId); // Sử dụng Bill_Id
                    psUpdateBillDetail.addBatch();
                }
                psUpdateBillDetail.executeBatch();

                connection.commit();
                return true;
            }
            else {
                ConsoleView.displayMessage("Không tìm thấy phiếu nhập với mã đã nhập.");
                return false;
            }
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void updateBillDetailStatus(String billCode, byte status) {
        String sql = "UPDATE bill SET Bill_Status = ? WHERE Bill_Code = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setByte(1, status);
            ps.setString(2, billCode);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateProductQuantity(BillDetails detail) {
        try {
            connection.setAutoCommit(false); // Bắt đầu giao dịch

            String productId = detail.getProductId();
            int quantity = detail.getQuantity();
            // Lấy số lượng hiện tại của sản phẩm trong kho
            PreparedStatement ps = connection.prepareStatement("SELECT Quantity FROM product WHERE Product_Id = ?");
            ps.setString(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                int currentQuantity = rs.getInt("Quantity");
                int newQuantity = currentQuantity + quantity;
                // Cập nhật số lượng mới của sản phẩm trong kho
                PreparedStatement updatePs = connection.prepareStatement("UPDATE product SET Quantity = ? WHERE Product_Id = ?");
                updatePs.setInt(1, newQuantity);
                updatePs.setString(2, productId);
                updatePs.executeUpdate();
            }

            connection.commit(); // Commit giao dịch
        } catch (SQLException e) {
            try {
                connection.rollback(); // Rollback nếu có lỗi
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true); // Đặt lại auto-commit về true sau khi hoàn thành giao dịch
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public List<Bill> getReceiptsByStatus(byte status) {
        List<Bill> receiptList = new ArrayList<>();
        String sql = "SELECT * FROM bill WHERE Bill_Type = 0 AND Bill_Status = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setByte(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                long billId = rs.getLong("Bill_Id");
                String billCode = rs.getString("Bill_Code");
                boolean billType = rs.getBoolean("Bill_Type");
                String empIdCreated = rs.getString("Emp_Id_Created");
                java.util.Date created = rs.getDate("Created");
                String empIdAuth = rs.getString("Emp_id_auth");
                java.util.Date authDate = rs.getDate("Auth_date");
                byte billStatus = rs.getByte("Bill_Status");
                Bill bill = new Bill(billId, billCode, billType, empIdCreated, created, empIdAuth, authDate, billStatus);
                receiptList.add(bill);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return receiptList;
    }

}