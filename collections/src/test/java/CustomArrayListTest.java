import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Iterator;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomArrayListTest {

    private CustomArrayList<String> createList() {
        CustomArrayList<String> list = new CustomArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        return list;
    }

    @Test
    void newListIsEmpty() {
        CustomArrayList<String> list = new CustomArrayList<>();

        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    void addOneElement() {
        CustomArrayList<String> list = new CustomArrayList<>();
        list.add("a");

        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
        assertEquals("a", list.get(0));
    }

    @Test
    void addSeveralElementsKeepsOrder() {
        CustomArrayList<String> list = createList();

        assertEquals(3, list.size());
        assertEquals("a", list.get(0));
        assertEquals("b", list.get(1));
        assertEquals("c", list.get(2));
    }

    @Test
    void addMoreThanTenElementsGrows() {
        CustomArrayList<Integer> list = new CustomArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }

        assertEquals(100, list.size());
        for (int i = 0; i < 100; i++) {
            assertEquals(i, list.get(i));
        }
    }

    @ParameterizedTest
    @CsvSource({"0, a", "1, b", "2, c"})
    void getReturnsElement(int index, String expected) {
        CustomArrayList<String> list = createList();

        assertEquals(expected, list.get(index));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 3})
    void getRejectsInvalidIndex(int index) {
        CustomArrayList<String> list = createList();

        assertThrows(IndexOutOfBoundsException.class, () -> list.get(index));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 3})
    void removeRejectsInvalidIndex(int index) {
        CustomArrayList<String> list = createList();

        assertThrows(IndexOutOfBoundsException.class, () -> list.remove(index));
    }

    @ParameterizedTest
    @CsvSource({"0, a, b, c", "1, b, a, c", "2, c, a, b"})
    void removeReturnsElementAndShiftsTail(int index, String expected, String first, String second) {
        CustomArrayList<String> list = createList();

        assertEquals(expected, list.remove(index));
        assertEquals(2, list.size());
        assertEquals(first, list.get(0));
        assertEquals(second, list.get(1));
    }

    @Test
    void addNullThrowsException() {
        CustomArrayList<String> list = new CustomArrayList<>();

        assertThrows(NullPointerException.class, () -> list.add(null));
    }

    @Test
    void iteratorKeepsOrder() {
        CustomArrayList<String> list = createList();
        StringBuilder res = new StringBuilder();
        for (String s : list) {
            res.append(s);
        }

        assertEquals("abc", res.toString());
    }

    @Test
    void finishedIteratorThrowsException() {
        CustomArrayList<String> list = createList();
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            iterator.next();
        }

        assertThrows(NoSuchElementException.class, iterator::next);
    }
}
