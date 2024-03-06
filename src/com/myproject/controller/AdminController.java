package com.myproject.controller;

import com.myproject.dao.*;
import com.myproject.model.*;
import com.myproject.dao.util.Manager;
import com.myproject.view.ConsoleView;
import com.myproject.controller.manager.*;
import com.myproject.dao.util.DatabaseConnector;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class AdminController implements Manager {
    private Scanner scanner;
    private Connection connection;

    private ProductDAO productDAO;
    private EmployeeDAO employeeDAO;
    private AccountDAO accountDAO;
    private ReceiptDAO receiptDAO;
    private BillDAO billDAO;
    private ReportDAO reportDAO;
    private ProductManagement productManagement;
    private EmployeeManagement employeeManagement;
    private AccountManager accountManager;
    private ReceiptManagement receiptManagement;
    private BillManagement billManagement;
    private ReportManagement reportManagement;

    public AdminController(Connection connection) {
        this.connection = connection;
        scanner = new Scanner(System.in);
        productDAO = new ProductDAO(connection);
        employeeDAO = new EmployeeDAO(connection);
        reportDAO = new ReportDAO(connection);
        productManagement = new ProductManagement(scanner, productDAO);
        employeeManagement = new EmployeeManagement(scanner, employeeDAO);
        billManagement = new BillManagement(billDAO);
        reportManagement = new ReportManagement(reportDAO);
    }

    @Override
    public void run() {
        int choice;
        do {
            choice = ConsoleView.showMainMenu();
            switch (choice) {
                case 1:
                    productManagement.run();
                    break;
                case 2:
                    employeeManagement.run();
                    break;
                case 3:
                    AccountDAO accountDAO = new AccountDAO(connection);
                    AccountManager accountManager = new AccountManager(accountDAO);
                    accountManager.run();
                    break;
                case 4:
                    ReceiptDAO receiptDAO = new ReceiptDAO(connection);
                    ReceiptManagement receiptManagement = new ReceiptManagement(receiptDAO);
                    receiptManagement.run();
                    break;
                case 5:
                    billManagement.run();
                    break;
                case 6:
                    reportManagement.run(); // Thêm xử lý cho ReportManagement
                    break;
                case 7:
                    ConsoleView.displayMessage("Đã thoát khỏi chương trình.");
                    break;
                default:
                    ConsoleView.displayMessage("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
                    break;
            }
        } while (choice != 7);
    }
}
