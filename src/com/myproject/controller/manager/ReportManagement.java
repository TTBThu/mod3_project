package com.myproject.controller.manager;

import com.myproject.dao.ReportDAO;
import com.myproject.model.Report;
import com.myproject.view.ConsoleView;
import com.myproject.model.Product;

import java.sql.SQLException;
import java.util.List;

public class ReportManagement {
    private ReportDAO reportDAO;

    public ReportManagement(ReportDAO reportDAO) {
        this.reportDAO = reportDAO;
    }

    public void run() {
        int choice;
        do {
            choice = ConsoleView.showReportManagementMenu();
            switch (choice) {
                case 1:
                    // Xử lý thống kê chi phí theo ngày, tháng, năm
                    handleExpenseReportByDate();
                    break;
                case 2:
                    // Xử lý thống kê chi phí theo khoảng thời gian
                    handleExpenseReportByTimeRange();
                    break;
                case 3:
                    // Xử lý thống kê doanh thu theo ngày, tháng, năm
                    handleRevenueReportByDate();
                    break;
                case 4:
                    // Xử lý thống kê doanh thu theo khoảng thời gian
                    handleRevenueReportByTimeRange();
                    break;
                case 5:
                    // Xử lý thống kê số nhân viên theo từng trạng thái
                    handleEmployeeCountByStatus();
                    break;
                case 6:
                    // Xử lý thống kê sản phẩm nhập nhiều nhất trong khoảng thời gian
                    handleProductWithMaxImportQuantity();
                    break;
                case 7:
                    // Xử lý thống kê sản phẩm nhập ít nhất trong khoảng thời gian
                    handleProductWithMinImportQuantity();
                    break;
                case 8:
                    // Xử lý thống kê sản phẩm xuất nhiều nhất trong khoảng thời gian
                    handleProductWithMaxExportQuantity();
                    break;
                case 9:
                    // Xử lý thống kê sản phẩm xuất ít nhất trong khoảng thời gian
                    handleProductWithMinExportQuantity();
                    break;
                case 10:
                    ConsoleView.displayMessage("Đã thoát khỏi quản lý báo cáo.");
                    break;
                default:
                    ConsoleView.displayMessage("Lựa chọn không hợp lệ. Vui lòng chọn lại.");
                    break;
            }
        } while (choice != 10);
    }

    private void handleExpenseReportByDate() {
        // Xử lý thống kê chi phí theo ngày, tháng, năm
        int year = ConsoleView.getIntInput("Nhập năm: ");
        int month = ConsoleView.getIntInput("Nhập tháng: ");
        int day = ConsoleView.getIntInput("Nhập ngày: ");
        try {
            List<Report> reports = reportDAO.getExpenseReportByDate(year, month, day);
            for (Report report : reports) {
                ConsoleView.displayMessage(report.toString());
            }
        } catch (SQLException e) {
            ConsoleView.displayMessage("Lỗi khi thực hiện thống kê chi phí theo ngày: " + e.getMessage());
        }
    }

    private void handleExpenseReportByTimeRange() {
        // Xử lý thống kê chi phí theo khoảng thời gian
        String startDate = ConsoleView.getInput("Nhập ngày bắt đầu (yyyy-MM-dd): ");
        String endDate = ConsoleView.getInput("Nhập ngày kết thúc (yyyy-MM-dd): ");
        try {
            List<Report> reports = reportDAO.getExpenseReportByTimeRange(startDate, endDate);
            for (Report report : reports) {
                ConsoleView.displayMessage(report.toString());
            }
        } catch (SQLException e) {
            ConsoleView.displayMessage("Lỗi khi thực hiện thống kê chi phí theo khoảng thời gian: " + e.getMessage());
        }
    }

    private void handleRevenueReportByDate() {
        // Xử lý thống kê doanh thu theo ngày, tháng, năm
        int year = ConsoleView.getIntInput("Nhập năm: ");
        int month = ConsoleView.getIntInput("Nhập tháng: ");
        int day = ConsoleView.getIntInput("Nhập ngày: ");
        try {
            List<Report> reports = reportDAO.getRevenueReportByDate(year, month, day);
            for (Report report : reports) {
                ConsoleView.displayMessage(report.toString());
            }
        } catch (SQLException e) {
            ConsoleView.displayMessage("Lỗi khi thực hiện thống kê doanh thu theo ngày: " + e.getMessage());
        }
    }

