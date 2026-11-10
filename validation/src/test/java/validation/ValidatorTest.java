package validation;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidatorTest {
    @Test
    void validObjectHasNoErrors() {
        ValidationResult result = Validator.validate(new User("Alex", "a@b.com", 30));

        assertTrue(result.isValid());
        assertTrue(result.getErrors().isEmpty());
    }

    @Test
    void eachConstraintFindsViolation() {
        ValidationResult notNullResult = Validator.validate(new User(null, "a@b.com", 30));
        ValidationResult sizeResult = Validator.validate(new User("A", "a@b.com", 30));
        ValidationResult emailResult = Validator.validate(new User("Alex", "bad", 30));
        ValidationResult rangeResult = Validator.validate(new User("Alex", "a@b.com", 200));

        assertTrue(notNullResult.getErrors().contains(
                new ValidationError("name", "NotNull", "Name is required")));
        assertTrue(sizeResult.getErrors().contains(
                new ValidationError("name", "Size", "Name size is invalid")));
        assertTrue(emailResult.getErrors().contains(
                new ValidationError("email", "Email", "Email is invalid")));
        assertTrue(rangeResult.getErrors().contains(
                new ValidationError("age", "Range", "Age is invalid")));
    }

    @Test
    void emailRejectsNonBreakingSpace() {
        ValidationResult result = Validator.validate(new User("Alex", "a@\u00a0b.com", 30));

        assertTrue(result.getErrors().contains(
                new ValidationError("email", "Email", "Email is invalid")));
    }

    @Test
    void collectsErrorsFromDifferentFields() {
        ValidationResult result = Validator.validate(new User("A", "bad", 200));

        assertEquals(3, result.getErrors().size());
        assertTrue(result.getErrors().stream().anyMatch(error -> error.fieldName().equals("name")));
        assertTrue(result.getErrors().stream().anyMatch(error -> error.fieldName().equals("email")));
        assertTrue(result.getErrors().stream().anyMatch(error -> error.fieldName().equals("age")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Al", "Alexandr"})
    void sizeBoundsAreIncluded(String name) {
        assertTrue(Validator.validate(new User(name, "a@b.com", 30)).isValid());
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 150})
    void rangeBoundsAreIncluded(int age) {
        assertTrue(Validator.validate(new User("Alex", "a@b.com", age)).isValid());
    }

    @Test
    void nullObjectIsRejected() {
        assertThrows(NullPointerException.class, () -> Validator.validate(null));
    }

    @Test
    void invalidBoundsAreRejected() {
        assertThrows(ValidationConfigurationException.class, () -> Validator.validate(new BadSize()));
        assertThrows(ValidationConfigurationException.class, () -> Validator.validate(new BadRange()));
    }

    @Test
    void unsupportedFieldTypesAreRejected() {
        assertThrows(ValidationConfigurationException.class, () -> Validator.validate(new WrongSizeType()));
        assertThrows(ValidationConfigurationException.class, () -> Validator.validate(new WrongRangeType()));
        assertThrows(ValidationConfigurationException.class, () -> Validator.validate(new WrongEmailType()));
    }

    @Test
    void validatesPrivateAndInheritedFieldsAndIgnoresStaticFields() {
        ValidationResult result = Validator.validate(new Child(null));

        assertEquals(1, result.getErrors().size());
        assertEquals("parentName", result.getErrors().get(0).fieldName());
    }

    @Test
    void errorsListCannotBeChanged() {
        ValidationResult result = new ValidationResult(List.of());

        assertThrows(UnsupportedOperationException.class,
                () -> result.getErrors().add(new ValidationError("name", "NotNull", "required")));
    }

    @Test
    void resultCopiesErrorsAndRejectsNullList() {
        List<ValidationError> errors = new ArrayList<>();
        errors.add(new ValidationError("name", "NotNull", "required"));
        ValidationResult result = new ValidationResult(errors);
        errors.clear();

        assertEquals(1, result.getErrors().size());
        assertThrows(NullPointerException.class, () -> new ValidationResult(null));
    }

    private record User(@NotNull(message = "Name is required") @Size(min = 2, max = 8, message = "Name size is invalid") String name,
                        @Email(message = "Email is invalid") String email,
                        @Range(min = 0, max = 150, message = "Age is invalid") int age) {
    }

    private static class BadSize {
        @Size(min = 4, max = 2)
        private String value;
    }

    private static class BadRange {
        @Range(min = 5, max = 2)
        private int value;
    }

    private static class WrongSizeType {
        @Size(min = 0, max = 2)
        private Object value;
    }

    private static class WrongRangeType {
        @Range(min = 0, max = 2)
        private String value;
    }

    private static class WrongEmailType {
        @Email
        private int value;
    }

    private static class Parent {
        @NotNull
        private String parentName;

        @NotNull
        private static String shared;

        private Parent(String parentName) {
            this.parentName = parentName;
        }
    }

    private static class Child extends Parent {
        private Child(String parentName) {
            super(parentName);
        }
    }
}
