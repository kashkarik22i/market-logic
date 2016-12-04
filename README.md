# Meeting Scheduler

### Requirements

#### Java
Java 7 is the version which has been tested
Java 8 should work, but has not been tested

### Command line tool

Run the following command to create a command
line executable: 

```
./gradlew installApp
```

The executable can be found here:
```
./build/install/scheduler/bin/scheduler
```

For a menu of available commands and options run

```
./build/install/scheduler/bin/scheduler help
```

or 

```
./build/install/scheduler/bin/scheduler help schedule
```

A sample input file for the command can be found here:
```
./src/test/resources/org/ilya/scheduler/command/complex-test.in
```

### How to use the library

To use the scheduler library you can proceed
in one of the two following ways

#### Build a jar

To build a jar containing all the dependencies run:

```
./gradlew jarAll
```

To build a jar only containing the library run:

```
./gradlew jar
```

Both jars can be found under
```
./build/libs/
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

#### API documentation
Here is a link to the [API documentation](https://kashkarik22i.github.io/scheduler/) 

#### Example usage
This section should be eventually moved to the API documentation itself.

Here is an example of an entry point (based
upon [SchedulerMain.java](https://github.com/kashkarik22i/scheduler/blob/master/src/main/java/org/ilya/scheduler/SchedulerMain.java#L122)):

```java
// Create a notifier, which will receive notifications regarding
// successes of failures of scheduling requests
// The notifier below does nothing, but it could e.g. save notifications
// to a file or print them
RequestNotifier<Meeting> notifier = new DoNothingRequestNotifier<Meeting>();

// Create a request prioritizer, which prioritizes requests
// The prioritizer below prioritizes requests by submittion time,
// but another prioritizer could take other information into account 
RequestPrioritizer<MeetingRequest> prioritizer = new FifoRequestPrioritizer();

// Create a schedule for storing events. It may grow to become a database in theory.
// The only available implementation is for Meeting objects. It has a restriction
// that all meetings have to be within a certain time boundary.
Schedule<Meeting> schedule = new NavigableDateSchedule(startOfficeHours, endOfficeHours);

// Create a scheduler from the objects instantiated above. A scheduler
// does the job of scheduling events and notifying the notifier.
// It is the main entry point for this library. 
Scheduler<MeetingRequest> scheduler = new DefaultScheduler<>(
                    schedule, resolver, notifier);

// To schedule events run the following on an Iterable of event requests
scheduler.schedule(requests);

// Events are stored in the schedule and for this particular implementation can be accesed via
Iterable<Meeting> items = schedule.getItems();
```

Here are links to the relevant documentation pages:
* Data types:
  * [Meeting](https://kashkarik22i.github.io/scheduler/org/ilya/scheduler/request/Meeting.html)
  * [Request](https://kashkarik22i.github.io/scheduler/org/ilya/scheduler/request/Request.html)
  * [MeetingRequest](https://kashkarik22i.github.io/scheduler/org/ilya/scheduler/request/MeetingRequest.html)
* Scheduling:
  * [Scheduler](https://kashkarik22i.github.io/scheduler/org/ilya/scheduler/Scheduler.html) 
  * [DefaultScheduler](https://kashkarik22i.github.io/scheduler/org/ilya/scheduler/DefaultScheduler.html)
  * [RequestNotifier](https://kashkarik22i.github.io/scheduler/org/ilya/scheduler/request/RequestNotifier.html)
  * [Schedule](https://kashkarik22i.github.io/scheduler/org/ilya/scheduler/schedule/Schedule.html)
  * [NavigableDateSchedule](https://kashkarik22i.github.io/scheduler/org/ilya/scheduler/schedule/NavigableDateSchedule.html)
  * [RequestPrioritizer](https://kashkarik22i.github.io/scheduler/org/ilya/scheduler/resolution/RequestPrioritizer.html)
  * [FifoRequestPrioritizer](https://kashkarik22i.github.io/scheduler/org/ilya/scheduler/resolution/FifoRequestPrioritizer.html)

### Known issues
Assuming requirements are not strict, the library should work.
However, of course, there are details and corner cases which have not been
covered.

One issue worth mentioning is that parsing is a bit lenient because
the parsing library is so. See a bug report [here](https://github.com/JodaOrg/joda-time/issues/60).

Another issue which currently bothers me a lot is that the types for
meetings, requests and dumpers are not very flexible and convenient to use.
  
In many cases potential issues were commented as "TODO" in the code. 
One can grep "TODO" (yes, just like that) to have an overview.

### Implementation notes
The library was developed within one week and some quality aspects were compromised.
This is probably not a desirable approach, but I was too excited to make it look
like a "real" usable library. If the result turns out not very good, at least
I enjoyed the process! I took certain amount of time at the end to improve robustness,
re-factor unclear bits and remove unused flexibility.

#### Work log
High level information:

| Type of work   | Duration |
| -------------- | -------- |
| infrastructure |   04:30  |
|    learning    |   02:30  |
| implementation |   14:00  |
|    testing     |   06:00  |
|     total      |   27:00  |

Low level log (still an approximation for simplicity):

|  Date  | Duration |                    Description                       |
| ------ | -------- | ---------------------------------------------------- |
| Nov 29 |   00:30  | set up basic gradle project and some dependencies    |
| Nov 29 |   00:30  | read the task description carefully, making notes    |
| Nov 29 |   00:15  | sceleton classes just to have something              |
| Nov 30 |   00:30  | learned how to generate API docs and set it up       |
| Nov 30 |   00:45  | design sketch on paper                               |
| Nov 30 |   00:30  | small detour into joda-time package                  |
| Nov 30 |   00:45  | implementation of scheduler, resolver, executor      |
| Dec 01 |   03:00  | adding details to classes, added an example schedule |
| Dec 01 |   03:30  | implemented all but data handling and notifications  |
| Dec 02 |   00:30  | re-factoring                                         |
| Dec 02 |   00:30  | investigating fast maven deployment options          |
| Dec 02 |   00:30  | renaming packages and repositories                   |
| Dec 02 |   00:30  | deployed with maven on github and tested             |
| Dec 03 |   00:30  | implemented a way to dump meetings                   |
| Dec 03 |   01:15  | finished first copy which could word                 |
| Dec 03 |   00:15  | debugging                                            |
| Dec 03 |   03:30  | writing tests                                        |
| Dec 03 |   01:15  | adding command line                                  |
| Dec 03 |   00:15  | setting up command line tests                        |
| Dec 03 |   01:45  | added more tests                                     |
| Dec 04 |   00:30  | testing command line executable                      |
| Dec 04 |   01:30  | readme file                                          |
| Dec 04 |   04:00  | javadoc, re-factoring, simplification                |

