package org.ilya.marketlogic.scheduler.command;

import org.ilya.marketlogic.scheduler.DefaultMeetingScheduler;
import org.ilya.marketlogic.scheduler.request.DoNothingRequestNotifier;
import org.ilya.marketlogic.scheduler.request.Meeting;
import org.ilya.marketlogic.scheduler.request.MeetingRequest;
import org.ilya.marketlogic.scheduler.MeetingScheduler;
import org.ilya.marketlogic.scheduler.request.RequestNotifier;
import org.ilya.marketlogic.scheduler.resolution.ConflictResolver;
import org.ilya.marketlogic.scheduler.resolution.FifoConflictResolver;
import org.ilya.marketlogic.scheduler.schedule.NavigableDateSchedule;
import org.ilya.marketlogic.scheduler.schedule.Schedule;

public class SchedulerMain {

    public static void main(String[] args) {
        Schedule<Meeting> schedule = new NavigableDateSchedule();
        ConflictResolver<MeetingRequest, Meeting> resolver = new FifoConflictResolver();
        RequestNotifier<Meeting> notifier = new DoNothingRequestNotifier<>();


        MeetingScheduler<MeetingRequest> scheduler = new DefaultMeetingScheduler<MeetingRequest, Meeting>(
              schedule, resolver, notifier
        );
    }

}
