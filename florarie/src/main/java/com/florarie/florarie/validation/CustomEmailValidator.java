/** Validator custom pentru verificarea formatului email-ului
 * @author Dinca (Mateas) Marta
 * @version 05 Ianuarie 2026
 */
package com.florarie.florarie.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CustomEmailValidator implements ConstraintValidator<ValidEmail, String> {

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }

        // Verifică că există exact un @
        int atIndex = email.indexOf('@');
        if (atIndex == -1 || atIndex != email.lastIndexOf('@')) {
            return false;
        }

        // Partea înainte de @
        String beforeAt = email.substring(0, atIndex);
        if (beforeAt.isEmpty()) {
            return false;
        }
        
        // Trebuie să înceapă cu literă sau cifră
        char firstChar = beforeAt.charAt(0);
        if (!Character.isLetterOrDigit(firstChar)) {
            return false;
        }

        // Partea după @
        String afterAt = email.substring(atIndex + 1);
        if (afterAt.isEmpty()) {
            return false;
        }

        // Verifică că există punct după @
        int dotIndex = afterAt.indexOf('.');
        if (dotIndex == -1 || dotIndex == 0 || dotIndex == afterAt.length() - 1) {
            return false;
        }

        // Partea înainte de punct (după @) trebuie să înceapă cu literă
        if (!Character.isLetter(afterAt.charAt(0))) {
            return false;
        }

        // Partea după punct trebuie să înceapă cu literă
        String afterDot = afterAt.substring(dotIndex + 1);
        if (afterDot.isEmpty() || !Character.isLetter(afterDot.charAt(0))) {
            return false;
        }

        return true;
    }
}
