package com.myproject.model;

import java.util.Date;

public class Employee {
    private String empId;
    private String empName;
    private Date birthDate;
    private String email;
    private String phone;
    private String address;
    private byte empStatus;

    public Employee(String empId, String empName, Date birthDate, String email, String phone, String address, byte empStatus) {
        this.empId = empId;
        this.empName = empName;
        this.birthDate = birthDate;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.empStatus = empStatus;
    }

    // Getters and setters
    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public byte getEmpStatus() {
        return empStatus;
    }

    public void setEmpStatus(byte empStatus) {
        this.empStatus = empStatus;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "empId='" + empId + '\'' +
                ", empName='" + empName + '\'' +
                ", birthDate=" + birthDate +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", empStatus=" + empStatus +
                '}';
    }
}
