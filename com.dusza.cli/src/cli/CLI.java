package cli;

import datasource.IOHandler;
import game.Game;

import java.io.IOException;
import java.nio.file.Path;
import java.util.IllegalFormatException;
import java.util.List;
import java.util.Scanner;

public class CLI {
    /*

    A CLI - Command Line Interface, azaz Parancssoros felhasználói felület fogja nekünk biztosítani a játékban a grafikus elemek megjelenítését.
    Itt kap lehetőséget az adott játékos a különböző opciók kiválasztására, melyek a 'start()' függvényben találhatóak meg.
    A játékosunknak választási lehetősége van, melyek a printMenu() függvényben lelhetőek fel:

    1. Már Létező pálya betöltése
    2. Egy új pályának a legenerálása
    3. Kilépés a játékból

    A függvény azt is vizsgálja, ha éppen nem 1-3 közötti számot viszünk be és ezt egy hibaüzenetként vissza is adja a játékosnak.
    Továbbiakban ha az 1-es lehetőséget választjuk, a 'printFiles()' beolvassa a már lementett pályákat, amelyeket az index váltózóban tárol el,
    ha éppen nincs már létező pálya azaz a index változó értéke továbbra is '1'-es marad, akkor hibaüzenetként vissz a is kapjuk hogy "Nincsenek mentett játékok!"
    Majd ezt követően a játék be is zárul.
    A már mentett játékok között megtalálható a 'kilépés' is mint opció, ezt lenyomva visszakerülünk a főmenübe.
    A CLI osztály tartalmaz kivételkezelést, mely a futtatásnál történő bármely hibánál egy hibaüzenettel tér vissza hozzánk.

    */
    private final Scanner input = new Scanner(System.in);

    private Game game;

    public void start() {
        String option;

        while(true) {
            clearConsole();
            System.out.println("Üdv a Dusza Pop-it játékában!");
            System.out.println("Mit szeretne tenni?");

            printMenu();

            option = input.nextLine().trim();

            if(!option.matches("\\d+")) {
                System.out.println("Érvénytelen bemenent: " + option);
                waitForInput();
                continue;
            }

            switch (option) {
                case "1":
                    loadGame();
                    break;
                case "2":
                    generateNewGame();
                    break;
                case "3":
                    System.out.println("Köszönöm, hogy játszottál!");
                    System.exit(0);
                default:
                    System.out.println("Nincs ilyen opció: " + option);
                    break;
            }
        }
    }

    private void printMenu() {
        System.out.println("\n1. Létező pálya betöltése");
        System.out.println("2. Pálya generalás");
        System.out.println("3. Kilépés");
    }

    private void generateNewGame() {
        int x, n, k;
        String inputStr;

        while(true) {
            try {
                clearConsole();
                System.out.print("Kérem a pálya számát: ");
                inputStr = input.nextLine().trim();
                if(!inputStr.matches("\\d+")) throw new Exception("Érvénytelen bemenet: " + inputStr);
                x = Integer.parseInt(inputStr);
                if(x < 0) throw new Exception("A pálya száma nem lehet negatív!");
                break;
            } catch (IllegalFormatException e) {
                System.out.println("Érvénytelen bemenet!");
                waitForInput();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                waitForInput();
            }
        }

        while(true) {
            try {
                clearConsole();
                System.out.print("Kérem a pálya méretét: ");
                inputStr = input.nextLine().trim();
                if(!inputStr.matches("\\d+")) throw new Exception("Érvénytelen bemenet: " + inputStr);
                n = Integer.parseInt(inputStr);
                if(n < 4 || n > 10) throw new Exception("Az érték 4-10 között lehet!");
                break;
            } catch (IllegalFormatException e) {
                System.out.println("Érvénytelen bemenet!");
                waitForInput();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                waitForInput();
            }
        }

        while(true) {
            try {
                clearConsole();
                System.out.print("Kérem a hajlítások számát: ");
                inputStr = input.nextLine().trim();
                if(!inputStr.matches("\\d+")) throw new Exception("Érvénytelen bemenet: " + inputStr);
                k = Integer.parseInt(inputStr);
                if(k < 0 || k > 4) throw new Exception("Az érték 0-4 között lehet!");
                break;
            } catch (IllegalFormatException e) {
                System.out.println("Érvénytelen bemenet!");
                waitForInput();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                waitForInput();
            }
        }

        game = new Game(x, n, k);
        startGame();
    }

    private void loadGame() {
        List<Path> pathList = IOHandler.loadSavedTables();

        if(pathList == null || pathList.size() == 0) {
            System.out.println("Nincsenek mentett játékok!");
            waitForInput();
        } else {
            int index;
            String optionStr;
            int option;

            while(true) {
                clearConsole();
                index = 1;
                System.out.println("Mentett játékok:");
                for(Path p : pathList) {
                    System.out.printf("%d. %s\n", index++, p.getFileName().toString());
                }
                System.out.printf("%d. Kilépés\n", index);

                optionStr = input.nextLine().trim();

                if(!optionStr.matches("\\d+")) {
                    System.out.println("Érvénytelen opció: " + optionStr);
                    waitForInput();
                    continue;
                }

                option = Integer.parseInt(optionStr);

                if(option >= 1 && option < index) {
                    game = new Game(Integer.parseInt(pathList.get(option-1).getFileName().toString().replaceAll("\\D*", "")));
                    startGame();
                    break;
                } else if(option == index) {
                    break;
                }
            }
        }
    }

    private void startGame() {
        String line;
        while(!game.testWinCase()) {
            try {
                clearConsole();
                System.out.println("A játékban a nagybetűk a lenyomható mezőket, a kisbetűk pedig a már lenyomott mezőket jelölik.\n\n");
                drawGame();
                line = input.nextLine();
                if(line.equals("exit") || line.equals("kilepes")) return;

                game.press(line);
            }catch (Exception e) {
                System.out.println(e.getMessage());
                waitForInput();
            }
        }

        System.out.println((game.getPlayer() ? "Játékos1": "Játékos2") + " a nyertes!");
        waitForInput();
    }

    private void drawGame() {
        int h = game.getTable().getHeight();
        int w = game.getTable().getWidth();
        System.out.print("    ");
        for (int i = 0; i < w; i++) {
            System.out.print(Game.abc.charAt(i));
        }
        System.out.println();
        System.out.println();
        for (int i = 0; i < h; i++) {
            System.out.println(i + "   " + game.getTable().getLine(i));
        }
        System.out.println(game.getPlayer() ? "Játékos 1 jön": "Játékos 2 jön");
        System.out.println();
    }

    private void clearConsole() {
        try {
            String operatingSystem = System.getProperty("os.name");
            ProcessBuilder pb;
            if(operatingSystem.contains("Windows")) {
                 pb = new ProcessBuilder("cmd", "/c", "cls");
            } else {
                pb = new ProcessBuilder("clear");
            }
            Process startProcess = pb.inheritIO().start();
            startProcess.waitFor();
        } catch (IOException | InterruptedException e) {
            System.out.println("Nem sikerült törölni a konzol tartalmát: " + e.getMessage());
        }
    }

    private void waitForInput() {
        System.out.println("\nA folytatáshoz nyomd meg az ENTER-t!");
        input.nextLine();
    }
}
