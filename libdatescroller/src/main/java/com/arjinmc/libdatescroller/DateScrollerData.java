package com.arjinmc.libdatescroller;

/**
 * date bean
 * Created by Eminem Lo on 24/2/17.
 * Email arjinmc@hotmail.com
 */

public class DateScrollerData {

    private long time;

    private String day;
    private int dayOfMonth;
    private int month;
    private int year;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
