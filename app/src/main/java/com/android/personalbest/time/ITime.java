package com.android.personalbest.time;

public interface ITime
{
    int SECONDS_IN_DAY = 86400000;
    /**
     * @return current time in milliseconds
     */
    long timeNow();

    /**
     * Get todays time at 12:00a.m. in milliseconds
     */
    long timeStartToday();

    /**
     * @return time in milliseconds one week ago from now.
     */
    long timeOneWeekAgo();

    /**
     * @return time in milliseconds one month ago from now.
     */
    long timeOneMonthAgo();

    /**
     * @return start time of some day
     */
    long getDay(int i);
}
