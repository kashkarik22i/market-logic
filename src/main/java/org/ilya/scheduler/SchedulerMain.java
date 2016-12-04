package org.ilya.scheduler;

import com.github.rvesse.airline.Cli;
import com.github.rvesse.airline.annotations.Arguments;
import com.github.rvesse.airline.annotations.Command;
import com.github.rvesse.airline.annotations.Option;
import com.github.rvesse.airline.annotations.restrictions.Required;
import com.github.rvesse.airline.help.Help;
import com.github.rvesse.airline.model.GlobalMetadata;
import com.github.rvesse.airline.parser.errors.ParseException;
import org.ilya.scheduler.io.FileParseException;
import org.ilya.scheduler.io.MeetingDumper;
import org.ilya.scheduler.io.MeetingRequestDumper;
import org.ilya.scheduler.request.*;
import org.ilya.scheduler.resolution.RequestPrioritizer;
import org.ilya.scheduler.resolution.FifoRequestPrioritizer;
import org.ilya.scheduler.io.RequestInputFile;
import org.ilya.scheduler.schedule.NavigableDateSchedule;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Entry point to run a default version of {@link Scheduler} on specified inputs.
 * Command line parsing is included.
 */
public class SchedulerMain {

    public static Cli<Callable<Integer>> getCli() {
        return Cli.<Callable<Integer>>builder("scheduler")
                .withCommand(SchedulerHelp.class)
                .withDefaultCommand(ScheduleCommand.class)
                .withCommand(ScheduleCommand.class)
                .build();
    }

    @Command(name = "help", description = "Display help information")
    public static class SchedulerHelp implements Callable<Integer>{

        @Inject
        public GlobalMetadata<Callable<Integer>> global;

        @Arguments
        public List<String> command = new ArrayList<>();

        @Override
        public Integer call() throws Exception {
            Help.help(global, command);
            return 0;
        }
    }

    public static void main(String[] args) throws Exception {

        // TODO should use logging throughout the code
        // try to catch exceptions and change exit code
        try {
            System.exit(getCli().parse(args).call());
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    @Command(name = "schedule", description = "Schedule meeting requests loaded from file.")
    public final static class ScheduleCommand implements Callable<Integer> {

        @Required
        @Option(name = { "-i", "--input-file", }, title = "INPUT-FILE",
                description = "Input file to read from.")
        public String input;

        @Option(name = { "-p", "--print-notifications", }, title = "PRINT-NOTIFICATIONS",
                description = "Whether to print notifications. Default do not print.")
        public boolean printNotifications = false;

        @Option(name = { "-s", "--stacktrace", }, title = "PRINT-STACKTRACE",
                description = "Whether to print stacktrace in case of an exception. "
                        + "Default do not print.")
        public boolean printStacktrace = false;

        @Required
        @Option(name = { "-o", "output-file" }, title = "OUTPUT-FILE",
                description = "Output file to write scheduled meetings to.")
        public String output;

        @Override
        public Integer call() {
            validate();
            return doRun();
        }

        private void validate() {
            Path outPath = Paths.get(output);
            if (Files.exists(outPath)) {
                throw new ParseException(String.format("Output file %s already exists", outPath));
            }

            // TODO there seems to be a bug in !Files.isWritable, this doesn't work
            // TODO correctly on mac
            //if (!Files.isWritable(outPath)) {
            //    throw new ParseException(String.format("Output file %s is not writable", outPath));
            //}

            Path inputPath = Paths.get(input);
            if (!Files.exists(inputPath)) {
                throw new ParseException(String.format("Input file %s does not exists", inputPath));
            }
            if (!Files.isRegularFile(inputPath)) {
                throw new ParseException(String.format("Input file %s is not a regular file", inputPath));
            }
        }

        private Integer doRun() {

            // set formats to be used
            // TODO could be added to the command line with some kind of format validation
            MeetingDumper dumper = new MeetingDumper(
                    DateTimeFormat.forPattern("yyyy-MM-dd"),
                    DateTimeFormat.forPattern("HH:mm"));
            MeetingRequestDumper requestDumper = new MeetingRequestDumper(dumper,
                    DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"));


            // create a notifier for scheduling events
            RequestNotifier<Meeting> notifier = printNotifications ?
                    new StdoutRequestNotifier<>(requestDumper) : new DoNothingRequestNotifier<Meeting>();

            // create a resolver for scheduling events
            RequestPrioritizer<MeetingRequest> prioritizer = new FifoRequestPrioritizer();

            // process the input file
            RequestInputFile inputFile = new RequestInputFile(Paths.get(input));

            Iterable<MeetingRequest> requests;
            LocalTime startOfficeHours;
            LocalTime endOfficeHours;
            try {
                requests = inputFile.getRequests();
                startOfficeHours = inputFile.getOfficeHoursStart();
                endOfficeHours = inputFile.getOfficeHoursEnd();
            } catch (FileParseException | IOException e) {
                if (printStacktrace) {
                    e.printStackTrace();
                }
                System.out.println(String.format("Failed to parse file %s. %s", input, e.getMessage()));
                return 1;
            }

            // create a schedule
            NavigableDateSchedule schedule = new NavigableDateSchedule(
                    startOfficeHours, endOfficeHours);

            // create a scheduler
            Scheduler<MeetingRequest> scheduler = new DefaultScheduler<>(
                    schedule, prioritizer, notifier);


            // schedule requests
            scheduler.schedule(requests);

            try {
                dumper.dumpToFile(schedule.getItems(), Paths.get(output));
            } catch (IOException e) {
                if (printStacktrace) {
                    e.printStackTrace();
                }
                System.out.println(String.format("Failed to save results to file %s. %s", input, e.getMessage()));
                return 1;
            }

            return 0;
        }

    }

}
