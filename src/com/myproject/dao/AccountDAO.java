package com.myproject.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.myproject.dao.util.DatabaseConnector;
import com.myproject.model.Account;

public class AccountDAO {
    private final Connection connection;

    public AccountDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT * FROM Account";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                Account account = mapResultSetToAccount(resultSet);
                accounts.add(account);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    public void addAccount(Account newAccount) {
        String query = "INSERT INTO Account (User_name, password, permission, Emp_id, Acc_status) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, newAccount.getUsername());
            preparedStatement.setString(2, newAccount.getPassword());
            preparedStatement.setBoolean(3, newAccount.isAdmin());
            preparedStatement.setString(4, newAccount.getEmpId());
            preparedStatement.setBoolean(5, newAccount.isStatus());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateStatus(String empId, boolean newStatus) {
        String query = "UPDATE Account SET Acc_status = ? WHERE Emp_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setBoolean(1, newStatus);
            preparedStatement.setString(2, empId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Account> searchAccounts(String keyword) {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT * FROM Account WHERE User_name LIKE ? OR Emp_Id IN (SELECT Emp_Id FROM Employee WHERE Emp_Name LIKE ?) ORDER BY User_name";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, "%" + keyword + "%");
            preparedStatement.setString(2, "%" + keyword + "%");
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    Account account = mapResultSetToAccount(resultSet);
                    accounts.add(account);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return accounts;
    }

    private Account mapResultSetToAccount(ResultSet resultSet) throws SQLException {
        Account account = new Account(
                resultSet.getString("User_name"),
                resultSet.getString("Password"),
                resultSet.getBoolean("Permission"),
                resultSet.getString("Emp_id"),
                resultSet.getBoolean("Acc_status")
        );
        account.setAccountId(resultSet.getInt("Acc_id"));
        return account;
    }
}
