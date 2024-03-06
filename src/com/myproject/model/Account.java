package com.myproject.model;

public class Account {
    private int accountId;
    private String username;
    private String password;
    private boolean isAdmin;
    private String empId;
    private boolean status;

    public Account(String username, String password, boolean isAdmin, String empId, boolean status) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
        this.empId = empId;
        this.status = status;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", isAdmin=" + isAdmin +
                ", empId='" + empId + '\'' +
                ", status=" + status +
                '}';
    }
}
