package org.ilya.scheduler.request;

import org.ilya.scheduler.io.Dumper;

public class StdoutRequestNotifier<T> implements RequestNotifier<T> {

    private final Dumper<Request<T>> dumper;

    public StdoutRequestNotifier(Dumper<Request<T>> dumper) {
        this.dumper = dumper;
    }

    @Override
    public void notify(Request<T> request, RequestResult<T> status) {
        System.out.println(status.getFormattedMessage(request, dumper));
    }

}
