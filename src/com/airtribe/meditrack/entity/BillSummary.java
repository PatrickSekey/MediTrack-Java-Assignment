package com.airtribe.meditrack.entity;

import java.time.LocalDateTime;

public final class BillSummary {
    private final String billId;
    private final double totalAmount;
    private final double taxAmount;
    private final LocalDateTime generatedDate;

    public BillSummary(String billId, double totalAmount, double taxAmount) {
        this.billId = billId;
        this.totalAmount = totalAmount;
        this.taxAmount = taxAmount;
        this.generatedDate = LocalDateTime.now();
    }

    // Only getters - no setters
    public String getBillId() { return billId; }
    public double getTotalAmount() { return totalAmount; }
    public double getTaxAmount() { return taxAmount; }
    public LocalDateTime getGeneratedDate() { return generatedDate; }

    @Override
    public String toString() {
        return String.format("Bill %s: $%.2f (Tax: $%.2f) on %s",
                billId, totalAmount, taxAmount, generatedDate);
    }
}