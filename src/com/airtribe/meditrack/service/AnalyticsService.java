package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.*;
import com.airtribe.meditrack.enums.*;
import java.util.*;
import java.util.stream.Collectors;

public class AnalyticsService {

    public Map<Specialization, Long> getAppointmentCountBySpecialization(
            List<Appointment> appointments, List<Doctor> doctors) {

        Map<String, Specialization> doctorSpecMap = doctors.stream()
                .collect(Collectors.toMap(Doctor::getDoctorId, Doctor::getSpecialization));

        return appointments.stream()
                .filter(apt -> apt.getStatus() == AppointmentStatus.COMPLETED)
                .collect(Collectors.groupingBy(
                        apt -> doctorSpecMap.get(apt.getDoctorId()),
                        Collectors.counting()
                ));
    }

    public List<Doctor> getTopRatedDoctors(List<Doctor> doctors, int limit) {
        return doctors.stream()
                .sorted((d1, d2) -> Double.compare(d2.getRating(), d1.getRating()))
                .limit(limit)
                .collect(Collectors.toList());
    }

    public double getAverageConsultationFee(List<Doctor> doctors) {
        return doctors.stream()
                .mapToDouble(Doctor::getConsultationFee)
                .average()
                .orElse(0.0);
    }
}