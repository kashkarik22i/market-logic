package org.ilya.scheduler.io;

import com.google.common.base.Joiner;
import org.ilya.scheduler.request.Meeting;
import org.ilya.scheduler.request.Request;
import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class MeetingRequestDumper implements Dumper<Request<Meeting>> {

    private static final Joiner SEMICOLON_JOINER = Joiner.on(" ");
    private final Dumper<Meeting> meetingDumper;
    private final DateTimeFormatter submissionFormatter;

    public MeetingRequestDumper(Dumper<Meeting> meetingDumper,
                                DateTimeFormatter submissionFormatter) {
        this.meetingDumper = meetingDumper;
        this.submissionFormatter=submissionFormatter;
    }

    @Override
    public String toString(Request<Meeting> request) {
        return SEMICOLON_JOINER.join(
                submissionFormatter.print(request.getSubmissionTime()),
                meetingDumper.toString(request.getData()));
    }

    @Override
    public void dumpToFile(Iterable<Request<Meeting>> requests, Path filePath) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath,
                StandardCharsets.UTF_8)) {
            for (Request<Meeting> request : requests) {
                writer.write(toString(request));
                writer.newLine();
            }
        }
    }
}
