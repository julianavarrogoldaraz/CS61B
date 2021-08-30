public class Test {

    @org.junit.Test
    public void test1() {
        Graph g = Graph.loadFromText("inputs/graphTestAllDisjoint.in");
        System.out.println(g.kruskals());
    }
}
