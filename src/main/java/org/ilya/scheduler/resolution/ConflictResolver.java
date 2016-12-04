package org.ilya.scheduler.resolution;

import org.ilya.scheduler.request.Request;

public interface ConflictResolver<T extends Request<?>> {

    Iterable<T> resolve(Iterable<T> requests);

}
