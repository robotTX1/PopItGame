package common;

public class Table {
    public static final char EMPTY_SPACE_CHAR = '.';

    private Button[][] map;
    private int pressedCount;
    private final int width;
    private final int height;

    public Table(Table t) {
        this.map = deepCopy(t.getMap());
        this.height = t.height;
        this.width = t.width;
        this.pressedCount = t.pressedCount;
    }

    public Table(Button[][] map) {
        this.map = map;
        this.width = map[0].length;
        this.height = map.length;
        setStartingPressCount();
    }

    private void setStartingPressCount() {
        pressedCount = 0;
        for (Button[] bA : map) {
            for (Button b : bA) {
                if (b.getId() == EMPTY_SPACE_CHAR) pressedCount++;
            }
        }
    }

    public void press(int x, int y) throws Exception {
        if (!(x >= 0 && x < width && y >= 0 && y < height)) throw new Exception("Érvénytelen mező!");
        if (map[y][x].getId() == EMPTY_SPACE_CHAR) throw new Exception("Üres mezőt nem lehet lenyomni!");
        if (map[y][x].isPressed()) throw new Exception("Már lenyomott mezőt nem lehet mégegyszer lenyomni!");

        if (!map[y][x].isPressed()) {
            map[y][x].setPressed(true);
            pressedCount++;
        }
    }

    public String getLine(int y) {
        Button[] line = map[y];

        StringBuilder ln = new StringBuilder();
        String ch;

        for (Button a : line) {
            ch = a.getId() + "";
            ln.append(a.isPressed() ? ch : ch.toUpperCase());
        }

        return ln.toString();
    }

    public Button[][] getMap() {
        return map;
    }

    public void setMap(Button[][] map) {
        this.map = map;
    }

    public int getPressedCount() {
        return pressedCount;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    private Button[][] deepCopy(Button[][] inp) {
        Button[][] out = new Button[inp.length][inp[0].length];

        for (int i = 0; i < inp.length; i++) {
            for (int j = 0; j < inp[0].length; j++) {
                out[i][j] = new Button(inp[i][j].getId(), inp[i][j].isPressed());
            }
        }

        return out;
    }

}
