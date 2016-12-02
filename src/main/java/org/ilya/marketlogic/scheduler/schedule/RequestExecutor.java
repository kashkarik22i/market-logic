package org.ilya.marketlogic.scheduler.schedule;

public interface RequestExecutor<T> {

    boolean execute(Action<Schedule<T>> action);

}
