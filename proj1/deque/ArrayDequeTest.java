package deque;

import org.junit.Test;

import static org.junit.Assert.*;

/* Performs some basic array deque tests. */
public class ArrayDequeTest<T> {

    /** You MUST use the variable below for all of your tests. If you test
     * using a local variable, and not this static variable below, the
     * autograder will not grade that test. If you would like to test
     * ArrayDeques with types other than Integer (and you should),
     * you can define a new local variable. However, the autograder will
     * not grade that test. */

    public static Deque<Integer> ad = new ArrayDeque<Integer>();

    @Test
    public void addIsEmptySizeTest() {
        assertTrue("A newly initialized ArrayDeque should be empty", ad.isEmpty());
        ad.addFirst(0);
        assertFalse("ad should now contain 1 item", ad.isEmpty());
        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void test() {
        ad.addFirst(0);
        ad.printDeque();
        ad.addLast(1);
        ad.printDeque();
        ad.get(1);
        ad.printDeque();
        assertEquals(2, ad.size());
        assertEquals((Integer) 1, ad.get(1));
        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void addFirstTest() {
        ad.addFirst(4);
        ad.addFirst(3);
        ad.addFirst(2);
        ad.addFirst(1);
        assertEquals(4, ad.size());
//        assertEquals((Integer)1, ad.get(0));
        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void addFirstTest1() {
        ad.addFirst(1);
        ad.addFirst(0);
        ad.printDeque();
        assertEquals(2, ad.size());
//        assertEquals((Integer) 0, ad.get(0));
        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void addFirstTest2() {
        ad.addFirst(15);
        assertEquals(1, ad.size());
//        assertEquals((Integer)15, ad.get(0));
        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void addFirstTest3() {
        ad.addFirst(14);
        ad.addFirst(12);
        ad.addFirst(10);
        ad.addFirst(8);
        ad.addFirst(6);
        ad.addFirst(4);
        assertEquals(6, ad.size());
        assertEquals((Integer) 4, ad.get(0));
        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void addFirstTestv2() {
        ad.addFirst(14);
        ad.printDeque();
        ad.addFirst(12);
        ad.printDeque();
        ad.addFirst(10);
        ad.printDeque();
        ad.addFirst(8);
        ad.addFirst(6);
        ad.addFirst(4);
        ad.addFirst(2);
        ad.addFirst(0);
        ad.printDeque();
        ad.addFirst(22);
        ad.printDeque();
        ad.addFirst(44);
        ad.printDeque();
        assertEquals(10, ad.size());
        assertEquals((Integer) 44, ad.get(0));
        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void addLastTest() {
        ad.addFirst(9);
        ad.addFirst(7);
        ad.addFirst(5);
        ad.addLast(1);
        assertEquals(4, ad.size());
        assertEquals((Integer) 1, ad.get(3));
        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void addLastTest1() {
        ad.addFirst(18);
        ad.addLast(0);
        assertEquals(2, ad.size());
        assertEquals((Integer) 0, ad.get(1));
        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void addLastTest2() {
        ad.addLast(15);
        assertEquals(1, ad.size());
        assertEquals((Integer) 15, ad.get(0));
        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void addLastTest3() {
        ad.addFirst(11);
        ad.addFirst(9);
        ad.addFirst(7);
        ad.addFirst(5);
        ad.addFirst(3);
        ad.addFirst(1);
        ad.addLast(13);
        assertEquals(7, ad.size());
        assertEquals((Integer) 13, ad.get(6));
        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void removeFirstTest() {
        ad.addFirst(4);
        ad.addFirst(3);
        ad.addFirst(2);
        int val = ad.removeFirst();
        assertEquals(2, val);
        assertEquals(2, ad.size());
        assertEquals((Integer) 3, ad.get(0));
        assertEquals((Integer) 4, ad.get(1));
        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void removeFirstTest1() {
        ad.addFirst(1);
        int val1 = ad.removeFirst();
        assertEquals(1, val1);
        assertEquals(0, ad.size());
        assertEquals(null, ad.get(0));
        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void removeFirstTest2() {
        assertNull(ad.removeFirst());
        assertEquals(0, ad.size());
        assertEquals(null, ad.get(0));
        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void removeFirstTest3() {
        ad.addFirst(10);
        ad.addFirst(8);
        ad.addFirst(6);
        ad.addFirst(4);
        ad.addFirst(2);
        ad.addFirst(0);
        int val3 = ad.removeFirst();
        assertEquals(0, val3);
        assertEquals(5, ad.size());
        assertEquals((Integer) 2, ad.get(0));
        assertEquals((Integer) 10, ad.get(4));
        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void removeLastTest() {
        ad.addFirst(4);
        ad.addFirst(3);
        ad.addFirst(2);
        int val = ad.removeLast();
        assertEquals(4, val);
        assertEquals(2, ad.size());
        assertEquals((Integer) 2, ad.get(0));
        assertEquals((Integer) 3, ad.get(1));
        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void removeLastTest1() {
        ad.addFirst(1);
        int val1 = ad.removeLast();
        assertEquals(1, val1);
        assertEquals(0, ad.size());
        assertEquals(null, ad.get(0));
        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void removeLastTest2() {
        assertNull(ad.removeLast());
        assertEquals(0, ad.size());
        assertEquals(null, ad.get(0));
        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void removeLastTest3() {
        ad.addFirst(10);
        ad.addFirst(8);
        ad.addFirst(6);
        ad.addFirst(4);
        ad.addFirst(2);
        ad.addFirst(0);
        int val3 = ad.removeLast();
        assertEquals(10, val3);
        assertEquals(5, ad.size());
        assertEquals((Integer) 0, ad.get(0));
        assertEquals((Integer) 8, ad.get(4));
        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void getTest() {
        ad.addFirst(4);
        ad.addFirst(3);
        ad.addFirst(2);
        int val = ad.get(1);
        assertEquals(3, val);
        assertEquals(3, ad.size());
        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void getTest1() {
        ad.addFirst(1);
        int val1 = ad.get(0);
        assertEquals(1, val1);
        assertEquals(1, ad.size());
        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void getTest2() {
        assertNull(ad.get(3));
        assertEquals(0, ad.size());
        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void getTest3() {
        ad.addFirst(10);
        ad.addFirst(8);
        ad.addFirst(6);
        ad.addFirst(4);
        ad.addFirst(2);
        ad.addFirst(0);
        int val3 = ad.get(4);
        assertEquals(8, val3);
        assertEquals(6, ad.size());
        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void getTest4() {
        for (int i = 0; i < 390; i += 1) {
            ad.addLast(i);
        }
        for (int i = 0; i < 390; i += 1) {
            assertEquals((Integer) i, ad.get(i));
        }
        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void getTest5() {
        ad.addLast(0);
        ad.addFirst(1);
        assertEquals((Integer) 0, ad.removeLast());
        ad.addFirst(3);
        assertEquals((Integer) 1, ad.get(1));
        ad.addFirst(5);
        assertEquals((Integer) 1, ad.removeLast());
        ad.addLast(7);
        ad.addLast(8);
        ad.addLast(9);
        ad.addFirst(10);
        ad.addLast(11);
        ad.addFirst(12);
        ad.addLast(13);
        assertEquals((Integer) 3, ad.get(3));
        ad = new ArrayDeque<Integer>();
    }

    @Test
    public void testResizeDown() {
        for (int i = 0; i < 70; i += 1) {
            ad.addLast(i);
        }
        for (int i = 0; i < 68; i += 1) {
            ad.removeLast();
        }
        ad = new ArrayDeque<Integer>();
    }
}
