package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class World {
    public static final ArrayList<Point> roomPts = new ArrayList<>();
    public static final ArrayList<Point> floor = new ArrayList<>();
    public static final ArrayList<Point> lights = new ArrayList<>();
    public static final HashMap<Integer, Integer> floorPts = new HashMap<>();
    public static final ArrayList<Point> cornerPts = new ArrayList<>();
    public static final ArrayList<Integer> widths = new ArrayList<>();
    public static final ArrayList<Integer> heights = new ArrayList<>();
    public static final ArrayList<Point> allPts = new ArrayList<>();
    public static Point avatar = new Point();
    public static int[] og = new int[2];
    public static String currInput = "";

    private static final int WIDTH = 80;
    private static final int HEIGHT = 40;

    private static TERenderer TER;
    private long SEED;
    private Random RANDOM;

    public World(long seed, TERenderer ter) {
        TER = ter;
        SEED = seed;
        RANDOM = new Random(SEED);
    }

    public void fillWithNothing(TETile[][] tiles) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                tiles[x][y] = Tileset.NOTHING;
            }
        }
    }

    public static void moveAvatarAndHUD(TETile[][] tiles) {
        while (true) {
            int i = (int) StdDraw.mouseX();
            int j = (int) StdDraw.mouseY();
            if (i < WIDTH && j < HEIGHT) {
                StdDraw.setPenColor(Color.BLACK);
                StdDraw.filledRectangle(1, 41, 20, 0.51);
                if (tiles == null) {
                    continue;
                }
                TETile t = tiles[i][j];
                String type = t.description();
                Font font = new Font("Monaco", Font.BOLD, 15);
                StdDraw.setFont(font);
                StdDraw.setPenColor(Color.WHITE);
                StdDraw.text(6, 41, "Tile type: " + type);
                StdDraw.show();
            }
            System.out.println(avatar);
            int x = (int) avatar.getX();
            int y = (int) avatar.getY();
            if (StdDraw.hasNextKeyTyped()) {
                char c = Character.toUpperCase(StdDraw.nextKeyTyped());
                if (c != 'A' && c != 'S' && c != 'W' && c != 'D' && c != 'Q' && c != ':') {
                    continue;
                }
                currInput += c;
                if (c == 'A') {
                    if (tiles[x - 1][y] != Tileset.WALL) {
                        tiles[x][y] = Tileset.FLOOR;
                        tiles[x - 1][y] = Tileset.AVATAR;
                        avatar = new Point(x - 1, y);
                        TER.renderFrame(tiles);
                    }
                } else if (c == 'Q') {
                    if (currInput.substring(currInput.length() - 2).equals(":Q") || currInput.substring(currInput.length() - 2).equals(":q")) {
                        saveWorld(tiles);
                        System.exit(0);
                    }
                } else if (c == 'S') {
                    if (tiles[x][y - 1] != Tileset.WALL) {
                        tiles[x][y] = Tileset.FLOOR;
                        tiles[x][y - 1] = Tileset.AVATAR;
                        avatar = new Point(x, y - 1);
                        TER.renderFrame(tiles);
                    }
                } else if (c == 'W') {
                    if (tiles[x][y + 1] != Tileset.WALL) {
                        tiles[x][y] = Tileset.FLOOR;
                        tiles[x][y + 1] = Tileset.AVATAR;
                        avatar = new Point(x, y + 1);
                        TER.renderFrame(tiles);
                    }
                } else if (c == 'D') {
                    if (tiles[x + 1][y] != Tileset.WALL) {
                        tiles[x][y] = Tileset.FLOOR;
                        tiles[x + 1][y] = Tileset.AVATAR;
                        avatar = new Point(x + 1, y);
                        TER.renderFrame(tiles);
                    }
                }
            }
        }
    }

    public static void moveAvatarWithChar(TETile[][] tiles, char c) {
        System.out.println(avatar);
        int x = (int) avatar.getX();
        int y = (int) avatar.getY();
        c = Character.toUpperCase(c);
        currInput += c;
        if (c == 'A') {
            if (tiles[x - 1][y] != Tileset.WALL) {
                tiles[x][y] = Tileset.FLOOR;
                tiles[x - 1][y] = Tileset.AVATAR;
                avatar = new Point(x - 1, y);
            }
        } else if (c == 'S') {
            if (tiles[x][y - 1] != Tileset.WALL) {
                tiles[x][y] = Tileset.FLOOR;
                tiles[x][y - 1] = Tileset.AVATAR;
                avatar = new Point(x, y - 1);
            }
        } else if (c == 'W') {
            if (tiles[x][y + 1] != Tileset.WALL) {
                tiles[x][y] = Tileset.FLOOR;
                tiles[x][y + 1] = Tileset.AVATAR;
                avatar = new Point(x, y + 1);
            }
        } else if (c == 'D') {
            if (tiles[x + 1][y] != Tileset.WALL) {
                tiles[x][y] = Tileset.FLOOR;
                tiles[x + 1][y] = Tileset.AVATAR;
                avatar = new Point(x + 1, y);
            }
        } else if (c == 'Q') {
            if (currInput.substring(currInput.length() - 2).equals(":Q")) {
                saveWorld(tiles);
            }
        }
    }


    public static void saveWorld(TETile[][] tiles) {
        File f = new File("saveWorld.txt");
        FileWriter writer = null;
        try {
            writer = new FileWriter("saveWorld.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            if (Engine.all.length() != 0) {
                writer.write(Engine.all.substring(0, Engine.all.length() - 3) + Engine.currInput + currInput);
            }
            writer.write(Engine.currInput + currInput);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void roomsAroundCenter(TETile[][] tiles) {
        int centerX = randomX();
        int centerY = randomY();
        int height = randomNum();
        int width = randomNum();
        int x = centerX - (width / 2);
        int y = centerY - (height / 2);
        int top = y + height;
        ArrayList<Point> currPts = new ArrayList<>();

        for (int i = x; i < x + width + 1; i++) {
            for (int k = y; k < y + height + 1; k++) {
                currPts.add(new Point(i, k));
            }
        }
        for (Point p : currPts) {
            if (allPts.contains(p)) {
                return;
            }
        }
        for (int currX = x; currX <= x + width; currX++) {
            if (x < WIDTH && x > 0 && y < HEIGHT && y > 0 && x + width < WIDTH && y + height < HEIGHT) {

                if (tiles[currX][y] != Tileset.FLOOR) {
                    tiles[currX][y] = Tileset.WALL;
                }
                if (tiles[currX][y + height] != Tileset.FLOOR) {
                    tiles[currX][y + height] = Tileset.WALL;
                }
                Point p = new Point(centerX, centerY);
                if (!roomPts.contains(p)) {
                    roomPts.add(p);
                    cornerPts.add(new Point(x, y));
                }
                for (int i = x - 1; i < x + width + 2; i++) {
                    for (int k = y - 1; k < y + height + 2; k++) {
                        allPts.add(new Point(i, k));
                    }
                }
                widths.add(width);
                heights.add(height);
            }
        }
        for (int currY = y; currY <= y + height; currY++) {
            if (x < WIDTH && x > 0 && y < HEIGHT && y > 0 && y + height < HEIGHT && x + width < WIDTH) {
                if (tiles[x][currY] != Tileset.FLOOR) {
                    tiles[x][currY] = Tileset.WALL;
                }
                if (tiles[x + width][currY] != Tileset.FLOOR) {
                    tiles[x + width][currY] = Tileset.WALL;
                }
            }
        }
        for (int currX = x + 1; currX < x + width; currX++) {
            for (int currY = y + 1; currY < top; currY++) {
                if (x < WIDTH && x > 0 && y < HEIGHT && y > 0 && y + height < HEIGHT && x + width < WIDTH) {
                    tiles[currX][currY] = Tileset.FLOOR;
                    floorPts.put(currX, currY);
                    Point p = new Point(currX, currY);
                    if (!floor.contains(p)) {
                        floor.add(p);
                    }
                }
            }
        }
    }

    public void fixFloors(TETile[][] tiles) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                Point p = new Point(x, y);
                if (floor.contains(p)) {
                    tiles[x][y] = Tileset.FLOOR;
                }
            }
        }
    }

    public void createAvatar(TETile[][] tiles) {
        Point p = randomFloorPt(floor);
        int x = (int) p.getX();
        int y = (int) p.getY();
        tiles[x][y] = Tileset.AVATAR;
        avatar = new Point(x, y);
        og[0] = x;
        og[1] = y;
    }

    private Point randomFloorPt(ArrayList<Point> lst) {
        return lst.get(RANDOM.nextInt(lst.size()));
    }

    public void connectRooms(TETile[][] tiles) {
        for (int i = 0; i + 1 < roomPts.size(); i++) {
            int x = (int) roomPts.get(i).getX();
            int y = (int) roomPts.get(i).getY();
            int currX = (int) roomPts.get(i + 1).getX();
            int currY = (int) roomPts.get(i + 1).getY();
            int xDiff = x - currX;
            int yDiff = y - currY;
            if (xDiff < 0) {
                drawHorizontalHallwayRight(tiles, x, y - yDiff, abs(xDiff));
            } else {
                drawHorizontalHallwayLeft(tiles, x, y - yDiff, xDiff);
            }
            if (yDiff < 0) {
                drawVerticalHallwayUp(tiles, x, y, abs(yDiff));
            } else {
                drawVerticalHallwayDown(tiles, x, y, yDiff);
            }
        }
    }

    public void drawVerticalHallwayUp(TETile[][] tiles, int x, int y, int height) {
        for (int i = y; i < y + height; i++) {
            if (x - 1 < WIDTH && x - 1 > 0 && x + 1 < WIDTH && y + height < HEIGHT) {
                tiles[x - 1][i] = Tileset.WALL;
                tiles[x][i] = Tileset.FLOOR;
                tiles[x + 1][i] = Tileset.WALL;
            }
        }
    }

    public void drawVerticalHallwayDown(TETile[][] tiles, int x, int y, int height) {
        for (int i = y; i > y - height; i--) {
            if (x - 1 < WIDTH && x - 1 > 0 && x + 1 < WIDTH && y - height < HEIGHT && y - height > 0) {
                tiles[x - 1][i] = Tileset.WALL;
                tiles[x][i] = Tileset.FLOOR;
                tiles[x + 1][i] = Tileset.WALL;
            }
        }
    }

    public void drawHorizontalHallwayRight(TETile[][] tiles, int x, int y, int width) {
        for (int i = x; i < x + width; i++) {
            if (y - 1 < HEIGHT && y - 1 > 0 && y + 2 < HEIGHT && x + width < WIDTH) {
                tiles[i][y - 1] = Tileset.WALL;
                tiles[i][y] = Tileset.FLOOR;
                tiles[i][y + 1] = Tileset.WALL;
            }
        }
    }

    public void drawHorizontalHallwayLeft(TETile[][] tiles, int x, int y, int width) {
        for (int i = x; i > x - width; i--) {
            if (y - 1 < HEIGHT && y - 1 > 0 && y + 2 < HEIGHT && x - width < WIDTH &&  x - width > 0) {
                tiles[i][y - 1] = Tileset.WALL;
                tiles[i][y] = Tileset.FLOOR;
                tiles[i][y + 1] = Tileset.WALL;
            }
        }
    }

    private int abs(int i) {
        if (i < 0) {
            i = -i;
        }
        return i;
    }

    private int randomNum() {
        int randNum = 4 + RANDOM.nextInt(5);
        return randNum;
    }

    private int randomX() {
        int randX = RANDOM.nextInt(80);
        return randX;
    }

    private int randomY() {
        int randY = RANDOM.nextInt(40);
        return randY;
    }

    private int randomRoomNum() {
        int randRooms = 30 + RANDOM.nextInt(20);
        return randRooms;
    }

    private int randomLightsNum() {
        int rand = 3 + RANDOM.nextInt(roomPts.size());
        return rand;
    }

    private int randomCoinsNum() {
        int randCoins = 5 + RANDOM.nextInt(floor.size() - 5);
        return randCoins;
    }

    public void connectAdjRooms(TETile[][] tiles) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                if (x < WIDTH && x + 1 < WIDTH && x - 1 < WIDTH && x - 1 > 0) {
                    if (tiles[x][y] == Tileset.WALL && tiles[x - 1][y] == Tileset.FLOOR) {
                        if (x + 2 < WIDTH && tiles[x + 1][y] == Tileset.WALL && tiles[x + 2][y] == Tileset.FLOOR) {
                            tiles[x][y] = Tileset.FLOOR;
                            tiles[x + 1][y] = Tileset.FLOOR;
                        }
                        if (tiles[x + 1][y] == Tileset.FLOOR) {
                            tiles[x][y] = Tileset.FLOOR;
                        }
                    }
                }
                if (y < HEIGHT && y + 1 < HEIGHT && y - 1 < HEIGHT && y - 1 > 0) {
                    if (tiles[x][y] == Tileset.WALL && tiles[x][y - 1] == Tileset.FLOOR && tiles[x][y + 1] == Tileset.FLOOR) {
                        tiles[x][y] = Tileset.FLOOR;
                    }
                }
            }
        }
    }

    private void fixFloatingFloors(TETile[][] tiles) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                if (x + 1 < WIDTH && y + 1 < HEIGHT && x - 1 < WIDTH && x - 1 > 0 && y - 1 > 0 && y - 1 < HEIGHT) {
                    if (tiles[x][y] == Tileset.FLOOR && tiles[x + 1][y] == Tileset.NOTHING) {
                        tiles[x + 1][y] = Tileset.WALL;
                    }
                    if (tiles[x][y] == Tileset.FLOOR && tiles[x + 1][y + 1] == Tileset.NOTHING) {
                        tiles[x + 1][y + 1] = Tileset.WALL;
                    }
                    if (tiles[x][y] == Tileset.FLOOR && tiles[x + 1][y - 1] == Tileset.NOTHING) {
                        tiles[x + 1][y - 1] = Tileset.WALL;
                    }
                    if (tiles[x][y] == Tileset.FLOOR && tiles[x - 1][y] == Tileset.NOTHING) {
                        tiles[x - 1][y] = Tileset.WALL;
                    }
                    if (tiles[x][y] == Tileset.FLOOR && tiles[x - 1][y - 1] == Tileset.NOTHING) {
                        tiles[x - 1][y - 1] = Tileset.WALL;
                    }
                    if (tiles[x][y] == Tileset.FLOOR && tiles[x - 1][y + 1] == Tileset.NOTHING) {
                        tiles[x - 1][y + 1] = Tileset.WALL;
                    }
                }
            }
        }
    }

    private void fixFloatingWalls(TETile[][] tiles) {
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                if (x + 1 < WIDTH && y + 1 < HEIGHT && x - 1 < WIDTH && x - 1 > 0 && y - 1 > 0 && y - 1 < HEIGHT) {
                    if (tiles[x][y] == Tileset.WALL && tiles[x - 1][y] == Tileset.FLOOR && tiles[x + 1][y] == Tileset.FLOOR
                            && tiles[x][y - 1] == Tileset.FLOOR && tiles[x][y + 1] == Tileset.FLOOR) {
                        tiles[x][y] = Tileset.FLOOR;
                    }
                }
            }
        }
    }

    public TETile[][] createWorld() {
        TETile[][] world = new TETile[WIDTH][HEIGHT];
        fillWithNothing(world);
        for (int i = 0; i < randomRoomNum(); i++) {
            roomsAroundCenter(world);
        }
        connectRooms(world);
        fixFloors(world);
        connectAdjRooms(world);
        fixFloatingFloors(world);
        fixFloatingWalls(world);
        createAvatar(world);

        return world;
    }
}
