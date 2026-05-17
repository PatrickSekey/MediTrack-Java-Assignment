package com.airtribe.meditrack.interfaces;

public interface Payable {
    boolean processPayment(double amount);
    double getChange();
    String getPaymentStatus();
}