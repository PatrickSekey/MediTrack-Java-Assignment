package com.airtribe.meditrack.interfaces;

public class StandardBillingStrategy implements BillingStrategy {
    @Override
    public double calculateAmount(double baseFee) {
        return baseFee * 1.10; // 10% tax
    }
}
