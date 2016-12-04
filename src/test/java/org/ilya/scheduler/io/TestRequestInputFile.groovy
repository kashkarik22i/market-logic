package org.ilya.scheduler.io

import org.joda.time.LocalTime
import spock.lang.Ignore
import spock.lang.Specification

import java.nio.file.Paths

class TestRequestInputFile extends Specification {

    def "test read standard file"() {
        given:
        def inputFilePath = Paths.get(getClass().getResource('complex-test.in').file)

        when:
        def input = new RequestInputFile(inputFilePath);
        def start = input.officeHoursStart
        def end = input.officeHoursEnd
        def requests = input.requests

        then:
        notThrown(FileParseException)

        start == new LocalTime(12, 15)
        end == new LocalTime(14, 30)

        requests.collect { it.data.details.employee } == ['EMP001', 'EMP003']
        requests.collect { it.data.duration.standardHours } == [2L, 1L]

        // TODO validate other fields

    }

    def "test read invalid time file"() {
        given:
        def inputFilePath = Paths.get(getClass().getResource('invalid-time.in').file)

        when:
        def input = new RequestInputFile(inputFilePath);
        def requests = input.requests

        then:
        def e = thrown(FileParseException)
        e.lineNumber == 4
    }

    // TODO these is a work-around
    @Ignore('https://github.com/JodaOrg/joda-time/issues/60')
    def "test read invalid hours file"() {
        given:
        def inputFilePath = Paths.get(getClass().getResource('invalid-hours.in').file)

        when:
        def input = new RequestInputFile(inputFilePath);
        input.requests

        then:
        def e = thrown(FileParseException)
        e.lineNumber == 1
    }

    def "test multi-word employee"() {
        given:
        def inputFilePath = Paths.get(getClass().getResource('multiword-employee.in').file)

        when:
        def input = new RequestInputFile(inputFilePath);
        def requests = input.requests

        then:
        notThrown(FileParseException)

        requests.collect { it.data.details.employee } == ['Ilya Kashkarev']
    }

}
