public class UnionFind {

    int[] arr;

    /* Creates a UnionFind data structure holding N items. Initially, all
       items are in disjoint sets. */
    public UnionFind(int N) {
        arr = new int[N];
        for (int i = 0; i < N; i ++) {
            arr[i] = -1;
        }
    }

    /* Returns the size of the set V belongs to. */
    public int sizeOf(int v) {
        return -(arr[find(v)]);
    }

    /* Returns the parent of V. If V is the root of a tree, returns the
       negative size of the tree for which V is the root. */
    public int parent(int v) {
        return arr[v];
    }

    /* Returns true if nodes V1 and V2 are connected. */
    public boolean connected(int v1, int v2) {
        if (find(v1) == find(v2)) {
            return true;
        } else {
            return false;
        }
    }

    /* Returns the root of the set V belongs to. Path-compression is employed
       function, throw an IllegalArgumentException. */
    public int find(int v) {
        if (v < 0 || v > arr.length) {
            throw new IllegalArgumentException();
        }
        if (arr[v] < 0) {
            return v;
        } else {
            arr[v] = find(arr[v]);
            return arr[v];
        }
    }

    /* Connects two items V1 and V2 together by connecting their respective
       sets. V1 and V2 can be any element, and a union-by-size heuristic is
       used. If the sizes of the sets are equal, tie break by connecting V1's
       root to V2's root. Union-ing a item with itself or items that are
       already connected should not change the structure. */
    public void union(int v1, int v2) {
        if (connected(v1, v2) || v1 == v2) {
            return;
        }
        if (sizeOf(find(v1)) == sizeOf(find(v2))) {
            arr[find(v2)] -= sizeOf(find(v1));
            arr[find(v1)] = find(v2);
        } else if (sizeOf(find(v1)) > sizeOf(find(v2))) {
            arr[find(v1)] -= sizeOf(find(v2));
            arr[find(v2)] = find(v1);
        } else {
            arr[find(v2)] -= sizeOf(find(v1));
            arr[find(v1)] = find(v2);
        }

    }
}
