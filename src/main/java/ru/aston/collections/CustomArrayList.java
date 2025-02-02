package ru.aston.collections;

import java.util.*;

/**
 * Пользовательская реализация ArrayList с динамическим изменением размера. Длина списка определяется не константой,
 * а начальным количеством элементов в списке при создании, расширение массива происходит сразу в 2 раза.
 * @param <T>
 */
public class CustomArrayList<T> implements List<T> {

    private T[] container;
    private int size;
    private int modCount;

    /**
     * Создает новый CustomArrayList с указанной начальной емкостью.
     * @param capacity начальная емкость списка
     */
    public CustomArrayList(int capacity) {
        container = (T[]) new Object[capacity];
    }

    /**
     * Расширяет размер массива при необходимости.
     */
    public void arrSize() {
        container = Arrays.copyOf(container, container.length == 0 ? 1 : container.length * 2);
    }

    /**
     * Добавляет элемент в список
     * @param value - добавляемый элемент
     */
    @Override
    public void add(T value) {
        if (size == container.length) {
            arrSize();
        }
        container[size] = value;
        size++;
        modCount++;
    }

    /**
     * Заменяет элемент в указанной позиции новым значением.
     * @param index - индекс заменяемого элемента
     * @param newValue - новое значение
     * @return - предыдущее значение в указанной позиции
     */
    @Override
    public T set(int index, T newValue) {
        try {
            Objects.checkIndex(index, size);
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException();
        }
        T value = container[index];
        container[index] = newValue;
        return value;
    }

    /**
     * Удаляет элемент в указанной позиции.
     * @param index - индекс удаляемого элемента
     * @return - удаленный элемент
     */
    @Override
    public T remove(int index) {
        try {
            Objects.checkIndex(index, size);
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException();
        }
        T value = container[index];
        System.arraycopy(container, index + 1, container, index, size - index - 1);
        container[size - 1] = null;
        size--;
        modCount++;
        return value;
    }

    /**
     * Возвращает элемент в указанной позиции.
     * @param index - индекс элемента
     * @return - элемент в указанной позиции
     */
    @Override
    public T get(int index) {
        try {
            Objects.checkIndex(index, size);
        } catch (IndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException();
        }
        return container[index];
    }

    /**
     * Сортирует элементы списка в естественном порядке.
     */
    @Override
    public void sort() {
        Arrays.sort(container, 0, size);
        modCount++;
    }

    /**
     * Возвращает количество элементов в списке.
     * @return - размер коллекции
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Очищает список, удаляя все элементы.
     */
    @Override
    public void clean() {
        Arrays.fill(container, 0, size, null);
        size = 0;
        modCount++;
    }

    /**
     * Возвращает итератор для элементов списка.
     * @return - итератор списка
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int currentIndex = 0;
            private final int expectedModCount = modCount;

            @Override
            public boolean hasNext() {
                if (modCount != expectedModCount) {
                    throw new ConcurrentModificationException();
                }
                return currentIndex < size;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                if (modCount != expectedModCount) {
                    throw new ConcurrentModificationException();
                }
                return container[currentIndex++];
            }
        };
    }
}
