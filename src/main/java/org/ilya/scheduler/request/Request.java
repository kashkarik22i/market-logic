package org.ilya.scheduler.request;

import org.joda.time.DateTime;

public interface Request<T> {

    RequestType getType();

    T getData();

    //TODO move it to MeetingReques
    DateTime getSubmissionTime();
}
