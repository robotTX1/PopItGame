package game;

import common.Table;
import datasource.IOHandler;

import java.util.Arrays;

public class Game {
    public static final String abc = "abcdefghijklmnopqrstuvwxyz";
    private static final String numbers = "0123456789";

    private boolean player;
    private Table table;
    private final int tableArea;

    public Game(int x, int n, int k) {
        Generator generator = new Generator(n, k);
        Table newTable = generator.generateNewMap();
        IOHandler.saveTable(x, newTable);

        this.table = newTable;
        this.player = true;
        this.tableArea = table.getHeight() * table.getWidth();
    }

    public Game(int index) {
        this.table = IOHandler.readSavedTable(index);
        this.player = true;
        this.tableArea = table.getHeight() * table.getWidth();
    }

    public boolean testWinCase() {
        return table.getPressedCount() == tableArea;
    }

    public boolean getPlayer() {
        return player;
    }

    public Table getTable() {
        return this.table;
    }

    public void press(String line) throws Exception {
        String[] coords;
        int[][] coordsInt;
        int[] prevCoord;

        Table tempTable = new Table(table);

        coords = line.split(" ");
//        Arrays.sort(coords);
//        System.out.println(Arrays.toString(coords));

        for (String a : coords) {
            if (a.length() != 2 || !abc.contains(a.charAt(0) + "") || !numbers.contains(a.charAt(1) + "")) {
                throw new Exception("Érvénytelen formátum. A mezők helyes formátuma: [a-z][0-9]");
            }
        }

        coordsInt = new int[coords.length][2];
        for (int i = 0; i < coords.length; i++) {
            coordsInt[i] = new int[]{
                    Integer.parseInt(coords[i].charAt(1) + ""), abc.indexOf(coords[i].charAt(0))
            };
            if (coordsInt[i][0] >= tempTable.getHeight() || coordsInt[i][1] >= tempTable.getWidth()) {
                throw new Exception("A megadott koordináták között nincs mindegyik a pályán belül.");
            }
        }
        char lineStarter = tempTable.getMap()[coordsInt[0][0]][coordsInt[0][1]].getId();

        prevCoord = null;

        for (int[] coord : coordsInt) {
            if (coord[0] > tempTable.getHeight() - 1 || coord[1] > tempTable.getWidth() - 1) {
                throw new Exception("A megadott koordináták között van olyan, amelyik a pályán kívűlre mutat.");
            }

            if (tempTable.getMap()[coord[0]][coord[1]].getId() != lineStarter) {
                throw new Exception("A megadott koordináták közül az egyik nem tartozik az elsővel megegyező vonalhoz.");
            }
            if (prevCoord != null) {
                // ellenőrzés, hogy az előző mezővel érintkezik-e egyik oldalán a mostani mező
                int dy = Math.abs(prevCoord[1] - coord[1]);
                int dx = Math.abs(prevCoord[0] - coord[0]);
                if (dy + dx > 1) {
                    // TODO Nem tökéletes, van amikor nem ismeri fel a vonalat
                    throw new Exception("A megadott koordináták nem alkotnak folytonos vonalat.");
                }
            }
            tempTable.press(coord[1], coord[0]);
            prevCoord = coord.clone();
        }
        table = tempTable;
        player = !player;
    }
}
