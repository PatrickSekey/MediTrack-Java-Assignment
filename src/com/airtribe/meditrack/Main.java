package com.airtribe.meditrack;

import com.airtribe.meditrack.entity.*;
import com.airtribe.meditrack.enums.*;
import com.airtribe.meditrack.service.*;
import com.airtribe.meditrack.util.*;
import com.airtribe.meditrack.exception.*;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static PatientService patientService;
    private static DoctorService doctorService;
    private static AppointmentService appointmentService;
    private static Scanner scanner;
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public static void main(String[] args) {
        // Initialize services
        patientService = new PatientService();
        doctorService = new DoctorService();
        appointmentService = new AppointmentService();
        scanner = new Scanner(System.in);

        // Set up service dependencies
        appointmentService.setPatientService(patientService);
        appointmentService.setDoctorService(doctorService);

        // Print welcome message
        printWelcomeMessage();

        // Check if data files exist and load data
        boolean loadData = Arrays.asList(args).contains("--loadData");

        if (loadData) {
            loadDataFromFiles();
        } else {
            // Check if we have any data, if not, initialize sample data
            if (patientService.getAllPatients().isEmpty() && doctorService.getAllDoctors().isEmpty()) {
                initializeSampleData();
            }
        }

        // Main menu loop
        while (true) {
            displayMainMenu();
            int choice = getUserChoice();
            if (!handleMainMenuChoice(choice)) {
                break;
            }
        }

        scanner.close();
    }

    private static void printWelcomeMessage() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("   WELCOME TO MEDITRACK - CLINIC MANAGEMENT SYSTEM");
        System.out.println("=".repeat(60));
        System.out.println("Version 1.0.0 | Developed for AirTribe");
        System.out.println("=".repeat(60));
    }

    private static void displayMainMenu() {
        System.out.println("\nMAIN MENU");
        System.out.println("-".repeat(40));
        System.out.println("1. Patient Management");
        System.out.println("2. Doctor Management");
        System.out.println("3. Appointment Management");
        System.out.println("4. Billing Management");
        System.out.println("5. Analytics & Reports");
        System.out.println("6. Save Data to Files");
        System.out.println("7. Exit");
        System.out.println("-".repeat(40));
        System.out.print("Enter your choice (1-7): ");
    }

    private static int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static boolean handleMainMenuChoice(int choice) {
        switch (choice) {
            case 1:
                patientManagementMenu();
                break;
            case 2:
                doctorManagementMenu();
                break;
            case 3:
                appointmentManagementMenu();
                break;
            case 4:
                billingManagementMenu();
                break;
            case 5:
                analyticsMenu();
                break;
            case 6:
                saveDataToFiles();
                break;
            case 7:
                System.out.println("\nThank you for using MediTrack! Goodbye!");
                return false;
            default:
                System.out.println("Invalid choice! Please enter a number between 1 and 7.");
        }
        return true;
    }

    // ==================== PATIENT MANAGEMENT ====================

    private static void patientManagementMenu() {
        while (true) {
            System.out.println("\nPATIENT MANAGEMENT");
            System.out.println("-".repeat(40));
            System.out.println("1. Register New Patient");
            System.out.println("2. View All Patients");
            System.out.println("3. Search Patient");
            System.out.println("4. Update Patient");
            System.out.println("5. Delete Patient");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choice: ");

            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    registerPatient();
                    break;
                case 2:
                    viewAllPatients();
                    break;
                case 3:
                    searchPatientMenu();
                    break;
                case 4:
                    updatePatient();
                    break;
                case 5:
                    deletePatient();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void registerPatient() {
        System.out.println("\nREGISTER NEW PATIENT");
        System.out.println("-".repeat(40));

        try {
            System.out.print("Full Name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Age: ");
            int age = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Phone Number (10 digits): ");
            String phone = scanner.nextLine().trim();

            System.out.print("Email: ");
            String email = scanner.nextLine().trim();

            System.out.print("Address: ");
            String address = scanner.nextLine().trim();

            Patient patient = patientService.registerPatient(name, age, phone, email, address);
            System.out.println("\nPATIENT REGISTERED SUCCESSFULLY!");
            System.out.println("   Patient ID: " + patient.getPatientId());
            System.out.println("   Name: " + patient.getName());

        } catch (InvalidDataException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid age format!");
        }
    }

    private static void viewAllPatients() {
        List<Patient> patients = patientService.getAllPatients();
        if (patients.isEmpty()) {
            System.out.println("\nNo patients found in the system.");
            return;
        }

        System.out.println("\nALL PATIENTS");
        System.out.println("=".repeat(80));
        System.out.printf("%-12s %-20s %-5s %-12s %-20s%n", "ID", "Name", "Age", "Phone", "Email");
        System.out.println("-".repeat(80));
        for (Patient p : patients) {
            System.out.printf("%-12s %-20s %-5d %-12s %-20s%n",
                    p.getPatientId(),
                    truncate(p.getName(), 20),
                    p.getAge(),
                    p.getPhone(),
                    truncate(p.getEmail(), 20));
        }
        System.out.println("=".repeat(80));
        System.out.println("Total Patients: " + patients.size());
    }

    private static void searchPatientMenu() {
        System.out.println("\nSEARCH PATIENT");
        System.out.println("-".repeat(40));
        System.out.println("1. Search by ID");
        System.out.println("2. Search by Name");
        System.out.println("3. Search by Age");
        System.out.print("Choice: ");

        int choice = getUserChoice();

        switch (choice) {
            case 1:
                searchPatientById();
                break;
            case 2:
                searchPatientByName();
                break;
            case 3:
                searchPatientByAge();
                break;
            default:
                System.out.println("Invalid choice!");
        }
    }

    private static void searchPatientById() {
        System.out.print("\nEnter Patient ID: ");
        String id = scanner.nextLine().trim();

        Optional<Patient> patient = patientService.searchPatientById(id);
        if (patient.isPresent()) {
            displayPatientDetails(patient.get());
        } else {
            System.out.println("Patient not found with ID: " + id);
        }
    }

    private static void searchPatientByName() {
        System.out.print("\nEnter Name (or partial name): ");
        String name = scanner.nextLine().trim();

        List<Patient> patients = patientService.searchPatientByName(name);
        if (patients.isEmpty()) {
            System.out.println("No patients found with name containing: " + name);
        } else {
            System.out.println("\nSearch Results:");
            for (Patient p : patients) {
                System.out.printf("   %s - %s (Age: %d)%n", p.getPatientId(), p.getName(), p.getAge());
            }
        }
    }

    private static void searchPatientByAge() {
        System.out.print("\nEnter Age: ");
        int age = Integer.parseInt(scanner.nextLine().trim());

        List<Patient> patients = patientService.searchPatientByAge(age);
        if (patients.isEmpty()) {
            System.out.println("No patients found with age: " + age);
        } else {
            System.out.println("\nSearch Results:");
            for (Patient p : patients) {
                System.out.printf("   %s - %s (Phone: %s)%n", p.getPatientId(), p.getName(), p.getPhone());
            }
        }
    }

    private static void displayPatientDetails(Patient p) {
        System.out.println("\nPATIENT DETAILS");
        System.out.println("=".repeat(50));
        System.out.println("ID:          " + p.getPatientId());
        System.out.println("Name:        " + p.getName());
        System.out.println("Age:         " + p.getAge());
        System.out.println("Phone:       " + p.getPhone());
        System.out.println("Email:       " + p.getEmail());
        System.out.println("Address:     " + p.getAddress());
        System.out.println("Total Spent: ₹" + String.format("%.2f", p.getTotalSpent()));
        System.out.println("=".repeat(50));
    }

    private static void updatePatient() {
        System.out.print("\nEnter Patient ID to update: ");
        String id = scanner.nextLine().trim();

        Optional<Patient> patientOpt = patientService.searchPatientById(id);
        if (patientOpt.isEmpty()) {
            System.out.println("Patient not found with ID: " + id);
            return;
        }

        Patient patient = patientOpt.get();
        System.out.println("\nUPDATE PATIENT (Leave blank to keep current value)");
        System.out.println("-".repeat(40));

        System.out.println("Current Name: " + patient.getName());
        System.out.print("New Name: ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) patient.setName(name);

        System.out.println("Current Phone: " + patient.getPhone());
        System.out.print("New Phone: ");
        String phone = scanner.nextLine().trim();
        if (!phone.isEmpty()) patient.setPhone(phone);

        System.out.println("Current Email: " + patient.getEmail());
        System.out.print("New Email: ");
        String email = scanner.nextLine().trim();
        if (!email.isEmpty()) patient.setEmail(email);

        System.out.println("Current Address: " + patient.getAddress());
        System.out.print("New Address: ");
        String address = scanner.nextLine().trim();
        if (!address.isEmpty()) patient.setAddress(address);

        patientService.updatePatient(patient);
        System.out.println("\nPatient updated successfully!");
    }

    private static void deletePatient() {
        System.out.print("\nEnter Patient ID to delete: ");
        String id = scanner.nextLine().trim();

        Optional<Patient> patient = patientService.searchPatientById(id);
        if (patient.isEmpty()) {
            System.out.println("Patient not found with ID: " + id);
            return;
        }

        System.out.print("Are you sure you want to delete " + patient.get().getName() + "? (yes/no): ");
        String confirm = scanner.nextLine().trim();

        if (confirm.equalsIgnoreCase("yes")) {
            if (patientService.deletePatient(id)) {
                System.out.println("Patient deleted successfully!");
            } else {
                System.out.println("Failed to delete patient.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    // ==================== DOCTOR MANAGEMENT ====================

    private static void doctorManagementMenu() {
        while (true) {
            System.out.println("\nDOCTOR MANAGEMENT");
            System.out.println("-".repeat(40));
            System.out.println("1. Add New Doctor");
            System.out.println("2. View All Doctors");
            System.out.println("3. Search Doctor by Specialization");
            System.out.println("4. Update Doctor");
            System.out.println("5. Delete Doctor");
            System.out.println("6. Back to Main Menu");
            System.out.print("Choice: ");

            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    addDoctor();
                    break;
                case 2:
                    viewAllDoctors();
                    break;
                case 3:
                    searchDoctorsBySpecialization();
                    break;
                case 4:
                    updateDoctor();
                    break;
                case 5:
                    deleteDoctor();
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void addDoctor() {
        System.out.println("\nADD NEW DOCTOR");
        System.out.println("-".repeat(40));

        try {
            System.out.print("Full Name: ");
            String name = scanner.nextLine().trim();

            System.out.print("Age: ");
            int age = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Phone Number: ");
            String phone = scanner.nextLine().trim();

            System.out.print("Email: ");
            String email = scanner.nextLine().trim();

            System.out.println("\nSpecializations:");
            for (Specialization spec : Specialization.values()) {
                System.out.printf("   %d. %s%n", spec.ordinal() + 1, spec.getDisplayName());
            }
            System.out.print("Choose (1-" + Specialization.values().length + "): ");
            int specChoice = Integer.parseInt(scanner.nextLine().trim());
            Specialization specialization = Specialization.values()[specChoice - 1];

            System.out.print("Consultation Fee (₹): ");
            double fee = Double.parseDouble(scanner.nextLine().trim());

            Doctor doctor = doctorService.addDoctor(name, age, phone, email, specialization, fee);
            System.out.println("\nDOCTOR ADDED SUCCESSFULLY!");
            System.out.println("   Doctor ID: " + doctor.getDoctorId());
            System.out.println("   Name: " + doctor.getName());

        } catch (InvalidDataException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Error: Invalid number format!");
        }
    }

    private static void viewAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();
        if (doctors.isEmpty()) {
            System.out.println("\nNo doctors found in the system.");
            return;
        }

        System.out.println("\nALL DOCTORS");
        System.out.println("=".repeat(90));
        System.out.printf("%-12s %-20s %-18s %-10s %-10s%n", "ID", "Name", "Specialization", "Fee (₹)", "Rating");
        System.out.println("-".repeat(90));
        for (Doctor d : doctors) {
            System.out.printf("%-12s %-20s %-18s ₹%-9.2f %-10.1f%n",
                    d.getDoctorId(),
                    truncate(d.getName(), 20),
                    d.getSpecialization().getDisplayName(),
                    d.getConsultationFee(),
                    d.getRating());
        }
        System.out.println("=".repeat(90));
        System.out.println("Total Doctors: " + doctors.size());
    }

    private static void searchDoctorsBySpecialization() {
        System.out.println("\nSEARCH DOCTORS BY SPECIALIZATION");
        System.out.println("-".repeat(40));

        Specialization[] specializations = Specialization.values();
        for (int i = 0; i < specializations.length; i++) {
            System.out.printf("%d. %s%n", i + 1, specializations[i].getDisplayName());
        }
        System.out.print("Choose specialization (enter number 1-" + specializations.length + "): ");

        try {
            int choice = Integer.parseInt(scanner.nextLine().trim());
            if (choice < 1 || choice > specializations.length) {
                System.out.println("Invalid choice! Please enter a number between 1 and " + specializations.length);
                return;
            }

            Specialization spec = specializations[choice - 1];
            List<Doctor> doctors = doctorService.getDoctorsBySpecialization(spec);

            if (doctors.isEmpty()) {
                System.out.println("No doctors found with specialization: " + spec.getDisplayName());
            } else {
                System.out.println("\nDOCTORS - " + spec.getDisplayName());
                System.out.println("-".repeat(60));
                System.out.printf("%-12s %-25s %-10s %-10s%n", "ID", "Name", "Fee (₹)", "Rating");
                System.out.println("-".repeat(60));
                for (Doctor d : doctors) {
                    System.out.printf("%-12s %-25s ₹%-9.2f %-10.1f%n",
                            d.getDoctorId(),
                            d.getName(),
                            d.getConsultationFee(),
                            d.getRating());
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input! Please enter a number.");
        }
    }

    private static void updateDoctor() {
        System.out.print("\nEnter Doctor ID to update: ");
        String id = scanner.nextLine().trim();

        Optional<Doctor> doctorOpt = doctorService.getDoctorById(id);
        if (doctorOpt.isEmpty()) {
            System.out.println("Doctor not found with ID: " + id);
            return;
        }

        Doctor doctor = doctorOpt.get();
        System.out.println("\nUPDATE DOCTOR");
        System.out.println("-".repeat(40));

        System.out.println("Doctor: " + doctor.getName());
        System.out.println("Current Specialization: " + doctor.getSpecialization().getDisplayName());
        System.out.println("Current Consultation Fee: ₹" + doctor.getConsultationFee());
        System.out.print("New Consultation Fee (₹ - press Enter to keep current): ");
        String newFee = scanner.nextLine().trim();
        if (!newFee.isEmpty()) {
            try {
                double fee = Double.parseDouble(newFee);
                doctor.setConsultationFee(fee);
                System.out.println("Fee updated to ₹" + fee);
            } catch (NumberFormatException e) {
                System.out.println("Invalid fee amount. Keeping current fee.");
            }
        }

        System.out.println("Current Rating: " + doctor.getRating());
        System.out.print("New Rating (1-5 - press Enter to keep current): ");
        String newRating = scanner.nextLine().trim();
        if (!newRating.isEmpty()) {
            try {
                double rating = Double.parseDouble(newRating);
                if (rating >= 1 && rating <= 5) {
                    doctor.setRating(rating);
                    System.out.println("Rating updated to " + rating);
                } else {
                    System.out.println("Rating must be between 1 and 5. Keeping current rating.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid rating. Keeping current rating.");
            }
        }

        doctorService.updateDoctor(doctor);
        System.out.println("\nDoctor updated successfully!");
    }

    private static void deleteDoctor() {
        System.out.print("\nEnter Doctor ID to delete: ");
        String id = scanner.nextLine().trim();

        Optional<Doctor> doctor = doctorService.getDoctorById(id);
        if (doctor.isEmpty()) {
            System.out.println("Doctor not found with ID: " + id);
            return;
        }

        System.out.print("Are you sure you want to delete " + doctor.get().getName() + "? (yes/no): ");
        String confirm = scanner.nextLine().trim();

        if (confirm.equalsIgnoreCase("yes")) {
            if (doctorService.deleteDoctor(id)) {
                System.out.println("Doctor deleted successfully!");
            } else {
                System.out.println("Failed to delete doctor.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    // ==================== APPOINTMENT MANAGEMENT ====================

    private static void appointmentManagementMenu() {
        while (true) {
            System.out.println("\nAPPOINTMENT MANAGEMENT");
            System.out.println("-".repeat(40));
            System.out.println("1. Create Appointment");
            System.out.println("2. View Appointments");
            System.out.println("3. Cancel Appointment");
            System.out.println("4. Back");
            System.out.print("Choice: ");

            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    createAppointment();
                    break;
                case 2:
                    viewAllAppointments();
                    break;
                case 3:
                    cancelAppointment();
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void createAppointment() {
        System.out.println("\nCREATE APPOINTMENT");
        System.out.println("-".repeat(40));

        try {
            // Step 1: Get Patient ID
            System.out.print("Enter Patient ID: ");
            String patientId = scanner.nextLine().trim();

            // Verify patient exists
            Optional<Patient> patient = patientService.searchPatientById(patientId);
            if (patient.isEmpty()) {
                System.out.println("Error: Patient not found with ID: " + patientId);
                return;
            }

            // Step 2: Get Symptoms
            System.out.print("Enter Symptoms: ");
            String symptoms = scanner.nextLine().trim();

            // Step 3: AI Recommendation based on symptoms
            Specialization recommendedSpecialization = AIHelper.recommendDoctor(symptoms);
            System.out.println("Recommended Specialist: " + recommendedSpecialization.getDisplayName());

            // Step 4: Get doctors with that specialization
            List<Doctor> availableDoctors = doctorService.getDoctorsBySpecialization(recommendedSpecialization);

            if (availableDoctors.isEmpty()) {
                System.out.println("Error: No doctors available for " + recommendedSpecialization.getDisplayName());
                return;
            }

            // Display available doctors
            System.out.println("\nAvailable Doctors:");
            for (int i = 0; i < availableDoctors.size(); i++) {
                Doctor d = availableDoctors.get(i);
                System.out.printf("%d. %s (ID: %s)%n", i + 1, d.getName(), d.getDoctorId());
            }

            System.out.print("Select Doctor (number): ");
            int docChoice = Integer.parseInt(scanner.nextLine().trim());

            if (docChoice < 1 || docChoice > availableDoctors.size()) {
                System.out.println("Error: Invalid doctor selection");
                return;
            }

            Doctor selectedDoctor = availableDoctors.get(docChoice - 1);

            // Step 5: AI suggests available slots
            List<Appointment> existingAppointments = appointmentService.getAppointmentsByDoctor(selectedDoctor.getDoctorId());
            List<LocalDateTime> availableSlots = AIHelper.suggestSlots(selectedDoctor, existingAppointments);

            if (availableSlots.isEmpty()) {
                System.out.println("Error: No available slots for this doctor");
                return;
            }

            // Display available slots
            System.out.println("\nAvailable Slots:");
            for (int i = 0; i < availableSlots.size(); i++) {
                LocalDateTime slot = availableSlots.get(i);
                System.out.printf("%d. %s%n", i + 1,
                        slot.format(DateTimeFormatter.ofPattern("dd-MM-yyyy hh:mm a")));
            }

            System.out.print("Select Slot (number): ");
            int slotChoice = Integer.parseInt(scanner.nextLine().trim());

            if (slotChoice < 1 || slotChoice > availableSlots.size()) {
                System.out.println("Error: Invalid slot selection");
                return;
            }

            LocalDateTime selectedSlot = availableSlots.get(slotChoice - 1);

            // Step 6: Book the appointment
            Appointment appointment = appointmentService.bookAppointment(
                    patientId,
                    selectedDoctor.getDoctorId(),
                    selectedSlot,
                    symptoms
            );

            System.out.println("\nAppointment booked successfully!");
            System.out.println("Appointment ID: " + appointment.getAppointmentId());
            System.out.println("Doctor: " + selectedDoctor.getName());
            System.out.println("Date & Time: " + appointment.getFormattedDateTime());
            System.out.println("Status: " + appointment.getStatus());

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewAllAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();
        if (appointments.isEmpty()) {
            System.out.println("\nNo appointments found.");
            return;
        }

        System.out.println("\nALL APPOINTMENTS");
        System.out.println("=".repeat(90));
        System.out.printf("%-12s %-12s %-12s %-20s %-12s%n", "Appt ID", "Patient ID", "Doctor ID", "Date & Time", "Status");
        System.out.println("-".repeat(90));
        for (Appointment apt : appointments) {
            System.out.printf("%-12s %-12s %-12s %-20s %-12s%n",
                    apt.getAppointmentId(),
                    apt.getPatientId(),
                    apt.getDoctorId(),
                    apt.getFormattedDateTime(),
                    apt.getStatus());
        }
        System.out.println("=".repeat(90));
        System.out.println("Total Appointments: " + appointments.size());
    }

    private static void cancelAppointment() {
        System.out.print("\nEnter Appointment ID to cancel: ");
        String aptId = scanner.nextLine().trim();

        try {
            appointmentService.cancelAppointment(aptId);
            System.out.println("Appointment cancelled successfully!");
        } catch (AppointmentNotFoundException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ==================== BILLING MANAGEMENT ====================

    private static void billingManagementMenu() {
        System.out.println("\nBILLING MANAGEMENT");
        System.out.println("-".repeat(40));
        System.out.print("Enter Appointment ID to generate bill: ");
        String aptId = scanner.nextLine().trim();

        try {
            Appointment appointment = appointmentService.getAppointmentById(aptId)
                    .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found"));

            Optional<Doctor> doctor = doctorService.getDoctorById(appointment.getDoctorId());
            if (doctor.isPresent()) {
                Bill bill = new Bill(aptId, doctor.get().getConsultationFee());
                BillSummary summary = bill.generateSummary();

                System.out.println("\n" + "=".repeat(50));
                System.out.println("         INVOICE");
                System.out.println("=".repeat(50));
                System.out.println("Bill ID:       " + summary.getBillId());
                System.out.println("Appointment:   " + aptId);
                System.out.println("Doctor:        " + doctor.get().getName());
                System.out.println("Base Amount:   ₹" + String.format("%.2f", bill.getBaseAmount()));
                System.out.println("Tax (10%):     ₹" + String.format("%.2f", bill.getTaxAmount()));
                System.out.println("Total Amount:  ₹" + String.format("%.2f", bill.getTotalAmount()));
                System.out.println("Date:          " + summary.getGeneratedDate());
                System.out.println("=".repeat(50));

                System.out.print("\nEnter payment amount: ₹");
                double amount = Double.parseDouble(scanner.nextLine().trim());

                if (bill.processPayment(amount)) {
                    System.out.println("\nPAYMENT SUCCESSFUL!");
                    if (bill.getChange() > 0) {
                        System.out.println("   Change: ₹" + String.format("%.2f", bill.getChange()));
                    }
                    System.out.println("   Status: " + bill.getPaymentStatus());
                } else {
                    System.out.println("\nPARTIAL PAYMENT RECEIVED");
                    System.out.println("   Amount Due: ₹" + String.format("%.2f", bill.getBalanceDue()));
                    System.out.println("   Status: " + bill.getPaymentStatus());
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ==================== ANALYTICS ====================

    private static void analyticsMenu() {
        System.out.println("\nANALYTICS & REPORTS");
        System.out.println("=".repeat(50));

        List<Patient> patients = patientService.getAllPatients();
        List<Doctor> doctors = doctorService.getAllDoctors();
        List<Appointment> appointments = appointmentService.getAllAppointments();

        System.out.println("\nSYSTEM STATISTICS");
        System.out.println("-".repeat(40));
        System.out.printf("   Total Patients:      %d%n", patients.size());
        System.out.printf("   Total Doctors:       %d%n", doctors.size());
        System.out.printf("   Total Appointments:  %d%n", appointments.size());

        if (!doctors.isEmpty()) {
            double avgFee = doctors.stream()
                    .mapToDouble(Doctor::getConsultationFee)
                    .average()
                    .orElse(0.0);
            System.out.printf("   Average Consultation Fee: ₹%.2f%n", avgFee);
        }

        System.out.println("\nAPPOINTMENT STATUS BREAKDOWN");
        System.out.println("-".repeat(40));
        if (!appointments.isEmpty()) {
            for (AppointmentStatus status : AppointmentStatus.values()) {
                long count = appointments.stream()
                        .filter(a -> a.getStatus() == status)
                        .count();
                System.out.printf("   %-10s: %d%n", status, count);
            }
        } else {
            System.out.println("   No appointments found.");
        }

        if (!appointments.isEmpty()) {
            Map<String, Long> doctorAppointments = appointments.stream()
                    .collect(Collectors.groupingBy(Appointment::getDoctorId, Collectors.counting()));

            if (!doctorAppointments.isEmpty()) {
                String topDoctorId = doctorAppointments.entrySet().stream()
                        .max(Map.Entry.comparingByValue())
                        .map(Map.Entry::getKey)
                        .orElse(null);

                if (topDoctorId != null) {
                    doctorService.getDoctorById(topDoctorId).ifPresent(d ->
                            System.out.printf("\n   Most Popular Doctor: %s (%d appointments)%n",
                                    d.getName(), doctorAppointments.get(topDoctorId)));
                }
            }
        }

        double totalRevenue = patients.stream()
                .mapToDouble(Patient::getTotalSpent)
                .sum();
        System.out.printf("   Total Revenue: ₹%.2f%n", totalRevenue);

        if (!appointments.isEmpty()) {
            long completedCount = appointments.stream()
                    .filter(a -> a.getStatus() == AppointmentStatus.COMPLETED)
                    .count();
            double completionRate = (double) completedCount / appointments.size() * 100;
            System.out.printf("   Appointment Completion Rate: %.1f%%%n", completionRate);
        }

        System.out.println("\n" + "=".repeat(50));
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }

    // ==================== DATA PERSISTENCE ====================

    private static void loadDataFromFiles() {
        System.out.println("\nLoading data from files...");
        try {
            File patientFile = new File("data/patients.csv");
            File doctorFile = new File("data/doctors.csv");

            if (patientFile.exists() && doctorFile.exists()) {
                CSVUtil.loadData(patientService, doctorService, appointmentService);
                System.out.println("Data loaded successfully!");
            } else {
                System.out.println("No existing data files found. Starting fresh.");
                initializeSampleData();
            }
        } catch (Exception e) {
            System.out.println("Error loading data: " + e.getMessage());
            System.out.println("Starting with sample data...");
            initializeSampleData();
        }
    }

    private static void saveDataToFiles() {
        System.out.println("\nSaving data to files...");
        try {
            CSVUtil.saveData(
                    patientService.getAllPatients(),
                    doctorService.getAllDoctors(),
                    appointmentService.getAllAppointments()
            );
            System.out.println("Data saved successfully to data/ directory!");
        } catch (Exception e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    private static void initializeSampleData() {
        System.out.println("\nInitializing sample data...");

        try {
            Doctor d1 = doctorService.addDoctor("Rajesh Kumar", 48, "9988776655", "rajesh.kumar@meditrack.com",
                    Specialization.CARDIOLOGY, 1800.0);
            d1.setRating(4.9);

            Doctor d2 = doctorService.addDoctor("Priya Sharma", 42, "9988776656", "priya.sharma@meditrack.com",
                    Specialization.NEUROLOGY, 2200.0);
            d2.setRating(4.8);

            Doctor d3 = doctorService.addDoctor("Amit Patel", 39, "9988776657", "amit.patel@meditrack.com",
                    Specialization.ORTHOPEDICS, 1600.0);
            d3.setRating(4.7);

            Doctor d4 = doctorService.addDoctor("Sneha Reddy", 45, "9988776658", "sneha.reddy@meditrack.com",
                    Specialization.PEDIATRICS, 1400.0);
            d4.setRating(4.9);

            Doctor d5 = doctorService.addDoctor("Vikram Singh", 51, "9988776659", "vikram.singh@meditrack.com",
                    Specialization.DERMATOLOGY, 1700.0);
            d5.setRating(4.8);

            System.out.println("   Added " + doctorService.getAllDoctors().size() + " doctors");

            Patient p1 = patientService.registerPatient("John Doe", 32, "9876543210", "john.doe@email.com", "123 Main Street, Mumbai");
            Patient p2 = patientService.registerPatient("Jane Smith", 28, "9876543211", "jane.smith@email.com", "456 Park Avenue, Delhi");
            Patient p3 = patientService.registerPatient("Robert Johnson", 45, "9876543212", "robert.j@email.com", "789 Lake Road, Bangalore");
            Patient p4 = patientService.registerPatient("Emily Davis", 35, "9876543213", "emily.davis@email.com", "321 Hill Street, Chennai");
            Patient p5 = patientService.registerPatient("Michael Brown", 52, "9876543214", "michael.brown@email.com", "654 Forest Ave, Kolkata");

            System.out.println("   Added " + patientService.getAllPatients().size() + " patients");

            List<Patient> patients = patientService.getAllPatients();
            List<Doctor> doctors = doctorService.getAllDoctors();

            if (!patients.isEmpty() && !doctors.isEmpty()) {
                LocalDateTime tomorrow = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0);
                LocalDateTime dayAfter = LocalDateTime.now().plusDays(2).withHour(11).withMinute(30);
                LocalDateTime nextWeek = LocalDateTime.now().plusDays(7).withHour(14).withMinute(0);

                appointmentService.bookAppointment(patients.get(0).getPatientId(), doctors.get(0).getDoctorId(),
                        tomorrow, "Chest pain and shortness of breath");
                appointmentService.bookAppointment(patients.get(1).getPatientId(), doctors.get(1).getDoctorId(),
                        dayAfter, "Severe headaches and dizziness");
                appointmentService.bookAppointment(patients.get(2).getPatientId(), doctors.get(2).getDoctorId(),
                        nextWeek, "Knee pain and swelling");

                System.out.println("   Created " + appointmentService.getAllAppointments().size() + " appointments");
            }

            System.out.println("Sample data initialized successfully!");

        } catch (Exception e) {
            System.out.println("Error initializing sample data: " + e.getMessage());
        }
    }

    // ==================== HELPER METHODS ====================

    private static String truncate(String str, int length) {
        if (str == null) return "";
        if (str.length() <= length) return str;
        return str.substring(0, length - 3) + "...";
    }
}