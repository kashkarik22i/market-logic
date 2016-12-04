package org.ilya.scheduler.schedule;

import org.ilya.scheduler.request.Request;
import org.ilya.scheduler.request.RequestResult;

/**
 *
 * Container for a schedule. It is responsible for executing
 * requests, e.g. adding new items to the schedule, and for
 * checking consistency of potential new items in the current
 * state of the schedule. The schedule should check whether
 * the type of the request is supported. The schedule interface
 * if write-only, it makes no assumptions on how to retrieve the data.
 *
 * @param <T> type of the elements stored in the schedule
 */
public interface Schedule<T> {

    /**
     * Try to execute a {@link Request}
     *
     * @param request request to be executed
     * @return {@link RequestResult} containing the information
     * on how successful the execution was
     *
     */
    RequestResult<T> schedule(Request<? extends T> request);

}
