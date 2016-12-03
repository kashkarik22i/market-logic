package org.ilya.scheduler.request;

import org.joda.time.DateTime;
import org.joda.time.Duration;

public class DefaultMeetingRequest implements MeetingRequest {

    private final RequestType type;
    private Meeting meeting;
    private DateTime submissionTime;

    public DefaultMeetingRequest(RequestType type, DateTime submissionTime) {
        this.type = type;
        this.submissionTime = submissionTime;
    }

    public DefaultMeetingRequest withData(MeetingDetails details,
                                          DateTime start, DateTime end) {
        this.meeting = new Meeting(this, details, start, end);
        return this;
    }

    public DefaultMeetingRequest withData(MeetingDetails details,
                                          DateTime start, Duration duration) {
        this.meeting = new Meeting(this, details, start, duration);
        return this;
    }

    @Override
    public RequestType getType() {
        return type;
    }

    @Override
    public Meeting getData() {
        if (meeting == null) {
            throw new IllegalStateException("Cannot retrieve requested meeting, because it was not set");
        }
        return meeting;
    }

    @Override
    public DateTime getSubmissionTime() {
        return submissionTime;
    }
}
