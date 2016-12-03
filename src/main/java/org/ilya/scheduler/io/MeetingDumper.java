package org.ilya.scheduler.io;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import org.ilya.scheduler.request.Meeting;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.PeriodFormatter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

public class MeetingDumper implements Dumper<Meeting> {

    private final DateTimeFormatter timeFormatter;
    private final PeriodFormatter durationFormatter;
    private final DateTimeFormatter dateFormatter;

    public MeetingDumper(DateTimeFormatter timeFormatter,
                         PeriodFormatter durationFormatter,
                         DateTimeFormatter dateFormatter) {
        this.timeFormatter = timeFormatter;
        this.durationFormatter = durationFormatter;
        this.dateFormatter = dateFormatter;
    }

    @Override
    public String toString(Meeting meeting) {
        return String.format("%s %s %s",
                dateFormatter.print(meeting.getStart()),
                timeFormatter.print(meeting.getStart().toLocalTime()),
                durationFormatter.print(meeting.getDuration().toPeriod()));
    }

    @Override
    public void toFile(Iterable<Meeting> meetings, File file) throws IOException {
        Multimap<LocalDate, Meeting> byDate = Multimaps.index(meetings, new Function<Meeting, LocalDate>() {
            @Override
            public LocalDate apply(Meeting meeting) {
                return meeting.getStart().toLocalDate();
            }
        });
        List<LocalDate> sortedDates = Lists.newArrayList(byDate.keys());
        Collections.sort(sortedDates);
        try (BufferedWriter writer = Files.newBufferedWriter(file.toPath(),
                StandardCharsets.UTF_8)) {
            for (LocalDate date : sortedDates) {
                writer.write(dateFormatter.print(date));
                writer.newLine();
                for (Meeting meeting : byDate.get(date)) {
                    writer.write(timeFormatter.print(meeting.getStart().toLocalTime()));
                    writer.append(' ');
                    writer.write(meeting.getDetails().getEmployee());
                    writer.newLine();
                }
            }
        }
    }
}
