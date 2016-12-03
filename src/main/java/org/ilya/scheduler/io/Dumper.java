package org.ilya.scheduler.io;

import java.io.IOException;
import java.nio.file.Path;

public interface Dumper<T> {

    String toString(T object);

    void dumpToFile(Iterable<T> objects, Path filePath) throws IOException;

}
