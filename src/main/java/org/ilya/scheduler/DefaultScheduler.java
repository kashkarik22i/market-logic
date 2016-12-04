package org.ilya.scheduler;

import org.ilya.scheduler.request.Request;
import org.ilya.scheduler.request.RequestNotifier;
import org.ilya.scheduler.resolution.ConflictResolver;
import org.ilya.scheduler.schedule.Action;
import org.ilya.scheduler.schedule.RequestExecutor;
import org.ilya.scheduler.schedule.Schedule;

/**
 * Default implementation of {@link Scheduler} with a more complex type.
 * {@link Request Requests} are prioritized by a given {@link ConflictResolver}
 * and are stored in a given {@link Schedule}
 *
 * @param <T> type of requests to be processed
 * @param <S> type of data the schedule can accommodate
 */
public class DefaultScheduler<T extends Request<? extends S>, S> implements Scheduler<T> {

    private final Schedule<S> schedule;
    private final ConflictResolver<T, S> resolver;
    private final RequestExecutor<S> executor;
    private final RequestNotifier notifier;

    public DefaultScheduler(Schedule<S> schedule,
                            ConflictResolver<T, S> resolver,
                            RequestNotifier<S> notifier) {
        this.schedule = schedule;
        this.resolver = resolver;
        this.notifier = notifier;
        this.executor = schedule.getRequestExecutor();
    }

    /**
     *
     * Schedule requests. When called several times, prioritization
     * is performed on every separate batch and all requested elements
     * are stores in the {@link Schedule}
     *
     * @param requests requests to be scheduled
     */
    @Override
    public void schedule(Iterable<T> requests) {
        Action<Schedule<S>> action = resolver.resolve(schedule.readOnlyView(), requests, notifier);
        executor.execute(action);
    }

}
