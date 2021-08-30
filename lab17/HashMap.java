import java.lang.Object;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class HashMap<K, V> /*implements Map61BL<K, V>*/ implements Iterable<K> {

    private LinkedList<Entry<K, V>>[] names;
    private int size;
    private int initialCapacity;
    private double maxLoadFactor;
    private ArrayList<K> keys = new ArrayList<>();


    public HashMap() {
        this.size = 0;
        this.initialCapacity = 16;
        this.maxLoadFactor = 0.75;
        names = (LinkedList<Entry<K, V>>[]) new LinkedList[this.initialCapacity];
    }

    /* Creates a new hash map with an array of size INITIALCAPACITY and a maximum load factor of 0.75. */
    HashMap(int initialCapacity) {
        this.size = 0;
        this.initialCapacity = initialCapacity;
        this.maxLoadFactor = 0.75;
        names = (LinkedList<Entry<K, V>>[]) new LinkedList[initialCapacity];
    }

    /* Creates a new hash map with INITIALCAPACITY and LOADFACTOR. */
    HashMap(int initialCapacity, double loadFactor) {
        this.size = 0;
        this.initialCapacity = initialCapacity;
        this.maxLoadFactor = loadFactor;
        names = (LinkedList<Entry<K, V>>[]) new LinkedList[initialCapacity];
    }

    /* Returns the number of items contained in this map. */
    public int size() {
        return size;
    }

    /* Returns true if the map contains the KEY. */
    public boolean containsKey(K key) {
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
    public V get(K key) {
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
    public void put(K key, V value) {
        int hash = hashCode(key);
        if (names[hash] == null) {
            if (resizeNeeded()) {
                resize();
            }
            names[hash] = new LinkedList<Entry<K, V>>();
            names[hash].add(new Entry<K, V>(key, value));
            keys.add(key);
            this.size += 1;
            return;
        }
        for (int i = 0; i < names[hash].size(); i++) {
            if (names[hash].get(i).getKey().equals(key)) {
                names[hash].get(i).setValue(value);
                keys.add(key);
                return;
            }
        }
        if (resizeNeeded()) {
            resize();
        }
        names[hash].addLast(new Entry<K, V>(key, value));
        keys.add(key);
        this.size += 1;
    }

    /* Removes a single entry, KEY, from this table and return the VALUE if
       successful or NULL otherwise. */
    public V remove(K key) {
        int hash = hashCode(key);
        if (names[hash] == null) {
            return null;
        }
        for (int i = 0; i < names[hash].size(); i ++) {
            if (names[hash].get(i).getKey().equals(key)) {
                V val = names[hash].get(i).getValue();
                names[hash].remove(i);
                keys.remove(key);
                this.size -= 1;
                return val;
            }
        }
        return null;
    }

    private int hashCode(K key) {
        return java.lang.Math.floorMod(key.hashCode(), initialCapacity);
    }

    private double loadFactor() {
        return (double) (this.size + 1) / this.initialCapacity;
    }

    private boolean resizeNeeded() {
        if (loadFactor() > this.maxLoadFactor) {
            return true;
        }
        return false;
    }

    private void resize() {
        this.initialCapacity = this.initialCapacity * 2;
        HashMap<K, V> resized = new HashMap();
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

    public void clear() {
        for (int i = 0; i < names.length; i++) {
            if (names[i] != null) {
                names[i].clear();
            }
        }
        size = 0;
    }

    public boolean remove(K key, V value) {
        int hash = hashCode(key);
        if (names[hash] == null) {
            return false;
        }
        for (int i = 0; i < names[hash].size(); i ++) {
            if (names[hash].get(i).getKey().equals(key)) {
                V val = names[hash].get(i).getValue();
                names[hash].remove(i);
                keys.remove(key);
                return true;
            }
        }
        return false;
    }

    public Iterator<K> iterator() {
        return keys.listIterator();
//        throw new UnsupportedOperationException();
    }


    public int capacity() {
        return initialCapacity;
    }

//    private class MapIterator<K> implements java.util.Iterator<K> {
//        curr = keys.getHead();
//
//        public boolean hasNext() {
//            return index < keys.size();
//        }
//
//        public Object next() {
//            return keys[index + 1];
//        }
//    }

    private static class Entry<K, V> {

        private K key;
        private V value;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            return this.value;
        }

        public void setValue(V value) {
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
