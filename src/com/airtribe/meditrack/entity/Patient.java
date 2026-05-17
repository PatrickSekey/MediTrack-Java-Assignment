package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.util.IdGenerator;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Patient extends Person implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    private String patientId;
    private String address;
    private String medicalHistory;
    private List<String> appointmentIds;
    private double totalSpent;

    // Constructor 1: For new patient (called from registerPatient)
    public Patient(String name, int age, String phone, String email, String address) {
        super(name, age, phone, email);
        this.patientId = IdGenerator.getInstance().generatePatientId();
        this.address = address;
        this.medicalHistory = "";
        this.appointmentIds = new ArrayList<>();
        this.totalSpent = 0.0;
    }

    // Constructor 2: For loading from file with all fields
    public Patient(String patientId, String name, int age, String phone, String email,
                   String address, String medicalHistory, double totalSpent) {
        super(name, age, phone, email);
        this.patientId = patientId;
        this.address = address;
        this.medicalHistory = medicalHistory;
        this.appointmentIds = new ArrayList<>();
        this.totalSpent = totalSpent;
    }

    // Getters and Setters
    public String getPatientId() { return patientId; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getMedicalHistory() { return medicalHistory; }
    public void setMedicalHistory(String medicalHistory) { this.medicalHistory = medicalHistory; }

    public List<String> getAppointmentIds() { return new ArrayList<>(appointmentIds); }
    public void addAppointmentId(String appointmentId) { this.appointmentIds.add(appointmentId); }

    public double getTotalSpent() { return totalSpent; }
    public void addToTotalSpent(double amount) { this.totalSpent += amount; }

    @Override
    public Patient clone() {
        try {
            Patient cloned = (Patient) super.clone();
            cloned.appointmentIds = new ArrayList<>(this.appointmentIds);
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Clone not supported", e);
        }
    }

    @Override
    public String toString() {
        return String.format("Patient{id='%s', name='%s', age=%d, phone='%s', spent=₹%.2f}",
                patientId, getName(), getAge(), getPhone(), totalSpent);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Patient patient = (Patient) o;
        return Objects.equals(patientId, patient.patientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), patientId);
    }
}