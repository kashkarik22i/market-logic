package org.ilya.scheduler.command;

import org.ilya.scheduler.DefaultMeetingScheduler;
import org.ilya.scheduler.io.MeetingDumper;
import org.ilya.scheduler.io.MeetingRequestDumper;
import org.ilya.scheduler.io.RequestInputFile;
import org.ilya.scheduler.request.*;
import org.ilya.scheduler.MeetingScheduler;
import org.ilya.scheduler.resolution.ConflictResolver;
import org.ilya.scheduler.resolution.FifoConflictResolver;
import org.ilya.scheduler.schedule.NavigableDateSchedule;
import org.ilya.scheduler.schedule.Schedule;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;

import java.nio.file.Paths;

public class SchedulerMain {

    public static void main(String[] args) throws Exception {
        ConflictResolver<MeetingRequest, Meeting> resolver = new FifoConflictResolver();
        //RequestNotifier<Meeting> notifier = new DoNothingRequestNotifier<>();

        MeetingDumper dumper = new MeetingDumper(
                DateTimeFormat.forPattern("y-M-d"),
                DateTimeFormat.forPattern("H:m"));
        MeetingRequestDumper requestDumper = new MeetingRequestDumper(dumper,
                DateTimeFormat.forPattern("y-M-d H:m:s"));

        RequestNotifier<Meeting> notifier = new StdoutRequestNotifier<>(requestDumper);

        RequestInputFile input = new RequestInputFile(
                Paths.get("src/main/java/org/ilya/scheduler/command/test"));
        Interval officeHours = input.getOfficeHours();
        Schedule<Meeting> schedule = new NavigableDateSchedule(officeHours);

        MeetingScheduler<MeetingRequest> scheduler = new DefaultMeetingScheduler<>(
                schedule, resolver, notifier
        );

        scheduler.schedule(input.getRequests());

        dumper.dumpToFile(schedule.getItems(),
                Paths.get("src/main/java/org/ilya/scheduler/command/test.out"));
    }

}
