import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Динамический массив на основе Object[].
 * При заполнении ёмкость увеличивается в 1.5 раза.
 *
 * @param <E> тип элементов
 */
public class CustomArrayList<E> implements CustomList<E> {
    private static final int DEFAULT_CAPACITY = 10;

    private Object[] arr;
    private int size;

    /**
     * Создаёт пустой список с ёмкостью 10.
     */
    public CustomArrayList() {
        arr = new Object[DEFAULT_CAPACITY];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(E element) {
        if (element == null) {
            throw new NullPointerException("Нельзя добавить null");
        }
        if (size == arr.length) {
            grow();
        }
        arr[size] = element;
        size++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public E get(int index) {
        checkIndex(index);
        return (E) arr[index];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("unchecked")
    public E remove(int index) {
        checkIndex(index);
        E removed = (E) arr[index];
        int tail = size - index - 1;
        if (tail > 0) {
            System.arraycopy(arr, index + 1, arr, index, tail);
        }
        size--;
        arr[size] = null;
        return removed;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Возвращает итератор, который обходит элементы от 0 до size - 1.
     *
     * @return итератор списка
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<>() {
            private int current;

            @Override
            public boolean hasNext() {
                return current < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                E element = (E) arr[current];
                current++;
                return element;
            }
        };
    }

    private void grow() {
        int capacity = arr.length + arr.length / 2;
        arr = Arrays.copyOf(arr, capacity);
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Индекс " + index + ", размер " + size);
        }
    }
}
