/** A data structure to represent a Linked List of Integers.
 * Each IntList represents one node in the overall Linked List.
 *
 * @author Maurice Lee and Wan Fung Chui
 */

public class IntList {

    /** The integer stored by this node. */
    public int item;
    /** The next node in this IntList. */
    public IntList next;

    /** Constructs an IntList storing ITEM and next node NEXT. */
    public IntList(int item, IntList next) {
        this.item = item;
        this.next = next;
    }

    /** Constructs an IntList storing ITEM and no next node. */
    public IntList(int item) {
        this(item, null);
    }

    /** Returns an IntList consisting of the elements in ITEMS.
     * IntList L = IntList.list(1, 2, 3);
     * System.out.println(L.toString()) // Prints 1 2 3 */
    public static IntList of(int... items) {
        /** Check for cases when we have no element given. */
        if (items.length == 0) {
            return null;
        }
        /** Create the first element. */
        IntList head = new IntList(items[0]);
        IntList last = head;
        /** Create rest of the list. */
        for (int i = 1; i < items.length; i++) {
            last.next = new IntList(items[i]);
            last = last.next;
        }
        return head;
    }

    /**
     * Returns [position]th item in this list. Throws IllegalArgumentException
     * if index out of bounds.
     *
     * @param position, the position of element.
     * @return The element at [position]
     */
    public int get(int position) {
        IntList pointer = this;
        int counter = 0;

        if (position < 0) {
            throw new IllegalArgumentException("Position out of bounds.");
        }

        while (counter < position) {
            if (pointer.next == null) {
                throw new IllegalArgumentException("Position out of bounds.");
            } else {
                pointer = pointer.next;
                counter += 1;
            }
        }
        return pointer.item;
    }

    /**
     * Returns the string representation of the list. For the list (1, 2, 3),
     * returns "1 2 3".
     *
     * @return The String representation of the list.
     *
     * Learned about String.valueOf() method here:
     * @https://beginnersbook.com/2015/05/java-int-to-string/
     */
    public String toString() {
        IntList pointer = this;
        String s = "";

        while (pointer.next != null) {
            s += String.valueOf(pointer.item + " ");
            pointer = pointer.next;
        }
        s += String.valueOf(pointer.item);
        return s;
    }

    /**
     * Returns whether this and the given list or object are equal.
     *
     * NOTE: A full implementation of equals requires checking if the
     * object passed in is of the correct type, as the parameter is of
     * type Object. This also requires we convert the Object to an
     * IntList, if that is legal. The operation we use to do this is called
     * casting, and it is done by specifying the desired type in
     * parenthesis. An example of this is on line 84.
     *
     * @param obj, another list (object)
     * @return Whether the two lists are equal.
     */
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof IntList)) {
            return false;
        }
        IntList otherLst = (IntList) obj;

        IntList pointer1 = this;
        IntList pointer2 = otherLst;

        while (pointer1 != null && pointer2 != null) {
            if (pointer1.next == null && pointer2.next != null) {
                return false;
            } else if (pointer1.next != null && pointer2.next == null) {
                return false; }
            if (pointer1.item == pointer2.item) {
                pointer1 = pointer1.next;
                pointer2 = pointer2.next;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds the given value at the end of the list.
     *
     * @param value, the int to be added.
     */
    public void add(int value) {
        IntList pointer = this;
        IntList result = new IntList(value, null);

        while (pointer.next != null) {
            pointer = pointer.next;
        }
        pointer.next = result;
    }

    /**
     * Returns the smallest element in the list.
     *
     * @return smallest element in the list
     */
    public int smallest() {
        IntList pointer = this;
        int min = this.item;

        while (pointer.next != null) {
            pointer = pointer.next;
            if (pointer.item < min) {
                min = pointer.item;
            }
        }
        return min;
    }

    /**
     * Returns the sum of squares of all elements in the list.
     *
     * @return The sum of squares of all elements.
     */
    public int squaredSum() {
        IntList pointer = this;
        int sum = 0;

        while (pointer != null) {
            sum += pointer.item * pointer.item;
            pointer = pointer.next;
        }
        return sum;
    }

    /**
     * Destructively squares each item of the list.
     *
     * @param L list to destructively square.
     */
    public static void dSquareList(IntList L) {
        while (L != null) {
            L.item = L.item * L.item;
            L = L.next;
        }
    }

    /**
     * Returns a list equal to L with all elements squared. Non-destructive.
     *
     * @param L list to non-destructively square.
     * @return the squared list.
     */
    public static IntList squareListIterative(IntList L) {
        if (L == null) {
            return null;
        }
        IntList res = new IntList(L.item * L.item, null);
        IntList ptr = res;
        L = L.next;
        while (L != null) {
            ptr.next = new IntList(L.item * L.item, null);
            L = L.next;
            ptr = ptr.next;
        }
        return res;
    }

    /** Returns a list equal to L with all elements squared. Non-destructive.
     *
     * @param L list to non-destructively square.
     * @return the squared list.
     */
    public static IntList squareListRecursive(IntList L) {
        if (L == null) {
            return null;
        }
        return new IntList(L.item * L.item, squareListRecursive(L.next));
    }

    /**
     * Returns a new IntList consisting of A followed by B,
     * destructively.
     *
     * @param A list to be on the front of the new list.
     * @param B list to be on the back of the new list.
     * @return new list with A followed by B.
     */
    public static IntList dcatenate(IntList A, IntList B) {
        IntList pointer = A;

        if (B == null) {
            return A;
        }

        if (A == null) {
            A = B;
            return A;
        }

        while (pointer.next != null) {
            pointer = pointer.next;
        }
        pointer.next = B;
        return A;
    }

    /**
     * Returns a new IntList consisting of A followed by B,
     * non-destructively.
     *
     * @param A list to be on the front of the new list.
     * @param B list to be on the back of the new list.
     * @return new list with A followed by B.
     */
     public static IntList catenate(IntList A, IntList B) {
        if (A == null) {
            return B; }

        if (B == null) {
            return A; }

        IntList result = new IntList(A.item, null);
        IntList pointer = result;

        A = A.next;
        while (A != null) {
            pointer.next = new IntList(A.item, null);
            A = A.next;
            pointer = pointer.next;
        }
        while (B != null) {
            pointer.next = new IntList(B.item, null);
            B = B.next;
            pointer = pointer.next;
        }
        return result;
     }
}