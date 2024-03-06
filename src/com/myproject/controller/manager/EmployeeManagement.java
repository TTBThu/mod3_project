package com.myproject.controller.manager;

import com.myproject.dao.EmployeeDAO;
import com.myproject.model.Employee;
import com.myproject.view.ConsoleView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class EmployeeManagement {
    private Scanner scanner;
    private EmployeeDAO employeeDAO;

    public EmployeeManagement(Scanner scanner, EmployeeDAO employeeDAO) {
        this.scanner = scanner;
        this.employeeDAO = employeeDAO;
    }

    public void run() {
        int choice;
        do {
            choice = ConsoleView.showEmployeeManagementMenu();
            switch (choice) {
                case 1:
                    displayEmployeeList();
                    break;
                case 2:
                    addEmployee();
                    break;
                case 3:
                    updateEmployee();
                    break;
                case 4:
                    updateEmployeeStatus();
                    break;
                case 5:
                    searchEmployee();
                    break;
                case 6:
                    ConsoleView.displayMessage("Đã thoát khỏi quản lý nhân viên.");
                    break;
                default:
                    ConsoleView.displayMessage("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
                    break;
            }
        } while (choice != 6);
    }

    public static class InputHelper {

        public static Date getDateInput(String message) {
            Date date = null;
            boolean isValid = false;
            do {
                String input = ConsoleView.getInput(message + " (yyyy-MM-dd): ");
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    date = dateFormat.parse(input);
                    isValid = true;
                } catch (ParseException e) {
                    ConsoleView.displayMessage("Định dạng không hợp lệ. Vui lòng nhập lại.");
                }
            } while (!isValid);
            return date;
        }
    }

    private void displayEmployeeList() {
        int offset = 0;
        List<Employee> employees = employeeDAO.getEmployees(offset);
        while (!employees.isEmpty()) {
            for (Employee employee : employees) {
                ConsoleView.displayMessage(employee.toString());
            }
            ConsoleView.displayMessage("Nhấn Enter để xem tiếp...");
            scanner.nextLine();
            offset += 10;
            employees = employeeDAO.getEmployees(offset);
        }
    }

    private void addEmployee() {
        ConsoleView.displayMessage("Thêm nhân viên mới:");
        String empId = ConsoleView.getInput("Mã nhân viên 入力:");
        String empName = ConsoleView.getInput("Tên nhân viên 入力:");
        Date birthDate = InputHelper.getDateInput("Ngày sinh 入力:");
        String email = ConsoleView.getInput("Email 入力:");
        String phone = ConsoleView.getInput("Số điện thoại 入力:");
        String address = ConsoleView.getInput("Địa chỉ 入力:");
        byte empStatus = 0; // Mặc định trạng thái là hoạt động
        Employee newEmployee = new Employee(empId, empName, birthDate, email, phone, address, empStatus);
        boolean success = employeeDAO.addEmployee(newEmployee);
        if (success) {
            ConsoleView.displayMessage("Thêm mới nhân viên 完成.");
        } else {
            ConsoleView.displayMessage("Thêm mới nhân viên 失敗.");
        }
    }

    private void updateEmployee() {
        String empIdToUpdate = ConsoleView.getInput("Nhập mã nhân viên cần cập nhật");
        if (employeeDAO.isEmployeeExists(empIdToUpdate)) {
            String empName = ConsoleView.getInput("Tên nhân viên 入力:");
            Date birthDate = InputHelper.getDateInput("Ngày sinh 入力:");
            String email = ConsoleView.getInput("Email 入力:");
            String phone = ConsoleView.getInput("Số điện thoại 入力:");
            String address = ConsoleView.getInput("Địa chỉ 入力:");
            Employee updatedEmployee = new Employee(empIdToUpdate, empName, birthDate, email, phone, address, (byte) 0);
            boolean success = employeeDAO.updateEmployee(updatedEmployee);
            if (success) {
                ConsoleView.displayMessage("Cập nhật 完成.");
            } else {
                ConsoleView.displayMessage("Cập nhật 失敗.");
            }
        } else {
            ConsoleView.displayMessage("見つかりませんでした。");
        }
    }

    private void updateEmployeeStatus() {
        String keyword = ConsoleView.getInput("Nhập mã hoặc tên nhân viên cần cập nhật trạng thái : ");
        List<Employee> searchResult = employeeDAO.searchEmployees(keyword);
        if (!searchResult.isEmpty()) {
            if (searchResult.size() == 1) {
                // Nếu chỉ có một nhân viên được tìm thấy, chọn ngay nhân viên này để cập nhật trạng thái
                Employee employeeToUpdate = searchResult.get(0);
                byte empStatus = (byte) ConsoleView.getIntInput("Chọn trạng thái mới cho nhân viên (0: Hoạt động | 1: Nghỉ chế độ | 2: Nghỉ việc) : ");
                boolean success = employeeDAO.updateEmployeeStatus(employeeToUpdate.getEmpId(), empStatus);
                if (success) {
                    ConsoleView.displayMessage("Cập nhật trạng thái 完成.");
                } else {
                    ConsoleView.displayMessage("Cập nhật trạng thái 失敗.");
                }
            } else {
                // Nếu có nhiều hơn một nhân viên được tìm thấy, yêu cầu người dùng chọn mã nhân viên cụ thể
                ConsoleView.displayMessage("Có nhiều nhân viên được tìm thấy. Vui lòng chọn mã nhân viên cụ thể để cập nhật trạng thái:");
                for (Employee employee : searchResult) {
                    ConsoleView.displayMessage(employee.toString());
                }
                String empIdToUpdate = ConsoleView.getInput("Mã nhân viên cần cập nhật trạng thái 入力:");
                // Kiểm tra xem mã nhân viên nhập vào có tồn tại trong danh sách tìm kiếm hay không
                boolean found = false;
                for (Employee employee : searchResult) {
                    if (employee.getEmpId().equalsIgnoreCase(empIdToUpdate)) {
                        found = true;
                        break;
                    }
                }
                if (found) {
                    byte empStatus = (byte) ConsoleView.getIntInput("Chọn trạng thái mới cho nhân viên (0: Hoạt động | 1: Nghỉ chế độ | 2: Nghỉ việc)");
                    boolean success = employeeDAO.updateEmployeeStatus(empIdToUpdate, empStatus);
                    if (success) {
                        ConsoleView.displayMessage("Cập nhật trạng thái 完成.");
                    } else {
                        ConsoleView.displayMessage("Cập nhật trạng thái 失敗.");
                    }
                } else {
                    ConsoleView.displayMessage("見つかりませんでした。");
                }
            }
        } else {
            ConsoleView.displayMessage("見つかりませんでした。");
        }
    }

    private void searchEmployee() {
        String keyword = ConsoleView.getInput("Nhập mã hoặc tên nhân viên cần tìm kiếm");
        List<Employee> searchResult = employeeDAO.searchEmployees(keyword);
        if (!searchResult.isEmpty()) {
            ConsoleView.displayMessage("Kết quả tìm kiếm:");
            for (Employee employee : searchResult) {
                ConsoleView.displayMessage(employee.toString());
            }
        } else {
            ConsoleView.displayMessage("見つかりませんでした。");
        }
    }
}
