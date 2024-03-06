package com.myproject.controller.manager;

import com.myproject.dao.*;
import com.myproject.model.*;
import com.myproject.view.ConsoleView;
import com.myproject.dao.util.Manager;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ProductManagement implements Manager {
    private Scanner scanner;
    private ProductDAO productDAO;

    public ProductManagement(Scanner scanner, ProductDAO productDAO) {
        this.scanner = scanner;
        this.productDAO = productDAO;
    }

    @Override
    public void run() {
        int choice;
        do {
            choice = ConsoleView.showProductManagementMenu();
            switch (choice) {
                case 1:
                    displayProductList();
                    break;
                case 2:
                    addNewProduct();
                    break;
                case 3:
                    updateProduct();
                    break;
                case 4:
                    searchProduct();
                    break;
                case 5:
                    updateProductStatus();
                    break;
                case 6:
                    ConsoleView.displayMessage("Đã thoát khỏi quản lý sản phẩm.");
                    break;
                default:
                    ConsoleView.displayMessage("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
                    break;
            }
        } while (choice != 6);
    }

    private void displayProductList() {
        try {
            List<Product> productList = productDAO.getAllProducts();
            System.out.println("Danh sách sản phẩm:");
            for (Product product : productList) {
                System.out.println(product.getProductId() + " - " + product.getProductName() + ": " + product.getQuantity() + " " + product.isProductStatus());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void addNewProduct() {
        System.out.print("Mã sản phẩm 入力: ");
        String productId = scanner.nextLine();
        System.out.print("Tên sản phẩm 入力: ");
        String productName = scanner.nextLine();
        System.out.print("Nhà sản xuất 入力: ");
        String manufacturer = scanner.nextLine();
        System.out.print("Lô chứa sản phẩm 入力: ");
        byte batch = scanner.nextByte();
        scanner.nextLine(); // Đọc dòng trống sau khi nhập số
        Product product = new Product(productId, productName, manufacturer, batch, true, null);
        try {
            productDAO.addProduct(product);
            System.out.println("Thêm mới sản phẩm 完成.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateProduct() {
        System.out.print("Mã sản phẩm 入力: ");
        String productId = scanner.nextLine();
        System.out.print("Tên sản phẩm 入力: ");
        String newName = scanner.nextLine();
        System.out.print("Nhà sản xuất 入力: ");
        String newManufacturer = scanner.nextLine();
        System.out.print("Lô chứa sản phẩm 入力: ");
        byte newBatch = scanner.nextByte();
        scanner.nextLine(); // Đọc dòng trống sau khi nhập số
        Product updatedProduct = new Product(productId, newName, newManufacturer, newBatch, true, null);
        try {
            productDAO.updateProduct(updatedProduct);
            System.out.println("Cập nhật sản phẩm 完成.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void searchProduct() {
        System.out.print("Nhập tên sản phẩm cần tìm: ");
        String productName = scanner.nextLine();
        try {
            List<Product> productList = productDAO.searchProductByName(productName);
            if (productList.isEmpty()) {
                System.out.println("検索失敗。");
            } else {
                System.out.println("Kết quả tìm kiếm:");
                for (Product product : productList) {
                    System.out.println(product.getProductId() + " - " + product.getProductName());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateProductStatus() {
        System.out.print("Mã hoặc tên sản phẩm 入力: ");
        String productIdOrName = scanner.nextLine();

        boolean status;
        do {
            System.out.print("Trạng thái (true - hoạt động, false - không hoạt động) 入力: ");
            String statusInput = scanner.nextLine().toLowerCase(); // Chuyển đổi sang chữ thường
            if (statusInput.equals("true")) {
                status = true;
                break;
            } else if (statusInput.equals("false")) {
                status = false;
                break;
            } else {
                System.out.println("Giá trị không hợp lệ. Vui lòng nhập lại.");
            }
        } while (true);

        try {
            List<Product> productList = productDAO.searchProductByIdOrName(productIdOrName);

            if (productList.isEmpty()) {
                System.out.println("商品が見つかりません。");
            } else if (productList.size() > 1) {
                System.out.println("Có nhiều sản phẩm phù hợp. Vui lòng nhập rõ hơn.");
            } else {
                Product product = productList.get(0);
                productDAO.updateProductStatus(product.getProductId(), status);
                System.out.println("Cập nhật trạng thái 完成.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
