package org.ilya.scheduler.schedule;

import org.ilya.scheduler.request.Request;
import org.ilya.scheduler.request.RequestResult;

public interface ReadOnlySchedule<T> {

    Iterable<T> getItems();

    RequestResult canSchedule(Request<? extends T> request);

}
