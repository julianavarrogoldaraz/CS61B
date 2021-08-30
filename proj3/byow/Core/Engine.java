package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 40;
    public static final int HEIGHT = 40;
    public static TETile[][] world;
    public static String currInput = "";
    private static boolean DEBUG = false;
    public static String all = "";

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        createMenu();
        interactWithMenu();
        World.moveAvatarAndHUD(world);
    }

    public void createMenu() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, WIDTH);
        StdDraw.enableDoubleBuffering();
        Font fontBig = new Font("Monaco", Font.BOLD, 45);
        StdDraw.setFont(fontBig);
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT / 1.5, "CS61BL: THE GAME");
        StdDraw.setFont(font);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT / 2.2, "New Game (N)");
        StdDraw.text(WIDTH / 2, HEIGHT / 2.57, "Load Game (L)");
        StdDraw.text(WIDTH / 2, HEIGHT / 3, "Replay Game (R)");
        StdDraw.text(WIDTH / 2, HEIGHT / 3.8, "Quit (Q)");
        StdDraw.show();
    }

    public TETile[][] interactWithMenu() {
        String typed = "";
        while (typed.length() < 1) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toUpperCase(StdDraw.nextKeyTyped());
                if (c != 'N' && c != 'Q' && c != 'L' && c != 'R') {
                    continue;
                }
                typed += c;
                if (c != 'R') {
                    currInput += c;
                }
            }
        }
        if (typed.equals("N")) {
            return typedN();
        } else if (typed.equals("R")) {
            replay();
        } else if (typed.equals("L")) {
            String str = "";
            try {
                str = Files.readString(Path.of("saveWorld.txt"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            String seed = "";
            for (int j = 0; j < str.length(); j++) {
                if (str.charAt(j) == 'N') {
                    continue;
                } else if (str.charAt(j) == 'S') {
                    break;
                } else {
                    seed += str.charAt(j);
                }
            }
            all += str;
            String actions = str.substring(seed.length() + 2, str.length() - 2);
            long s = Long.valueOf(seed);
            World w = new World(s, ter);
            TETile[][] finalWorldFrame = w.createWorld();
            for (int in = 0; in < actions.length(); in++) {
                World.moveAvatarWithChar(finalWorldFrame, actions.charAt(in));
            }
            StdDraw.setXscale(0, WIDTH);
            StdDraw.setYscale(0, HEIGHT);
            StdDraw.setCanvasSize(80 * 16, 42 * 16);
            StdDraw.setXscale(0, 80);
            StdDraw.setYscale(0, 42);
            ter.renderFrame(finalWorldFrame);
            world = finalWorldFrame;
            return finalWorldFrame;
        } else if (typed.equals("Q")) {
            System.exit(0);
        }
        return null;
    }

    public TETile[][] typedN() {
        StdDraw.clear(Color.BLACK);
        StdDraw.setCanvasSize(WIDTH * 16, HEIGHT * 16);
        Font font = new Font("Monaco", Font.BOLD, 30);
        StdDraw.setFont(font);
        StdDraw.setXscale(0, WIDTH);
        StdDraw.setYscale(0, HEIGHT);
        StdDraw.enableDoubleBuffering();
        StdDraw.clear(Color.BLACK);
        StdDraw.setPenColor(Color.WHITE);
        StdDraw.text(WIDTH / 2, HEIGHT / 1.75, "Please enter a random seed: ");
        StdDraw.show();
        String curr = "";
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toUpperCase(StdDraw.nextKeyTyped());
                if (c == 'S') {
                    currInput += c;
                    break;
                } else if (!isDigit(c)) {
                    continue;
                }
                curr += c;
                currInput += c;
                StdDraw.clear(Color.BLACK);
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.text(WIDTH / 2, HEIGHT / 1.75, "Please enter a random seed: ");
                StdDraw.text(WIDTH / 2, HEIGHT / 2.3, curr);
                StdDraw.show();
            }
        }
        String seed = curr;
        long s = Long.valueOf(seed);
        World w = new World(s, ter);
        TETile[][] finalWorldFrame = w.createWorld();
        StdDraw.setCanvasSize(80 * 16, 42 * 16);
        StdDraw.setXscale(0, 80);
        StdDraw.setYscale(0, 42);
        ter.renderFrame(finalWorldFrame);
        world = finalWorldFrame;
        return finalWorldFrame;
    }

    public boolean isDigit(char c) {
        char[] digits = "0123456789".toCharArray();
        for (int i = 0; i < digits.length; i++) {
            if (c == digits[i]) {
                return true;
            }
        }
        return false;
    }

    public void replay() {
        String saved = "";
        try {
            saved = Files.readString(Path.of("saveWorld.txt"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String seed = "";
        for (int i = 0; i < saved.length(); i++) {
            if (saved.charAt(i) == 'N' || saved.charAt(i) == 'n') {
                continue;
            } else if (saved.charAt(i) != 'S' && saved.charAt(i) != 's') {
                seed += saved.charAt(i);
            } else if (saved.charAt(i) == 'S' || saved.charAt(i) == 's') {
                break;
            }
        }
        long s = Long.valueOf(seed);
        World w = new World(s, ter);
        TETile[][] finalWorldFrame = w.createWorld();
        finalWorldFrame[World.og[0]][World.og[1]] = Tileset.AVATAR;
        if (DEBUG) {
            StdDraw.clear(Color.BLACK);
            StdDraw.setCanvasSize(80 * 16, 42 * 16);
            StdDraw.setXscale(0, 80);
            StdDraw.setYscale(0, 42);
            ter.renderFrame(finalWorldFrame);
            StdDraw.pause(300);
        }
        String actions = saved.substring(seed.length() + 2, saved.length() - 2);
        int x = World.og[0];
        int y = World.og[1];
        System.out.println(x);
        System.out.println(y);
        World.avatar = new Point(x, y);
        for (int i = 0; i < actions.length(); i++) {
            World.moveAvatarWithChar(finalWorldFrame, actions.charAt(i));
            if (DEBUG) {
                ter.renderFrame(finalWorldFrame);
                StdDraw.pause(125);
            }
        }
        StdDraw.pause(250);
        System.exit(0);
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        // TODO: Fill out this method so that it run the engine using the input
        // passed in as an argument, and return a 2D tile representation of the
        // world that would have been drawn if the same inputs had been given
        // to interactWithKeyboard().
        //
        // See proj3.byow.InputDemo for a demo of how you can make a nice clean interface
        // that works for many different input types.
        if (DEBUG) {
            StdDraw.setXscale(0, WIDTH);
            StdDraw.setYscale(0, HEIGHT);
            StdDraw.setCanvasSize(80 * 16, 42 * 16);
            StdDraw.setXscale(0, 80);
            StdDraw.setYscale(0, 42);
        }
        if (input.charAt(0) != 'n' && input.charAt(0) != 'N' && input.charAt(0) != 'l' && input.charAt(0) != 'L') {
            return null;
        } else if (input.charAt(0) == 'n' || input.charAt(0) == 'N') {
            currInput += input.charAt(0);
            String seed = "";
            for (int i = 0; i < input.length(); i++) {
                if (input.charAt(i) == 'N' || input.charAt(i) == 'n') {
                    continue;
                } else if (input.charAt(i) != 'S' && input.charAt(i) != 's') {
                    seed += input.charAt(i);
                } else if (input.charAt(i) == 'S' || input.charAt(i) == 's') {
                    break;
                }
            }
            currInput += seed;
            currInput += 's';
            long s = Long.valueOf(seed);
            World w = new World(s, ter);
            TETile[][] finalWorldFrame = w.createWorld();
            String actions = input.substring(seed.length() + 2, input.length());
            for (int in = 0; in < actions.length(); in++) {
                World.moveAvatarWithChar(finalWorldFrame, actions.charAt(in));
            }
            if (DEBUG) {
                ter.renderFrame(finalWorldFrame);
            }
            return finalWorldFrame;
        } else {
            String str = "";
            try {
                str = Files.readString(Path.of("saveWorld.txt"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            String seed = "";
            for (int j = 0; j < str.length(); j++) {
                if (str.charAt(j) == 'N' || str.charAt(j) == 'n') {
                    continue;
                } else if (str.charAt(j) == 'S' || str.charAt(j) == 's') {
                    break;
                } else {
                    seed += str.charAt(j);
                }
            }
            long s = Long.valueOf(seed);
            World w = new World(s, ter);
            TETile[][] finalWorldFrame = w.createWorld();
            String actions = str.substring(seed.length() + 2, str.length() - 2) + input.substring(1);
            for (int in = 0; in < actions.length(); in++) {
                World.moveAvatarWithChar(finalWorldFrame, actions.charAt(in));
            }
            if (DEBUG) {
                ter.renderFrame(finalWorldFrame);
            }
            return finalWorldFrame;
        }
    }
}
