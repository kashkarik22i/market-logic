package org.ilya.scheduler;

import org.ilya.scheduler.request.Request;
import org.ilya.scheduler.request.RequestNotifier;
import org.ilya.scheduler.request.RequestResult;
import org.ilya.scheduler.resolution.RequestPrioritizer;
import org.ilya.scheduler.schedule.Schedule;

/**
 * Default implementation of {@link Scheduler} with a more complex type.
 * {@link Request Requests} are prioritized by a given {@link RequestPrioritizer}
 * and are stored in a given {@link Schedule}
 *
 * @param <T> type of requests to be processed
 * @param <S> type of data the schedule can accommodate
 */
public class DefaultScheduler<T extends Request<? extends S>, S> implements Scheduler<T> {

    private final Schedule<S> schedule;
    private final RequestPrioritizer<T> resolver;
    private final RequestNotifier<S> notifier;

    public DefaultScheduler(Schedule<S> schedule,
                            RequestPrioritizer<T> resolver,
                            RequestNotifier<S> notifier) {
        this.schedule = schedule;
        this.resolver = resolver;
        this.notifier = notifier;
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
        Iterable<T> prioritized = resolver.prioritize(requests);
        // TODO support batch mode for requests which can only be executed together
        for (T request : prioritized) {
            RequestResult<S> result = schedule.schedule(request);
            notifier.notify(request, result);
        }
    }

}
