package org.ilya.scheduler.io

import org.ilya.scheduler.FileSpecification
import org.ilya.scheduler.request.Meeting
import org.ilya.scheduler.request.MeetingDetails
import org.joda.time.DateTime
import org.joda.time.Duration
import org.joda.time.format.DateTimeFormat

class TestMeetingDumper extends FileSpecification {

    final def now = new DateTime(2016, 12, 03, 8, 0);
    final def hour = Duration.standardHours(1L)

    def "test to string"() {
        given:
        def dumper = new MeetingDumper(
                DateTimeFormat.forPattern('yyyy-MM-dd'),
                DateTimeFormat.forPattern('HH:mm'));
        def meeting = new Meeting(new MeetingDetails('x'), now, hour)

        expect:
        dumper.toString(meeting) == '2016-12-03 08:00 x'
    }

    def "test one to file"() {
        given:
        def dumper = new MeetingDumper(
                DateTimeFormat.forPattern('yyyy-MM-dd'),
                DateTimeFormat.forPattern('HH:mm'));
        def meeting = new Meeting(new MeetingDetails('x'), now, hour)
        def outPath = tmpDir.toPath().resolve("dumper.out")

        when:
        dumper.dumpToFile([meeting], outPath)

        then:
        outPath.toFile().exists()
        outPath.toFile().readLines() == ['2016-12-03', '08:00 09:00 x']
    }
}
