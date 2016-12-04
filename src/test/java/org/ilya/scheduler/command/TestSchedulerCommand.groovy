package org.ilya.scheduler.command

import org.ilya.scheduler.FileSpecification
import org.ilya.scheduler.SchedulerMain

class TestSchedulerCommand extends FileSpecification {

    def "test help"() {
        when:
        SchedulerMain.cli.parse('help').call()

        then:
        notThrown(Exception)

    }

    def "test on a complex file"() {
        given:
        def inputFilePath = getClass().getResource('complex-test.in').file
        def expected = new File(getClass().getResource('complex-test.expected').file).text
        def outFile = tmpDir.toPath().resolve('complex-test.out')

        when:
        def exitCode = SchedulerMain.cli.parse('-i', inputFilePath,
                '-o', outFile.toString(), '-s', '-p').call()

        then:
        exitCode == 0
        outFile.toFile().exists()
        outFile.toFile().text == expected
    }

    def "test empty file"() {
        given:
        def inputFilePath = getClass().getResource('empty').file
        def outFile = tmpDir.toPath().resolve('empty.out')

        when:
        def exitCode = SchedulerMain.cli.parse('-i', inputFilePath,
                '-o', outFile.toString(), '-p').call()

        then:
        notThrown(Exception)
        exitCode != 0
        // TODO make sure the error message is printed and meaningful
    }

}
