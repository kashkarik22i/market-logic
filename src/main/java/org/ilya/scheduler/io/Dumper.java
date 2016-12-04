package org.ilya.scheduler.io;

import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * A class which can be used to give string representation to data types.
 * It encapsulates the way data is represented.
 *
 * @param <T> type of the data to dump
 */
public interface Dumper<T> {

    /**
     *
     * Gives string representation of the data
     *
     */
    String toString(T object);

    /**
     *
     * Writes a collection of data elements to a file
     *
     */
    void dumpToFile(Iterable<? extends T> objects, Path filePath) throws IOException;

}
