import java.lang.Object;
import java.util.LinkedList;

public class SimpleNameMap {

    private LinkedList<Entry>[] names;
    private int size;
    private int length;
    private double maxLoadFactor = 0.75;

    public SimpleNameMap() {
        this.size = 0;
        this.length = 26;
        this.names = (LinkedList<Entry>[]) new LinkedList[length];
    }

    /* Returns the number of items contained in this map. */
    public int size() {
        return size;
    }

    /* Returns true if the map contains the KEY. */
    public boolean containsKey(String key) {
        int hash = hashCode(key);
        if (names[hash] == null) {
            return false;
        }
        for (int i = 0; i < names[hash].size(); i ++) {
            if (names[hash].get(i).getKey().equals(key)) {
                return true;
            }
        }
        return false;
    }

    /* Returns the value for the specified KEY. If KEY is not found, return
       null. */
    public String get(String key) {
        int hash = hashCode(key);
        if (names[hash] == null) {
            return null;
        }
        for (int i = 0; i < names[hash].size(); i ++) {
            if (names[hash].get(i).getKey().equals(key)) {
                return names[hash].get(i).getValue();
            }
        }
        return null;
    }

    /* Puts a (KEY, VALUE) pair into this map. If the KEY already exists in the
       SimpleNameMap, replace the current corresponding value with VALUE. */
    public void put(String key, String value) {
        int hash = hashCode(key);
        if (names[hash] == null) {
            if (resizeNeeded()) {
                resize();
            }
            names[hash] = new LinkedList<Entry>();
            names[hash].add(new Entry(key, value));
            this.size += 1;
            return;
        }
        for (int i = 0; i < names[hash].size(); i++) {
            if (names[hash].get(i).getKey().equals(key)) {
                names[hash].get(i).setValue(value);
                return;
            }
        }
        if (resizeNeeded()) {
            resize();
        }
        names[hash].addLast(new Entry(key, value));
        this.size += 1;
    }

    /* Removes a single entry, KEY, from this table and return the VALUE if
       successful or NULL otherwise. */
    public String remove(String key) {
        int hash = hashCode(key);
        if (names[hash] == null) {
            return null;
        }
        for (int i = 0; i < names[hash].size(); i ++) {
            if (names[hash].get(i).getKey().equals(key)) {
                String val = names[hash].get(i).getValue();
                names[hash].remove(i);
                this.size -= 1;
                return val;
            }
        }
        return null;
    }

    private int hashCode(String key) {
        return java.lang.Math.floorMod(key.hashCode(), this.length);
    }

    private double loadFactor() {
        return (double) (this.size + 1) / this.length;
    }

    private boolean resizeNeeded() {
        if (loadFactor() > this.maxLoadFactor) {
            return true;
        }
        return false;
    }

    private void resize() {
        length = length * 2;
        SimpleNameMap resized = new SimpleNameMap();
        for (int i = 0; i < names.length; i ++) { //for each bucket
            if (names[i] == null) {
                continue;
            } else {
                for (int k = 0; k < names[i].size(); k++) { //for each node in the LL
                    resized.put(names[i].get(k).getKey(), names[i].get(k).getValue());
                }
            }
        }
    }

    private static class Entry {

        private String key;
        private String value;

        Entry(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return this.key;
        }

        public String getValue() {
            return this.value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        /* Returns true if this key matches with the OTHER's key. */
        public boolean keyEquals(Entry other) {
            return key.equals(other.key);
        }

        /* Returns true if both the KEY and the VALUE match. */
        @Override
        public boolean equals(Object other) {
            return (other instanceof Entry
                    && key.equals(((Entry) other).key)
                    && value.equals(((Entry) other).value));
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }
    }

}