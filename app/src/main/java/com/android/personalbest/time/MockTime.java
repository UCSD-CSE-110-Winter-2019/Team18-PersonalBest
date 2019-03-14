package com.android.personalbest.time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MockTime implements ITime
{
    private long mockTime;
    public static String time;

    /**
     * Constructor - Initialize the mock time passed in by tester
     * Pass in time in seconds then convert to milliseconds
     */
    public MockTime(){}
    MockTime(long mockTime)
    {
        this.mockTime = mockTime;
    }

    /**
     * @return "current" time in milliseconds
     */
    public long timeNow()
    {
        return this.mockTime;
    }

    /**
     * Get "todays" time at 12:00a.m. in milliseconds
     */
    public long timeStartToday()
    {
        Calendar cal = Calendar.getInstance();
        Date now = new Date(mockTime);
        cal.setTime(now);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTimeInMillis();
    }

    /**
     * @return time in milliseconds "one week ago from now".
     */
    public long timeOneWeekAgo()
    {
        Calendar cal = Calendar.getInstance();
        Date now = new Date(mockTime);
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
        Date now = new Date(mockTime);
        cal.setTime(now);
        cal.add(Calendar.DAY_OF_YEAR, -27);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTimeInMillis();
    }
    public Date getTime(){
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d=new Date();
        try {
            d = sf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return d;
    }
    public void setTime(String date){
        time=date;
    }
}
