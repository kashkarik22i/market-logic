package org.ilya.marketlogic.scheduler.resolution;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.ilya.marketlogic.scheduler.request.Meeting;
import org.ilya.marketlogic.scheduler.request.MeetingRequest;
import org.ilya.marketlogic.scheduler.request.RequestResult;
import org.ilya.marketlogic.scheduler.schedule.ReadOnlySchedule;
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
                        return r0.getSubmissionTime().compareTo(r1.getSubmissionTime());
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

        MeetingRequest previous = existing.floorEntry(
                requestedMeeting.getStart()).getValue();
        if (previous.getData().overlap(requestedMeeting)) {
            return RequestResult.conflict(previous);
        }

        MeetingRequest next = existing.ceilingEntry(
                requestedMeeting.getStart()).getValue();
        if (next.getData().overlap(requestedMeeting)) {
            return RequestResult.conflict(next);
        }

        return null;
    }

}
