package org.ilya.scheduler.io;

public class FileParseException extends Exception {

    private final int lineNumber;

    public FileParseException(int lineNumber, String message) {
        super(String.format("%s [line number: %d]", message, lineNumber));
        this.lineNumber = lineNumber;
    }

    public int getLineNumber() {
        return lineNumber;
    }

}
