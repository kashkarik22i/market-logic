package org.ilya.marketlogic.scheduler.schedule;

import org.ilya.marketlogic.scheduler.request.Request;
import org.ilya.marketlogic.scheduler.request.RequestResult;

public interface ReadOnlySchedule<T> {

    Iterable<T> getItems();

    RequestResult canSchedule(Request<? extends T> request);

}
