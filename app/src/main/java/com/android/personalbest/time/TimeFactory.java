package com.android.personalbest.time;

public class TimeFactory
{
    /**
     * Factory that creates a TimeAdapter object to be used for real time
     * or to create a MockTime object for testing.
     * @return an ITime object
     */
    public static ITime create(String i)
    {
        if(i.equals("test"))
        {
            return new MockTime();
        }else
        {
            return new TimeAdapter();
        }
    }
}
