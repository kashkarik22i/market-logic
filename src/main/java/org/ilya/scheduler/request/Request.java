package org.ilya.scheduler.request;

import org.joda.time.DateTime;

public interface Request<T> {

    RequestType getType();

    T getData();

    DateTime getSubmissionTime();
}
