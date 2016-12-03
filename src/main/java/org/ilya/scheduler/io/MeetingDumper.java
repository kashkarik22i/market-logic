package org.ilya.scheduler.io;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.ilya.scheduler.request.Meeting;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

public class MeetingDumper implements Dumper<Meeting> {

    private static final Joiner SPACE_JOINER = Joiner.on(" ");
    private final DateTimeFormatter dateFormatter;
    private final DateTimeFormatter timeFormatter;

    public MeetingDumper(DateTimeFormatter dateFormatter,
                         DateTimeFormatter timeFormatter) {
        this.timeFormatter = timeFormatter;
        this.dateFormatter = dateFormatter;
    }

    @Override
    public String toString(Meeting meeting) {
        return SPACE_JOINER.join(
                dateFormatter.print(meeting.getStart()),
                timeFormatter.print(meeting.getStart().toLocalTime()),
                meeting.getDetails().getEmployee());
    }

    @Override
    public void dumpToFile(Iterable<Meeting> meetings, Path filePath) throws IOException {
        Multimap<LocalDate, Meeting> byDate = Multimaps.index(meetings, new Function<Meeting, LocalDate>() {
            @Override
            public LocalDate apply(Meeting meeting) {
                return meeting.getStart().toLocalDate();
            }
        });
        List<LocalDate> sortedDates = Lists.newArrayList(byDate.keys());
        Collections.sort(sortedDates);
        try (BufferedWriter writer = Files.newBufferedWriter(filePath,
                StandardCharsets.UTF_8)) {
            for (LocalDate date : sortedDates) {
                writer.write(dateFormatter.print(date));
                writer.newLine();
                for (Meeting meeting : byDate.get(date)) {
                    writer.write(SPACE_JOINER.join(
                            timeFormatter.print(meeting.getStart().toLocalTime()),
                            timeFormatter.print(meeting.getEnd().toLocalTime()),
                            meeting.getDetails().getEmployee()));
                    writer.newLine();
                }
            }
        }
    }
}
