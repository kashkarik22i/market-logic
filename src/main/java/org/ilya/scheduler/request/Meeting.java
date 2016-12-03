package org.ilya.scheduler.request;

import com.google.common.base.Preconditions;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;

public class Meeting {

    private final Interval interval;
    private final MeetingDetails details;

    public Meeting(MeetingDetails details,
                   DateTime start, Duration duration) {
        this(details, start, start.plus(duration));
    }

    public Meeting(MeetingDetails details,
                   DateTime start, DateTime end) {
        Preconditions.checkArgument(start.isBefore(end),
                "Meeting's start time must be after meetings end time");
        Preconditions.checkArgument(start.toLocalDate().equals(end.toLocalDate()),
                "A meeting must start and end on the same day");
        this.interval = new Interval(start, end);
        this.details = details;
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

    public boolean isWithin(Interval interval) {
        return interval.contains(this.interval);
    }

    public MeetingDetails getDetails() {
        return details;
    }

}
