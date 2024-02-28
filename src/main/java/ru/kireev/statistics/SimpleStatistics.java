package ru.kireev.statistics;

public class SimpleStatistics<T> implements Statistics<T> {

    private int counter = 0;
    private String typeName;

    public SimpleStatistics(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public void add(T element) {
        counter++;
    }

    @Override
    public String getMessage() {
        return "Количество элементов типа \"" + typeName + "\": " + getCounter() + ";";
    }

    public int getCounter() {
        return counter;
    }
}
