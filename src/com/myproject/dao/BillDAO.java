package com.myproject.dao;

import com.myproject.controller.manager.Bill;

import java.util.List;

public interface BillDAO {
    List<Bill> getAllReceipts();
    boolean create(Bill bill);
    boolean updateReceipt(Bill bill);
    boolean employeeExists(String empId);
    Bill getBillById(String billCode);
    boolean updateProductQuantity(String productId, int quantity);
    List<Bill> getBillsByStatus(byte status);
}
