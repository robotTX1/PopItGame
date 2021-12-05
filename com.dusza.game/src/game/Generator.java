package game;

import common.Button;
import common.Table;

public class Generator {
    private final int n;
    private final int curve;
    private Button[][] table;

    public Generator(int n, int curve) {
        this.n = n;
        this.curve = curve;

        table = new Button[n][n];
    }

    public Table generateNewMap() {
        int charIndex;
        boolean success;

        do {
            success = true;
            table = new Button[n][n];
            charIndex = 0;
            for(int i=0; i<curve; i++) {
                success = success && generateCurve(Game.abc.charAt(charIndex));
                charIndex++;
            }

        }while(!success);

        int[] pos = getEmptySpot();
        boolean right;
        while(getEmptySpot() != null) {
            pos = getEmptySpot();
            right = (int) (Math.random() * 2) == 0;

            if(right) {
                createLine(pos[0], pos[1], n, pos[1], Game.abc.charAt(charIndex));
            } else {
                createLine(pos[0], pos[1], pos[0], n, Game.abc.charAt(charIndex));
            }
            charIndex++;
            if(charIndex == Game.abc.length() - 1 && getEmptySpot() != null) return generateNewMap();
        }

        return new Table(table);
    }

    private boolean generateCurve(char c) {
        int x, y;
        int dirX, dirY;
        int freeX, freeY;
        int tries = 0;
        do {
            do {
                x = (int) (Math.random() * n);
                y = (int) (Math.random() * n);
            } while(!isValidSpot(x, y));

            dirX = (int) (Math.random() * 2) == 0 ? -1 : 1;
            dirY = (int) (Math.random() * 2) == 0 ? -1 : 1;

            freeX = countFreeSpacesInARow(x, y, n, dirX);
            freeY = countFreeSpacesInAColumn(x, y, n, dirY);

            tries++;
            if(tries > 10) return false;
        } while(freeX < 1 || freeY < 1);

        for (int i = 0; i <= freeX; i++) {
            table[y][x + i * dirX] = new Button(c);
        }

        for (int i = 1; i <= freeY; i++) {
            table[y + i * dirY][x] = new Button(c);
        }

        return true;
    }

    private int countFreeSpacesInARow(int x, int y, int length, int dir) {
        int counter = 0;
        for(int i = 1; i <= length; i++) {
            if(isValidSpot(x + i * dir, y)) {
                counter++;
            } else {
                break;
            }
        }
        return counter;
    }

    private int countFreeSpacesInAColumn(int x, int y, int length, int dir) {
        int counter = 0;
        for(int i = 1; i <= length; i++) {
            if(isValidSpot(x, y + i * dir)) {
                counter++;
            } else {
                break;
            }
        }
        return counter;
    }

    private int[] getEmptySpot() {
        for (int h = 0; h < n; h++) {
            for (int w = 0; w < n; w++) {
                if(table[h][w] == null) return new int[] { w, h };
            }
        }
        return null;
    }

    private void createLine(int x, int y, int x2, int y2, char c) {
        int validX = 0, validY = 0;
        boolean valid = true;
        for (int h = y; h <= y2; h++) {
            for(int w = x; w <= x2; w++) {
                if(!isValidSpot(w, h)) {
                    valid = false;
                    break;
                } else {
                    validX = w;
                    validY = h;
                }
            }
            if(!valid) break;
        }

        for (int h = y; h <= validY; h++) {
            for(int w = x; w <= validX; w++) {
                table[h][w] = new Button(c);
            }
        }
    }

    private boolean isValidSpot(int x, int y) {
        if(!(x >= 0 && x < n && y >= 0 && y < n)) return false;
        return table[y][x] == null;
    }
}
