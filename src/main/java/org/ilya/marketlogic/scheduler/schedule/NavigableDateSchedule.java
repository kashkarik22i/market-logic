package org.ilya.marketlogic.scheduler.schedule;

import com.google.common.base.Preconditions;
import org.ilya.marketlogic.scheduler.request.Meeting;
import org.ilya.marketlogic.scheduler.request.Request;
import org.ilya.marketlogic.scheduler.request.RequestResult;
import org.joda.time.DateTime;

import java.util.NavigableMap;
import java.util.TreeMap;

public class NavigableDateSchedule implements Schedule<Meeting> {

    final NavigableMap<DateTime, Meeting> requests;

    public NavigableDateSchedule() {
        requests = new TreeMap<>();
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

        Meeting previousMeeting = requests.floorEntry(
                requestedMeeting.getStart()).getValue();
        if (previousMeeting.overlap(requestedMeeting)) {
            return RequestResult.conflict(previousMeeting.getRequest());
        }

        Meeting nextMeeting = requests.ceilingEntry(
                requestedMeeting.getStart()).getValue();
        if (nextMeeting.overlap(requestedMeeting)) {
            return RequestResult.conflict(nextMeeting.getRequest());
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
