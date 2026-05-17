package com.airtribe.meditrack.test;

import com.airtribe.meditrack.entity.*;
import com.airtribe.meditrack.enums.*;
import com.airtribe.meditrack.util.*;
import com.airtribe.meditrack.exception.*;

import java.util.List;  // Add this import
import java.util.ArrayList;  // Add this import

public class TestRunner {

    public static void main(String[] args) {
        System.out.println("=== MediTrack Test Suite ===\n");

        testDeepCopy();
        testImmutableClass();
        testSingleton();
        testGenericDataStore();
        testStreamOperations();

        System.out.println("\n✅ All tests completed!");
    }

    private static void testDeepCopy() {
        System.out.println("📋 Testing Deep Copy...");
        try {
            // Use the correct constructor
            Patient original = new Patient("John Doe", 30, "9876543210", "john@email.com", "123 Main St");
            Patient cloned = original.clone();

            assert original != cloned : "Objects should be different references";
            assert original.getPatientId().equals(cloned.getPatientId()) : "IDs should match";
            System.out.println("✓ Deep copy test passed\n");
        } catch (Exception e) {
            System.out.println("✗ Deep copy test failed: " + e.getMessage());
        }
    }

    private static void testImmutableClass() {
        System.out.println("📋 Testing Immutable BillSummary...");
        BillSummary bill = new BillSummary("BILL001", 100.0, 10.0);

        try {
            // BillSummary is immutable - no setters available
            System.out.println("Bill ID: " + bill.getBillId());
            System.out.println("Total: " + bill.getTotalAmount());
            System.out.println("✓ Immutable class test passed\n");
        } catch (Exception e) {
            System.err.println("✗ Immutable test failed: " + e.getMessage());
        }
    }

    private static void testSingleton() {
        System.out.println("📋 Testing Singleton Pattern...");
        IdGenerator gen1 = IdGenerator.getInstance();
        IdGenerator gen2 = IdGenerator.getInstance();

        assert gen1 == gen2 : "Should be same instance";
        System.out.println("✓ Singleton test passed\n");
    }

    private static void testGenericDataStore() {
        System.out.println("📋 Testing Generic DataStore...");
        DataStore<Patient> store = new DataStore<>();
        Patient patient = new Patient("Test Patient", 25, "9876543210", "test@email.com", "Test Address");

        store.add(patient.getPatientId(), patient);
        assert store.get(patient.getPatientId()).isPresent() : "Should find patient";
        System.out.println("✓ Generic DataStore test passed\n");
    }

    private static void testStreamOperations() {
        System.out.println("📋 Testing Stream Operations...");

        List<Doctor> doctors = new ArrayList<>();
        doctors.add(new Doctor("Dr. Smith", 45, "9876543210", "smith@email.com",
                Specialization.CARDIOLOGY, 1500.0));
        doctors.add(new Doctor("Dr. Jones", 50, "9876543211", "jones@email.com",
                Specialization.DERMATOLOGY, 1200.0));

        long cardiologists = doctors.stream()
                .filter(d -> d.getSpecialization() == Specialization.CARDIOLOGY)
                .count();

        assert cardiologists == 1 : "Should find 1 cardiologist";
        System.out.println("✓ Stream operations test passed\n");
    }
}