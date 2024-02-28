package ru.kireev;

import ru.kireev.exception.OutputFileIOException;
import ru.kireev.statistics.Statistics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.function.Function;

public class FileFilterWriter<T> {

    private String fileName;
    private FileWriter fileWriter;
    private boolean append;
    private Function<String, T> parser;
    private Statistics<T> statistics;

    public FileFilterWriter(String fileName, Function<String, T> parser, boolean append, Statistics<T> statistics) {
        this.fileName = fileName;
        this.parser = parser;
        this.append = append;
        this.statistics = statistics;
    }

    private void openFile() throws OutputFileIOException {
        try {
            File file = new File(fileName);
            if(file.getParentFile() != null) {
                file.getParentFile().mkdirs();
            }
            fileWriter = new FileWriter(file, append);
        } catch (IOException e) {
            throw new OutputFileIOException("Ошибка открытия выходного файла " + fileName, e);
        }
    }

    private void write(T input) throws OutputFileIOException {
        if (fileWriter == null) {
            openFile();
        }
        try {
            fileWriter.write(input + System.lineSeparator());
        } catch (IOException e) {
            throw new OutputFileIOException("Ошибка записи в выходной файл", e);
        }
    }

    public boolean checkAndWrite(String input) throws IOException {
        T parsedInput;
        try {
            parsedInput = parser.apply(input);
        } catch (Exception e) {
            return false;
        }

        if (statistics != null) {
            statistics.add(parsedInput);
        }
        write(parsedInput);

        return true;
    }

    public void writeStatistics() {
        if (statistics != null) {
            System.out.println(statistics.getMessage());
        }
    }

    public void close() {
        if (fileWriter != null) {
            try {
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Ошибка закрытия выходного файла.");
            }
        }
    }
}
