package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.enums.Specialization;
import com.airtribe.meditrack.util.IdGenerator;
import java.io.Serializable;
import java.util.Objects;

public class Doctor extends Person implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    private String doctorId;
    private Specialization specialization;
    private double consultationFee;
    private double rating;
    private boolean isAvailable;

    // Constructor 1: For new doctor (called from addDoctor)
    public Doctor(String name, int age, String phone, String email,
                  Specialization specialization, double consultationFee) {
        super(name, age, phone, email);
        this.doctorId = IdGenerator.getInstance().generateDoctorId();
        this.specialization = specialization;
        this.consultationFee = consultationFee;
        this.rating = 5.0;
        this.isAvailable = true;
    }

    // Constructor 2: For loading from file with all fields
    public Doctor(String doctorId, String name, int age, String phone, String email,
                  Specialization specialization, double consultationFee, double rating) {
        super(name, age, phone, email);
        this.doctorId = doctorId;
        this.specialization = specialization;
        this.consultationFee = consultationFee;
        this.rating = rating;
        this.isAvailable = true;
    }

    // Getters and Setters
    public String getDoctorId() { return doctorId; }

    public Specialization getSpecialization() { return specialization; }
    public void setSpecialization(Specialization specialization) { this.specialization = specialization; }

    public double getConsultationFee() { return consultationFee; }
    public void setConsultationFee(double consultationFee) { this.consultationFee = consultationFee; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    @Override
    public Doctor clone() {
        try {
            return (Doctor) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Clone not supported", e);
        }
    }

    @Override
    public String toString() {
        return String.format("Doctor{id='%s', name='%s', specialization=%s, fee=₹%.2f, rating=%.1f}",
                doctorId, getName(), specialization.getDisplayName(), consultationFee, rating);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(doctorId, doctor.doctorId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), doctorId);
    }
}