package PlusWorld;
import org.junit.Test;
import static org.junit.Assert.*;

import byowTools.TileEngine.TERenderer;
import byowTools.TileEngine.TETile;
import byowTools.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of plus shaped regions.
 */
public class PlusWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    public static void addPlus(int s, int left, int right, TETile[][] tiles) {
        int leftEdge = left - (s + (s / 2));
        int rightEdge = left + (s + (s / 2));
        int widthStart = right - (s / 2);
        int widthEnd = right + (s / 2);
        int widthStart1 = left - (s / 2);
        int widthEnd2 = left + (s / 2);
        int topEdge = right + (s + (s / 2));
        int bottomEdge = right - (s + (s / 2));
        if (s % 2 != 0) {
            widthEnd = right + (s / 2) + 1;
            rightEdge = left + (s + (s / 2)) + 1;
            topEdge = right + (s + (s / 2)) + 1;
            widthEnd2 = left + (s / 2) + 1;
        }

        for (int x = leftEdge; x < rightEdge; x += 1) {
            for (int y = widthStart; y < widthEnd; y += 1) {
                tiles[x][y] = Tileset.FLOWER;
            }
        }

        for (int x = widthStart1; x < widthEnd2; x += 1) {
            for (int y = bottomEdge; y < topEdge; y += 1) {
                tiles[x][y] = Tileset.FLOWER;
            }
        }

    }

    public static void main(String[] args) {
        // initialize the tile rendering engine with a window of size WIDTH x HEIGHT
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        // initialize tiles
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }

        addPlus(5, 25, 25, world);

        // draws the world to the screen
        ter.renderFrame(world);
    }
}
