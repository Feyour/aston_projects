package ru.aston.collections;

import java.util.*;

import java.util.Iterator;

/**
 * Пользовательская реализация LinkedList
 * @param <E>
 */
public class CustomLinkedList<E extends Comparable<E>> implements List<E> {

    private int size;
    private int modCount;
    private Node<E> head;
    private Node<E> tail;

    /**
     * Добавляет элемент в конец списка.
     * @param value - значение, которое будет добавлено.
     */
    @Override
    public void add(E value) {
        addLast(value);
    }

    /**
     * Добавляет элемент в начало списка.
     * @param value - значение, которое будет добавлено.
     */
    public void addFirst(E value) {
        Node<E> newNode = new Node<>(null, value, head);
        if (head != null) {
            head.prev = newNode;
        } else {
            tail = newNode;
        }
        head = newNode;
        size++;
        modCount++;
    }

    /**
     * Добавляет элемент в конец списка.
     * @param value - значение, которое будет добавлено.
     */
    public void addLast(E value) {
        Node<E> newNode = new Node<>(tail, value, null);
        if (tail != null) {
            tail.next = newNode;
        } else {
            head = newNode;
        }
        tail = newNode;
        size++;
        modCount++;
    }

    /**
     * Удаляет и возвращает первый элемент списка.
     * @return - удаленный элемент.
     */
    public E removeFirst() {
        if (head == null) {
            throw new NoSuchElementException("Список пуст");
        }
        E removedValue = head.item;
        head = head.next;
        if (head != null) {
            head.prev = null;
        } else {
            tail = null;
        }
        size--;
        modCount++;
        return removedValue;
    }

    /**
     * Удаляет и возвращает последний элемент списка.
     * @return - удаленный элемент.
     */
    public E removeLast() {
        if (tail == null) {
            throw new NoSuchElementException("Список пуст");
        }
        E removedValue = tail.item;
        tail = tail.prev;
        if (tail != null) {
            tail.next = null;
        } else {
            head = null;
        }
        size--;
        modCount++;
        return removedValue;
    }

    /**
     * Возвращает первый элемент списка.
     * @return - возвращаемый элемент.
     */
    public E getFirst() {
        if (head == null) {
            throw new NoSuchElementException("Список пуст");
        }
        return head.item;
    }

    /**
     * Возвращает последний элемент списка.
     * @return - последний элемент.
     */
    public E getLast() {
        if (tail == null) {
            throw new NoSuchElementException("Список пуст");
        }
        return tail.item;
    }

    /**
     * Заменяет элемент по указанному индексу на новое значение.
     * @param index - индекс заменяемого элемента.с
     * @param newValue - новое значение элемента.
     * @return - старое значение элемента.
     */
    @Override
    public E set(int index, E newValue) {
        checkIndex(index);
        Node<E> node = getNode(index);
        E oldValue = node.item;
        node.item = newValue;
        modCount++;
        return oldValue;
    }

    /**
     * Удаляет элемент по указанному индексу.
     * @param index - индекс удаляемого элемента.
     * @return - удаленный элемент.
     */
    @Override
    public E remove(int index) {
        checkIndex(index);
        if (index == 0) {
            return removeFirst();
        } else if (index == size - 1) {
            return removeLast();
        } else {
            Node<E> node = getNode(index);
            E removedValue = node.item;
            node.prev.next = node.next;
            node.next.prev = node.prev;
            size--;
            modCount++;
            return removedValue;
        }
    }

    /**
     * Получает элемент по индексу.
     * @param index - индекс искомого элемента.
     * @return - значение по индексу.
     */
    @Override
    public E get(int index) {
        checkIndex(index);
        return getNode(index).item;
    }

    /**
     * Сортирует список.
     */
    @Override
    public void sort() {
        if (size > 1) {
            Object[] array = new Object[size];
            Node<E> current = head;
            for (int i = 0; i < size; i++) {
                array[i] = current.item;
                current = current.next;
            }

            Arrays.sort(array);

            current = head;
            for (Object item : array) {
                current.item = (E) item;
                current = current.next;
            }
            modCount++;
        }
    }

    /**
     * Возвращает размер списка.
     * @return - размер списка.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Очищает список.
     */
    @Override
    public void clean() {
        Node<E> current = head;
        while (current != null) {
            Node<E> next = current.next;
            current.prev = null;
            current.item = null;
            current.next = null;
            current = next;
        }
        head = null;
        tail = null;
        size = 0;
        modCount++;
    }

    /**
     * Возвращает итератор для элементов списка.
     * @return - итератор списка
     */
    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = head;
            private int expectedModCount = modCount;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                checkForComodification();
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                E item = current.item;
                current = current.next;
                return item;
            }

            private void checkForComodification() {
                if (expectedModCount != modCount) {
                    throw new ConcurrentModificationException();
                }
            }
        };
    }

    private Node<E> getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        Node<E> current;
        if (index < size / 2) {
            current = head;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
        } else {
            current = tail;
            for (int i = size - 1; i > index; i--) {
                current = current.prev;
            }
        }
        return current;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    private static class Node<E> {
        private Node<E> prev;
        private E item;
        private Node<E> next;

        Node(Node<E> prev, E element, Node<E> next) {
            this.prev = prev;
            this.item = element;
            this.next = next;
        }
    }
}