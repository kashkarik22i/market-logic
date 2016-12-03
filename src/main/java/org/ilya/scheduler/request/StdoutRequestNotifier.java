package org.ilya.scheduler.request;

import org.ilya.scheduler.io.RequestDumper;

public class StdoutRequestNotifier<T> implements RequestNotifier<T> {

    private final RequestDumper<T> dumper;

    public StdoutRequestNotifier(RequestDumper<T> dumper) {
        this.dumper = dumper;
    }

    @Override
    public void notify(Request<T> request, RequestResult<T> status) {
        System.out.println(status.getFormattedMessage(request, dumper));
    }

}
