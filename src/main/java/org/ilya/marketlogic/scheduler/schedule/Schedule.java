package org.ilya.marketlogic.scheduler.schedule;

import org.ilya.marketlogic.scheduler.request.Request;
import org.ilya.marketlogic.scheduler.request.RequestResult;

/**
 * A conflict-free meeting schedule
 */
public interface Schedule<T> extends ReadOnlySchedule<T> {

    ReadOnlySchedule<T> readOnlyView();

    RequestExecutor<T> getRequestExecutor();

    RequestResult schedule(Request<? extends T> request);

}
