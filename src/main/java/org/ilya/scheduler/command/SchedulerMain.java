package org.ilya.scheduler.command;

import org.ilya.scheduler.DefaultMeetingScheduler;
import org.ilya.scheduler.request.DoNothingRequestNotifier;
import org.ilya.scheduler.request.Meeting;
import org.ilya.scheduler.request.MeetingRequest;
import org.ilya.scheduler.MeetingScheduler;
import org.ilya.scheduler.request.RequestNotifier;
import org.ilya.scheduler.resolution.ConflictResolver;
import org.ilya.scheduler.resolution.FifoConflictResolver;
import org.ilya.scheduler.schedule.NavigableDateSchedule;
import org.ilya.scheduler.schedule.Schedule;
import org.joda.time.Interval;

public class SchedulerMain {

    public static void main(String[] args) {
        Schedule<Meeting> schedule = new NavigableDateSchedule(new Interval(100, 200));
        ConflictResolver<MeetingRequest, Meeting> resolver = new FifoConflictResolver();
        RequestNotifier<Meeting> notifier = new DoNothingRequestNotifier<>();


        MeetingScheduler<MeetingRequest> scheduler = new DefaultMeetingScheduler<MeetingRequest, Meeting>(
              schedule, resolver, notifier
        );
    }

}
