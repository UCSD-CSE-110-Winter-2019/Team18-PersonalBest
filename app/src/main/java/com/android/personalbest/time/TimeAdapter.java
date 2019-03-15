package com.android.personalbest.time;

import java.util.Calendar;
import java.util.Date;

public class TimeAdapter implements ITime
{
    /**
     * @return current time in milliseconds
     */
    public long timeNow()
    {

        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        return cal.getTimeInMillis();
    }

    /**
     * Get todays time at 12:00a.m. in milliseconds
     */
    public long timeStartToday()
    {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTimeInMillis();
    }

    /**
     * @return time in milliseconds one week ago from now.
     */
    public long timeOneWeekAgo()
    {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_YEAR, -6);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTimeInMillis();
    }

    /**
     * @return time in milliseconds one month ago from now.
     */
    public long timeOneMonthAgo()
    {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_YEAR, -27);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTimeInMillis();
    }
}