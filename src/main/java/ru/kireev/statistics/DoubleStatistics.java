package ru.kireev.statistics;

public class DoubleStatistics extends SimpleStatistics<Double> {
    private double min = Double.MAX_VALUE;
    private double max = Double.MIN_VALUE;
    private double sum = 0L;

    public DoubleStatistics() {
        super("Вещественное число");
    }

    @Override
    public void add(Double element) {
        super.add(element);
        min = Double.min(min, element);
        max = Double.max(max, element);
        sum += element;
    }

    @Override
    public String getMessage() {
        if (getCounter() == 0) {
            return super.getMessage();
        }
        return super.getMessage() + System.lineSeparator() +
                "Максимальное значение вещественных чисел: " + max + ";" + System.lineSeparator() +
                "Минимальное значение вещественных чисел: " + min + ";" + System.lineSeparator() +
                "Сумма вещественных чисел: " + sum + ";" + System.lineSeparator() +
                "Среднее вещественных чисел: " + sum / getCounter() + ";";
    }
}
