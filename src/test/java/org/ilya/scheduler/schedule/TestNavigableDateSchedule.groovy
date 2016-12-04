package org.ilya.scheduler.schedule

import org.ilya.scheduler.request.DefaultMeetingRequest
import org.ilya.scheduler.request.Meeting
import org.ilya.scheduler.request.MeetingDetails
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.LocalTime
import spock.lang.Specification;

public class TestNavigableDateSchedule extends Specification {

    final def start = new LocalTime(10, 0)
    final def end = new LocalTime(18, 30)
    final def now = new DateTime(2016, 12, 03, 17, 0);
    final def hour = Duration.standardHours(1L)
    final def halfHour = Duration.standardMinutes(30L)

    def employee(String employee) {
        new MeetingDetails(employee)
    }

    def "test schedule none"() {
        given:
        def schedule = new NavigableDateSchedule(start, end)

        expect:
        schedule.items.collect() == []
    }

    def "test schedule one"() {
        given:
        def schedule = new NavigableDateSchedule(start, end)
        def meeting = new Meeting(employee('x'), now, hour)

        when:
        schedule.schedule(new DefaultMeetingRequest(meeting, now))

        then:
        schedule.items.collect() == [ meeting ]
    }

    def "test schedule two conflicting"() {
        given:
        def schedule = new NavigableDateSchedule(start, end)
        def meetings = [new Meeting(employee('x'), now, hour),
                        new Meeting(employee('y'), now + halfHour, now + halfHour + hour)]
        def counter = 1L;
        def requests = meetings.collect { new DefaultMeetingRequest(it, (now - hour) - counter++) }

        when:
        requests.each {
            schedule.schedule(it)
        }

        then:
        def items = schedule.items.collect()
        items.size() == 1
        items[0] == meetings[0]
    }

    def "test schedule outside of allowed"() {
        given:
        def schedule = new NavigableDateSchedule(start, end)
        def meeting = new Meeting(employee('x'), now, hour + hour)

        when:
        schedule.schedule(new DefaultMeetingRequest(meeting, now))

        then:
        schedule.items.collect() == []
    }

}
