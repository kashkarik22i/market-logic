package org.ilya.scheduler.resolution;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.ilya.scheduler.request.Meeting;
import org.ilya.scheduler.request.MeetingRequest;
import org.ilya.scheduler.request.RequestResult;
import org.ilya.scheduler.schedule.ReadOnlySchedule;
import org.joda.time.DateTime;

import java.sql.ResultSet;
import java.util.*;

public class FifoConflictResolver extends AbstractConflictResolver<MeetingRequest, Meeting> {

    @Override
    protected Iterable<ResolvedRequest<Meeting>> resolveRequests(
            final ReadOnlySchedule<Meeting> schedule,
            Iterable<MeetingRequest> requests) {

        List<MeetingRequest> sorted = Lists.newArrayList(requests);
        Collections.sort(sorted,
                new Comparator<MeetingRequest>() {
                    @Override
                    public int compare(MeetingRequest r0, MeetingRequest r1) {
                        return r1.getSubmissionTime().compareTo(r0.getSubmissionTime());
                    }
        });

        return Iterables.transform(sorted, new Function<MeetingRequest, ResolvedRequest<Meeting>>() {

            NavigableMap<DateTime, MeetingRequest> accepted = new TreeMap<>();

            @Override
            public ResolvedRequest<Meeting> apply(MeetingRequest request) {
                // TODO naming is confusing here
                RequestResult localConflict = conflicts(request, accepted);
                if (!localConflict.isSuccess()) {
                    return new ResolvedRequest<>(request, localConflict);
                } else {
                    RequestResult<Meeting> schedulingResult = schedule.canSchedule(request);
                    if (!schedulingResult.isSuccess()) {
                        return new ResolvedRequest<>(request, schedulingResult);
                    } else {
                        accepted.put(request.getData().getStart(), request);
                        return new ResolvedRequest<>(request);
                    }
                }
            }
        });
    }

    private RequestResult<Meeting> conflicts(MeetingRequest newRequest, NavigableMap<DateTime, MeetingRequest> existing) {
        // consideration: meeting with length zero with the same start cause troubles
        Meeting requestedMeeting = newRequest.getData();

        Map.Entry<DateTime, MeetingRequest> previousEntry = existing.floorEntry(
                requestedMeeting.getStart());
        if (previousEntry != null && previousEntry.getValue().getData().overlap(requestedMeeting)) {
            return RequestResult.conflict(previousEntry.getValue().getData());
        }

        Map.Entry<DateTime, MeetingRequest> nextEntry = existing.ceilingEntry(
                requestedMeeting.getStart());
        if (nextEntry != null && nextEntry.getValue().getData().overlap(requestedMeeting)) {
            return RequestResult.conflict(nextEntry.getValue().getData());
        }

        return RequestResult.scheduled();
    }

}
