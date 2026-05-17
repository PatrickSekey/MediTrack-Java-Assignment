package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.exception.InvalidDataException;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.Validator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PatientService {
    private DataStore<Patient> patientStore;

    public PatientService() {
        this.patientStore = new DataStore<>();
    }

    public Patient registerPatient(String name, int age, String phone, String email, String address)
            throws InvalidDataException {
        Validator.validateName(name);
        Validator.validateAge(age);
        Validator.validatePhone(phone);
        Validator.validateEmail(email);

        Patient patient = new Patient(name, age, phone, email, address);
        patientStore.add(patient.getPatientId(), patient);
        return patient;
    }

    public Optional<Patient> searchPatientById(String id) {
        return patientStore.get(id);
    }

    public List<Patient> searchPatientByName(String name) {
        return patientStore.getAll().stream()
                .filter(p -> p.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Patient> searchPatientByAge(int age) {
        return patientStore.getAll().stream()
                .filter(p -> p.getAge() == age)
                .collect(Collectors.toList());
    }

    public List<Patient> getAllPatients() {
        return patientStore.getAll();
    }

    public void updatePatient(Patient patient) {
        patientStore.add(patient.getPatientId(), patient);
    }

    public boolean deletePatient(String id) {
        return patientStore.remove(id);
    }
}