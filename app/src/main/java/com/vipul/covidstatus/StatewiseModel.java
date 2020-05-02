package com.vipul.covidstatus;

public class StatewiseModel {
    private String state;
    private String confirmed;
    private String active;
    private String deceased;
    private String recovered;

    public StatewiseModel(){

    }

    public StatewiseModel(String state, String confirmed, String active, String deceased, String recovered) {
        this.state = state;
        this.confirmed = confirmed;
        this.active = active;
        this.deceased = deceased;
        this.recovered = recovered;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getConfirmed() {
        return confirmed;
    }

    public void setConfirmed(String confirmed) {
        this.confirmed = confirmed;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getDeceased() {
        return deceased;
    }

    public void setDeceased(String deceased) {
        this.deceased = deceased;
    }

    public String getRecovered() {
        return recovered;
    }

    public void setRecovered(String recovered) {
        this.recovered = recovered;
    }
}
