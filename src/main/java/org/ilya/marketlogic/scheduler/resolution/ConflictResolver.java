package org.ilya.marketlogic.scheduler.resolution;

import org.ilya.marketlogic.scheduler.schedule.Action;
import org.ilya.marketlogic.scheduler.request.Request;
import org.ilya.marketlogic.scheduler.request.RequestNotifier;
import org.ilya.marketlogic.scheduler.schedule.ReadOnlySchedule;
import org.ilya.marketlogic.scheduler.schedule.Schedule;

public interface ConflictResolver<T extends Request<? extends S>, S> {

    Action<Schedule<S>> resolve(ReadOnlySchedule<S> schedule,
                                Iterable<T> requests,
                                RequestNotifier<S> notifier);

}
