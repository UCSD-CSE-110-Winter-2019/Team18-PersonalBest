package com.android.personalbest;

import com.android.personalbest.time.ITime;
import com.android.personalbest.time.TimeFactory;

import org.junit.Before;
import org.junit.Test;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;

public class TestMockTime
{
    ITime mtime;
    long mockedTime = 1548802800;

    /**
     * Initialize time variable
     */
    @Before
    public void setup()
    {
        this. mtime = TimeFactory.create(mockedTime * 1000);
    }

    @Test
    public void testTimeNow()
    {
        DateFormat dateFormat = DateFormat.getDateInstance();
        DateFormat timeFormat = DateFormat.getTimeInstance();

        assertEquals("Jan 29, 2019", dateFormat.format(mtime.timeNow()) );

    }

    @Test
    public void testTimeStartToday()
    {
        DateFormat timeFormat = DateFormat.getTimeInstance();

        assertEquals("12:00:00 AM", timeFormat.format(mtime.timeStartToday()));
    }

    @Test
    public void testTimeOneWeekAgoStart()
    {
        DateFormat dateFormat = DateFormat.getDateInstance();
        DateFormat timeFormat = DateFormat.getTimeInstance();

        assertEquals("Jan 23, 2019 12:00:00 AM",dateFormat.format(mtime.timeOneWeekAgo() )
                     + " " + timeFormat.format(mtime.timeOneWeekAgo()));
    }

    @Test
    public void testTimeOneMonthAgoStart()
    {
        DateFormat dateFormat = DateFormat.getDateInstance();
        DateFormat timeFormat = DateFormat.getTimeInstance();

        assertEquals("Jan 2, 2019 12:00:00 AM",dateFormat.format(mtime.timeOneMonthAgo() )
                + " " + timeFormat.format(mtime.timeOneWeekAgo()));
    }


}
