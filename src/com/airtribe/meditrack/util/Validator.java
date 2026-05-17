package com.airtribe.meditrack.util;

import com.airtribe.meditrack.exception.InvalidDataException;
import java.util.regex.Pattern;

public class Validator {

    // Updated pattern to accept Dr., spaces, and dots
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z\\s\\.]{2,50}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^\\d{10}$");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public static void validateName(String name) throws InvalidDataException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidDataException("Name cannot be empty");
        }
        // Allow letters, spaces, and dots (for Dr., Mr., Mrs., etc.)
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new InvalidDataException("Name must contain only letters, spaces, and dots (2-50 characters)");
        }
    }

    public static void validateAge(int age) throws InvalidDataException {
        if (age < 0 || age > 150) {
            throw new InvalidDataException("Age must be between 0 and 150");
        }
    }

    public static void validatePhone(String phone) throws InvalidDataException {
        if (phone == null || !PHONE_PATTERN.matcher(phone).matches()) {
            throw new InvalidDataException("Phone number must be 10 digits");
        }
    }

    public static void validateEmail(String email) throws InvalidDataException {
        if (email != null && !email.isEmpty() && !EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidDataException("Invalid email format");
        }
    }

    public static void validateAmount(double amount) throws InvalidDataException {
        if (amount <= 0) {
            throw new InvalidDataException("Amount must be positive");
        }
    }
}