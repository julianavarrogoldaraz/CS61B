import org.junit.Test;

import java.util.ArrayList;

public class Testing {

    @Test
    public void test1() {
        DLList<Integer> lst = new DLList<>();
        lst.addLast(1);
        lst.addLast(5);
        lst.addLast(0);
        lst.addLast(4);
        lst.addLast(2);
        System.out.println(lst.insertionSort());
    }

    @Test
    public void test2() {
        DLList<Integer> lst = new DLList<>();
        lst.addLast(1);
        lst.addLast(5);
        lst.addLast(0);
        lst.addLast(4);
        lst.addLast(2);
        System.out.println(lst.mergeSort());
    }

}
