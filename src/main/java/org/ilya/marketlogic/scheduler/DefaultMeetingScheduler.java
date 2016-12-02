package org.ilya.marketlogic.scheduler;

import org.ilya.marketlogic.scheduler.request.Request;
import org.ilya.marketlogic.scheduler.request.RequestNotifier;
import org.ilya.marketlogic.scheduler.resolution.ConflictResolver;
import org.ilya.marketlogic.scheduler.schedule.Action;
import org.ilya.marketlogic.scheduler.schedule.RequestExecutor;
import org.ilya.marketlogic.scheduler.schedule.Schedule;

public class DefaultMeetingScheduler<T extends Request<? extends S>, S> implements MeetingScheduler<T> {

    private final Schedule<S> schedule;
    private final ConflictResolver<T, S> resolver;
    private final RequestExecutor<S> executor;
    private final RequestNotifier notifier;

    public DefaultMeetingScheduler(Schedule<S> schedule,
                                   ConflictResolver<T, S> resolver,
                                   RequestNotifier<S> notifier) {
        this.schedule = schedule;
        this.resolver = resolver;
        this.notifier = notifier;
        this.executor = schedule.getRequestExecutor();
    }

    @Override
    public boolean schedule(Iterable<T> requests) {
        Action<Schedule<S>> action = resolver.resolve(schedule.readOnlyView(), requests, notifier);
        return executor.execute(action);
    }

}
