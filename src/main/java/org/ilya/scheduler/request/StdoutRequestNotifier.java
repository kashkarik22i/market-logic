package org.ilya.scheduler.request;

import org.ilya.scheduler.io.Dumper;

public class StdoutRequestNotifier<T> implements RequestNotifier<T> {

    private final Dumper<T> dumper;

    public StdoutRequestNotifier(Dumper<T> dumper) {
        this.dumper = dumper;
    }

    @Override
    public void notify(Request<T> request, RequestResult status) {
        System.out.println(status.getFormattedMessage(request, dumper));
    }

}
