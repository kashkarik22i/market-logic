package org.ilya.scheduler.resolution;

import com.google.common.collect.Lists;
import org.ilya.scheduler.request.MeetingRequest;

import java.util.*;

/**
 *
 * A {@link RequestPrioritizer} which orders requests based on their submission time.
 *
 */
public class FifoRequestPrioritizer implements RequestPrioritizer<MeetingRequest> {

    @Override
    public Iterable<MeetingRequest> prioritize(Iterable<MeetingRequest> requests) {
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
