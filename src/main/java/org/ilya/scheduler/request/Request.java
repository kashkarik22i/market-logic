package org.ilya.scheduler.request;

import org.joda.time.DateTime;

/**
 *
 * Class represents a request a request to make an action with some data.
 * A request has a type of {@link RequestType} to represent the action,
 * and its data can be anything.
 *
 * @param <T> type of the data this request contains
 */

public interface Request<T> {

    RequestType getType();

    T getData();

    //TODO move it to MeetingReques
    DateTime getSubmissionTime();
}
