package org.ilya.scheduler.resolution;

import org.ilya.scheduler.schedule.Action;
import org.ilya.scheduler.request.Request;
import org.ilya.scheduler.request.RequestNotifier;
import org.ilya.scheduler.schedule.ReadOnlySchedule;
import org.ilya.scheduler.schedule.Schedule;

public interface ConflictResolver<T extends Request<? extends S>, S> {

    Action<Schedule<S>> resolve(ReadOnlySchedule<S> schedule,
                                Iterable<T> requests,
                                RequestNotifier<S> notifier);

}
