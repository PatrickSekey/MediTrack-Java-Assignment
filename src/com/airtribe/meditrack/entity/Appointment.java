package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.enums.AppointmentStatus;
import com.airtribe.meditrack.util.IdGenerator;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Appointment implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    private String appointmentId;
    private String patientId;
    private String doctorId;
    private LocalDateTime dateTime;
    private String symptoms;
    private AppointmentStatus status;
    private String notes;

    public Appointment(String patientId, String doctorId, LocalDateTime dateTime, String symptoms) {
        this.appointmentId = IdGenerator.getInstance().generateAppointmentId();
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.dateTime = dateTime;
        this.symptoms = symptoms;
        this.status = AppointmentStatus.PENDING;
        this.notes = "";
    }

    // Getters and Setters
    public String getAppointmentId() { return appointmentId; }
    public String getPatientId() { return patientId; }
    public String getDoctorId() { return doctorId; }
    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }
    public String getSymptoms() { return symptoms; }
    public void setSymptoms(String symptoms) { this.symptoms = symptoms; }
    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    @Override
    public Appointment clone() {
        try {
            Appointment cloned = (Appointment) super.clone();
            cloned.dateTime = this.dateTime; // LocalDateTime is immutable
            return cloned;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Clone not supported", e);
        }
    }

    public String getFormattedDateTime() {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    @Override
    public String toString() {
        return String.format("Appointment{id='%s', patient='%s', doctor='%s', date=%s, status=%s}",
                appointmentId, patientId, doctorId, getFormattedDateTime(), status);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return Objects.equals(appointmentId, that.appointmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appointmentId);
    }
}