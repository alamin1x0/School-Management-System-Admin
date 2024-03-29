package com.example.admindashboard.activity.pdf;

public class PdfModel {

    long invoiceNo;
    String customerName;
    long date;
    String fuelType;
    double fuelQty;
    double amount;

    public PdfModel() {
    }

    public long getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(long invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public double getFuelQty() {
        return fuelQty;
    }

    public void setFuelQty(double fuelQty) {
        this.fuelQty = fuelQty;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
