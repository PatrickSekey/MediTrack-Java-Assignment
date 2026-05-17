package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.enums.Specialization;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.Validator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DoctorService {
    private DataStore<Doctor> doctorStore;

    public DoctorService() {
        this.doctorStore = new DataStore<>();
    }

    public Doctor addDoctor(String name, int age, String phone, String email,
                            Specialization specialization, double consultationFee)
            throws InvalidDataException {
        Validator.validateName(name);
        Validator.validateAge(age);
        Validator.validatePhone(phone);
        Validator.validateEmail(email);

        Doctor doctor = new Doctor(name, age, phone, email, specialization, consultationFee);
        doctorStore.add(doctor.getDoctorId(), doctor);
        return doctor;
    }

    public Optional<Doctor> getDoctorById(String id) {
        return doctorStore.get(id);
    }

    public List<Doctor> getAllDoctors() {
        return doctorStore.getAll();
    }

    public List<Doctor> getDoctorsBySpecialization(Specialization specialization) {
        return doctorStore.getAll().stream()
                .filter(d -> d.getSpecialization() == specialization)
                .collect(Collectors.toList());
    }

    public List<Doctor> getAvailableDoctors() {
        return doctorStore.getAll().stream()
                .filter(Doctor::isAvailable)
                .collect(Collectors.toList());
    }

    public void updateDoctor(Doctor doctor) {
        doctorStore.add(doctor.getDoctorId(), doctor);
    }

    public boolean deleteDoctor(String id) {
        return doctorStore.remove(id);
    }
}