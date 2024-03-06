package com.myproject.controller.manager;

import com.myproject.dao.AccountDAO;
import com.myproject.model.Account;
import com.myproject.view.ConsoleView;
import com.myproject.dao.util.DatabaseConnector;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class AccountManager {
    private Scanner scanner;
    private AccountDAO accountDAO;

    public AccountManager(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        int choice;
        do {
            choice = ConsoleView.showAccountManagementMenu();
            switch (choice) {
                case 1:
                    displayAccounts();
                    break;
                case 2:
                    createAccount();
                    break;
                case 3:
                    updateAccountStatus();
                    break;
                case 4:
                    searchAccount();
                    break;
                case 5:
                    ConsoleView.displayMessage("Đã thoát khỏi quản lý tài khoản.");
                    break;
                default:
                    ConsoleView.displayMessage("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
                    break;
            }
        } while (choice != 5);
    }

    private void displayAccounts() {
        List<Account> accounts = accountDAO.getAllAccounts();
        ConsoleView.displayMessage("Danh sách tài khoản:");
        for (Account account : accounts) {
            ConsoleView.displayMessage(account.toString());
        }
    }

    private void createAccount() {
        ConsoleView.displayMessage("Tạo tài khoản mới:");
        String username = ConsoleView.getInput("Nhập username:");
        String password = ConsoleView.getInput("Nhập password:");
        int permission = ConsoleView.getIntInput("Nhập quyền (0-admin, 1-user):");
        String empId = ConsoleView.getInput("Nhập mã nhân viên:");
        boolean status = true; // Mặc định là active
        Account newAccount = new Account(username, password, permission == 0, empId, status);
        accountDAO.addAccount(newAccount);
        ConsoleView.displayMessage("Tạo tài khoản mới thành công.");
    }

    private void updateAccountStatus() {
        String empId = ConsoleView.getInput("Nhập mã nhân viên của tài khoản cần cập nhật trạng thái:");
        boolean newStatus = ConsoleView.getIntInput("Trạng thái (0-Block, 1-Active):") == 1;
        accountDAO.updateStatus(empId, newStatus);
        ConsoleView.displayMessage("Cập nhật trạng thái tài khoản thành công.");
    }

    private void searchAccount() {
        String keyword = ConsoleView.getInput("Nhập từ khóa tìm kiếm:");
        List<Account> accounts = accountDAO.searchAccounts(keyword);
        if (accounts.isEmpty()) {
            ConsoleView.displayMessage("Không tìm thấy tài khoản nào.");
        } else {
            ConsoleView.displayMessage("Kết quả tìm kiếm:");
            for (int i = 0; i < accounts.size(); i++) {
                ConsoleView.displayMessage((i + 1) + ". " + accounts.get(i).toString());
            }

            int confirm = ConsoleView.getIntInput("Bạn có muốn cập nhật trạng thái của tài khoản không? (1-Có, 0-Không):");
            if (confirm == 1) {
                int choice = ConsoleView.getIntInput("Chọn số thứ tự của tài khoản để cập nhật trạng thái (hoặc 0 để thoát):");
                if (choice >= 1 && choice <= accounts.size()) {
                    Account selectedAccount = accounts.get(choice - 1);
                    ConsoleView.displayMessage("Bạn đã chọn tài khoản: " + selectedAccount.getUsername());
                    updateAccountStatus(selectedAccount);
                } else if (choice != 0) {
                    ConsoleView.displayMessage("Lựa chọn không hợp lệ.");
                }
            }
        }
    }
    private void updateAccountStatus(Account account) {
        String empId = account.getEmpId();
        boolean newStatus = ConsoleView.getIntInput("Trạng thái (0-Block, 1-Active):") == 1;
        accountDAO.updateStatus(empId, newStatus);
        ConsoleView.displayMessage("Cập nhật trạng thái tài khoản thành công.");
    }
}
