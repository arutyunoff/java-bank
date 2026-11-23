package validation;

import java.util.List;
import java.util.Objects;

public final class ValidationResult {
    private final List<ValidationError> errors;

    public ValidationResult(List<ValidationError> errors) {
        this.errors = List.copyOf(Objects.requireNonNull(errors));
    }

    public boolean isValid() {
        return errors.isEmpty();
    }

    public List<ValidationError> getErrors() {
        return errors;
    }
}
