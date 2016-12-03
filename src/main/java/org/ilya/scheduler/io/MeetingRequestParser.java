package org.ilya.scheduler.io;

import org.ilya.scheduler.request.DefaultMeetingRequest;
import org.ilya.scheduler.request.MeetingDetails;
import org.ilya.scheduler.request.MeetingRequest;
import org.ilya.scheduler.request.RequestType;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;

public class MeetingRequestParser {

    private static final RequestType DEFAULT_REQUEST_TYPE = RequestType.ADD_REQUEST;
    private DateTime start;
    private Duration duration;
    private DateTime submission;
    private String employee;

    private final DateTimeFormatter submissionFormatter;
    private final DateTimeFormatter meetingFormatter;
    private final PeriodFormatter durationFormatter;

    public MeetingRequestParser(DateTimeFormatter submissionFormatter,
                                DateTimeFormatter meetingFormatter,
                                PeriodFormatter durationFormatter) {
        this.submissionFormatter = submissionFormatter;
        this.meetingFormatter = meetingFormatter;
        this.durationFormatter = durationFormatter;
    }

    public void parseSubmissionTime(String submissionTime) {
        this.submission = submissionFormatter.parseDateTime(submissionTime);
    }

    public void parseEmployee(String employee) {
        this.employee = employee;
    }

    public void parseDuration(String duration) {
        this.duration = durationFormatter.parsePeriod(duration).toStandardDuration();
    }

    public void parseMeetingStart(String meetingStart) {
        this.start = meetingFormatter.parseDateTime(meetingStart);
    }

    public MeetingRequest getMeetingRequest() {
        validate();
        return new DefaultMeetingRequest(DEFAULT_REQUEST_TYPE, submission)
                .withData(new MeetingDetails(employee), start, duration);
    }

    public void clear() {
        start = null;
        duration = null;
        submission = null;
        employee = null;
    }

    private void validate() {
        if (start == null || duration == null
                || submission == null || employee == null) {
            // TODO improve the exception
            throw new IllegalStateException("Cannot construct a meting request");
        }
    }

}
