package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.enums.AppointmentStatus;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.util.DataStore;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AppointmentService {
    private DataStore<Appointment> appointmentStore;
    private PatientService patientService;
    private DoctorService doctorService;

    public AppointmentService() {
        this.appointmentStore = new DataStore<>();
        this.patientService = new PatientService();
        this.doctorService = new DoctorService();
    }

    // Setter for dependency injection (to avoid circular dependency)
    public void setPatientService(PatientService patientService) {
        this.patientService = patientService;
    }

    public void setDoctorService(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    public Appointment bookAppointment(String patientId, String doctorId,
                                       LocalDateTime dateTime, String symptoms)
            throws Exception {
        Optional<Patient> patient = patientService.searchPatientById(patientId);
        Optional<Doctor> doctor = doctorService.getDoctorById(doctorId);

        if (patient.isEmpty()) {
            throw new Exception("Patient not found with ID: " + patientId);
        }
        if (doctor.isEmpty()) {
            throw new Exception("Doctor not found with ID: " + doctorId);
        }

        Appointment appointment = new Appointment(patientId, doctorId, dateTime, symptoms);
        appointmentStore.add(appointment.getAppointmentId(), appointment);
        patient.get().addAppointmentId(appointment.getAppointmentId());

        return appointment;
    }

    public Optional<Appointment> getAppointmentById(String id) {
        return appointmentStore.get(id);
    }

    public List<Appointment> getAllAppointments() {
        return appointmentStore.getAll();
    }

    public List<Appointment> getAppointmentsByPatient(String patientId) {
        return appointmentStore.getAll().stream()
                .filter(a -> a.getPatientId().equals(patientId))
                .collect(Collectors.toList());
    }

    public List<Appointment> getAppointmentsByDoctor(String doctorId) {
        return appointmentStore.getAll().stream()
                .filter(a -> a.getDoctorId().equals(doctorId))
                .collect(Collectors.toList());
    }

    public List<Appointment> getAppointmentsByStatus(AppointmentStatus status) {
        return appointmentStore.getAll().stream()
                .filter(a -> a.getStatus() == status)
                .collect(Collectors.toList());
    }

    public void cancelAppointment(String appointmentId) throws AppointmentNotFoundException {
        if (!appointmentStore.remove(appointmentId)) {
            throw new AppointmentNotFoundException("Appointment not found with ID: " + appointmentId);
        }
    }

    public void updateAppointmentStatus(String appointmentId, AppointmentStatus newStatus)
            throws AppointmentNotFoundException {
        Optional<Appointment> appointmentOpt = appointmentStore.get(appointmentId);
        if (appointmentOpt.isEmpty()) {
            throw new AppointmentNotFoundException("Appointment not found with ID: " + appointmentId);
        }
        Appointment appointment = appointmentOpt.get();
        appointment.setStatus(newStatus);
        appointmentStore.add(appointmentId, appointment);
    }
}