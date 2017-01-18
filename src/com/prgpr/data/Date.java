package com.prgpr.data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by strange on 1/17/17.
 * This but a crude Date thingy.
 *
 * Parses ISO date string foo and does comparison bar.
 * Used for putting data from the wiki api into context. Deep stuff.
 */
public class Date {

    public final int year;
    public final int month;
    public final int day;
    public final int hour;
    public final int minute;
    public final int second;

    private final Pattern isoDatePatternFull = Pattern.compile("\\+(\\d+)-(\\d+)-(\\d+)T(\\d+):(\\d+):(\\d+)Z");
    private final Pattern isoDatePatternJustDate = Pattern.compile("\\+(\\d+)-(\\d+)-(\\d+)T");

    /**
     * A constructor, most complicated.
     * @param year (exactly what it states here)
     * @param month (exactly what it states here)
     * @param day (exactly what it states here)
     * @param hour (exactly what it states here)
     * @param minute (exactly what it states here)
     * @param second (exactly what it states here)
     */
    public Date(int year, int month, int day, int hour, int minute, int second) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    /**
     * Constructs this Date instance from an ISO style date string.
     * ISO style means:
     *
     * 1. Jan. 2016 - 19:30:10
     * becomes
     * +2016-01-01T19:30:10Z
     *
     * @param isoDateString ^ read the goddamn text above
     */
    public Date(String isoDateString) {

        Matcher m = isoDatePatternFull.matcher(isoDateString);
        if (m.find()) {
            this.year = Integer.parseInt(m.group(1));
            this.month = Integer.parseInt(m.group(2));
            this.day = Integer.parseInt(m.group(3));
            this.hour = Integer.parseInt(m.group(4));
            this.minute = Integer.parseInt(m.group(5));
            this.second = Integer.parseInt(m.group(6));
        } else {
            m = isoDatePatternJustDate.matcher(isoDateString);
            if (m.find()) {
                this.year = Integer.parseInt(m.group(1));
                this.month = Integer.parseInt(m.group(2));
                this.day = Integer.parseInt(m.group(3));
                this.hour = 0;
                this.minute = 0;
                this.second = 0;
            } else {
                this.year = 0;
                this.month = 0;
                this.day = 0;
                this.hour = 0;
                this.minute = 0;
                this.second = 0;
            }
        }
    }

    /**
     * Checks whether this Date is greater than another Date
     * @param other Another Date.
     * @return Reading comprehension skills are of utmost importance.
     */
    public boolean greaterThan(Date other) {
        if (this.year > other.year) return true;
        else if (this.year < other.year) return false;
        else if (this.month > other.month) return true;
        else if (this.month < other.month) return false;
        else if (this.day > other.day) return true;
        else if (this.day < other.day) return false;
        else if (this.hour > other.hour) return true;
        else if (this.hour < other.hour) return false;
        else if (this.minute > other.minute) return true;
        else if (this.minute < other.minute) return false;
        else if (this.second > other.second) return true;
        else if (this.second < other.second) return false;
        else return false;
    }

}
