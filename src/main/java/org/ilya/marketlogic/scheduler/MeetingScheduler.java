package org.ilya.marketlogic.scheduler;

public interface MeetingScheduler<T> {

    boolean schedule(Iterable<T> requests);

}
