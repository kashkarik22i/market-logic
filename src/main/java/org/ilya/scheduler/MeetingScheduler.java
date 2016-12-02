package org.ilya.scheduler;

public interface MeetingScheduler<T> {

    boolean schedule(Iterable<T> requests);

}
