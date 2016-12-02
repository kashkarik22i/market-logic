package org.ilya.scheduler.schedule;

public class SimpleRequestExecutor<T> implements RequestExecutor<T> {

    private final Schedule<T> schedule;

    public SimpleRequestExecutor(Schedule<T> schedule) {
        this.schedule = schedule;
    }

    @Override
    public boolean execute(Action<Schedule<T>> action) {
        boolean applied = false;
        try {
            applied = action.apply(schedule);
        } catch (Exception e) {
            // TODO it should not happen in the current settings
            // but ideally changes to the schedule should be reverted
            action.onFailure();
        }
        if (applied) {
            action.onSuccess();
        }
        return applied;
    }

}
