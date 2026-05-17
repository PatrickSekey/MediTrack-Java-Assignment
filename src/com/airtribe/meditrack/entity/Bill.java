package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.interfaces.Payable;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Bill implements Payable, Serializable {
    private static final long serialVersionUID = 1L;
    private static final double TAX_RATE = 0.10; // 10% tax

    private String billId;
    private String appointmentId;
    private double baseAmount;
    private double taxAmount;
    private double totalAmount;
    private double amountPaid;
    private LocalDateTime billDate;
    private boolean isPaid;

    public Bill(String appointmentId, double baseAmount) {
        this.billId = "BILL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.appointmentId = appointmentId;
        this.baseAmount = baseAmount;
        this.taxAmount = baseAmount * TAX_RATE;
        this.totalAmount = baseAmount + taxAmount;
        this.amountPaid = 0;
        this.billDate = LocalDateTime.now();
        this.isPaid = false;
    }

    public String getBillId() { return billId; }
    public String getAppointmentId() { return appointmentId; }
    public double getBaseAmount() { return baseAmount; }
    public double getTaxAmount() { return taxAmount; }
    public double getTotalAmount() { return totalAmount; }
    public double getAmountPaid() { return amountPaid; }
    public LocalDateTime getBillDate() { return billDate; }
    public boolean isPaid() { return isPaid; }
    public double getBalanceDue() { return totalAmount - amountPaid; }

    @Override
    public boolean processPayment(double amount) {
        if (amount >= getBalanceDue()) {
            this.amountPaid = totalAmount;
            this.isPaid = true;
            return true;
        } else {
            this.amountPaid += amount;
            return false;
        }
    }

    @Override
    public double getChange() {
        if (isPaid) {
            return amountPaid - totalAmount;
        }
        return 0;
    }

    @Override
    public String getPaymentStatus() {
        if (isPaid) return "PAID";
        if (amountPaid > 0) return "PARTIAL";
        return "UNPAID";
    }

    public BillSummary generateSummary() {
        return new BillSummary(billId, totalAmount, taxAmount);
    }

    @Override
    public String toString() {
        return String.format("Bill{id='%s', total=₹%.2f, paid=₹%.2f, status=%s}",
                billId, totalAmount, amountPaid, getPaymentStatus());
    }
}