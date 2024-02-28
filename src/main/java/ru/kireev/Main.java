package ru.kireev;

import ru.kireev.exception.NoInputFilesException;

public class Main {
    public static void main(String[] args) {
        try {
            FileFilter fileFilter = new FileFilter(args);
            fileFilter.filter();
        } catch (NoInputFilesException e) {
            System.out.println(e.getMessage());
        }
    }
}