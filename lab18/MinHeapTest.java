import org.junit.Test;
import static org.junit.Assert.*;

public class MinHeapTest {

    @Test
    public void test1() {
        MinHeap m = new MinHeap();
        System.out.println(m.contains(5));
        m.insert(5);
        m.insert(2);
        m.insert(4);
        m.insert(1);
        System.out.println(m.removeMin());
        assertEquals(3, m.size());
    }
}
