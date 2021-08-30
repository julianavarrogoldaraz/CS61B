package deque;

public class LinkedListDeque<T> implements Deque<T> {

    private class LinkedListNode<T> {
        T item;
        LinkedListNode prev;
        LinkedListNode next;

        LinkedListNode(LinkedListNode prev, T item, LinkedListNode next) {
            this.prev = prev;
            this.item = item;
            this.next = next;
        }
    }

    private LinkedListNode sentinel;
    private int size;

    /** Creates an empty Linked List Deque. */
    public LinkedListDeque() {
        sentinel = new LinkedListNode(null, 0, null);
        sentinel.next = sentinel;
        sentinel.prev = sentinel;
        size = 0;
    }

    @Override
    public void addFirst(T item) {
        sentinel.next = new LinkedListNode(sentinel, item, sentinel.next);
        sentinel.next.next.prev = sentinel.next;
        size += 1;
    }

    @Override
    public void addLast(T item) {
        sentinel.prev = new LinkedListNode(sentinel.prev, item, sentinel);
        sentinel.prev.prev.next = sentinel.prev;
        size += 1;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        } else {
            T first = (T) sentinel.next.item;
            sentinel.next = sentinel.next.next;
            sentinel.next.prev = sentinel;
            size -= 1;
            return first;
        }
    }

    @Override
    public T removeLast() {
        T last = (T) sentinel.prev.item;
        sentinel.prev = sentinel.prev.prev;
        sentinel.prev.next = sentinel;
        size -= 1;
        return last;
    }

    @Override
    public int size() {
        if (size < 0) {
            return 0;
        } else {
            return size;
        }
    }

    @Override
    public T get(int index) {
        LinkedListNode curr = sentinel.next;
        if (index < size) {
            for (int counter = 0; counter < index; counter += 1) {
                curr = curr.next;
            }
            return (T) curr.item;
        } else {
            return null;
        }
    }

    public T getRecursive(int index) {
        LinkedListNode copy = sentinel.next;
        return getRecursiveHelper(copy, index);
    }

    private T getRecursiveHelper(LinkedListNode L, int index) {
        if (L == null) {
            return null;
        } else if (index == 0) {
            return (T) L.item;
        } else {
            return (T) getRecursiveHelper(L.next, index - 1);
        }
    }

    @Override
    public void printDeque() {
        LinkedListNode curr = sentinel.next;
        for (int index = 0; index < size; index += 1) {
            System.out.print(curr.item + " ");
            curr = curr.next;
        }
        System.out.println();
    }

    public boolean equals(Object o) {
        if (!(o instanceof Deque)) {
            return false;
        } else {
            Deque l = (Deque) o;
            if (this.size != l.size()) {
                return false;
            } else {
                for (int index = 0; index < size; index += 1) {
                    if (!(this.get(index).equals(l.get(index)))) {
                        return false;
                    }
                }
                return true;
            }
        }
    }
}
