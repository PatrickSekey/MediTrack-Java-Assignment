package com.airtribe.meditrack.util;

import com.airtribe.meditrack.entity.*;
import com.airtribe.meditrack.service.*;
import java.io.*;
import java.util.*;

public class CSVUtil {

    private static final String PATIENT_FILE = "data/patients.csv";
    private static final String DOCTOR_FILE = "data/doctors.csv";
    private static final String APPOINTMENT_FILE = "data/appointments.csv";

    public static void loadData(PatientService patientService, DoctorService doctorService,
                                AppointmentService appointmentService) throws IOException {
        // Load patients
        File patientFile = new File(PATIENT_FILE);
        if (patientFile.exists()) {
            List<String[]> patients = readCSV(PATIENT_FILE);
            for (String[] data : patients) {
                // Parse and add patient (implementation depends on your Patient class)
            }
        }
    }

    public static void saveData(List<Patient> patients, List<Doctor> doctors,
                                List<Appointment> appointments) throws IOException {
        // Create data directory if not exists
        new File("data").mkdirs();

        // Save patients
        List<String[]> patientData = new ArrayList<>();
        patientData.add(new String[]{"ID", "Name", "Age", "Phone", "Email", "Address"});
        for (Patient p : patients) {
            patientData.add(new String[]{
                    p.getPatientId(), p.getName(), String.valueOf(p.getAge()),
                    p.getPhone(), p.getEmail(), p.getAddress()
            });
        }
        writeCSV(PATIENT_FILE, patientData);

        // Save doctors
        List<String[]> doctorData = new ArrayList<>();
        doctorData.add(new String[]{"ID", "Name", "Age", "Phone", "Email", "Specialization", "Fee"});
        for (Doctor d : doctors) {
            doctorData.add(new String[]{
                    d.getDoctorId(), d.getName(), String.valueOf(d.getAge()),
                    d.getPhone(), d.getEmail(), d.getSpecialization().name(),
                    String.valueOf(d.getConsultationFee())
            });
        }
        writeCSV(DOCTOR_FILE, doctorData);
    }

    private static List<String[]> readCSV(String filePath) throws IOException {
        List<String[]> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                records.add(line.split(","));
            }
        }
        return records;
    }

    private static void writeCSV(String filePath, List<String[]> data) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String[] row : data) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
        }
    }
}