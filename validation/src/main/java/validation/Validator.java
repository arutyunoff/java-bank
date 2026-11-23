package validation;

import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Validator {
    private Validator() {
    }

    public static ValidationResult validate(Object object) {
        Objects.requireNonNull(object);
        List<ValidationError> errors = new ArrayList<>();
        Class<?> type = object.getClass();

        while (type != Object.class) {
            for (Field field : type.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers()) || field.isSynthetic()) {
                    continue;
                }
                validateField(object, field, errors);
            }
            type = type.getSuperclass();
        }

        return new ValidationResult(errors);
    }

    private static void validateField(Object object, Field field, List<ValidationError> errors) {
        NotNull notNull = field.getAnnotation(NotNull.class);
        Size size = field.getAnnotation(Size.class);
        Range range = field.getAnnotation(Range.class);
        Email email = field.getAnnotation(Email.class);

        if (notNull == null && size == null && range == null && email == null) {
            return;
        }

        validateConfiguration(field, size, range, email);
        Object value = readValue(object, field);

        if (notNull != null && value == null) {
            addError(errors, field, "NotNull", notNull.message());
        }
        if (size != null && value != null && !hasValidSize(field, value, size)) {
            addError(errors, field, "Size", size.message());
        }
        if (range != null && value != null && !hasValidRange((Number) value, range)) {
            addError(errors, field, "Range", range.message());
        }
        if (email != null && value != null && !hasValidEmail((CharSequence) value)) {
            addError(errors, field, "Email", email.message());
        }
    }

    private static void validateConfiguration(Field field, Size size, Range range, Email email) {
        if (size != null) {
            if (size.min() < 0 || size.min() > size.max()) {
                throw configurationError(field, "invalid @Size bounds");
            }
            if (!CharSequence.class.isAssignableFrom(field.getType())) {
                throw configurationError(field, "@Size requires a CharSequence field");
            }
        }
        if (range != null) {
            if (Double.isNaN(range.min()) || Double.isNaN(range.max())
                    || Double.isInfinite(range.min()) || Double.isInfinite(range.max())
                    || range.min() > range.max()) {
                throw configurationError(field, "invalid @Range bounds");
            }
            if (!isNumericType(field.getType())) {
                throw configurationError(field, "@Range requires a numeric field");
            }
        }
        if (email != null && !CharSequence.class.isAssignableFrom(field.getType())) {
            throw configurationError(field, "@Email requires a CharSequence field");
        }
    }

    private static boolean hasValidSize(Field field, Object value, Size size) {
        if (!(value instanceof CharSequence text)) {
            throw configurationError(field, "@Size value does not implement CharSequence");
        }
        return text.length() >= size.min() && text.length() <= size.max();
    }

    private static boolean hasValidRange(Number value, Range range) {
        double number = value.doubleValue();
        return !Double.isNaN(number) && number >= range.min() && number <= range.max();
    }

    private static boolean hasValidEmail(CharSequence value) {
        String text = value.toString();
        int at = text.indexOf('@');
        if (at <= 0 || at != text.lastIndexOf('@') || at == text.length() - 1) {
            return false;
        }
        for (int i = 0; i < text.length(); i++) {
            if (Character.isWhitespace(text.charAt(i)) || Character.isSpaceChar(text.charAt(i))) {
                return false;
            }
        }
        int dot = text.indexOf('.', at + 1);
        return dot > at + 1 && dot < text.length() - 1;
    }

    private static boolean isNumericType(Class<?> type) {
        if (Number.class.isAssignableFrom(type)) {
            return true;
        }
        return type == byte.class || type == short.class || type == int.class
                || type == long.class || type == float.class || type == double.class;
    }

    private static Object readValue(Object object, Field field) {
        try {
            if (!field.trySetAccessible()) {
                throw configurationError(field, "field is not accessible");
            }
            return field.get(object);
        } catch (IllegalAccessException | InaccessibleObjectException | SecurityException exception) {
            throw configurationError(field, "field is not accessible");
        }
    }

    private static void addError(List<ValidationError> errors, Field field, String name, String message) {
        errors.add(new ValidationError(field.getName(), name, message));
    }

    private static ValidationConfigurationException configurationError(Field field, String message) {
        return new ValidationConfigurationException(
                "Class " + field.getDeclaringClass().getName() + ", field " + field.getName() + ": " + message);
    }
}
