package org.ilya.scheduler.resolution;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.ilya.scheduler.request.Meeting;
import org.ilya.scheduler.request.MeetingRequest;
import org.ilya.scheduler.request.RequestResult;
import org.ilya.scheduler.schedule.ReadOnlySchedule;
import org.joda.time.DateTime;

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
                RequestResult conflict = conflicts(request, accepted);
                if (conflict == null && schedule.canSchedule(request).isSuccess()) {
                    accepted.put(request.getData().getStart(), request);
                    return new ResolvedRequest<>(request, true);
                } else {
                    return new ResolvedRequest<>(request, false, conflict);
                }
            }
        });
    }

    private RequestResult conflicts(MeetingRequest newRequest, NavigableMap<DateTime, MeetingRequest> existing) {
        // consideration: meeting with length zero with the same start cause troubles
        Meeting requestedMeeting = newRequest.getData();

        Map.Entry<DateTime, MeetingRequest> previousEntry = existing.floorEntry(
                requestedMeeting.getStart());
        if (previousEntry != null && previousEntry.getValue().getData().overlap(requestedMeeting)) {
            return RequestResult.conflict(previousEntry.getValue());
        }

        Map.Entry<DateTime, MeetingRequest> nextEntry = existing.ceilingEntry(
                requestedMeeting.getStart());
        if (nextEntry != null && nextEntry.getValue().getData().overlap(requestedMeeting)) {
            return RequestResult.conflict(nextEntry.getValue());
        }

        return null;
    }

}
