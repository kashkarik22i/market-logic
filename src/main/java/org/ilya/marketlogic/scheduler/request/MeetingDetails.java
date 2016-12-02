package org.ilya.marketlogic.scheduler.request;

public class MeetingDetails {

    private final String employee;

    public MeetingDetails(String employee) {
        this.employee = employee;
    }

    public String getEmployee() {
        return employee;
    }

}
