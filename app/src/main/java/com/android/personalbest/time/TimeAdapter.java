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
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DATE);
        cal.clear();
        cal.set(year, month, date);
        return cal.getTimeInMillis();
    }

    /**
     * @return time in milliseconds one week ago from now.
     */
    public long timeOneWeekAgo()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DATE);
        cal.clear();
        cal.set(year, month, date);
        cal.add(Calendar.DAY_OF_YEAR, -6);
        return cal.getTimeInMillis();
    }

    /**
     * @return time in milliseconds one month ago from now.
     */
    public long timeOneMonthAgo()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DATE);
        cal.clear();
        cal.set(year, month, date);
        cal.add(Calendar.DAY_OF_YEAR, -27);
        return cal.getTimeInMillis();
    }
    public Date getTime(){
        return Calendar.getInstance().getTime();
    }

    @Override
    public void setTime(String date) {
    }

    @Override
    public void setMockTime(Long mockTime){}

    /**
     * @return start time of some day
     */
    public long getDay(int i)
    {
        int daysAgo = i * SECONDS_IN_DAY;
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DATE);
        cal.clear();
        cal.set(year, month, date);
        return cal.getTimeInMillis() - daysAgo;
    }
}
