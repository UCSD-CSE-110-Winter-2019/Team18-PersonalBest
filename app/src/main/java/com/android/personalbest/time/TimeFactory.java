package com.android.personalbest.time;

public class TimeFactory
{
    /**
     * Factory that creates a TimeAdapter object to be used for real time
     * or to create a MockTime object for testing.
     * @param mockTime pass in the time you wish to mock in seconds
     *                 to create a MockTime object or pass in 0 to
     *                 create a TimeAdapterObject.
     * @return an ITime object
     */
    public static ITime create(long mockTime)
    {
        if(mockTime == 0)
        {
            return new TimeAdapter();
        }else
        {
            return new MockTime(mockTime);
        }
    }
}
