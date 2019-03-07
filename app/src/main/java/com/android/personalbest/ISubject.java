package com.android.personalbest;

public interface ISubject<ObserverT> {
    /**
     * Register a new listener.
     */
    void register(ObserverT observer);

    /**
     * Unregister a listener.
     */
    void unregister();

    // note that using a generic interface like this is DRY, but precludes
    // adding type specific methods as we talked in discussion.
}
