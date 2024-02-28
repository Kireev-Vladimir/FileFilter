package ru.kireev;

import ru.kireev.exception.NoInputFilesException;
import ru.kireev.exception.OutputFileIOException;
import ru.kireev.statistics.DoubleStatistics;
import ru.kireev.statistics.LongStatistics;
import ru.kireev.statistics.SimpleStatistics;
import ru.kireev.statistics.StringStatistics;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FileFilter {

    private final List<File> inputFiles = new ArrayList<>();
    private String outputDir;
    private String prefix = "";
    private final List<FileFilterWriter> writers = new ArrayList<>();
    private StatisticsType statisticsType = StatisticsType.NO_STATISTICS;
    private boolean append = false;

    public FileFilter(String[] args) throws NoInputFilesException {
        List<String> fileNames = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "-o":
                    i++;
                    if (i != args.length) {
                        outputDir = args[i];
                    } else {
                        System.out.println("Не указан путь для результатов");
                    }
                    break;
                case "-p":
                    i++;
                    if (i != args.length) {
                        prefix = args[i];
                    } else {
                        System.out.println("Не указан путь для результатов");
                    }
                    break;
                case "-s":
                    statisticsType = StatisticsType.SIMPLE_STATISTICS;
                    break;
                case "-f":
                    statisticsType = StatisticsType.FULL_STATISTICS;
                    break;
                case "-a":
                    append = true;
                    break;
                default:
                    fileNames.add(args[i]);
                    break;
            }
        }
        if (fileNames.isEmpty()) {
            throw new NoInputFilesException("Отсутствуют входные файлы.");
        }
        initFiles(fileNames);
        initWriters();
    }

    private void initFiles(List<String> fileNames) {
        for (String fileName : fileNames) {
            inputFiles.add(new File(fileName));
        }
    }

    private void initWriters() {
        String pref;
        if (outputDir == null) {
            pref = prefix;
        } else {
            if (outputDir.endsWith("\\") || outputDir.endsWith("/"))
                pref = outputDir + prefix;
            else
                pref = outputDir + File.separator + prefix;
        }
        if (statisticsType == StatisticsType.NO_STATISTICS) {
            FileFilterWriter<Long> integerWriter = new FileFilterWriter<>(pref + "integers.txt", Long::parseLong, append, null);
            writers.add(integerWriter);
            FileFilterWriter<Double> floatWriter = new FileFilterWriter<>(pref + "floats.txt", Double::parseDouble, append, null);
            writers.add(floatWriter);
            FileFilterWriter<String> stringWriter = new FileFilterWriter<>(pref + "strings.txt", (s) -> s, append, null);
            writers.add(stringWriter);
        } else if (statisticsType == StatisticsType.SIMPLE_STATISTICS) {
            FileFilterWriter<Long> integerWriter = new FileFilterWriter<>(pref + "integers.txt", Long::parseLong, append, new SimpleStatistics<>("Целое число"));
            writers.add(integerWriter);
            FileFilterWriter<Double> floatWriter = new FileFilterWriter<>(pref + "floats.txt", Double::parseDouble, append, new SimpleStatistics<>("Вещественное число"));
            writers.add(floatWriter);
            FileFilterWriter<String> stringWriter = new FileFilterWriter<>(pref + "strings.txt", (s) -> s, append, new SimpleStatistics<>("Строка"));
            writers.add(stringWriter);
        } else {
            FileFilterWriter<Long> integerWriter = new FileFilterWriter<>(pref + "integers.txt", Long::parseLong, append, new LongStatistics());
            writers.add(integerWriter);
            FileFilterWriter<Double> floatWriter = new FileFilterWriter<>(pref + "floats.txt", Double::parseDouble, append, new DoubleStatistics());
            writers.add(floatWriter);
            FileFilterWriter<String> stringWriter = new FileFilterWriter<>(pref + "strings.txt", (s) -> s, append, new StringStatistics());
            writers.add(stringWriter);
        }
    }

    public void filter() {
        List<Scanner> scanners = new ArrayList<>();
        for (File file : inputFiles) {
            try {
                scanners.add(new Scanner(file));
            } catch (FileNotFoundException e) {
                System.out.println("Файл " + file.getAbsolutePath() + " не найден.");
            }
        }
        while (!scanners.isEmpty()) {
            for (int i = 0; i < scanners.size(); i++) {
                if (scanners.get(i).hasNext()) {
                    try {
                        String input = scanners.get(i).nextLine();
                        for (var writer : writers) {
                            try {
                                if (writer.checkAndWrite(input)) {
                                    break;
                                }
                            }
                            catch (OutputFileIOException e) {
                                System.out.println(e.getMessage());
                                writers.remove(writer);
                                break;
                            }
                        }
                    } catch (IOException e) {
                        scanners.get(i).close();
                        scanners.remove(i);
                        i--;
                        System.out.println("Ошибка чтения файла #" + i);
                    }
                } else {
                    scanners.get(i).close();
                    scanners.remove(i);
                    i--;
                }
            }
        }
        for (var writer : writers) {
            writer.writeStatistics();
            writer.close();
        }
        for (var scanner : scanners) {
            scanner.close();
        }
        writers.clear();
    }


}
