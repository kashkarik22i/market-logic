package org.ilya.scheduler.request;

public interface RequestNotifier<T> {

    void notify(Request<? extends T> event, RequestResult<T> status);

}
