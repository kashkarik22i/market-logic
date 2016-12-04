package org.ilya.scheduler

import com.google.common.io.Files
import org.apache.commons.io.FileUtils
import spock.lang.Specification

class FileSpecification extends Specification{

    File tmpDir

    def createTempDir() {
        Files.createTempDir()
    }

    def setup() {
        tmpDir = createTempDir()
    }

    def cleanup() {
        FileUtils.deleteDirectory(tmpDir)
    }

}
