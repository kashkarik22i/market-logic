package org.ilya.scheduler.io;

import java.io.File;

public interface Dumper<T> {

    String toString(T object);

    void toFile(Iterable<Object> objects, File file);

}
