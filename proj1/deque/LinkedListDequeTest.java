package deque;

import org.junit.Test;
import static org.junit.Assert.*;


/** Performs some basic linked list deque tests. */
public class LinkedListDequeTest<T> {

    /** You MUST use the variable below for all of your tests. If you test
     * using a local variable, and not this static variable below, the
     * autograder will not grade that test. If you would like to test
     * LinkedListDeques with types other than Integer (and you should),
     * you can define a new local variable. However, the autograder will
     * not grade that test. */

    public static Deque<Integer> lld = new LinkedListDeque<Integer>();

    @Test
    /** Adds a few things to the list, checks that isEmpty() is correct.
     * This is one simple test to remind you how junit tests work. You
     * should write more tests of your own.
     *
     * && is the "and" operation. */
    public void addIsEmptySizeTest() {
        assertTrue("A newly initialized LLDeque should be empty", lld.isEmpty());
        lld.addFirst(0);

        assertFalse("lld should now contain 1 item", lld.isEmpty());

        // Reset the linked list deque at the END of the test.
        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void test() {
        lld.addFirst(0);
        lld.addFirst(1);
        lld.addFirst(2);
        lld.removeLast();
        assertEquals(2, lld.size());
        assertEquals((Integer) 1, lld.get(1));
        assertEquals((Integer) 2, lld.get(0));
        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void test1() {
        lld.addLast(0);
        lld.addLast(1);
        lld.addLast(2);
        lld.removeFirst();
        lld.printDeque();
        assertEquals(2, lld.size());
        assertEquals((Integer) 1, lld.get(0));
        assertEquals((Integer) 2, lld.get(1));
        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void test2() {
        lld.addLast(0);
        lld.get(0);
        lld.removeFirst();
        assertEquals(0, lld.size());
        assertEquals((Integer) null, lld.get(0));
        lld = new LinkedListDeque<Integer>();
    }


    @Test
    public void addFirstTest() {
        lld.addFirst(4);
        lld.addFirst(3);
        lld.addFirst(2);
        lld.addFirst(1);
        assertEquals(4, lld.size());
        assertEquals((Integer) 1, lld.get(0));
        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void addFirstTest1() {
        lld.addFirst(1);
        lld.addFirst(0);
        assertEquals(2, lld.size());
        assertEquals((Integer) 0, lld.get(0));
        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void addFirstTest2() {
        lld.addFirst(15);
        assertEquals(1, lld.size());
        assertEquals((Integer) 15, lld.get(0));
        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void addFirstTest3() {
        lld.addFirst(14);
        lld.addFirst(12);
        lld.addFirst(10);
        lld.addFirst(8);
        lld.addFirst(6);
        lld.addFirst(4);
        lld.addFirst(2);
        assertEquals(7, lld.size());
        assertEquals((Integer) 2, lld.get(0));
        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void addLastTest() {
        lld.addFirst(9);
        lld.addFirst(7);
        lld.addFirst(5);
        lld.addLast(1);
        assertEquals(4, lld.size());
        assertEquals((Integer) 1, lld.get(3));
        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void addLastTest1() {
        lld.addFirst(18);
        lld.addLast(0);
        assertEquals(2, lld.size());
        assertEquals((Integer) 0, lld.get(1));
        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void addLastTest2() {
        lld.addLast(15);
        assertEquals(1, lld.size());
        assertEquals((Integer) 15, lld.get(0));
        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void addLastTest3() {
        lld.addFirst(11);
        lld.addFirst(9);
        lld.addFirst(7);
        lld.addFirst(5);
        lld.addFirst(3);
        lld.addFirst(1);
        lld.addLast(13);
        assertEquals(7, lld.size());
        assertEquals((Integer) 13, lld.get(6));
        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void removeFirstTest() {
        lld.addFirst(4);
        lld.addFirst(3);
        lld.addFirst(2);
        int val = lld.removeFirst();
        assertEquals(2, val);
        assertEquals(2, lld.size());
        assertEquals((Integer) 3, lld.get(0));
        assertEquals((Integer) 4, lld.get(1));
        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void removeFirstTestEmpty() {
        assertNull(lld.removeFirst());
        assertEquals(0, lld.size());
        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void removeFirstTest1() {
        lld.addFirst(1);
        T val1 = (T) lld.removeFirst();
        assertEquals(1, val1);
        assertEquals(0, lld.size());
        assertEquals(null, lld.get(0));
        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void removeFirstTest2() {
        int val2 = lld.removeFirst();
        assertEquals(null, val2);
        assertEquals(0, lld.size());
        assertEquals(null, lld.get(0));
        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void removeFirstTest3() {
        lld.addFirst(10);
        lld.addFirst(8);
        lld.addFirst(6);
        lld.addFirst(4);
        lld.addFirst(2);
        lld.addFirst(0);
        int val3 = lld.removeFirst();
        assertEquals(0, val3);
        assertEquals(5, lld.size());
        assertEquals((Integer) 2, lld.get(0));
        assertEquals((Integer) 10, lld.get(4));
        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void removeLastTest() {
        lld.addFirst(4);
        lld.addFirst(3);
        lld.addFirst(2);
        int val = lld.removeLast();
        assertEquals(4, val);
        assertEquals(2, lld.size());
        assertEquals((Integer) 2, lld.get(0));
        assertEquals((Integer) 3, lld.get(1));
        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void removeLastTest1() {
        lld.addFirst(1);
        int val1 = lld.removeLast();
        assertEquals(1, val1);
        assertEquals(0, lld.size());
        assertEquals(null, lld.get(0));
        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void removeLastTest2() {
        int val2 = lld.removeLast();
        assertEquals(null, val2);
        assertEquals(0, lld.size());
        assertEquals(null, lld.get(0));
        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void removeLastTest3() {
        lld.addFirst(10);
        lld.addFirst(8);
        lld.addFirst(6);
        lld.addFirst(4);
        lld.addFirst(2);
        lld.addFirst(0);
        int val3 = lld.removeLast();
        assertEquals(10, val3);
        assertEquals(5, lld.size());
        assertEquals((Integer) 0, lld.get(0));
        assertEquals((Integer) 8, lld.get(4));
        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void getTest() {
        lld.addFirst(4);
        lld.addFirst(3);
        lld.addFirst(2);
        int val = lld.get(1);
        assertEquals(2, val);
        assertEquals(3, lld.size());
        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void getTest1() {
        lld.addFirst(1);
        int val1 = lld.get(0);
        assertEquals(1, val1);
        assertEquals(1, lld.size());
        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void getTest2() {
        int val2 = lld.get(3);
        assertEquals(null, val2);
        assertEquals(0, lld.size());
        lld = new LinkedListDeque<Integer>();
    }

    @Test
    public void getTest3() {
        lld.addFirst(10);
        lld.addFirst(8);
        lld.addFirst(6);
        lld.addFirst(4);
        lld.addFirst(2);
        lld.addFirst(0);
        int val3 = lld.get(4);
        assertEquals(8, val3);
        assertEquals(6, lld.size());
        lld = new LinkedListDeque<Integer>();
    }


    @Test
    public void equalsTest() {

    }

}
