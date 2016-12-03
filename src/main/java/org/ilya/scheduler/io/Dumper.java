package org.ilya.scheduler.io;

import java.io.File;
import java.io.IOException;

public interface Dumper<T> {

    String toString(T object);

    void toFile(Iterable<T> objects, File file) throws IOException;

}
