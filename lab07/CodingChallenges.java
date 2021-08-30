import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.HashSet;
import java.util.Set;

public class CodingChallenges {

    /**
     * Return the missing number from an array of length N containing all the
     * values from 0 to N except for one missing number.
     */
    public static int missingNumber(int[] values) {
        List<Integer> list = new ArrayList<>();

        for (int index = 0; index < values.length; index += 1) {
            list.add(values[index]); }
        for (int num = 0; num < values.length; num += 1) {
            if (!list.contains(num)) {
                return num;
            }
        }
        return values.length;
    }

    /**
     * Returns true if and only if two integers in the array sum up to n.
     * Assume all values in the array are unique.
     */
    public static boolean sumTo(int[] values, int n) {
        Set<Integer> set = new HashSet<>();
        for (int index = 0; index < values.length; index += 1) {
            set.add(values[index]);
            if (set.contains(n - values[index])) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns true if and only if s1 is a permutation of s2. s1 is a
     * permutation of s2 if it has the same number of each character as s2.
     */
    public static boolean isPermutation(String s1, String s2) {
        Map<Character, Integer> mapS1 = new HashMap<>();

        for (int index = 0; index < s1.length(); index += 1) {
            if (!mapS1.containsKey(s1.charAt(index))) {
                mapS1.put(s1.charAt(index), 1);
            } else {
                mapS1.put(s1.charAt(index), mapS1.get(s1.charAt(index)) + 1);
            }
        }
        for (int index = 0; index < s2.length(); index += 1) {
            if (!mapS1.containsKey(s2.charAt(index))) {
                return false;
            } else {
                mapS1.put(s2.charAt(index), mapS1.get(s2.charAt(index)) - 1);
            }
        }
        for (int value: mapS1.values()) {
            if (value != 0) {
                return false;
            }
        }
        return true;
    }

}