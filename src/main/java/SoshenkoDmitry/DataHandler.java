package SoshenkoDmitry;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataHandler {

    private static String outputDir = ".";
    private static String prefix = "";
    private static boolean appendMode = false;
    private static boolean isFullStats = false;
    private static boolean isBriefStats = false;
    private static List<String> inputFiles = new ArrayList<>();

    public static void main(String[] args) {

        parseArguments(args);

        IntegerStats intStats = new IntegerStats();
        DoubleStats doubleStats = new DoubleStats();
        StringStats stringStats = new StringStats();

        try {

            checkIfFilesExist();

            Map<String, List<String>> data = parseData();

            getDataStats(data, intStats, doubleStats, stringStats);

            writeDataToFiles(data);

            consoleOutput(intStats, doubleStats, stringStats, data);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void checkIfFilesExist() {

        List<String> validFiles = new ArrayList<>();

        for (String filename : inputFiles) {

            if (Files.exists(Paths.get(filename))) {
                validFiles.add(filename);
            } else {
                System.out.println("Файл " + filename + " не найден, файл с именем " + filename + " пропущен");
            }

        }

        inputFiles = validFiles;

        if (inputFiles.isEmpty()) {

            System.out.println("Файлов для обработки не найдено, работа программы завершена");

            System.exit(0);

        }

    }

    private static void getDataStats(Map<String, List<String>> data, IntegerStats intStats, DoubleStats doubleStats, StringStats stringStats) {

        for (String line : data.get("integers")) {

            long value = Long.parseLong(line);
            intStats.update(value);

        }

        for (String line : data.get("floats")) {

            double value = Float.parseFloat(line);
            doubleStats.update(BigDecimal.valueOf(value));

        }

        for (String line : data.get("strings")) {
            stringStats.update(line);
        }

    }

    private static void consoleOutput(IntegerStats intStats, DoubleStats doubleStats, StringStats stringStats, Map<String, List<String>> data) {

        if (isFullStats) {

            System.out.println("Статистика по целым числам:");
            intStats.print();

            System.out.println("\nСтатистика по числам с плавающей точкой:");
            doubleStats.print();

            System.out.println("\nСтатистика по строкам:");
            stringStats.print();

        } else if (isBriefStats) {

            System.out.println("Краткая статистика:");
            System.out.println("Чисел: " + data.get("integers").size());
            System.out.println("Чисел с плавающей точкой: " + data.get("floats").size());
            System.out.println("Строк: " + data.get("strings").size());

        }

    }

    private static void parseArguments(String[] args) {

        for (int i = 0; i < args.length; i++) {

            switch (args[i]) {

                case "-o":

                    if (i + 1 < args.length)
                        outputDir = args[++i];

                    break;

                case "-p":

                    if (i + 1 < args.length)
                        prefix = args[++i];

                    break;

                case "-a":

                    appendMode = true;

                    break;

                case "-s":

                    isBriefStats = true;

                    break;

                case "-f":
                    isFullStats = true;
                    break;

                default:
                    inputFiles.add(args[i]);

            }

        }

    }

    private static Map<String, List<String>> parseData() throws IOException {

        List<String> allLines = new ArrayList<>();
        List<String> integers = new ArrayList<>();
        List<String> floats = new ArrayList<>();
        List<String> strings = new ArrayList<>();

        for (String filename : inputFiles) {
            allLines.addAll(readFiles(filename));
        }

        for (String line : allLines) {

            if (tryParseInt(line)) {
                integers.add(line);
            } else if (tryParseDouble(line)) {
                floats.add(line);
            } else {
                strings.add(line);
            }

        }

        Map<String, List<String>> result = new HashMap<>();

        result.put("integers", integers);
        result.put("floats", floats);
        result.put("strings", strings);

        return result;

    }

    private static List<String> readFiles(String filename) throws IOException {

        Path path = Paths.get(filename);

        return Files.readAllLines(path);

    }

    private static boolean tryParseInt(String line) {

        try {

            Long.parseLong(line.trim());

            return true;

        } catch (NumberFormatException e) {
            return false;
        }

    }

    private static boolean tryParseDouble(String line) {

        try {

            Double.parseDouble(line);

            return line.contains(".");

        } catch (NumberFormatException e) {
            return false;
        }

    }

    private static void writeDataToFiles(Map<String, List<String>> data) throws IOException {

        if (data.get("integers") != null && !data.get("integers").isEmpty()) {
            writeToFile(getOutputPath("integers.txt"), data.get("integers"));
        }

        if (data.get("floats") != null && !data.get("floats").isEmpty()) {
            writeToFile(getOutputPath("floats.txt"), data.get("floats"));
        }

        if (data.get("strings") != null && !data.get("strings").isEmpty()) {
            writeToFile(getOutputPath("strings.txt"), data.get("strings"));
        }

    }

    private static void writeToFile(Path path, List<String> lines) throws IOException {

        Files.createDirectories(path.getParent());

        try (BufferedWriter writer = Files.newBufferedWriter(
                path,
                StandardOpenOption.CREATE,
                appendMode ? StandardOpenOption.APPEND : StandardOpenOption.TRUNCATE_EXISTING)) {
            for (String line : lines) {

                writer.write(line);
                writer.newLine();

            }
        }

    }

    private static Path getOutputPath(String filename) {
        return Paths.get(outputDir, prefix + filename);
    }

}
