package com.airtribe.meditrack.util;

import com.airtribe.meditrack.entity.*;
import com.airtribe.meditrack.enums.*;
import com.airtribe.meditrack.service.*;
import java.time.LocalDateTime;

public class DataInitializer {

    public static void initializeAllData(PatientService patientService,
                                         DoctorService doctorService,
                                         AppointmentService appointmentService) {

        System.out.println("🔧 Initializing sample data...");

        // Initialize Doctors
        initializeDoctors(doctorService);

        // Initialize Patients
        initializePatients(patientService);

        // Initialize Appointments
        initializeAppointments(appointmentService);

        System.out.println("✅ Sample data initialization complete!");
    }

    private static void initializeDoctors(DoctorService doctorService) {
        try {
            Doctor d1 = doctorService.addDoctor("Dr. Rajesh Kumar", 48, "9988776655",
                    "rajesh.kumar@meditrack.com", Specialization.CARDIOLOGY, 1800.0);
            d1.setRating(4.9);

            Doctor d2 = doctorService.addDoctor("Dr. Priya Sharma", 42, "9988776656",
                    "priya.sharma@meditrack.com", Specialization.NEUROLOGY, 2200.0);
            d2.setRating(4.8);

            Doctor d3 = doctorService.addDoctor("Dr. Amit Patel", 39, "9988776657",
                    "amit.patel@meditrack.com", Specialization.ORTHOPEDICS, 1600.0);
            d3.setRating(4.7);

            Doctor d4 = doctorService.addDoctor("Dr. Sneha Reddy", 45, "9988776658",
                    "sneha.reddy@meditrack.com", Specialization.PEDIATRICS, 1400.0);
            d4.setRating(4.9);

            System.out.println("✓ Added " + doctorService.getAllDoctors().size() + " doctors");
        } catch (Exception e) {
            System.err.println("Error adding doctors: " + e.getMessage());
        }
    }

    private static void initializePatients(PatientService patientService) {
        try {
            patientService.registerPatient("John Doe", 32, "9876543210",
                    "john.doe@email.com", "123 Main Street, Mumbai");

            patientService.registerPatient("Jane Smith", 28, "9876543211",
                    "jane.smith@email.com", "456 Park Avenue, Delhi");

            patientService.registerPatient("Robert Johnson", 45, "9876543212",
                    "robert.j@email.com", "789 Lake Road, Bangalore");

            patientService.registerPatient("Emily Davis", 35, "9876543213",
                    "emily.davis@email.com", "321 Hill Street, Chennai");

            System.out.println("✓ Added " + patientService.getAllPatients().size() + " patients");
        } catch (Exception e) {
            System.err.println("Error adding patients: " + e.getMessage());
        }
    }

    private static void initializeAppointments(AppointmentService appointmentService) {
        try {
            // Get the first patient and doctor
            Patient patient = appointmentService.getAllAppointments().isEmpty() ? null : null;
            // For demo, create sample appointments
            LocalDateTime apt1 = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
            LocalDateTime apt2 = LocalDateTime.now().plusDays(2).withHour(11).withMinute(30);

            // Note: You need valid IDs here
            System.out.println("✓ Sample appointments ready to be booked");
        } catch (Exception e) {
            System.err.println("Error creating appointments: " + e.getMessage());
        }
    }
}