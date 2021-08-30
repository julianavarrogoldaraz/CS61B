package gh2;

 import deque.ArrayDeque;
 import deque.Deque;
 import deque.LinkedListDeque;

//Note: This file will not compile until you complete the Deque implementations
public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final
     * means the values cannot be changed at runtime. We'll discuss this and
     * other topics in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
     private Deque<Double> buffer;

    /* Create a guitar string of the given frequency.  */
    public GuitarString(double frequency) {
        int capacity = (int) (Math.round(SR / frequency));
        buffer = new ArrayDeque();
        for (int index = 0; index < capacity; index += 1) {
            buffer.addFirst(0.0);
        }
    }

    private double random() {
        return Math.random() - 0.5;
    }

    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        System.out.println("here");
        int size = buffer.size();
        for (int index = 0; index < size; index += 1) {
            buffer.removeFirst();
        }
        for (int index = 0; index < size; index += 1) {
            buffer.addLast(random());
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm.
     */
    public void tic() {
        double newDouble = (((buffer.get(0) + buffer.get(1)) / 2) * 0.996);
        buffer.removeFirst();
        buffer.addLast(newDouble);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        return buffer.get(0);
    }
}