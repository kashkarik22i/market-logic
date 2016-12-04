package org.ilya.scheduler;

import org.ilya.scheduler.request.Request;

/**
 *
 * Interface for classes which can schedule.
 *
 * @param <T> type of requests
 *
 * @see DefaultScheduler
 * @see Request
 */
public interface Scheduler<T extends Request<?>> {

    /**
     *
     * Schedule requests.
     *
     * @param requests requests to be scheduled
     */
    void schedule(Iterable<T> requests);

}
