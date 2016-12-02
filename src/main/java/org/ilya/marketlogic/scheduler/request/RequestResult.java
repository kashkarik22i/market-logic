package org.ilya.marketlogic.scheduler.request;

import org.ilya.marketlogic.scheduler.io.Dumper;

public class RequestResult<T> {

    public static ResultType PROBLEM = ResultType.PROBLEM;
    public static ResultType CONFLICT = ResultType.CONFLICT;
    public static ResultType SCHEDULED = ResultType.SCHEDULED;

    private final ResultType type;
    private Request<T> conflictingRequest;
    private String description;

    private RequestResult(ResultType type) {
        this.type = type;
    }

    private RequestResult(ResultType type, Request<T> conflictingRequest) {
        this.type = type;
        this.conflictingRequest = conflictingRequest;
    }

    private RequestResult(ResultType type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getFormattedMessage(Request<T> request, Dumper<Request<T>> dumper) {
        switch (type) {
            case PROBLEM:
                return String.format("Could not schedule \"%s\": %s",
                        dumper.toString(request),
                        description);
            case SCHEDULED:
                return String.format("\"%s\" was scheduled",
                        dumper.toString(request));
            case CONFLICT:
                return String.format("Could not schedule \"%s\": conflicts with \"%s\"",
                        dumper.toString(request),
                        dumper.toString(conflictingRequest));
            default:
                // should not happen
                throw new IllegalStateException("Unknown type of request result");
        }
    }

    public Request<T> getConflictingRequest() {
        return conflictingRequest;
    }

    public String getDescription() {
        return description;
    }

    public boolean isSuccess() { return type.success; }

    public static <R> RequestResult<R> scheduled() {
        return new RequestResult<>(SCHEDULED);
    }

    public static <R> RequestResult<R> conflict(Request<R> conflictingRequest) {
        return new RequestResult<>(CONFLICT, conflictingRequest);
    }

    public static <R> RequestResult<R> problem(String description) {
        return new RequestResult<>(PROBLEM, description);
    }

    private enum ResultType {
        PROBLEM(false),
        CONFLICT(false),
        SCHEDULED(true);

        private final boolean success;

        ResultType(boolean success) {
            this.success = success;
        }

    }

}
