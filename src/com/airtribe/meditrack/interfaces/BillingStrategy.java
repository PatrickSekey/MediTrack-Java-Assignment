// interface/BillingStrategy.java
package com.airtribe.meditrack.interfaces;

@FunctionalInterface
public interface BillingStrategy {
    double calculateAmount(double baseFee);
}



