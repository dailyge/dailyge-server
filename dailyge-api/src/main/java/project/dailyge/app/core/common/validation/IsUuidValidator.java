package project.dailyge.app.core.common.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class IsUuidValidator implements ConstraintValidator<UuidFormat, String> {

    private static final String UUID_PATTERN = "[a-fA-F0-9]{8}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{4}-[a-fA-F0-9]{12}";
    private static final String ERROR_MESSAGE = "Invalid UUID format.";

    @Override
    public boolean isValid(
        final String value,
        final ConstraintValidatorContext context
    ) {
        try {
            return value.matches(UUID_PATTERN);
        } catch (IllegalArgumentException ex) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ERROR_MESSAGE).addConstraintViolation();
            return false;
        }
    }
}
