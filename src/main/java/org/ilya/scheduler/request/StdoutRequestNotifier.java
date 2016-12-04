package org.ilya.scheduler.request;

import org.ilya.scheduler.io.RequestDumper;

/**
 * A {@link RequestNotifier} which prints request results to standard output
 */
public class StdoutRequestNotifier<T> implements RequestNotifier<T> {

    private final RequestDumper<T> dumper;

    public StdoutRequestNotifier(RequestDumper<T> dumper) {
        this.dumper = dumper;
    }

    @Override
    public void notify(Request<? extends T> request, RequestResult<T> status) {
        System.out.println(status.getFormattedMessage(request, dumper));
    }

}
