package org.ilya.scheduler.request

import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.Interval
import spock.lang.Specification;

public class TestMeeting extends Specification {

    def now = new DateTime(2016, 12, 03, 18, 0);
    def day = Duration.standardDays(1L)
    def hour = Duration.standardHours(1L)
    def minute = Duration.standardMinutes(1L)

    def interval(from, to) {
        new Interval(from, to)
    }

    def "test meeting data consistency"() {
        given:
        def details = new MeetingDetails('EMP0002')
        def meeting = new Meeting(details, now, now + hour)

        expect:
        meeting.details == details
        meeting.duration == new Duration(hour)
        meeting.start == now
        meeting.end == now + hour
    }

    def "test create illegal meeting"() {
        given:
        def details = new MeetingDetails('EMP0001')

        when:
        new Meeting(details, now, Duration.ZERO)

        then:
        def e = thrown(IllegalArgumentException)
        e.message == 'Meeting\'s start time must be after meetings end time'

        when:
        new Meeting(details, now, now)

        then:
        e = thrown(IllegalArgumentException)
        e.message == 'Meeting\'s start time must be after meetings end time'

        when:
        new Meeting(details, now + minute, now)

        then:
        e = thrown(IllegalArgumentException)
        e.message == 'Meeting\'s start time must be after meetings end time'

        when:
        new Meeting(details, now, now + day)

        then:
        e = thrown(IllegalArgumentException)
        e.message == 'A meeting must start and end on the same day'
    }

    def "test meeting overlap"() {
        given:
        def details = new MeetingDetails('EMP0003')
        def meeting = new Meeting(details, now, now + hour)
        def previousMeeting = new Meeting(details, now - minute, now + minute)
        def nextMeeting = new Meeting(details, now + hour, now + hour + hour)
        def meetingTomorrow = new Meeting(details, now + day, now + day + hour)

        expect:
        meeting.overlap(meeting)
        previousMeeting.overlap(meeting)
        meeting.overlap(previousMeeting)
        !meeting.overlap(nextMeeting)
        !nextMeeting.overlap(meeting)
        !meetingTomorrow.overlap(meeting)
    }

    def "test meeting is within"() {
        given:
        def details = new MeetingDetails('EMP0004')
        def meeting = new Meeting(details, now, now + hour)

        expect:
        meeting.isWithin(interval(now, now + hour))
        meeting.isWithin(interval(now - minute, now + hour + minute))
        !meeting.isWithin(interval(now + minute, now + hour))
        !meeting.isWithin(interval(now - hour, now))
    }
}
