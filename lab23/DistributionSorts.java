import java.util.*;

public class DistributionSorts {

    /* Destructively sorts ARR using counting sort. Assumes that ARR contains
       only 0, 1, ..., 9. */
    public static void countingSort(int[] arr) {
        int numValues = 10;
        int counts[] = new int[numValues];
        int starts[] = new int[numValues];
        int result[] = new int[arr.length];

        for (int i = 0; i < arr.length; i++){
            counts[arr[i]] += 1;
        }

        int total = 0;
        for (int i = 1; i < counts.length; i++) {
            if (counts[i] != 0) {
                starts[i] = total;
                total += counts[1];
            }
        }

        int index = 0;
        for (int i = 0; i < counts.length; i++) {
            for (int k = 0; k < counts[i]; k++) {
                result[index] = i;
                starts[i] += 1;
                index += 1;
            }
        }

        for (int i = 0; i < result.length; i++) {
            arr[i] = result[i];
        }
    }

    /* Destructively sorts ARR using LSD radix sort. */
    public static void lsdRadixSort(int[] arr) {
        int maxDigit = mostDigitsIn(arr);
        for (int d = 0; d < maxDigit; d++) {
            countingSortOnDigit(arr, d);
        }
    }

    /* A helper method for radix sort. Modifies ARR to be sorted according to
       DIGIT-th digit. When DIGIT is equal to 0, sort the numbers by the
       rightmost digit of each number. */
    private static void countingSortOnDigit(int[] arr, int digit) {
        int digitCounts[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int startPts[] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
        int result[] = new int[arr.length];

        for (int i = 0; i < arr.length; i++) {
            int temp = arr[i];
            for (int k = 0; k < digit; k++) {
                temp = temp / 10;
            }
            int d = temp % 10;
            digitCounts[d] += 1;
        }

        int total = 0;
        for (int i = 0; i < digitCounts.length; i++) {
            if (digitCounts[i] != 0) {
                startPts[i] = total;
                total += digitCounts[i];
            }
        }

        int index = 0;
        for (int i = 0; i < digitCounts.length; i++) {
            for (int k = 0; k < digitCounts[i]; k++) {
                result[index] = i;
                startPts[i] += 1;
                index += 1;
            }
        }

//        int in = 0;
//        for (int k = 0; k < digitCounts.length; k++) {
//            for (int j = 0; j < numbers.get(k).size(); j++) {
//                result[in] = numbers.get(k).get(j);
//                in += 1;
//            }
//        }

        for (int i = 0; i < result.length; i++) {
            arr[i] = result[i];
        }
    }

    /* Returns the largest number of digits that any integer in ARR has. */
    private static int mostDigitsIn(int[] arr) {
        int maxDigitsSoFar = 0;
        for (int num : arr) {
            int numDigits = (int) (Math.log10(num) + 1);
            if (numDigits > maxDigitsSoFar) {
                maxDigitsSoFar = numDigits;
            }
        }
        return maxDigitsSoFar;
    }

    /* Returns a random integer between 0 and 9999. */
    private static int randomInt() {
        return (int) (10000 * Math.random());
    }

    /* Returns a random integer between 0 and 9. */
    private static int randomDigit() {
        return (int) (10 * Math.random());
    }

    private static void runCountingSort(int len) {
        int[] arr1 = new int[len];
        for (int i = 0; i < arr1.length; i++) {
            arr1[i] = randomDigit();
        }
        System.out.println("Original array: " + Arrays.toString(arr1));
        countingSort(arr1);
        if (arr1 != null) {
            System.out.println("Should be sorted: " + Arrays.toString(arr1));
        }
    }

    private static void runLSDRadixSort(int len) {
        int[] arr2 = new int[len];
        for (int i = 0; i < arr2.length; i++) {
            arr2[i] = randomDigit();
        }
        System.out.println("Original array: " + Arrays.toString(arr2));
        lsdRadixSort(arr2);
        System.out.println("Should be sorted: " + Arrays.toString(arr2));

    }

    public static void main(String[] args) {
        runCountingSort(20);
        runLSDRadixSort(3);
        runLSDRadixSort(30);
    }
}