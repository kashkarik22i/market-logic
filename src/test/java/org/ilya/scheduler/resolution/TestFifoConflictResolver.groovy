package org.ilya.scheduler.resolution

import org.ilya.scheduler.request.DefaultMeetingRequest
import org.ilya.scheduler.request.DoNothingRequestNotifier
import org.ilya.scheduler.request.Meeting
import org.ilya.scheduler.request.MeetingDetails
import org.ilya.scheduler.request.Request
import org.ilya.scheduler.request.RequestResult
import org.ilya.scheduler.schedule.ReadOnlySchedule
import org.ilya.scheduler.schedule.RequestExecutor
import org.ilya.scheduler.schedule.Schedule
import org.ilya.scheduler.schedule.SimpleRequestExecutor
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

    def createSchedule() {
        new Schedule<Meeting>() {
            def items = []

            @Override
            ReadOnlySchedule<Meeting> readOnlyView() {
                this
            }

            @Override
            RequestExecutor<Meeting> getRequestExecutor() {
                new SimpleRequestExecutor<Meeting>(this)
            }

            @Override
            RequestResult<Meeting> schedule(Request<? extends Meeting> request) {
                items.add(request.data)
                RequestResult.scheduled()
            }

            @Override
            List<Meeting> getItems() {
                items
            }

            @Override
            RequestResult<Meeting> canSchedule(Request<? extends Meeting> request) {
                RequestResult.scheduled()
            }
        }
    }


    def "test resolve empty"() {
        given:
        def resolver = new FifoConflictResolver()
        def notifier = new DoNothingRequestNotifier()

        when:
        def schedule = createSchedule()
        // TODO mimics default scheduler, so something is not right with the setup
        schedule.requestExecutor.execute(resolver.resolve(schedule.readOnlyView(), [], notifier))

        then:
        schedule.items == []
    }

    def "test resolve two non-conflicting"() {
        given:
        def resolver = new FifoConflictResolver()
        def notifier = new DoNothingRequestNotifier()
        def meetings = [new Meeting(employee('x'), now, hour),
                        new Meeting(employee('y'), now + hour, now + hour + hour)]
        def counter = 1L;
        def requests = meetings.collect { new DefaultMeetingRequest(it, (now - hour) - counter++) }

        when:
        def schedule = createSchedule()
        // TODO mimics default scheduler, so something is not right with the setup
        schedule.requestExecutor.execute(resolver.resolve(schedule.readOnlyView(), requests, notifier))

        then:
        schedule.items == meetings.reverse() // in the order of submission
    }

    def "test resolve two conflicting"() {
        given:
        def resolver = new FifoConflictResolver()
        def notifier = new DoNothingRequestNotifier()
        def meetings = [new Meeting(employee('x'), now, hour),
                        new Meeting(employee('y'), now + halfHour, now + halfHour + hour)]
        def counter = 1L;
        def requests = meetings.collect { new DefaultMeetingRequest(it, (now - hour) + counter++) }

        when:
        def schedule = createSchedule()
        // TODO mimics default scheduler, so something is not right with the setup
        schedule.requestExecutor.execute(resolver.resolve(schedule.readOnlyView(), requests, notifier))

        then:
        def items = schedule.items
        items.size() == 1
        items[0] == meetings[0]
    }

}