    private void handleRevenueReportByTimeRange() {
        // Xử lý thống kê doanh thu theo khoảng thời gian
        String startDate = ConsoleView.getInput("Nhập ngày bắt đầu (yyyy-MM-dd): ");
        String endDate = ConsoleView.getInput("Nhập ngày kết thúc (yyyy-MM-dd): ");
        try {
            List<Report> reports = reportDAO.getRevenueReportByTimeRange(startDate, endDate);
            for (Report report : reports) {
                ConsoleView.displayMessage(report.toString());
            }
        } catch (SQLException e) {
            ConsoleView.displayMessage("Lỗi khi thực hiện thống kê doanh thu theo khoảng thời gian: " + e.getMessage());
        }
    }

    private void handleEmployeeCountByStatus() {
        // Xử lý thống kê số nhân viên theo từng trạng thái
        try {
            int[] counts = reportDAO.getEmployeeCountByStatus();
            ConsoleView.displayMessage("Số nhân viên theo từng trạng thái: Active = " + counts[0] + ", Inactive = " + counts[1]);
        } catch (SQLException e) {
            ConsoleView.displayMessage("Lỗi khi thực hiện thống kê số nhân viên theo trạng thái: " + e.getMessage());
        }
    }

    private void handleProductWithMaxImportQuantity() {
        // Xử lý thống kê sản phẩm nhập nhiều nhất trong khoảng thời gian
        String startDate = ConsoleView.getInput("Nhập ngày bắt đầu (yyyy-MM-dd): ");
        String endDate = ConsoleView.getInput("Nhập ngày kết thúc (yyyy-MM-dd): ");
        try {
            Product product = reportDAO.getProductWithMaxImportQuantity(startDate, endDate);
            ConsoleView.displayProduct(product);
        } catch (SQLException e) {
            ConsoleView.displayMessage("Lỗi khi thực hiện thống kê sản phẩm nhập nhiều nhất: " + e.getMessage());
        }
    }

    private void handleProductWithMinImportQuantity() {
        // Xử lý thống kê sản phẩm nhập ít nhất trong khoảng thời gian
        String startDate = ConsoleView.getInput("Nhập ngày bắt đầu (yyyy-MM-dd): ");
        String endDate = ConsoleView.getInput("Nhập ngày kết thúc (yyyy-MM-dd): ");
        try {
            Product product = reportDAO.getProductWithMinImportQuantity(startDate, endDate);
            ConsoleView.displayProduct(product);
        } catch (SQLException e) {
            ConsoleView.displayMessage("Lỗi khi thực hiện thống kê sản phẩm nhập ít nhất: " + e.getMessage());
        }
    }

    private void handleProductWithMaxExportQuantity() {
        // Xử lý thống kê sản phẩm xuất nhiều nhất trong khoảng thời gian
        String startDate = ConsoleView.getInput("Nhập ngày bắt đầu (yyyy-MM-dd): ");
        String endDate = ConsoleView.getInput("Nhập ngày kết thúc (yyyy-MM-dd): ");
        try {
            Product product = reportDAO.getProductWithMaxExportQuantity(startDate, endDate);
            ConsoleView.displayProduct(product);
        } catch (SQLException e) {
            ConsoleView.displayMessage("Lỗi khi thực hiện thống kê sản phẩm xuất nhiều nhất: " + e.getMessage());
        }
    }

    private void handleProductWithMinExportQuantity() {
        // Xử lý thống kê sản phẩm xuất ít nhất trong khoảng thời gian
        String startDate = ConsoleView.getInput("Nhập ngày bắt đầu (yyyy-MM-dd): ");
        String endDate = ConsoleView.getInput("Nhập ngày kết thúc (yyyy-MM-dd): ");
        try {
            Product product = reportDAO.getProductWithMinExportQuantity(startDate, endDate);
            ConsoleView.displayProduct(product);
        } catch (SQLException e) {
            ConsoleView.displayMessage("Lỗi khi thực hiện thống kê sản phẩm xuất ít nhất: " + e.getMessage());
        }
    }
}
