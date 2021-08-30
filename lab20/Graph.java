
import java.lang.reflect.Array;
import java.util.*;
import java.util.PriorityQueue;

public class Graph implements Iterable<Integer> {

    private LinkedList<Edge>[] adjLists;
    private int vertexCount;

    /* Initializes a graph with NUMVERTICES vertices and no Edges. */
    public Graph(int numVertices) {
        adjLists = (LinkedList<Edge>[]) new LinkedList[numVertices];
        for (int k = 0; k < numVertices; k++) {
            adjLists[k] = new LinkedList<Edge>();
        }
        vertexCount = numVertices;
    }

    /* Adds a directed Edge (V1, V2) to the graph. That is, adds an edge
       in ONE directions, from v1 to v2. */
    public void addEdge(int v1, int v2) {
        addEdge(v1, v2, 0);
    }

    /* Adds an undirected Edge (V1, V2) to the graph. That is, adds an edge
       in BOTH directions, from v1 to v2 and from v2 to v1. */
    public void addUndirectedEdge(int v1, int v2) {
        addUndirectedEdge(v1, v2, 0);
    }

    /* Adds a directed Edge (V1, V2) to the graph with weight WEIGHT. If the
       Edge already exists, replaces the current Edge with a new Edge with
       weight WEIGHT. */
    public void addEdge(int v1, int v2, int weight) {
        for (int i = 0; i < adjLists[v1].size(); i++) {
            if (adjLists[v1].get(i).to == v1) {
                adjLists[v1].set(i, new Edge(v1, v2, weight));
            }
        }
        adjLists[v1].addLast(new Edge(v1, v2, weight));
    }

    /* Adds an undirected Edge (V1, V2) to the graph with weight WEIGHT. If the
       Edge already exists, replaces the current Edge with a new Edge with
       weight WEIGHT. */
    public void addUndirectedEdge(int v1, int v2, int weight) {
        for (int i = 0; i < adjLists[v1].size(); i++) {
            if (adjLists[v1].get(i).to == v1) {
                adjLists[v1].set(i, new Edge(v1, v2, weight));
            }
        }
        adjLists[v1].addLast(new Edge(v1, v2, weight));

        for (int i = 0; i < adjLists[v2].size(); i++) {
            if (adjLists[v2].get(i).to == v2) {
                adjLists[v2].set(i, new Edge(v2, v1, weight));
            }
        }
        adjLists[v2].addLast(new Edge(v2, v1, weight));
    }

    /* Returns true if there exists an Edge from vertex FROM to vertex TO.
       Returns false otherwise. */
    public boolean isAdjacent(int from, int to) {
        for (int i = 0; i < adjLists[from].size(); i++) {
            if (adjLists[from].get(i).to == to) {
                return true;
            }
        }
        return false;
    }

    /* Returns a list of all the vertices u such that the Edge (V, u)
       exists in the graph. */
    public List<Integer> neighbors(int v) {
        ArrayList<Integer> lst = new ArrayList<>();
        for (int i = 0; i < adjLists[v].size(); i++) {
            lst.add(adjLists[v].get(i).to);
        }
        return lst;
    }

    /* Returns the number of incoming Edges for vertex V. */
    public int inDegree(int v) {
        int counter = 0;
        for (int i = 0; i < adjLists.length; i++) {
            for (int k = 0; k < adjLists[i].size(); k++) {
                if (adjLists[i].get(k).to == v) {
                    counter += 1;
                }
            }
        }
        return counter;
    }

    /* Implements Dijkstra's algorithm. */
    public List<Integer> shortestPath(int start, int stop) {

        PriorityQueue<Node> fringe = new PriorityQueue<>(vertexCount, new weightComparator());
        Integer[] array = new Integer[vertexCount];
        for (int i = 0; i < vertexCount; i++) {
            if (i == 0) array[i] = 0;
            array[i] = Integer.MAX_VALUE;
        }
        ArrayList<Integer> visited = new ArrayList<>();

        // add start to fringe
        Node startNode = new Node(start, 0, null);
        visited.add(start);
        fringe.add(startNode);
        Node last = null;
        boolean b = false;

        while (!fringe.isEmpty()) {
            Node toExplore = fringe.peek();
            int current = toExplore.index;
            if (current == stop) {
                last = toExplore;
                break;
            }
            int soFar = toExplore.cumWeight;
            List<Integer> neighbors = neighbors(current);
            fringe.remove(toExplore);

            for (int w : neighbors) {
                if (visited.contains(w)) {
                    continue;
                }
//                if (array[w] != Integer.MAX_VALUE) {
//                    continue;
//                }
                Node neighbor = new Node(w, soFar + getEdge(current, w).weight, toExplore);
                fringe.add(neighbor);
                if (array[w] == neighbor.cumWeight) {
                    b = true;
                    break;
                }
                if (array[w] > neighbor.cumWeight) {
                    array[w] = neighbor.cumWeight;
                }
            }
            if (!visited.contains(current)) {
                visited.add(current);
            }
            if (b == true) {
                visited.remove(visited.size() - 1);
            }

//            visited = new ArrayList<>();
        }

        //return visited;
        ArrayList<Integer> lst = new ArrayList<>();
        lst.add(stop);
        while (last != startNode) {
            lst.add(last.predecessor.index);
            last = last.predecessor;
        }
        Collections.reverse(lst);
        return lst;
    }

    /* Returns the Edge object corresponding to vertex U and V. */
    public Edge getEdge(int u, int v) {
        for (int i = 0; i < adjLists[u].size(); i++) {
            if (adjLists[u].get(i).to == v) {
                return adjLists[u].get(i);
            }
        }
        return null;
    }

