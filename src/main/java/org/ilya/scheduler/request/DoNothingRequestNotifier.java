package org.ilya.scheduler.request;

/**
 * A {@link RequestNotifier} which does nothing
 */
public class DoNothingRequestNotifier<T> implements RequestNotifier<T> {

    @Override
    public void notify(Request<? extends T> event, RequestResult<T> status) {
        // do nothing
    }
}
