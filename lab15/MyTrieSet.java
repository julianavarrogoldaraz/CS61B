import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MyTrieSet {
    private Node root;

    public MyTrieSet() {
        root = new Node();
    }

    private static class Node {
        private char character;
        private boolean isKey;
        private HashMap<Character, Node> map;

        private Node() {
            this.map = new HashMap<Character, Node>();
        }

        private Node(char character, boolean isKey) {
            this.character = character;
            this.isKey = false;
            this.map = new HashMap<Character, Node>();
        }
    }

    public void clear() {
        root = new Node();
    }

    public boolean contains(String key) {
        Node curr = root;
        while (key.length() != 0){
            if (curr == null) {
                return false;
            } else {
                curr = curr.map.get(key.charAt(0));
                key = key.substring(1);
            }
        }
        return curr.isKey;
    }

    public void add(String key) {
        if (key == null || key.length() < 1) {
            return;
        }
        Node curr = root;
        for (int i = 0, n = key.length(); i < n; i++) {
            char c = key.charAt(i);
            if (!curr.map.containsKey(c)) {
                curr.map.put(c, new Node(c, false));
            }
            curr = curr.map.get(c);
        }
        curr.isKey = true;
    }

    public List<String> keysWithPrefix(String prefix) {
        Node curr = root;
        String orig = prefix;
        ArrayList<String> list = new ArrayList<>();
        while (prefix.length() != 0) {
            if (curr == null) {
                return list;
            } else {
                curr = curr.map.get(prefix.charAt(0));
                prefix = prefix.substring(1);
            }
        }
        System.out.println(list);
        for (Node n : curr.map.values()) {
            helper(orig, n, list);
        }
        return list;
    }

    private void helper(String soFar, Node curr, ArrayList<String> list) {
        if (curr == null) {
            list.add(soFar);
            return;
        } else if (curr.isKey) {
            list.add(soFar + curr.character);
        }
        for (Node n : curr.map.values()) {
            System.out.println(list);
            helper(soFar + curr.character, n, list);
        }
    }

    public String longestPrefixOf(String key) {
        throw new UnsupportedOperationException();
    }

}


