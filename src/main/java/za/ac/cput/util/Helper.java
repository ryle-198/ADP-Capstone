package za.ac.cput.util;

import org.apache.commons.validator.routines.EmailValidator;

public class Helper {
    public static boolean isEmptyOrNull(String str) {
        if (str == null || str.isBlank()) {
            return true;
        }
        return false;
    }

    public static boolean isNumNeg(float num) {
        return Float.isNaN(num) || num < 0;
    }

    public static boolean isValidEmail(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }

    public static <T> boolean isValidType(T t) {
        if (t == null) {
            return true;
        }
        return false;
    }
}




