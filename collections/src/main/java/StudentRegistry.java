import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * Реестр студентов и операции выборки по нему.
 */
public class StudentRegistry {
    /**
     * Минимальная оценка.
     */
    public static final double MIN_GRADE = 0.0;

    /**
     * Максимальная оценка.
     */
    public static final double MAX_GRADE = 10.0;

    private StudentRegistry() {
    }

    /**
     * Создаёт HashMap, где ключ равен id студента.
     *
     * @param arr список студентов
     * @return карта студентов
     */
    public static Map<Integer, Student> createHashMap(List<Student> arr) {
        Map<Integer, Student> res = new HashMap<>();
        for (Student student : arr) {
            res.put(student.id(), student);
        }
        return res;
    }

    /**
     * Создаёт TreeMap с сортировкой id по убыванию.
     *
     * @param arr список студентов
     * @return карта студентов
     */
    public static NavigableMap<Integer, Student> createTreeMap(List<Student> arr) {
        NavigableMap<Integer, Student> res = new TreeMap<>(Comparator.reverseOrder());
        for (Student student : arr) {
            res.put(student.id(), student);
        }
        return res;
    }

    /**
     * Возвращает студентов с оценкой в диапазоне [minGrade, maxGrade].
     *
     * @param students карта студентов
     * @param minGrade нижняя граница
     * @param maxGrade верхняя граница
     * @return новый список студентов
     * @throws IllegalArgumentException если minGrade больше maxGrade или границы вне [0.0, 10.0]
     */
    public static List<Student> findStudentsByGradeRange(
            Map<Integer, Student> students,
            double minGrade,
            double maxGrade) {
        if (minGrade > maxGrade || minGrade < MIN_GRADE || maxGrade > MAX_GRADE) {
            throw new IllegalArgumentException("Некорректный диапазон оценок");
        }
        List<Student> res = new ArrayList<>();
        for (Student student : students.values()) {
            if (student.grade() >= minGrade && student.grade() <= maxGrade) {
                res.add(student);
            }
        }
        return res;
    }

    /**
     * Возвращает не более n студентов с наибольшими id в порядке убывания id.
     *
     * @param students карта с сортировкой id по убыванию
     * @param n        количество студентов
     * @return новый список студентов
     * @throws IllegalArgumentException если n меньше 0
     */
    public static List<Student> getTopNStudentsById(
            NavigableMap<Integer, Student> students,
            int n) {
        if (n < 0) {
            throw new IllegalArgumentException("n не может быть отрицательным");
        }
        List<Student> res = new ArrayList<>();
        for (Student student : students.values()) {
            if (res.size() == n) {
                break;
            }
            res.add(student);
        }
        return res;
    }
}
