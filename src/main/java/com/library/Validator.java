package com.library;

import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextInputControl;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public final class Validator {

    private static final Pattern EMAIL = Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    private static final Pattern PHONE = Pattern.compile("^0\\d{9}$");   // 10 digits, e.g. 0711234567
    private static final String ERROR_CLASS = "input-error";

    private Validator() { }

    public static boolean isBlank(String text) {
        return text == null || text.trim().isEmpty();
    }

    public static boolean isValidEmail(String text) {
        return text != null && EMAIL.matcher(text.trim()).matches();
    }

    public static boolean isValidPhone(String text) {
        return text != null && PHONE.matcher(text.trim()).matches();
    }

    /** Returns the number, or null if the text is not a whole number. */
    public static Integer parseInt(String text) {
        if (text == null) return null;
        try {
            return Integer.parseInt(text.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** Stops the user from typing anything except digits (up to maxLength). */
    public static void digitsOnly(TextField field, int maxLength) {
        field.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().matches("\\d{0," + maxLength + "}") ? change : null));
    }

    /** Gives fields a red border (see .input-error in style.css). */
    public static void markInvalid(Control... controls) {
        for (Control c : controls) {
            if (!c.getStyleClass().contains(ERROR_CLASS)) {
                c.getStyleClass().add(ERROR_CLASS);
            }
        }
    }

    public static void clearInvalid(Control... controls) {
        for (Control c : controls) {
            c.getStyleClass().remove(ERROR_CLASS);
        }
    }

    /** Shared by the Add Member and Manage Members pages. */
    public static List<String> validateMemberFields(TextInputControl name, TextInputControl email,
                                                    TextInputControl phone, TextInputControl address) {
        List<String> errors = new ArrayList<>();
        if (isBlank(name.getText())) {
            errors.add("Full name is required.");
            markInvalid(name);
        }
        if (!isValidEmail(email.getText())) {
            errors.add("Enter a valid email address (e.g. name@example.com).");
            markInvalid(email);
        }
        if (!isValidPhone(phone.getText())) {
            errors.add("Phone number must be 10 digits starting with 0 (e.g. 0711234567).");
            markInvalid(phone);
        }
        if (isBlank(address.getText())) {
            errors.add("Address is required.");
            markInvalid(address);
        }
        return errors;
    }
}