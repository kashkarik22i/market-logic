package org.ilya.scheduler;

public interface Scheduler<T> {

    boolean schedule(Iterable<T> requests);

}
