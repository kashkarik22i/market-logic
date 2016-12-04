package org.ilya.scheduler.request;

/**
 * Interface for classes which handle request results.
 *
 * @param <T> type of data requested by requests
 *
 * @see RequestResult
 */
public interface RequestNotifier<T> {

    /**
     * A notification that an attempt to execute a request
     * resulted in a given {@link RequestResult}
     *
     * @param request request which was executed
     * @param status results of request execution
     */
    void notify(Request<? extends T> request, RequestResult<T> status);

}
