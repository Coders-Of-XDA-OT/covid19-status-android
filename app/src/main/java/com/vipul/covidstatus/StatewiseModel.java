package com.vipul.covidstatus;

public class StatewiseModel {
    private String state;
    private String confirmed;
//    private String active;
//    private String deceased;
//    private String recovered;

    public StatewiseModel(String state, String confirmed) {
        this.state = state;
        this.confirmed = confirmed;
//        this.active = active;
//        this.deceased = deceased;
//        this.recovered = recovered;
    }

    public String getState() {
        return state;
    }

    public String getConfirmed() {
        return confirmed;
    }
}
