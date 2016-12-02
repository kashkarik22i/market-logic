package org.ilya.scheduler.resolution;

import org.ilya.scheduler.schedule.Action;
import org.ilya.scheduler.request.Request;
import org.ilya.scheduler.request.RequestResult;
import org.ilya.scheduler.request.RequestNotifier;
import org.ilya.scheduler.schedule.ReadOnlySchedule;
import org.ilya.scheduler.schedule.Schedule;

public abstract class AbstractConflictResolver<T extends Request<S>, S>
        implements ConflictResolver<T, S> {

    @Override
    public final Action<Schedule<S>> resolve(ReadOnlySchedule<S> schedule,
                                             final Iterable<T> requests,
                                             final RequestNotifier<S> notifier) {
        final Iterable<ResolvedRequest<S>> resolved = resolveRequests(schedule, requests);
        return new Action<Schedule<S>>() {
            @Override
            public boolean apply(Schedule<S> schedule) throws Exception {
                boolean allApplied = true;
                for (ResolvedRequest<S> resolvedRequest : resolved) {
                    if (resolvedRequest.toSchedule) {
                        allApplied = allApplied
                                && schedule.schedule(resolvedRequest.request).isSuccess();
                    }
                }
                return allApplied;
            }

            @Override
            public void onFailure() {
                for (T request: requests) {
                    notifier.notify(request, RequestResult.problem("internal problem, please try again"));
                }
            }

            @Override
            public void onSuccess() {
                for (ResolvedRequest<S> resolvedRequest : resolved) {
                    if (resolvedRequest.toSchedule) {
                        notifier.notify(resolvedRequest.request,
                                RequestResult.scheduled());
                    } else {
                        notifier.notify(resolvedRequest.request,
                                resolvedRequest.requestFailure);
                    }
                }
            }
        };
    }

    protected abstract Iterable<ResolvedRequest<S>> resolveRequests(ReadOnlySchedule<S> schedule,
                                                                    Iterable<T> requests);

    protected static class ResolvedRequest<R> {

        protected final Request<R> request;
        protected final boolean toSchedule;
        protected final RequestResult requestFailure;

        protected ResolvedRequest(Request<R> request,
                                  boolean toSchedule) {
            this(request, toSchedule, null);
        }

        protected ResolvedRequest(Request<R> request,
                                  boolean toSchedule,
                                  RequestResult requestFailure) {
            this.request = request;
            this.toSchedule = toSchedule;
            this.requestFailure = requestFailure;
        }
    }
}
