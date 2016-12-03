package org.ilya.scheduler.request;

import org.ilya.scheduler.io.RequestDumper;

public class RequestResult<T> {

    public static ResultType PROBLEM = ResultType.PROBLEM;
    public static ResultType CONFLICT = ResultType.CONFLICT;
    public static ResultType SCHEDULED = ResultType.SCHEDULED;

    private final ResultType type;
    private T conflictingItem;
    private String description;

    private RequestResult(ResultType type) {
        this.type = type;
    }

    private RequestResult(ResultType type, T conflictingItem) {
        this.type = type;
        this.conflictingItem = conflictingItem;
    }

    private RequestResult(ResultType type, String description) {
        this.type = type;
        this.description = description;
    }

    public String getFormattedMessage(Request<T> request,
                                      RequestDumper<T> dumper) {
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
                        dumper.getDataDumper().toString(conflictingItem));
            default:
                // should not happen
                throw new IllegalStateException("Unknown type of request result");
        }
    }

    public boolean isSuccess() { return type.success; }

    public static <R> RequestResult<R> scheduled() {
        return new RequestResult<>(SCHEDULED);
    }

    public static <R> RequestResult<R> conflict(R conflictingItem) {
        return new RequestResult<>(CONFLICT, conflictingItem);
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
