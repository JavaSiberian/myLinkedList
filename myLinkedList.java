import java.util.*;

public class myLinkedList<E> extends AbstractList<E> {

    private static class Node<E> {
        E value;

        boolean exists;

        Node<E> prev;
        Node<E> next;
    }

    private Map<E, Node<E>> map = new HashMap<>();

    private Node<E> last = new Node<>();
    private Node<E> first = last;

    @Override
    public boolean isEmpty() {
        return first == last;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    @Override
    public boolean add(E e) {
        Node<E> element = map.putIfAbsent(e, last);

        if (element != null) {
            return false;
        }

        element = last;
        element.exists = true;
        element.value = e;

        last = new Node<>();
        last.prev = element;

        element.next = last;

        return true;
    }

    @Override
    public E get(int index) {
        if (index >= map.size() || index < 0)
            throw new IndexOutOfBoundsException();
        return getNode(index).value;
    }

    private Node<E> getNode(int index) {
        if (index < (map.size() >> 1)) {
            Node<E> x = first;
            for (int i = 0; i < index; i++)
                x = x.next;
            return x;
        } else {
            Node<E> x = last.prev;
            for (int i = map.size() - 1; i > index; i--)
                x = x.prev;
            return x;
        }
    }

    @Override
    public boolean remove(Object o) {
        Node<E> removedNode = map.remove(o);

        if (removedNode == null) {
            return false;
        }

        removeNode(removedNode);

        return true;
    }

    private void removeNode(Node<E> element) {
        element.exists = false;
        element.value = null;

        element.next.prev = element.prev;

        if (element.prev != null) {
            element.prev.next = element.next;
            element.prev = null;
        } else {
            first = element.next;
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new Itr();
    }

    private class Itr implements Iterator<E> {
        Node<E> next = first;
        Node<E> current = null;

        Node<E> findNext() {
            Node<E> n = next;

            while (!n.exists && n.next != null) {
                next = n = n.next;
            }

            return n;
        }

        @Override
        public boolean hasNext() {
            return findNext().exists;
        }

        @Override
        public E next() {
            Node<E> n = findNext();

            if (!n.exists) {
                throw new NoSuchElementException();
            }

            current = n;
            next = n.next;

            return n.value;
        }

        @Override
        public void remove() {
            if (current == null) {
                throw new IllegalStateException();
            }

            if (map.remove(current.value, current)) {
                removeNode(current);
            } else {
                throw new NoSuchElementException();
            }
        }
    }

    public static void main(String[] args) {
        myLinkedList list = new myLinkedList();
        list.add(1);
        list.add(2);
        list.add(3);
        System.out.println(list.get(2));


    }
}
