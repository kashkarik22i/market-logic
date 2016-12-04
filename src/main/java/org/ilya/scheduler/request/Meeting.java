package org.ilya.scheduler.request;

import com.google.common.base.Preconditions;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;

/**
 *
 * A data type to represent a meeting which is defined as an interval of
 * time and is restricted to start and end on the same day. A meeting can contain
 * additional details in {@link MeetingDetails}.
 * In an attempt to create an invalid meeting will result in
 * an {@link IllegalArgumentException}
 *
 */
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

    /**
     *
     * Whether the two meetings overlap in time. Is one meeting starts exactly
     * when the other ends they are not said to overlap
     *
     */
    public boolean overlap(Meeting other) {
        return interval.overlap(other.interval) != null;
    }

    /**
     *
     * Whether this meeting is within an interval of time. Since a meeting is a
     * continuous period of time, this can be rephrased as: it starts after the interval start
     * and ends before the interval end
     *
     */
    public boolean isWithin(Interval interval) {
        return interval.contains(this.interval);
    }

    public MeetingDetails getDetails() {
        return details;
    }

}
