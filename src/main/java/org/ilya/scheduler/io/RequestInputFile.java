package org.ilya.scheduler.io;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.ilya.scheduler.request.MeetingRequest;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class RequestInputFile {

    private static final Joiner SPACE_JOINER = Joiner.on(" ");
    private static final Splitter SPACE_SPLITTER = Splitter.on(" ");

    private Interval officeHours;
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


    public Interval getOfficeHours() throws IOException, FileParseException {
        if (officeHours == null) {
            parseFile();
        }
        return officeHours;
    }

    private void parseFile() throws IOException, FileParseException {
        MeetingRequestParser parser = new MeetingRequestParser(
                DateTimeFormat.forPattern("y-M-d H:m:s"),
                DateTimeFormat.forPattern("y-M-d H:m"),
                new PeriodFormatterBuilder().appendHours().toFormatter());
        try (BufferedReader reader = Files.newBufferedReader(filePath,
                StandardCharsets.UTF_8)) {
            String line = reader.readLine();
            DateTimeFormatter firstLineFormat = DateTimeFormat.forPattern("Hm");
            List<String> firstSplit = Lists.newArrayList(SPACE_SPLITTER.split(line));
            if (firstSplit.size() != 2) {
                throw new FileParseException();
            }
            officeHours = new Interval(firstLineFormat.parseDateTime(firstSplit.get(0)),
                    firstLineFormat.parseDateTime(firstSplit.get(1)));
            requests = Lists.newArrayList();
            boolean evenLine = true;
            while((line = reader.readLine()) != null) {
                List<String> split = Lists.newArrayList(SPACE_SPLITTER.split(line));
                if (split.size() != 3) {
                    throw new FileParseException();
                }
                String dateTimeString = SPACE_JOINER.join(split.get(0), split.get(1));
                if (evenLine) {
                    parser.parseSubmissionTime(dateTimeString);
                    parser.parseEmployee(split.get(2));
                } else {
                    parser.parseMeetingStart(dateTimeString);
                    parser.parseDuration(split.get(2));

                    requests.add(parser.getMeetingRequest());
                    parser.clear();
                }
                evenLine = !evenLine;
            }
        }
    }
}