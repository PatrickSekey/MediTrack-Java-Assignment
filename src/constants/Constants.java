package com.airtribe.meditrack.constants;

public class Constants {
    // File paths
    public static final String PATIENT_DATA_FILE = "data/patients.ser";
    public static final String DOCTOR_DATA_FILE = "data/doctors.ser";
    public static final String APPOINTMENT_DATA_FILE = "data/appointments.ser";
    public static final String PATIENT_CSV_FILE = "data/patients.csv";
    public static final String DOCTOR_CSV_FILE = "data/doctors.csv";

    // Tax rates
    public static final double GST_RATE = 0.05;
    public static final double SERVICE_TAX_RATE = 0.05;
    public static final double TOTAL_TAX_RATE = GST_RATE + SERVICE_TAX_RATE;

    // Appointment defaults
    public static final int DEFAULT_APPOINTMENT_DURATION_MINUTES = 30;
    public static final int MAX_APPOINTMENTS_PER_DAY = 20;

    // Validation
    public static final int MIN_AGE = 0;
    public static final int MAX_AGE = 150;
    public static final int MIN_NAME_LENGTH = 2;
    public static final int MAX_NAME_LENGTH = 50;

    // Menu options
    public static final String MENU_SEPARATOR = "=".repeat(50);
    public static final String APP_NAME = "MediTrack";
    public static final String APP_VERSION = "1.0.0";
}