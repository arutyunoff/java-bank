/**
 * Неизменяемая модель студента.
 *
 * @param id    положительный уникальный идентификатор
 * @param name  непустое имя
 * @param grade оценка от 0.0 до 10.0 включительно
 */
public record Student(int id, String name, double grade) {
    /**
     * Создаёт студента и проверяет параметры.
     *
     * @throws IllegalArgumentException если параметры некорректны
     */
    public Student {
        if (id <= 0) {
            throw new IllegalArgumentException("id должен быть положительным");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Имя не может быть пустым");
        }
        if (grade < StudentRegistry.MIN_GRADE || grade > StudentRegistry.MAX_GRADE) {
            throw new IllegalArgumentException("Оценка должна быть от 0.0 до 10.0");
        }
    }
}
