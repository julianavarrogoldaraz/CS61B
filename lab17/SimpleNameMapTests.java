import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

public class SimpleNameMapTests {

    @Test
    public void test() {
        SimpleNameMap map = new SimpleNameMap();
        map.put("Juli", "Navarro");
        System.out.println("should be Navarro: " + map.get("Juli"));
        assertEquals(1, map.size());
        map.put("Anjali", "Patel");
        assertEquals(2, map.size());
        System.out.println("should be Patel: " + map.get("Anjali"));
        map.put("Ant", "Chan");
        map.put("Andy", "Smith");
        String val = map.remove("Ant");
        System.out.println("should be Chan: " + val);
        System.out.println("should be null: " + map.get("Ant"));
    }

}
