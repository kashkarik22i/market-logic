package org.ilya.scheduler.io;

import org.ilya.scheduler.request.Request;

public interface RequestDumper<T> extends Dumper<Request<T>> {

    Dumper<T> getDataDumper();

}
