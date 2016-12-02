package org.ilya.scheduler.request;

public interface RequestNotifier<T> {

    void notify(Request<T> event, RequestResult status);

}
