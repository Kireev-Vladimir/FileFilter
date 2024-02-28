package ru.kireev.statistics;

public interface Statistics<T> {
    void add(T element);

    String getMessage();
}
