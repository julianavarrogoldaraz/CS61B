package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {

    private Comparator<T> c;

    public MaxArrayDeque(Comparator<T> c) {
        super();
        this.c = c;
    }

    public T max() {
        T max = null;
        if (isEmpty()) {
            return null;
        } else {
            max = (T) get(0);
            for (int index = 0; index < size(); index += 1) {
                if (c.compare((T) get(index), max) > 0) {
                    max = (T) get(index);
                }
            }
        }
        return max;
    }

    public T max(Comparator<T> c) {
        T max = null;
        if (isEmpty()) {
            return null;
        } else {
            max = (T) get(0);
            for (int index = 0; index < size(); index += 1) {
                if (c.compare((T) get(index), max) > 0) {
                    max = (T) get(index);
                }
            }
        }
        return max;
    }
}
