import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Set;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StudentRegistryTest {
    private final Student ivan = new Student(1, "Иван", 4.5);
    private final Student anna = new Student(2, "Анна", 10.0);
    private final Student oleg = new Student(3, "Олег", 0.0);
    private final Student maria = new Student(4, "Мария", 7.0);
    private final List<Student> arr = List.of(ivan, anna, oleg, maria);

    @Test
    void studentRejectsInvalidData() {
        assertThrows(IllegalArgumentException.class, () -> new Student(0, "Иван", 5.0));
        assertThrows(IllegalArgumentException.class, () -> new Student(1, " ", 5.0));
        assertThrows(IllegalArgumentException.class, () -> new Student(1, null, 5.0));
        assertThrows(IllegalArgumentException.class, () -> new Student(1, "Иван", -0.1));
        assertThrows(IllegalArgumentException.class, () -> new Student(1, "Иван", 10.1));
    }

    @Test
    void studentEqualsAndHashCode() {
        Student copy = new Student(1, "Иван", 4.5);

        assertEquals(ivan, copy);
        assertEquals(ivan.hashCode(), copy.hashCode());
        assertNotEquals(ivan, anna);
    }

    @Test
    void createHashMapUsesIdAsKey() {
        Map<Integer, Student> students = StudentRegistry.createHashMap(arr);

        assertInstanceOf(HashMap.class, students);
        assertEquals(4, students.size());
        for (Student student : arr) {
            assertEquals(student, students.get(student.id()));
        }
    }

    @Test
    void createTreeMapSortsIdsDescending() {
        NavigableMap<Integer, Student> students = StudentRegistry.createTreeMap(arr);

        assertInstanceOf(TreeMap.class, students);
        assertEquals(List.of(4, 3, 2, 1), new ArrayList<>(students.keySet()));
    }

    @Test
    void findStudentsByGradeRangeReturnsMatching() {
        Map<Integer, Student> students = StudentRegistry.createHashMap(arr);

        List<Student> res = StudentRegistry.findStudentsByGradeRange(students, 4.0, 8.0);

        assertEquals(Set.of(ivan, maria), new HashSet<>(res));
        assertEquals(2, res.size());
    }

    @Test
    void findStudentsByGradeRangeIncludesBounds() {
        Map<Integer, Student> students = StudentRegistry.createHashMap(arr);

        List<Student> res = StudentRegistry.findStudentsByGradeRange(students, 4.5, 7.0);

        assertEquals(Set.of(ivan, maria), new HashSet<>(res));
        assertEquals(2, res.size());
    }

    @Test
    void findStudentsByGradeRangeFullRange() {
        Map<Integer, Student> students = StudentRegistry.createHashMap(arr);

        List<Student> res = StudentRegistry.findStudentsByGradeRange(students, 0.0, 10.0);

        assertEquals(new HashSet<>(arr), new HashSet<>(res));
    }

    @Test
    void findStudentsByGradeRangeNoMatches() {
        Map<Integer, Student> students = StudentRegistry.createHashMap(arr);

        List<Student> res = StudentRegistry.findStudentsByGradeRange(students, 8.0, 9.0);

        assertTrue(res.isEmpty());
    }

    @Test
    void findStudentsByGradeRangeEmptyMap() {
        List<Student> res = StudentRegistry.findStudentsByGradeRange(new HashMap<>(), 0.0, 10.0);

        assertTrue(res.isEmpty());
    }

    @Test
    void findStudentsByGradeRangeRejectsInvalidRange() {
        Map<Integer, Student> students = StudentRegistry.createHashMap(arr);

        assertThrows(IllegalArgumentException.class,
                () -> StudentRegistry.findStudentsByGradeRange(students, 8.0, 4.0));
        assertThrows(IllegalArgumentException.class,
                () -> StudentRegistry.findStudentsByGradeRange(students, -1.0, 5.0));
        assertThrows(IllegalArgumentException.class,
                () -> StudentRegistry.findStudentsByGradeRange(students, 5.0, 11.0));
    }

    @Test
    void findStudentsByGradeRangeDoesNotChangeMap() {
        Map<Integer, Student> students = StudentRegistry.createHashMap(arr);
        Map<Integer, Student> copy = new HashMap<>(students);

        List<Student> res = StudentRegistry.findStudentsByGradeRange(students, 0.0, 5.0);
        res.clear();

        assertEquals(copy, students);
    }

    @Test
    void findStudentsByGradeRangeReturnsArrayList() {
        Map<Integer, Student> students = StudentRegistry.createHashMap(arr);

        List<Student> res = StudentRegistry.findStudentsByGradeRange(students, 0.0, 5.0);
        res.add(anna);

        assertInstanceOf(ArrayList.class, res);
    }

    @Test
    void getTopNStudentsByIdReturnsDescending() {
        NavigableMap<Integer, Student> students = StudentRegistry.createTreeMap(arr);

        List<Student> res = StudentRegistry.getTopNStudentsById(students, 2);

        assertEquals(List.of(maria, oleg), res);
    }

    @Test
    void getTopNStudentsByIdZero() {
        NavigableMap<Integer, Student> students = StudentRegistry.createTreeMap(arr);

        List<Student> res = StudentRegistry.getTopNStudentsById(students, 0);

        assertTrue(res.isEmpty());
    }

    @Test
    void getTopNStudentsByIdMoreThanSize() {
        NavigableMap<Integer, Student> students = StudentRegistry.createTreeMap(arr);

        List<Student> res = StudentRegistry.getTopNStudentsById(students, 10);

        assertEquals(List.of(maria, oleg, anna, ivan), res);
    }

    @Test
    void getTopNStudentsByIdEmptyMap() {
        NavigableMap<Integer, Student> students = StudentRegistry.createTreeMap(List.of());

        List<Student> res = StudentRegistry.getTopNStudentsById(students, 3);

        assertTrue(res.isEmpty());
    }

    @Test
    void getTopNStudentsByIdRejectsNegative() {
        NavigableMap<Integer, Student> students = StudentRegistry.createTreeMap(arr);

        assertThrows(IllegalArgumentException.class, () -> StudentRegistry.getTopNStudentsById(students, -1));
    }

    @Test
    void getTopNStudentsByIdDoesNotChangeMap() {
        NavigableMap<Integer, Student> students = StudentRegistry.createTreeMap(arr);
        Map<Integer, Student> copy = new TreeMap<>(students);

        List<Student> res = StudentRegistry.getTopNStudentsById(students, 2);
        res.add(ivan);

        assertInstanceOf(ArrayList.class, res);
        assertEquals(copy, students);
    }
}
