package com.myproject.controller.manager;

import com.myproject.controller.manager.*;
import com.myproject.dao.*;
import com.myproject.dao.util.BillType;
import java.util.Date;

import com.myproject.dao.util.Manager;
import com.myproject.view.ConsoleView;

import java.util.List;

public class ReceiptManagement implements Manager {
    private ReceiptDAO receiptDAO;

    public ReceiptManagement(ReceiptDAO receiptDAO) {
        this.receiptDAO = receiptDAO;
    }

    @Override
    public void run() {
        int choice;
        do {
            choice = ConsoleView.showReceiptManagementMenu();
            switch (choice) {
                case 1:
                    displayReceiptList();
                    break;
                case 2:
                    createReceipt();
                    break;
                case 3:
                    updateReceipt();
                    break;
                case 4:
                    displayReceiptDetails();
                    break;
                case 5:
                    approveReceipt();
                    break;
                case 6:
                    searchReceipt();
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

    private void displayReceiptList() {
        List<Bill> bills = receiptDAO.getAllReceipts();
        // Hiển thị danh sách phiếu nhập
        for (Bill bill : bills) {
            ConsoleView.displayMessage(bill.toString());
        }
    }

    private void createReceipt() {
        // Nhập thông tin của phiếu nhập
        String billCode = ConsoleView.getInput("Mã phiếu 入力:");
        boolean billType = BillType.IMPORT; // Sử dụng hằng số từ interface BillType
        String empIdCreated = ConsoleView.getInput("Mã nhân viên tạo phiếu 入力:");
        String empIdAuth = ConsoleView.getInput("Mã nhân viên duyệt phiếu 入力:");
        Date created = new Date();
        // Mặc định trạng thái là Tạo (0)
        byte billStatus = 0;

        // Kiểm tra xem mã nhân viên có tồn tại không
        if (receiptDAO.employeeExists(empIdCreated)) {
            // Mã nhân viên tồn tại, tiếp tục tạo phiếu
            // Tạo đối tượng phiếu nhập
            Bill bill = new Bill(billCode, billType, empIdCreated, created, empIdAuth, billStatus);

            // Nhập thông tin của chi tiết phiếu nhập
            int numberOfDetails = ConsoleView.getIntInput("Số lượng chi tiết phiếu nhập 入力:");
            for (int i = 0; i < numberOfDetails; i++) {
                String productId = ConsoleView.getInput("Mã sản phẩm 入力:");
                int quantity = ConsoleView.getIntInput("Số lượng 入力:");
                float price = ConsoleView.getFloatInput("Giá 入力:");
                // Tạo đối tượng chi tiết phiếu nhập
                BillDetails details = new BillDetails(0, 0, productId, quantity, price); // Mã phiếu nhập (billId) sẽ được cập nhật sau khi tạo phiếu nhập

                // Thêm chi tiết phiếu nhập vào danh sách chi tiết của phiếu nhập
                bill.getDetails().add(details);
            }

            // Lưu phiếu nhập và chi tiết phiếu nhập vào cơ sở dữ liệu
            boolean success = receiptDAO.createReceipt(bill);
            if (success) {
                ConsoleView.displayMessage("Tạo phiếu nhập 完了.");
            } else {
                ConsoleView.displayMessage("Tạo phiếu nhập 失敗. もう一度してください。");
            }
        } else {
            // Mã nhân viên không tồn tại, yêu cầu nhập lại mã nhân viên
            ConsoleView.displayMessage("Mã nhân viên 存在しません。 もう一度してください。");
        }
    }

    private void updateReceipt() {
        // Nhập mã phiếu nhập để tìm phiếu nhập cần cập nhật
        String billCode = ConsoleView.getInput("Mã phiếu nhập 入力:");
        Bill bill = receiptDAO.getBillById(billCode);
        if (bill != null) {
            // Kiểm tra trạng thái phiếu nhập
            if (bill.getBillStatus() == 0 || bill.getBillStatus() == 1) {
                // Cho phép cập nhật

                // Nhập mã nhân viên mới
                String newEmpIdCreated = ConsoleView.getInput("Mã nhân viên cập nhật 入力:");
                // Kiểm tra xem mã nhân viên mới có tồn tại không
                if (receiptDAO.employeeExists(newEmpIdCreated)) {
                    // Mã nhân viên tồn tại, tiếp tục cập nhật phiếu nhập
                    // Cập nhật mã nhân viên trong phiếu nhập
                    bill.setEmpIdCreated(newEmpIdCreated);
                    // Nhập thông tin chi tiết phiếu nhập cần cập nhật
                    int numberOfDetails = ConsoleView.getIntInput("Số lượng chi tiết phiếu nhập 入力:");
                    bill.getDetails().clear(); // Xóa danh sách chi tiết cũ để cập nhật lại
                    for (int i = 0; i < numberOfDetails; i++) {
                        String productId = ConsoleView.getInput("Mã sản phẩm 入力:");
                        int quantity = ConsoleView.getIntInput("Số lượng 入力:");
                        float price = ConsoleView.getFloatInput("Giá 入力:");
                        // Tạo đối tượng chi tiết phiếu nhập
                        BillDetails details = new BillDetails(0, 0, productId, quantity, price); // Mã phiếu nhập (billId) sẽ được cập nhật sau khi tạo phiếu nhập

                        // Thêm chi tiết phiếu nhập vào danh sách chi tiết của phiếu nhập
                        bill.getDetails().add(details);
                    }

                    // Thực hiện cập nhật phiếu nhập vào cơ sở dữ liệu
                    boolean success = receiptDAO.updateReceipt(bill);
                    if (success) {
                        ConsoleView.displayMessage("Cập nhật phiếu nhập 完了.");
                    } else {
                        ConsoleView.displayMessage("Cập nhật phiếu nhập 失敗. もう一度してください。");
                    }
                } else {
                    // Mã nhân viên không tồn tại, yêu cầu nhập lại mã nhân viên mới
                    ConsoleView.displayMessage("Mã nhân viên không tồn tại. Vui lòng nhập lại mã nhân viên mới.");
                }
            } else {
                ConsoleView.displayMessage("Không thể cập nhật phiếu nhập ở trạng thái hiện tại.");
            }
        } else {
            ConsoleView.displayMessage("Không tìm thấy phiếu nhập với mã đã nhập.");
        }
    }

    private void displayReceiptDetails() {
        // Hiển thị chi tiết của một phiếu nhập dựa trên mã phiếu nhập
        String billCode = ConsoleView.getInput("Mã phiếu nhập 入力:");
        Bill receipt = receiptDAO.getBillById(billCode);
        if (receipt != null) {
            List<BillDetails> billDetails = receipt.getDetails();
            for (BillDetails detail : billDetails) {
                ConsoleView.displayMessage("Bill_Detail_Id: " + detail.getBillDetailId());
                ConsoleView.displayMessage("Bill_Id: " + detail.getBillId());
                ConsoleView.displayMessage("Product_Id: " + detail.getProductId());
                ConsoleView.displayMessage("Quantity: " + detail.getQuantity());
                ConsoleView.displayMessage("Price: " + detail.getPrice());
                ConsoleView.displayMessage("-------------------------------------");
            }
        } else {
            ConsoleView.displayMessage("Không tìm thấy phiếu nhập với mã đã nhập.");
        }
    }

    private void approveReceipt() {
        // Kiểm tra trạng thái phiếu nhập, chỉ cho phép duyệt khi ở trạng thái Tạo
        // Duyệt phiếu nhập, cập nhật trạng thái và số lượng sản phẩm trong kho, cập nhật vào cơ sở dữ liệu
        String billCode = ConsoleView.getInput("Mã phiếu nhập 入力:");
        Bill receipt = receiptDAO.getBillById(billCode);
        if (receipt != null) {
            if (receipt.getBillStatus() == 0) {
                // Nhập mã nhân viên duyệt
                String empIdAuth = ConsoleView.getInput("Mã nhân viên duyệt 入力:");
                // Kiểm tra xem mã nhân viên có tồn tại không
                if (receiptDAO.employeeExists(empIdAuth)) {
                    // Mã nhân viên tồn tại, tiếp tục duyệt phiếu
                    // Duyệt phiếu nhập
                    receipt.setBillStatus((byte) 2); // Trạng thái Duyệt
                    receipt.setEmpIdAuth(empIdAuth); // Lưu mã nhân viên duyệt vào phiếu nhập
                    receipt.setAuthDate(new Date()); // Cập nhật ngày duyệt là ngày hiện tại
                    boolean success = receiptDAO.updateReceipt(receipt);
                    if (success) {
                        // Cập nhật số lượng sản phẩm trong kho cho từng chi tiết phiếu nhập
                        for (BillDetails detail : receipt.getDetails()) {
                            receiptDAO.updateProductQuantity(detail);
                        }
                        ConsoleView.displayMessage("Duyệt phiếu nhập 完了.");
                    } else {
                        ConsoleView.displayMessage("Duyệt phiếu nhập 失敗. もう一度してください。");
                    }
                } else {
                    // Mã nhân viên không tồn tại, yêu cầu nhập lại mã nhân viên
                    ConsoleView.displayMessage("Mã nhân viên 存在しません。 もう一度してください。");
                }
            } else {
                ConsoleView.displayMessage("Phiếu nhập đã được duyệt hoặc ở trạng thái khác.");
            }
        } else {
            ConsoleView.displayMessage("Không tìm thấy phiếu nhập với mã đã nhập.");
        }
    }

    private void searchReceipt() {
        // Tìm kiếm phiếu nhập theo mã hoặc mã code phiếu nhập, cho phép cập nhật và duyệt phiếu nhập
        String billCode = ConsoleView.getInput("Mã phiếu nhập 入力:");
        Bill receipt = receiptDAO.getBillById(billCode);
        if (receipt != null) {
            // Hiển thị thông tin phiếu nhập
            ConsoleView.displayMessage(receipt.toString());
            // Cho phép cập nhật và duyệt phiếu nhập
            ConsoleView.displayMessage("1. Cập nhật phiếu nhập");
            ConsoleView.displayMessage("2. Duyệt phiếu nhập");
            int option = ConsoleView.getIntInput("Chọn tác vụ:");
            switch (option) {
                case 1:
                    updateReceipt();
                    break;
                case 2:
                    approveReceipt();
                    break;
                default:
                    ConsoleView.displayMessage("Lựa chọn không hợp lệ.");
                    break;
            }
        } else {
            ConsoleView.displayMessage("Không tìm thấy phiếu nhập với mã đã nhập.");
        }
    }
}
