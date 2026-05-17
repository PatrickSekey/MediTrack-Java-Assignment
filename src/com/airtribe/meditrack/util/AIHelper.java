package com.airtribe.meditrack.util;

import com.airtribe.meditrack.entity.*;
import com.airtribe.meditrack.enums.Specialization;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class AIHelper {

    // Symptom to Specialization mapping
    private static final Map<String, Specialization> SYMPTOM_MAP = new HashMap<>();

    static {
        // Cardiac symptoms
        SYMPTOM_MAP.put("chest", Specialization.CARDIOLOGY);
        SYMPTOM_MAP.put("heart", Specialization.CARDIOLOGY);
        SYMPTOM_MAP.put("palpitations", Specialization.CARDIOLOGY);
        SYMPTOM_MAP.put("blood pressure", Specialization.CARDIOLOGY);

        // Neurological symptoms
        SYMPTOM_MAP.put("headache", Specialization.NEUROLOGY);
        SYMPTOM_MAP.put("migraine", Specialization.NEUROLOGY);
        SYMPTOM_MAP.put("dizziness", Specialization.NEUROLOGY);
        SYMPTOM_MAP.put("seizure", Specialization.NEUROLOGY);

        // Orthopedic symptoms
        SYMPTOM_MAP.put("knee", Specialization.ORTHOPEDICS);
        SYMPTOM_MAP.put("back", Specialization.ORTHOPEDICS);
        SYMPTOM_MAP.put("joint", Specialization.ORTHOPEDICS);
        SYMPTOM_MAP.put("fracture", Specialization.ORTHOPEDICS);
        SYMPTOM_MAP.put("muscle", Specialization.ORTHOPEDICS);

        // Dermatology symptoms
        SYMPTOM_MAP.put("rash", Specialization.DERMATOLOGY);
        SYMPTOM_MAP.put("skin", Specialization.DERMATOLOGY);
        SYMPTOM_MAP.put("acne", Specialization.DERMATOLOGY);
        SYMPTOM_MAP.put("itching", Specialization.DERMATOLOGY);

        // Pediatric symptoms
        SYMPTOM_MAP.put("child", Specialization.PEDIATRICS);
        SYMPTOM_MAP.put("baby", Specialization.PEDIATRICS);
        SYMPTOM_MAP.put("fever", Specialization.PEDIATRICS);
    }

    /**
     * Recommend doctor specialization based on symptoms
     */
    public static Specialization recommendDoctor(String symptoms) {
        if (symptoms == null || symptoms.trim().isEmpty()) {
            return Specialization.PEDIATRICS;
        }

        String lowerSymptoms = symptoms.toLowerCase();

        for (Map.Entry<String, Specialization> entry : SYMPTOM_MAP.entrySet()) {
            if (lowerSymptoms.contains(entry.getKey())) {
                return entry.getValue();
            }
        }

        return Specialization.PEDIATRICS;
    }

    /**
     * Suggest available appointment slots for a doctor
     */
    public static List<LocalDateTime> suggestSlots(Doctor doctor, List<Appointment> existingAppointments) {
        List<LocalDateTime> suggestedSlots = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        // Working hours: 9 AM, 11 AM, 2 PM, 4 PM
        int[] hours = {9, 11, 14, 16};

        // Check next 7 days
        for (int day = 1; day <= 7; day++) {
            LocalDateTime currentDate = now.plusDays(day);

            // Skip weekends (Saturday and Sunday)
            if (currentDate.getDayOfWeek().getValue() > 5) {
                continue;
            }

            for (int hour : hours) {
                LocalDateTime slot = currentDate.withHour(hour).withMinute(0).withSecond(0);

                // Check if slot is available (not booked)
                boolean isAvailable = true;
                for (Appointment apt : existingAppointments) {
                    if (apt.getDoctorId().equals(doctor.getDoctorId())) {
                        LocalDateTime aptTime = apt.getDateTime();
                        if (aptTime.toLocalDate().equals(slot.toLocalDate()) &&
                                aptTime.getHour() == slot.getHour()) {
                            isAvailable = false;
                            break;
                        }
                    }
                }

                if (isAvailable && slot.isAfter(now)) {
                    suggestedSlots.add(slot);
                    if (suggestedSlots.size() >= 3) {
                        return suggestedSlots;
                    }
                }
            }
        }

        return suggestedSlots;
    }
}