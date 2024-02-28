package ru.kireev.statistics;

public class StringStatistics extends SimpleStatistics<String> {
    private int min = Integer.MAX_VALUE;
    private int max = Integer.MIN_VALUE;

    public StringStatistics() {
        super("Строка");
    }

    @Override
    public void add(String element) {
        super.add(element);
        if (element.length() > max) {
            max = element.length();
        }
        if (element.length() < min) {
            min = element.length();
        }
    }

    @Override
    public String getMessage() {
        if (getCounter() == 0) {
            return super.getMessage();
        }
        return super.getMessage() + System.lineSeparator() +
                "Максимальная длина строк:" + max + ";" + System.lineSeparator() +
                "Минимальная длина строк:" + min + ";";
    }
}
