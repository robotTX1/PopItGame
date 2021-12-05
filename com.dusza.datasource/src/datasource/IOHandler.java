package datasource;

import common.Button;
import common.Table;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class IOHandler {
    private static final String FILE_NAME = "palya%d.txt";
    public static final String FILE_NAME_REGEX = "palya\\d+.txt";
    public static final Path workDir = FileSystems.getDefault().getPath(".");


    public static void saveTable(int index, Table table) {
        Button[][] map = table.getMap();
        try (BufferedWriter writer = Files.newBufferedWriter(workDir.resolve(String.format(FILE_NAME, index)))) {
            for (int y = 0; y < map.length; y++) {
                for (int x = 0; x < map.length; x++) {
                    writer.write(map[y][x].getId());
                }
                writer.write("\r\n");
            }
        } catch (IOException e) {
            System.out.println("Valami nem jó: " + e.getMessage());
        }
    }

    public static Table readSavedTable(int index) {
        try {
            List<String> lines = Files.readAllLines(workDir.resolve(String.format(FILE_NAME, index)));
            Button[][] map = new Button[lines.size()][lines.get(0).length()];

            for (int y = 0; y < lines.size(); y++) {
                for (int x = 0; x < lines.get(y).length(); x++) {
                    map[y][x] = new Button(lines.get(y).charAt(x));
                }
            }
            return new Table(map);
        } catch (IOException e) {
            System.out.println("Valami nem jó: " + e.getMessage());
            return null;
        }
    }

    public static List<Path> loadSavedTables() {
        try {
            List<Path> paths = new ArrayList<>();

            for(Path p : Files.newDirectoryStream(workDir, entry -> entry.getFileName().toString().matches(FILE_NAME_REGEX) && !Files.isDirectory(entry))) {
                paths.add(p);
            }

            paths.sort(Comparator.comparing(p -> Integer.parseInt(p.getFileName().toString().replaceAll("\\D*", ""))));

            return paths;
        } catch (IOException e) {
            System.out.println("Valami nem jó: " + e.getMessage());
            return null;
        }
    }

}
