import org.junit.Test;
import static org.junit.Assert.*;

public class MinHeapPQTest {

    @Test
    public void test1() {
        MinHeapPQ heap = new MinHeapPQ<>();
        heap.insert("d", 5);
        heap.insert("c", 4);
        heap.insert("b", 3);
        heap.insert("a", 2);
        System.out.println(heap.toString());
        heap.changePriority("d", 1);
        System.out.println(heap.peek());
        heap.poll();
        System.out.println(heap.peek());
        heap.poll();
        System.out.println(heap.peek());
        heap.poll();
        System.out.println(heap.peek());
    }
}
