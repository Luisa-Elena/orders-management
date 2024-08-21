package org.example.BusinessLogic.Validators;

import org.example.Model.Clients;
import java.util.regex.Pattern;

/**
 * Utility class for validating email addresses.
 */
public class EmailValidator {
    /**
     * Regular expression pattern for validating email addresses, ensuring it contains @ and .com
     */
    private static final String EMAIL_PATTERN = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.com$";

    /**
     * Validates the email address of a client.
     *
     * @param t The client whose email address needs to be validated.
     * @throws IllegalArgumentException if the email address is not valid.
     */
    public void validate(Clients t) {
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        if (!pattern.matcher(t.getEmail()).matches()) {
            throw new IllegalArgumentException("Not a valid amount!");
        }
    }
}