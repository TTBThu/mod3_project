package com.myproject.view;

import java.util.Scanner;
import com.myproject.model.Report;
import com.myproject.model.Product;
import com.myproject.controller.manager.*;

public class ConsoleView {

    private static Scanner scanner;

    static {
        scanner = new Scanner(System.in);
    }

    public static int showMainMenu() {
        System.out.println("******************WAREHOUSE MANAGEMENT****************");
        System.out.println("1. Quản lý sản phẩm");
        System.out.println("2. Quản lý nhân viên");
        System.out.println("3. Quản lý tài khoản");
        System.out.println("4. Quản lý phiếu nhập");
        System.out.println("5. Quản lý phiếu xuất");
        System.out.println("6. Quản lý báo cáo");
        System.out.println("7. Thoát");
        System.out.print("Chọn chức năng: ");
        return scanner.nextInt();
    }

    public static int showProductManagementMenu() {
        System.out.println("******************PRODUCT MANAGEMENT****************");
        System.out.println("1. Danh sách sản phẩm");
        System.out.println("2. Thêm mới sản phẩm");
        System.out.println("3. Cập nhật sản phẩm");
        System.out.println("4. Tìm kiếm sản phẩm");
        System.out.println("5. Cập nhật trạng thái sản phẩm");
        System.out.println("6. Thoát");
        System.out.print("Chọn chức năng: ");
        return scanner.nextInt();
    }

    public static int showEmployeeManagementMenu() {
        System.out.println("******************EMPLOYEE MANAGEMENT****************");
        System.out.println("1. Danh sách nhân viên");
        System.out.println("2. Thêm mới nhân viên");
        System.out.println("3. Cập nhật thông tin nhân viên");
        System.out.println("4. Cập nhật trạng thái nhân viên");
        System.out.println("5. Tìm kiếm nhân viên");
        System.out.println("6. Thoát");
        System.out.print("Chọn chức năng: ");
        return scanner.nextInt();
    }

    public static int showAccountManagementMenu() {
        System.out.println("******************ACCOUNT MANAGEMENT****************");
        System.out.println("1. Danh sách tài khoản");
        System.out.println("2. Tạo tài khoản mới");
        System.out.println("3. Cập nhật trạng thái tài khoản");
        System.out.println("4. Tìm kiếm tài khoản");
        System.out.println("5. Thoát");
        System.out.print("Chọn chức năng: ");
        return scanner.nextInt();
    }

    public static int showReceiptManagementMenu() {
        System.out.println("******************RECEIPT MANAGEMENT****************");
        System.out.println("1. Danh sách phiếu nhập");
        System.out.println("2. Tạo phiếu nhập");
        System.out.println("3. Cập nhật thông tin phiếu nhập");
        System.out.println("4. Chi tiết phiếu nhập");
        System.out.println("5. Duyệt phiếu nhập");
        System.out.println("6. Tìm kiếm phiếu nhập");
        System.out.println("7. Thoát");
        System.out.print("Chọn chức năng: ");
        return scanner.nextInt();
    }

    public static int showBillManagementMenu() {
        System.out.println("******************BILL MANAGEMENT****************");
        System.out.println("1. Danh sách phiếu xuất");
        System.out.println("2. Tạo phiếu xuất");
        System.out.println("3. Cập nhật thông tin phiếu xuất");
        System.out.println("4. Chi tiết phiếu xuất");
        System.out.println("5. Duyệt phiếu xuất");
        System.out.println("6. Tìm kiếm phiếu xuất");
        System.out.println("7. Thoát");
        System.out.print("Chọn chức năng: ");
        return scanner.nextInt();
    }

