package com.airtribe.meditrack.interfaces;

public class InsuranceBillingStrategy implements BillingStrategy {
    @Override
    public double calculateAmount(double baseFee) {
        return baseFee * 0.80; // 20% insurance discount
    }
}
