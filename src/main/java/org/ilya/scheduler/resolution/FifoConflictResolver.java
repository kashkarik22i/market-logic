package org.ilya.scheduler.resolution;

import com.google.common.collect.Lists;
import org.ilya.scheduler.request.MeetingRequest;

import java.util.*;

public class FifoConflictResolver implements ConflictResolver<MeetingRequest> {

    @Override
    public Iterable<MeetingRequest> resolve(Iterable<MeetingRequest> requests) {
        List<MeetingRequest> sorted = Lists.newArrayList(requests);
        Collections.sort(sorted,
                new Comparator<MeetingRequest>() {
                    @Override
                    public int compare(MeetingRequest r0, MeetingRequest r1) {
                        return r0.getSubmissionTime().compareTo(r1.getSubmissionTime());
                    }
                });
        return sorted;
    }
}
