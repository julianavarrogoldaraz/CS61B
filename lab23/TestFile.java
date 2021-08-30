import org.junit.Test;

import java.util.Random;

public class TestFile {

    @Test
    public void test1() {
        int[] arr = new int[1000000];
        for (int i = 0; i < 1000000; i++) {
            Random r = new Random();
            int rand = r.nextInt(10);
            arr[i] = rand;
        }
        DistributionSorts.countingSort(arr);
    }
}
