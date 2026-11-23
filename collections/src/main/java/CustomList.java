/**
 * Список элементов с доступом по индексу.
 *
 * @param <E> тип элементов
 */
public interface CustomList<E> extends Iterable<E> {
    /**
     * Добавляет элемент в конец списка.
     *
     * @param element элемент, не может быть null
     * @throws NullPointerException если element равен null
     */
    void add(E element);

    /**
     * Возвращает элемент по индексу.
     *
     * @param index индекс в диапазоне [0, size)
     * @return элемент
     * @throws IndexOutOfBoundsException если индекс вне диапазона
     */
    E get(int index);

    /**
     * Удаляет элемент по индексу и сдвигает остальные влево.
     *
     * @param index индекс в диапазоне [0, size)
     * @return удалённый элемент
     * @throws IndexOutOfBoundsException если индекс вне диапазона
     */
    E remove(int index);

    /**
     * Возвращает количество элементов.
     *
     * @return размер списка
     */
    int size();

    /**
     * Проверяет, пуст ли список.
     *
     * @return true, если элементов нет
     */
    boolean isEmpty();
}
