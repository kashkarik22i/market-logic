# Meeting Scheduler

### Requirements

#### Java
Java 7 is the version which has been tested
Java 8 should work, but has not been tested

#### git
To clone the repository

### Command line tool

Run the following commands to create a command
line executable tool for scheduling: 

```
./gradlew installApp
```

For a menu of available commands and options run

```
./build/install/scheduler/bin/scheduler help
```

or 

```
./build/install/scheduler/bin/scheduler help schedule
```

### How to use the library

To use the code as a library you can proceed
in one of the two following ways.

#### Build a jar
To build a library jar file run

```
./gradlew jar
```

#### From a gradle script
Here is a sample gradle script which uses the library:

```gradle
repositories {
    maven {
        url 'https://github.com/kashkarik22i/maven-artifactory/raw/master/'
    }
}
dependencies {
    compile 'ilya:scheduler:0.0.1'
}
```

### How to use the library

#### API documentation
Here is a link to the [API documentation](https://kashkarik22i.github.io/scheduler/) 

### Key classes description
This section should be eventually moved to the documentation

TODO

#### Known issues
Assuming requirements are not strict, the library should work.
However, of course, there are details and corner cases which have not been
covered.
  
In many cases potential issues were commented as "TODO" in the code. 
One can grep "TODO" (yes, just like that) to have an overview.

### Implementation notes
The library was developed within one week and quality was compromised
in favor of quantity. This is probably not a desirable approach, but
I was too excited to make it look like a "real" usable library. If the result
tuns out not very good, at least I enjoyed the process! I took certain amount of
time at the end to improve robustness, re-factor uncler parts and
remove unused flexibility.

#### Work log
High level information:
| Type of work   | Duration |
| infrastructure |   04:30  |
|    learning    |   02:30  |
| implementation |   14:00  |
|    testing     |   06:00  |
|     total      |   27:00  |

Low level information:
| Date |Start| Duration |                  Description                        |
|Nov 29|22:00|   00:30  |set up basic gradle project and some dependencies    |
|Nov 29|22:30|   00:30  |read the task description carefully, making notes    |
|Nov 29|23:30|   00:15  |sceleton classes just to have something              |
|Nov 30|00:00|   00:30  |learned how to generate API docs and set it up       |
|Nov 30|00:45|   00:45  |design sketch on paper                               |
|Nov 30|22:30|   00:30  |small detour into joda-time package                  |
|Nov 30|23:00|   00:45  |implementation of scheduler, resolver, executor      |
|Dec 01|00:45|   03:00  |adding details to classes, added an example schedule |
|Dec 01|20:00|   03:30  |implemented all but data handling and notifications  |
|Dec 02|00:30|   00:30  |re-factoring                                         |
|Dec 02|20:30|   00:30  |investigating fast maven deployment options          |
|Dec 02|21:30|   00:30  |renaming packages and repositories                   |
|Dec 02|22:00|   00:30  |deployed with maven on github and tested             |
|Dec 03|00:30|   00:30  |implemented a way to dump meetings                   |
|Dec 03|01:30|   01:15  |finished first copy which could word                 |
|Dec 03|02:45|   00:15  |debugging                                            |
|Dec 03|14:30|   03:30  |writing tests                                        |
|Dec 03|20:00|   01:15  |adding command line                                  |
|Dec 03|21:30|   00:15  |setting up command line tests                        |
|Dec 03|23:00|   01:45  |added more tests                                     |
|Dec 04|01:00|   00:30  |testing command line executable                      |
|Dec 04|12:00|   01:30  |readme file                                          |
|Dec 04|16:00|   01:00  |javadoc                                              |
|Dec 04|17:00|   03:00  |re-factoring, simplification                         |

