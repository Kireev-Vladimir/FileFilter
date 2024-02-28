package ru.kireev.exception;

public class NoInputFilesException extends Exception {
    public NoInputFilesException() {
    }

    public NoInputFilesException(String message) {
        super(message);
    }

    public NoInputFilesException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoInputFilesException(Throwable cause) {
        super(cause);
    }
}
