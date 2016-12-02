package org.ilya.scheduler.request;

public class DoNothingRequestNotifier<T> implements RequestNotifier<T> {

    @Override
    public void notify(Request<T> event, RequestResult status) {
        // do nothing
    }
}
