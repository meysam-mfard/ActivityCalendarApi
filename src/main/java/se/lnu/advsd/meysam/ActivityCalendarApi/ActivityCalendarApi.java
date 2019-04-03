package se.lnu.advsd.meysam.ActivityCalendarApi;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import se.lnu.advsd.meysam.ActivityCalendarApi.model.Activity;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

/**
 * Makes it possible to define activities with predefined budgets for events
 * in google calendar and add new events with some a specified time bidget.
 * @author Meysam M. Fard
 */
public interface ActivityCalendarApi {


    /**
     * Adds new activity with the specified name and budget to a calendar.
     * If activity already exists, its budget will be updated with the specified budget.
     * @param calendarId The Google's calendar id.
     * @param activityName
     * @param activityBudget
     */
    void addActivity(String calendarId, String activityName, Integer activityBudget);

    /**
     * Adds new activity to a calendar.
     * If activity already exists, its budget will be updated with the specified budget.
     * @param calendarId The Google's calendar id.
     * @param activity
     */
    void addActivity(String calendarId, Activity activity);

    /**
     * Removes the activity with the name.
     * @param calendarId The Google's calendar id.
     * @param activityName
     */
    void removeActivity(String calendarId, String activityName);

    /**
     * Sets or updates the budget of an existing activity.
     * @param calendarId The Google's calendar id.
     * @param activityName
     * @param activityBudget
     */
    void setBudget(String calendarId, String activityName, Integer activityBudget);

    /**
     * Adds the "event" to the calendar, associates it with the activity named "activityName" and
     * increases the actvity's spent budget by event's duration time.
     * If "activity" does not exist, a new one is created with zero budget and spent budget of event's duration.
     * @param calendar The Google's calendar.
     * @param calendarId The Google's calendar id.
     * @param activityName
     * @param event
     * @return desired event
     * @throws IOException
     */
    Event addEvent(Calendar calendar, String calendarId, String activityName, Event event) throws IOException;

    /**
     * Returns all activities from the specified calendar.
     * @param calendarId The Google's calendar id.
     * @return Map from Activity Name to Activity
     */
    Map<String, Activity> getActivities(String calendarId);


    /**
     * Returns percentage of time spent on each activity considering all activities in the specified calendar.
     * @param calendarId The Google's calendar id.
     * @return Map from an activity Name to its spent time percentage.
     */
    Map<String, Double> getActivitiesSpentPercentage(String calendarId);

    /**
     * Returns the activity with the specified name.
     * @param calendarId The Google's calendar id.
     * @param activityName
     * @return Optional Activity
     */
    Optional<Activity> getActivity(String calendarId, String activityName);

}