    /* Returns an Iterator that outputs the vertices of the graph in topological
       sorted order. */
    public Iterator<Integer> iterator() {
        return new TopologicalIterator();
    }

    /**
     * A class that iterates through the vertices of this graph,
     * starting with a given vertex. Does not necessarily iterate
     * through all vertices in the graph: if the iteration starts
     * at a vertex v, and there is no path from v to a vertex w,
     * then the iteration will not include w.
     */
    private class DFSIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;
        private HashSet<Integer> visited;

        public DFSIterator(Integer start) {
            fringe = new Stack<>();
            visited = new HashSet<>();
            fringe.push(start);
        }

        public boolean hasNext() {
            if (!fringe.isEmpty()) {
                int i = fringe.pop();
                while (visited.contains(i)) {
                    if (fringe.isEmpty()) {
                        return false;
                    }
                    i = fringe.pop();
                }
                fringe.push(i);
                return true;
            }
            return false;
        }

        public Integer next() {
            int curr = fringe.pop();
            ArrayList<Integer> lst = new ArrayList<>();
            for (int i : neighbors(curr)) {
                lst.add(i);
            }
            lst.sort((Integer i1, Integer i2) -> -(i1 - i2));
            for (Integer e : lst) {
                fringe.push(e);
            }
            visited.add(curr);
            return curr;
        }

//        //ignore this method
//        public void remove() {
//            throw new UnsupportedOperationException(
//                    "vertex removal not implemented");
//        }

    }

    /* Returns the collected result of performing a depth-first search on this
       graph's vertices starting from V. */
    public List<Integer> dfs(int v) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new DFSIterator(v);

        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    /* Returns true iff there exists a path from START to STOP. Assumes both
       START and STOP are in this graph. If START == STOP, returns true. */
    public boolean pathExists(int start, int stop) {
        if (start == stop) {
            return true;
        } else {
            List<Integer> dfs = dfs(start);
            for (int i = 0; i < dfs.size(); i++) {
                if (dfs.get(i) == stop) {
                    return true;
                }
            }
            return false;
        }
    }


    /* Returns the path from START to STOP. If no path exists, returns an empty
       List. If START == STOP, returns a List with START. */
    public List<Integer> path(int start, int stop) {
        ArrayList<Integer> dfsResult = new ArrayList<Integer>();
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new DFSIterator(start);
        Stack<Integer> fringe = new Stack<>();
        while (iter.hasNext()) {
            int next = iter.next();
            if (next != stop) {
                dfsResult.add(next);
            } else {
                dfsResult.add(next);
                break;
            }
        }
        for (int i = 0; i < dfsResult.size(); i++) {
            fringe.push(dfsResult.get(i));
        }
        if (!pathExists(start, stop)) {
            return result;
        } else if (start == stop) {
            result.add(start);
            return result;
        } else {
            int curr = stop;
            while (curr != start) {
                int pop = fringe.pop();
                if (isAdjacent(pop, curr)) {
                    curr = pop;
                    result.add(curr);
                }
            }
            Collections.reverse(result);
            result.add(stop);
            return result;
        }
    }

    public List<Integer> topologicalSort() {
        ArrayList<Integer> result = new ArrayList<Integer>();
        Iterator<Integer> iter = new TopologicalIterator();
        while (iter.hasNext()) {
            result.add(iter.next());
        }
        return result;
    }

    private class TopologicalIterator implements Iterator<Integer> {

        private Stack<Integer> fringe;
        private ArrayList<Integer> results;
        private ArrayList<Integer> currentInDegree;

        TopologicalIterator() {
            fringe = new Stack<Integer>();
            results = new ArrayList<>();
            currentInDegree = new ArrayList<>();
            for (int i = 0; i < adjLists.length; i++) {
                currentInDegree.add(inDegree(i));
            }
            for (int i = 0; i < currentInDegree.size(); i++) {
                if (currentInDegree.get(i) == 0) {
                    fringe.push(i);
                }
            }
        }

        public boolean hasNext() {
            if (!fringe.isEmpty()) {
                return true;
            }
            return false;
        }

        public Integer next() {
            int curr = fringe.pop();
            results.add(curr);
            ArrayList<Integer> lst = new ArrayList<>();
            for (int i : neighbors(curr)) {
                lst.add(i);
            }
            for (int i = 0; i < lst.size(); i++) {
                currentInDegree.set(lst.get(i), currentInDegree.get(lst.get(i)) - 1);
            }
            for (int i = 0; i < currentInDegree.size(); i++) {
                if (currentInDegree.get(i) == 0 && !fringe.contains(i)) {
                    if (results.contains(i) == false) {
                        fringe.push(i);
                    }
                }
            }
            return curr;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

    }

    private class Edge {

        private int from;
        private int to;
        private int weight;

        Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public String toString() {
            return "(" + from + ", " + to + ", weight = " + weight + ")";
        }

    }

    private class Node {
        private int index;
        private int cumWeight;
        private Node predecessor;

        Node(int index, int cumWeight, Node predecessor) {
            this.index = index;
            this.cumWeight = cumWeight;
            this.predecessor = predecessor;
        }

        public void setCumWeight(int weight) {
            this.cumWeight = weight;
        }
    }

    private class weightComparator implements Comparator<Node> {
        @Override
        public int compare(Node n1, Node n2) {
            if (n1 == null) {
                return n2.cumWeight;
            } else if (n2 == null) {
                return n1.cumWeight;
            } else {
                return n1.cumWeight - n2.cumWeight;
            }
        }
    }
}

