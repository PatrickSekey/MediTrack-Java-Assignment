package com.airtribe.meditrack.util;

import java.util.concurrent.atomic.AtomicInteger;

public class IdGenerator {
    private static volatile IdGenerator instance;
    private final AtomicInteger patientCounter = new AtomicInteger(1000);
    private final AtomicInteger doctorCounter = new AtomicInteger(2000);
    private final AtomicInteger appointmentCounter = new AtomicInteger(3000);

    private IdGenerator() {
        // Private constructor for Singleton
    }

    public static IdGenerator getInstance() {
        if (instance == null) {
            synchronized (IdGenerator.class) {
                if (instance == null) {
                    instance = new IdGenerator();
                }
            }
        }
        return instance;
    }

    public String generatePatientId() {
        return "PAT" + patientCounter.getAndIncrement();
    }

    public String generateDoctorId() {
        return "DOC" + doctorCounter.getAndIncrement();
    }

    public String generateAppointmentId() {
        return "APT" + appointmentCounter.getAndIncrement();
    }
}