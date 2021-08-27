package com.cleaner.cybercleanerapp.util;

import java.io.Serializable;

public class SharedData implements Serializable {
    int percent;
    String value;
    String date;

    public SharedData(int percent, String value, String date) {
        this.percent = percent;
        this.value = value;
        this.date = date;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
