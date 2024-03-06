package com.myproject.controller;
import java.sql.Connection;

import java.util.List;
import java.util.Scanner;

import com.myproject.dao.*;
import com.myproject.dao.util.Manager;
import com.myproject.controller.manager.*;
import com.myproject.view.ConsoleView;

public class UserController implements Manager {
    private Scanner scanner;
    private ReceiptDAO receiptDAO;
    private BillDAO billDAO;

    public UserController(Connection connection) {
        scanner = new Scanner(System.in);
        receiptDAO = new ReceiptDAO(connection);
    }

    @Override
    public void run() {
        int choice;
        do {
            choice = ConsoleView.showUserWarehouseManagementMenu();
            switch (choice) {
                case 1:
                    displayReceiptsByStatus();
                    break;
                case 2:
                    createReceipt();
                    break;
                case 3:
                    updateReceipt();
                    break;
                case 4:
                    searchReceipt();
                    break;
                case 5:
                    displayBillsByStatus();
                    break;
                case 6:
                    createBill();
                    break;
                case 7:
                    updateBill();
                    break;
                case 8:
                    searchBill();
                    break;
                case 9:
                    ConsoleView.displayMessage("Đã thoát khỏi chương trình.");
                    break;
                default:
                    ConsoleView.displayMessage("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
                    break;
            }
        } while (choice != 9);
    }

    private void displayReceiptsByStatus() {
        byte status = (byte) ConsoleView.getIntInput("Nhập trạng thái phiếu nhập (0-Tạo 1-Hủy 2-Duyệt): ");
        List<Bill> receiptList = receiptDAO.getReceiptsByStatus(status);
        if (receiptList != null && !receiptList.isEmpty()) {
            for (Bill receipt : receiptList) {
                ConsoleView.displayBill(receipt);
            }
        } else {
            ConsoleView.displayMessage("Không có phiếu nhập nào có trạng thái như vậy.");
        }
    }

    private void createReceipt() {
        Bill receipt = ConsoleView.createBillFromInput();
        if (receiptDAO.createReceipt(receipt)) {
            ConsoleView.displayMessage("Đã tạo phiếu nhập thành công.");
        } else {
            ConsoleView.displayMessage("Không thể tạo phiếu nhập.");
        }
    }

    private void updateReceipt() {
        String receiptCode = ConsoleView.getInput("Nhập mã phiếu nhập cần cập nhật: ");
        Bill existingReceipt = receiptDAO.getBillById(receiptCode);
        if (existingReceipt != null) {
            Bill updatedReceipt = ConsoleView.updateBillFromInput(existingReceipt);
            if (receiptDAO.updateReceipt(updatedReceipt)) {
                ConsoleView.displayMessage("Đã cập nhật phiếu nhập thành công.");
            } else {
                ConsoleView.displayMessage("Không thể cập nhật phiếu nhập.");
            }
        } else {
            ConsoleView.displayMessage("Không tìm thấy phiếu nhập có mã như vậy.");
        }
    }

    private void searchReceipt() {
        String receiptCode = ConsoleView.getInput("Nhập mã phiếu nhập cần tìm: ");
        Bill receipt = receiptDAO.getBillById(receiptCode);
        if (receipt != null) {
            ConsoleView.displayBill(receipt);
        } else {
            ConsoleView.displayMessage("Không tìm thấy phiếu nhập có mã như vậy.");
        }
    }

    private void displayBillsByStatus() {
        byte status = (byte) ConsoleView.getIntInput("Nhập trạng thái phiếu xuất (0-Tạo 1-Hủy 2-Duyệt): ");
        List<Bill> billList = billDAO.getBillsByStatus(status);
        if (billList != null && !billList.isEmpty()) {
            for (Bill bill : billList) {
                ConsoleView.displayBill(bill);
            }
        } else {
            ConsoleView.displayMessage("Không có phiếu xuất nào có trạng thái như vậy.");
        }
    }

    private void createBill() {
        Bill bill = ConsoleView.createBillFromInput();
        if (billDAO.create(bill)) {
            ConsoleView.displayMessage("Đã tạo phiếu xuất thành công.");
        } else {
            ConsoleView.displayMessage("Không thể tạo phiếu xuất.");
        }
    }

    private void updateBill() {
        String billCode = ConsoleView.getInput("Nhập mã phiếu xuất cần cập nhật: ");
        Bill existingBill = billDAO.getBillById(billCode);
        if (existingBill != null) {
            Bill updatedBill = ConsoleView.updateBillFromInput(existingBill);
            if (billDAO.updateReceipt(updatedBill)) {
                ConsoleView.displayMessage("Đã cập nhật phiếu xuất thành công.");
            } else {
                ConsoleView.displayMessage("Không thể cập nhật phiếu xuất.");
            }
        } else {
            ConsoleView.displayMessage("Không tìm thấy phiếu xuất có mã như vậy.");
        }
    }

    private void searchBill() {
        String billCode = ConsoleView.getInput("Nhập mã phiếu xuất cần tìm: ");
        Bill bill = billDAO.getBillById(billCode);
        if (bill != null) {
            ConsoleView.displayBill(bill);
        } else {
            ConsoleView.displayMessage("Không tìm thấy phiếu xuất có mã như vậy.");
        }
    }
}
