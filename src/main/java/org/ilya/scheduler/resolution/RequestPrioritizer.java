package org.ilya.scheduler.resolution;

import org.ilya.scheduler.request.Request;

/**
 *
 * Interface for classes which order requests.
 *
 * @param <T> type of requests which are ordered
 */
public interface RequestPrioritizer<T extends Request<?>> {

    Iterable<T> prioritize(Iterable<T> requests);

}
