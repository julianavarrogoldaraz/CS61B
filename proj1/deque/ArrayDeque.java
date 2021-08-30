package deque;

public class ArrayDeque<T> implements Deque<T> {

    private int totalSize;
    private int size;
    private int nextFirst;
    private int nextLast;
    private T[] items;

    /** Creates an empty Array Deque. */
    public ArrayDeque() {
        totalSize = 8;
        items = (T[]) new Object[totalSize];
        size = 0;
        nextFirst = 4;
        nextLast = 5;
    }

    private boolean isResize() {
        return size == totalSize;
    }

    private boolean isResizeDown() {
        double usageCapacity = (double) size / (double) items.length;
        return size >= 16 && usageCapacity <= 0.25;
    }

    @Override
    public void addFirst(T item) {
        if (isResize()) {
            resize();
        }
        items[nextFirst] = item;
        if (nextFirst == 0) {
            nextFirst = items.length - 1;
        } else {
            nextFirst -= 1;
        }
        size += 1;
    }

    @Override
    public void addLast(T item) {
        if (isResize()) {
            resize();
        }
        items[nextLast] = item;
        if (nextLast == items.length - 1) {
            nextLast = 0;
        } else {
            nextLast += 1;
        }
        size += 1;
    }

    private void resize() {
        if (size == items.length) {
            totalSize = items.length * 2;
            T[] newArr = (T[]) new Object[totalSize];
            for (int index = nextLast; index < size; index += 1) {
                newArr[index - nextLast] = items[index];
            }
            for (int index = 0; index <= nextFirst; index += 1) {
                newArr[index + size - nextFirst - 1] = items[index];
            }
            nextFirst = totalSize - 1;
            nextLast = size;
            items = newArr;
        }
    }

    private void resizeDown() {
        if (isResizeDown()) {
            totalSize = items.length / 2;
            T[] newArr = (T[]) new Object[totalSize];
            if (nextFirst <= nextLast) {
                for (int index = nextFirst + 1; index < nextLast; index += 1) {
                    newArr[index - nextFirst - 1] = items[index];
                }
            } else {
                for (int index = nextFirst + 1; index < items.length; index += 1) {
                    newArr[index - nextFirst - 1] = items[index];
                }
                for (int index = 0; index < nextLast; index += 1) {
                    newArr[index + items.length - nextFirst - 1] = items[index];
                }
            }
            nextFirst = totalSize - 1;
            nextLast = size;
            items = newArr;
        }
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
    public void printDeque() {
        for (int index = 0; index < size; index += 1) {
            System.out.print(get(index) + " ");
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (size > 0) {
            if (isResizeDown()) {
                resizeDown();
            }
            if (nextFirst == items.length - 1) {
                nextFirst = 0;
            } else {
                nextFirst += 1;
            }
            size -= 1;
            return items[nextFirst];
        }
        return null;
    }

    @Override
    public T removeLast() {
        if (size > 0) {
            if (isResizeDown()) {
                resizeDown();
            }
            if (nextLast == 0) {
                nextLast = items.length - 1;
            } else {
                nextLast -= 1;
            }
            size -= 1;
            return items[nextLast];
        }
        return null;
    }

    @Override
    public T get(int index) {
        if (index >= size) {
            return null;
        }
        return items[(nextFirst + index + 1) % items.length];
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Deque)) {
            return false;
        } else {
            Deque d = (Deque) o;
            if (this.size() != d.size()) {
                return false;
            }
            for (int index = 0; index < size; index += 1) {
                if (!(get(index).equals(d.get(index)))) {
                    return false;
                }
            }
            return true;
        }
    }
}
