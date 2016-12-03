package org.ilya.scheduler.request;

import com.google.common.base.Preconditions;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;

public class Meeting {

    private final Interval interval;
    private final MeetingDetails details;
    private final Request<Meeting> request;

    public Meeting(Request<Meeting> request, MeetingDetails details,
                   DateTime start, Duration duration) {
        Preconditions.checkArgument(!duration.isEqual(Duration.ZERO),
                "Meeting's duration is zero");
        this.interval = new Interval(start, duration);
        this.details = details;
        this.request = request;
    }

    public Meeting(Request<Meeting> request, MeetingDetails details,
                   DateTime start, DateTime end) {
        Preconditions.checkArgument(start.isEqual(end),
                "Meeting's start time and end time are identical");
        this.interval = new Interval(start, end);
        this.details = details;
        this.request = request;
    }

    public DateTime getStart() {
        return interval.getStart();
    }

    public Duration getDuration() {
        return interval.toDuration();
    }

    public DateTime getEnd() {
        return interval.getEnd();
    }

    public boolean overlap(Meeting other) {
        return interval.overlap(other.interval) != null;
    }

    public boolean overlap(Interval interval) {
        return this.interval.overlap(interval) != null;
    }

    public MeetingDetails getDetails() {
        return details;
    }

    public Request<Meeting> getRequest() {
        return request;
    }

}
