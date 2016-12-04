package org.ilya.scheduler.resolution

import org.ilya.scheduler.request.DefaultMeetingRequest
import org.ilya.scheduler.request.DoNothingRequestNotifier
import org.ilya.scheduler.request.Meeting
import org.ilya.scheduler.request.MeetingDetails
import org.joda.time.DateTime
import org.joda.time.Duration
import spock.lang.Specification

class TestFifoConflictResolver extends Specification {

    final def now = new DateTime(2016, 12, 03, 18, 0);
    final def hour = Duration.standardHours(1L)
    final def halfHour = Duration.standardMinutes(30L)

    def employee(String employee) {
        new MeetingDetails(employee)
    }

    def "test resolve empty"() {
        given:
        def resolver = new FifoConflictResolver()

        when:
        def resolved = resolver.resolve([]).collect()

        then:
        resolved == []
    }

    def "test resolve two non-conflicting"() {
        given:
        def resolver = new FifoConflictResolver()
        def meetings = [new Meeting(employee('x'), now, hour),
                        new Meeting(employee('y'), now + hour, now + hour + hour)]
        def counter = 1L;
        def requests = meetings.collect { new DefaultMeetingRequest(it, (now - hour) - counter++) }

        when:
        def resolved = resolver.resolve(requests).collect()

        then:
        resolved == requests.reverse() // in the order of submission
    }

    def "test resolve two conflicting"() {
        given:
        def resolver = new FifoConflictResolver()
        def meetings = [new Meeting(employee('x'), now, hour),
                        new Meeting(employee('y'), now + halfHour, now + halfHour + hour)]
        def counter = 1L;
        def requests = meetings.collect { new DefaultMeetingRequest(it, (now - hour) + counter++) }

        when:
        def resolved = resolver.resolve(requests).collect()

        then:
        resolved == requests
    }

}
