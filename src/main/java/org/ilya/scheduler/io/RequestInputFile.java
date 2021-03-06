package org.ilya.scheduler.io;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.ilya.scheduler.request.MeetingRequest;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

// TODO this class is to specific and looks rather ad hoc
/**
 * A very rudimentary class for parsing a particular type of
 * files. It was written with no plan for re-use or flexibility.
 * It should be converted into something more generic
 */
public class RequestInputFile {

    private static final Joiner SPACE_JOINER = Joiner.on(" ");
    private static final Splitter SPACE_SPLITTER = Splitter.on(" ");

    private LocalTime officeHoursStart;
    private LocalTime officeHoursEnd;
    private List<MeetingRequest> requests;

    private final Path filePath;

    public RequestInputFile(Path filePath) {
        this.filePath = filePath;
    }

    public Iterable<MeetingRequest> getRequests() throws IOException, FileParseException {
        if (requests == null) {
            parseFile();
        }
        return requests;
    }


    public LocalTime getOfficeHoursStart() throws IOException, FileParseException {
        if (officeHoursStart == null) {
            parseFile();
        }
        return officeHoursStart;
    }

    public LocalTime getOfficeHoursEnd() throws IOException, FileParseException {
        if (officeHoursEnd == null) {
            parseFile();
        }
        return officeHoursEnd;
    }

    private void parseFile() throws IOException, FileParseException {
        MeetingRequestParser parser = new MeetingRequestParser(
                DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss"),
                DateTimeFormat.forPattern("yyyy-MM-dd HH:mm"),
                new PeriodFormatterBuilder().appendHours().toFormatter());
        try (BufferedReader reader = Files.newBufferedReader(filePath,
                StandardCharsets.UTF_8)) {
            String line = reader.readLine();
            if (line == null) {
                throw new FileParseException(0, "File is empty");
            }
            DateTimeFormatter firstLineFormat = DateTimeFormat.forPattern("HHmm");
            List<String> firstSplit = Lists.newArrayList(SPACE_SPLITTER.split(line));
            if (firstSplit.size() != 2) {
                throw new FileParseException(1, "First line is not correctly formatted");
            }
            officeHoursStart = firstLineFormat.parseLocalTime(firstSplit.get(0));
            officeHoursEnd = firstLineFormat.parseLocalTime(firstSplit.get(1));

            requests = Lists.newArrayList();
            boolean evenLine = true;
            int lineNumber = 1;
            while((line = reader.readLine()) != null) {
                lineNumber++;
                List<String> split = Lists.newArrayList(SPACE_SPLITTER.limit(3).split(line));
                if (split.size() != 3) {
                    throw new FileParseException(lineNumber,
                            String.format("Line \"%s\" is not correctly formatted", line));
                }
                String dateTimeString = SPACE_JOINER.join(split.get(0), split.get(1));
                try {
                    if (evenLine) {
                        parser.parseSubmissionTime(dateTimeString);
                        parser.parseEmployee(split.get(2));
                    } else {
                        parser.parseMeetingStart(dateTimeString);
                        parser.parseDuration(split.get(2));

                        requests.add(parser.getMeetingRequest());
                        parser.clear();
                    }
                    // TODO this is quite BAD, need to wrap it with a custom one
                    // TODO the problem is that this is what joda does
                } catch (IllegalArgumentException e) {
                    throw new FileParseException(lineNumber,
                            String.format("Line \"%s\" is not correctly formatted", line));
                }
                evenLine = !evenLine;
            }
        }
    }
}
