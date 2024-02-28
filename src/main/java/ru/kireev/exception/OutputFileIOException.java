package ru.kireev.exception;

import java.io.IOException;

public class OutputFileIOException extends IOException {
    public OutputFileIOException() {
    }

    public OutputFileIOException(String message) {
        super(message);
    }

    public OutputFileIOException(String message, Throwable cause) {
        super(message, cause);
    }

    public OutputFileIOException(Throwable cause) {
        super(cause);
    }
}
