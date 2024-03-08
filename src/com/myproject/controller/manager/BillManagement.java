package com.myproject.controller.manager;

import com.myproject.dao.BillDAO;
import com.myproject.dao.util.BillDAOImpl;
import com.myproject.dao.util.BillType;
import com.myproject.dao.util.Manager;
import com.myproject.view.ConsoleView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BillManagement implements Manager {
    private BillDAO billDAO;

    public BillManagement(BillDAO billDAO) {this.billDAO = billDAO;}

    @Override
    public void run() {
        int choice;
        do {
            choice = ConsoleView.showBillManagementMenu();
            switch (choice) {
                case 1:
                    displayBillList();
                    break;
                case 2:
                    createBill();
                    break;
                case 3:
                    updateBill();
                    break;
                case 4:
                    displayBillDetails();
                    break;
                case 5:
                    approveBill();
                    break;
                case 6:
                    searchBill();
                    break;
                case 7:
                    ConsoleView.displayMessage("Thoát.");
                    break;
                default:
                    ConsoleView.displayMessage("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
                    break;
            }
        } while (choice != 7);
    }
    private void displayBillList() {
        List<Bill> bills = billDAO.getAllReceipts();
        for (Bill bill : bills) {
            ConsoleView.displayMessage(bill.toString());
        }
    }
    private void createBill() {
        String billCode = ConsoleView.getInput("Mã phiếu 入力:");
        boolean billType = BillType.EXPORT;
        String empIdCreated = ConsoleView.getInput("Mã nhân viên tạo phiếu 入力:");
        String empIdAuth = "";
        Date created = new Date();
        byte billStatus = 0;
        if (billDAO.employeeExists(empIdCreated)) {
            Bill bill = new Bill(billCode, billType, empIdCreated, created, empIdAuth, billStatus);

            // Kiểm tra và khởi tạo danh sách chi tiết nếu cần
            if (bill.getDetails() == null) {
                bill.setDetails(new ArrayList<>());
            }

            int numberOfDetails = ConsoleView.getIntInput("Số lượng chi tiết phiếu xuất 入力:");
            for (int i = 0; i < numberOfDetails; i++) {
                String productId = ConsoleView.getInput("Mã sản phẩm 入力:");
                int quantity = ConsoleView.getIntInput("Số lượng 入力:");
                float price = ConsoleView.getFloatInput("Giá 入力:");
                BillDetails details = new BillDetails(0, 0, productId, quantity, price);

                // Đảm bảo danh sách chi tiết không null
                if (bill.getDetails() == null) {
                    bill.setDetails(new ArrayList<>());
                }

                bill.getDetails().add(details);
            }

            boolean success = billDAO.create(bill);
            if (success) {
                ConsoleView.displayMessage("Tạo phiếu xuất 完了.");
            } else {
                ConsoleView.displayMessage("Tạo phiếu xuất 失敗. もう一度してください。");
            }
        } else {
            ConsoleView.displayMessage("Mã nhân viên 存在しません。 もう一度してください。");
        }
    }
    private void updateBill() {
        String billCode = ConsoleView.getInput("Mã phiếu xuất 入力:");
        Bill bill = billDAO.getBillById(billCode);
        if (bill != null) {
            if (bill.getBillStatus() == 0 || bill.getBillStatus() == 1) {
                String newEmpIdCreated = ConsoleView.getInput("Mã nhân viên cập nhật 入力:");
                if (billDAO.employeeExists(newEmpIdCreated)) {
                    bill.setEmpIdCreated(newEmpIdCreated);
                    int numberOfDetailsToUpdate = ConsoleView.getIntInput("Số lượng chi tiết phiếu xuất cần cập nhật 入力:");
                    for (int i = 0; i < numberOfDetailsToUpdate; i++) {
                        String productId = ConsoleView.getInput("Mã sản phẩm 入力:");
                        int quantity = ConsoleView.getIntInput("Số lượng sản phẩm xuất 入力:");
                        float price = ConsoleView.getFloatInput("Giá xuất 入力:");

                        for (BillDetails detail : bill.getDetails()) {
                            if (detail.getProductId().equals(productId)) {
                                detail.setQuantity(quantity);
                                detail.setPrice(price);
                            }
                        }
                    }

                    boolean success = billDAO.updateReceipt(bill);
                    if (success) {
                        ConsoleView.displayMessage("Cập nhật phiếu xuất 完了.");
                    } else {
                        ConsoleView.displayMessage("Cập nhật phiếu xuất 失敗. もう一度してください。");
                    }
                } else {
                    ConsoleView.displayMessage("Mã nhân viên không tồn tại. Vui lòng nhập lại mã nhân viên mới.");
                }
            } else {
                ConsoleView.displayMessage("Không thể cập nhật phiếu xuất ở trạng thái hiện tại.");
            }
        } else {
            ConsoleView.displayMessage("Không tìm thấy phiếu xuất với mã đã nhập.");
        }
    }
    private void displayBillDetails() {
        String billCode = ConsoleView.getInput("Mã phiếu xuất 入力:");
        Bill bill = billDAO.getBillById(billCode);
        if (bill != null) {
            List<BillDetails> billDetails = bill.getDetails();
            for (BillDetails detail : billDetails) {
                ConsoleView.displayMessage("Bill_Detail_Id: " + detail.getBillDetailId());
                ConsoleView.displayMessage("Bill_Id: " + detail.getBillId());
                ConsoleView.displayMessage("Product_Id: " + detail.getProductId());
                ConsoleView.displayMessage("Quantity: " + detail.getQuantity());
                ConsoleView.displayMessage("Price: " + detail.getPrice());
                ConsoleView.displayMessage("-------------------------------------");
            }
        } else {
            ConsoleView.displayMessage("Không tìm thấy phiếu xuất với mã đã nhập.");
        }
    }

    private void approveBill() {
        String billCode = ConsoleView.getInput("Mã phiếu xuất hoặc mã code phiếu xuất:");
        Bill bill = billDAO.getBillById(billCode);
        if (bill != null) {
            if (bill.getBillStatus() == 0) {
                // Chuyển trạng thái từ "tạo" thành "duyệt"
                bill.setBillStatus((byte) 2);
                boolean success = billDAO.updateReceipt(bill);
                if (success) {
                    ConsoleView.displayMessage("Duyệt phiếu xuất thành công.");

                    // Trừ số lượng sản phẩm xuất vào số lượng của sản phẩm trong bảng PRODUCT
                    List<BillDetails> details = bill.getDetails();
                    for (BillDetails detail : details) {
                        // Cập nhật số lượng sản phẩm trong bảng PRODUCT
                        boolean productUpdated = billDAO.updateProductQuantity(detail.getProductId(), detail.getQuantity());
                        if (!productUpdated) {
                            ConsoleView.displayMessage("Lỗi khi cập nhật số lượng sản phẩm.");
                        }
                    }
                } else {
                    ConsoleView.displayMessage("Duyệt phiếu xuất thất bại.");
                }
            } else {
                ConsoleView.displayMessage("Không thể duyệt phiếu xuất đã duyệt hoặc không tồn tại.");
            }
        } else {
            ConsoleView.displayMessage("Không tìm thấy phiếu xuất với mã đã nhập.");
        }
    }

    private void searchBill() {
        String billCode = ConsoleView.getInput("Mã phiếu xuất 入力:");
        Bill receipt = billDAO.getBillById(billCode);
        if (receipt != null) {
            ConsoleView.displayMessage(receipt.toString());
            ConsoleView.displayMessage("1. Cập nhật phiếu xuất");
            ConsoleView.displayMessage("2. Duyệt phiếu xuất");
            int option = ConsoleView.getIntInput("Chọn tác vụ:");
            switch (option) {
                case 1:
                    updateBill();
                    break;
                case 2:
                    approveBill();
                    break;
                default:
                    ConsoleView.displayMessage("Lựa chọn không hợp lệ.");
                    break;
            }
        } else {
            ConsoleView.displayMessage("Không tìm thấy phiếu xuất với mã đã nhập.");
        }
    }
}
