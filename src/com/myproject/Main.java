package com.myproject;

import com.myproject.dao.util.DatabaseConnector;
import com.myproject.view.ConsoleView;
import com.myproject.controller.AdminController;
import com.myproject.controller.UserController;

import java.sql.*;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = DatabaseConnector.getConnection()) {
            if (connection != null) {
                System.out.println("Connected to the database!");
                String username = ConsoleView.getInput("Enter username");
                String password = ConsoleView.getInput("Enter password");
                String query = "SELECT Permission FROM ACCOUNT WHERE User_name = ? AND Password = ?";
                try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
                    preparedStatement.setString(1, username);
                    preparedStatement.setString(2, password);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        int permission = resultSet.getInt("Permission");
                        if (permission == 0) {
                            AdminController adminController = new AdminController(connection);
                            adminController.run();
                        } else if (permission == 1) {
                            UserController userController = new UserController(connection);
                            userController.run();
                        } else {
                            System.out.println("Invalid permission level.");
                        }
                    } else {
                        System.out.println("Invalid username or password");
                    }
                }
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
