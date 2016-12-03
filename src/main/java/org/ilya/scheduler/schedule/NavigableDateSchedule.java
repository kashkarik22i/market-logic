package org.ilya.scheduler.schedule;

import com.google.common.base.Preconditions;
import org.ilya.scheduler.request.Meeting;
import org.ilya.scheduler.request.Request;
import org.ilya.scheduler.request.RequestResult;
import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

public class NavigableDateSchedule implements Schedule<Meeting> {

    final NavigableMap<DateTime, Meeting> requests;
    final Interval allowedHours;

    public NavigableDateSchedule(Interval allowedHours) {
        requests = new TreeMap<>();
        this.allowedHours = allowedHours;
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
        // consideration: meeting with length zero with the same start cause troubles
        Preconditions.checkArgument(request.getData().getStart()
                .compareTo(request.getData().getEnd()) < 0);
        Meeting requestedMeeting = request.getData();
        if (requestedMeeting.overlap(allowedHours)) {
            return RequestResult.problem("not within working hours");
        }

        Map.Entry<DateTime, Meeting> previousEntry = requests.floorEntry(
                requestedMeeting.getStart());
        if (previousEntry != null && previousEntry.getValue().overlap(requestedMeeting)) {
            return RequestResult.conflict(previousEntry.getValue().getRequest());
        }

        Map.Entry<DateTime, Meeting> nextEntry = requests.ceilingEntry(
                requestedMeeting.getStart());
        if (nextEntry != null && nextEntry.getValue().overlap(requestedMeeting)) {
            return RequestResult.conflict(nextEntry.getValue().getRequest());
        }

        return RequestResult.scheduled();
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
