package org.ilya.marketlogic.scheduler.request;

import org.joda.time.DateTime;

public interface Request<T> {

    RequestType getType();

    T getData();

    DateTime getSubmissionTime();
}
