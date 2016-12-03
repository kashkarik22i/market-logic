package org.ilya.scheduler.schedule;

import org.ilya.scheduler.request.Meeting;
import org.ilya.scheduler.request.Request;
import org.ilya.scheduler.request.RequestResult;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class NavigableDateSchedule implements Schedule<Meeting> {

    private final NavigableMap<DateTime, Meeting> requests;
    private final LocalTime start;
    private final LocalTime end;

    public NavigableDateSchedule(LocalTime start, LocalTime end) {
        requests = new TreeMap<>();
        this.start = start;
        this.end = end;
    }

    @Override
    public ReadOnlySchedule<Meeting> readOnlyView() {
        return new NavigableReadOnlySchedule();
    }

    @Override
    public RequestExecutor<Meeting> getRequestExecutor() {
        return new SimpleRequestExecutor<>(this);
    }

    @Override
    public RequestResult schedule(Request<? extends Meeting> request) {
        RequestResult result = canSchedule(request);
        if (result.isSuccess()) {
            requests.put(request.getData().getStart(), request.getData());
        }
        return result;
    }

    @Override
    public Iterable<Meeting> getItems() {
        return requests.values();
    }

    @Override
    public RequestResult<Meeting> canSchedule(Request<? extends Meeting> request) {
        Meeting requestedMeeting = request.getData();
        // TODO does not work if a meeting takes place in more than one day
        if (!requestedMeeting.isWithin(allowedHours(requestedMeeting.getStart().toLocalDate()))) {
            return RequestResult.problem("not within working hours");
        }

        Map.Entry<DateTime, Meeting> previousEntry = requests.floorEntry(
                requestedMeeting.getStart());
        if (previousEntry != null && previousEntry.getValue().overlap(requestedMeeting)) {
            return RequestResult.conflict(previousEntry.getValue());
        }

        Map.Entry<DateTime, Meeting> nextEntry = requests.ceilingEntry(
                requestedMeeting.getStart());
        if (nextEntry != null && nextEntry.getValue().overlap(requestedMeeting)) {
            return RequestResult.conflict(nextEntry.getValue());
        }

        return RequestResult.scheduled();
    }

    private Interval allowedHours(LocalDate date) {
        return new Interval(date.toDateTime(start), date.toDateTime(end));
    }

    private class NavigableReadOnlySchedule implements ReadOnlySchedule<Meeting> {

        @Override
        public Iterable<Meeting> getItems() {
            return NavigableDateSchedule.this.getItems();
        }

        @Override
        public RequestResult canSchedule(Request<? extends Meeting> request) {
            return NavigableDateSchedule.this.canSchedule(request);
        }
    }

}