    public static int showReportManagementMenu() {
        System.out.println("******************REPORT MANAGEMENT****************");
        System.out.println("1. Thống kê chi phí theo ngày, tháng, năm");
        System.out.println("2. Thống kê chi phí theo khoảng thời gian");
        System.out.println("3. Thống kê doanh thu theo ngày, tháng, năm");
        System.out.println("4. Thống kê doanh thu theo khoảng thời gian");
        System.out.println("5. Thống kê số nhân viên theo từng trạng thái");
        System.out.println("6. Thống kê sản phẩm nhập nhiều nhất trong khoảng thời gian");
        System.out.println("7. Thống kê sản phẩm nhập ít nhất trong khoảng thời gian");
        System.out.println("8. Thống kê sản phẩm xuất nhiều nhất trong khoảng thời gian");
        System.out.println("9. Thống kê sản phẩm xuất ít nhất trong khoảng thời gian");
        System.out.println("10. Thoát");
        System.out.print("Chọn chức năng: ");
        return scanner.nextInt();
    }

    public static int showUserWarehouseManagementMenu() {
        System.out.println("******************WAREHOUSE MANAGEMENT****************");
        System.out.println("1. Danh sách phiếu nhập theo trạng thái");
        System.out.println("2. Tạo phiếu nhập");
        System.out.println("3. Cập nhật phiếu nhập");
        System.out.println("4. Tìm kiếm phiếu nhập");
        System.out.println("5. Danh sách phiếu xuất theo trạng thái");
        System.out.println("6. Tạo phiếu xuất");
        System.out.println("7. Cập nhật phiếu xuất");
        System.out.println("8. Tìm kiếm phiếu xuất");
        System.out.println("9. Thoát");
        System.out.print("Chọn chức năng: ");
        return scanner.nextInt();
    }

    //public static void closeScanner() {
    //        scanner.close();
    //    }

    public static void displayMessage(String message) {
        System.out.println(message);
    }

    public static String getInput(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.next();
    }

    public static int getIntInput(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextInt();
    }

    public static float getFloatInput(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextFloat();
    }

    public static void displayProduct(Product product) {
        System.out.println("Thông tin sản phẩm:");
        System.out.println("Product ID: " + product.getProductId());
        System.out.println("Product Name: " + product.getProductName());
        System.out.println("Manufacturer: " + product.getManufacturer());
        System.out.println("Batch: " + product.getBatch());
        System.out.println("Quantity: " + product.getQuantity());
        System.out.println("Product Status: " + (product.isProductStatus() ? "Active" : "Inactive"));
        System.out.println("Created: " + product.getCreated());
    }

    public static void displayBill(Bill bill) {
        System.out.println("Thông tin phiếu:");
        System.out.println("Mã phiếu: " + bill.getBillCode());
        System.out.println("Loại phiếu: " + (bill.isBillType() ? "Xuất" : "Nhập"));
        System.out.println("Ngày tạo: " + bill.getCreated());
        System.out.println("Trạng thái: " + (bill.getBillStatus() == 1 ? "Đã xác nhận" : "Chưa xác nhận"));
        if (bill.getEmpIdAuth() != null && bill.getAuthDate() != null) {
            System.out.println("Nhân viên duyệt: " + bill.getEmpIdAuth());
            System.out.println("Ngày duyệt: " + bill.getAuthDate());
        }
        if (!bill.getDetails().isEmpty()) {
            System.out.println("Chi tiết phiếu:");
            for (BillDetails details : bill.getDetails()) {
                System.out.println("  Mã sản phẩm: " + details.getProductId());
                System.out.println("  Số lượng: " + details.getQuantity());
                System.out.println("  Đơn giá: " + details.getPrice());
            }
        } else {
            System.out.println("Phiếu không có chi tiết.");
        }
    }

    public static Bill createBillFromInput() {
        // Tạo và trả về một phiếu nhập mới từ dữ liệu nhập từ người dùng
        Bill bill = new Bill();
        // Điền thông tin cho phiếu nhập, bao gồm cả chi tiết nếu có
        return bill;
    }

    public static Bill updateBillFromInput(Bill existingBill) {
        // Cập nhật thông tin của phiếu nhập từ dữ liệu nhập từ người dùng
        Bill updatedBill = existingBill;
        // Cập nhật thông tin cho phiếu nhập, bao gồm cả chi tiết nếu có
        return updatedBill;
    }
}

