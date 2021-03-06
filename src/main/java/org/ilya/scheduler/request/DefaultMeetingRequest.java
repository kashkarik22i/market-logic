package org.ilya.scheduler.request;

import org.joda.time.DateTime;

/**
 * Default implementation for a {@link Request} of a {@link Meeting}
 */
public class DefaultMeetingRequest implements MeetingRequest {

    private final RequestType type;
    private final Meeting meeting;
    private final DateTime submissionTime;

    public DefaultMeetingRequest(RequestType type, Meeting meeting, DateTime submissionTime) {
        this.type = type;
        this.submissionTime = submissionTime;
        this.meeting = meeting;
    }

    public DefaultMeetingRequest(Meeting meeting, DateTime submissionTime) {
        this(RequestType.ADD_REQUEST, meeting, submissionTime);
    }

    @Override
    public RequestType getType() {
        return type;
    }

    @Override
    public Meeting getData() {
        return meeting;
    }

    @Override
    public DateTime getSubmissionTime() {
        return submissionTime;
    }
}
