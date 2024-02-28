package ru.kireev.statistics;

public class LongStatistics extends SimpleStatistics<Long> {

    private long min = Long.MAX_VALUE;
    private long max = Long.MIN_VALUE;
    private long sum = 0L;

    public LongStatistics() {
        super("Целое число");
    }

    @Override
    public void add(Long element) {
        super.add(element);
        min = Long.min(min, element);
        max = Long.max(max, element);
        sum += element;
    }

    @Override
    public String getMessage() {
        if (getCounter() == 0) {
            return super.getMessage();
        }
        return super.getMessage() + System.lineSeparator() +
                "Максимальное значение целых чисел: " + max + ";" + System.lineSeparator() +
                "Минимальное значение целых чисел: " + min + ";" + System.lineSeparator() +
                "Сумма целых чисел: " + sum + ";" + System.lineSeparator() +
                "Среднее целых чисел: " + (double) sum / getCounter() + ";";
    }
}
