package se.lnu.advsd.meysam.ActivityCalendarApi.model;

import com.google.api.services.calendar.model.Event;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Includes a "name", "budget", "spent budget" and a set of its associated events' "Id"s
 * accessible via "com.google.api.services.calendar.model.Event.getId()".
 * @author Meysam M. Fard
 */
public class Activity implements Serializable {
    String name;
    Integer budget;
    Integer spentBudget = 0;
    Set<String> eventsIds = new HashSet<>();     //eventId of each event is stored

    /**
     * @throws IllegalArgumentException if budget is negative.
     */
    public Activity(String name, Integer budget) throws IllegalArgumentException {
        if (budget<0)
            throw new IllegalArgumentException("Budget cannot be a negative number!");
        this.name = name;
        this.budget = budget;
    }

    public Activity(String name) {
        this.name = name;
        this.budget = 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBudget() {
        return budget;
    }

    /**
     * @throws IllegalArgumentException if budget is negative.
     */
    public void setBudget(Integer budget) throws IllegalArgumentException {
        if (budget<0)
            throw new IllegalArgumentException("Budget cannot be a negative number!");
        this.budget = budget;
    }

    public Integer getSpentBudget() {
        return spentBudget;
    }

    /**
     * If budget becomes less than zero after updating, it is set to zero.
     */
    public void increaseSpentBudgetBy(Integer increaseBy) {
        Integer result = spentBudget+increaseBy;
        if (result<0)
            result = 0;
        this.spentBudget = result;
    }

    public Set<String> getEventsIds() {
        return eventsIds;
    }

    public void setEvents(Set<String> eventsIds) {
        this.eventsIds = eventsIds;
    }

    /**
     * Adds the event's Id to the activity.
     * @param event
     */
    public void addEvent(Event event) {
        this.eventsIds.add(event.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return name.equals(activity.name) &&
                budget.equals(activity.budget) &&
                spentBudget.equals(activity.spentBudget);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, budget, spentBudget);
    }

    @Override
    public String toString() {
        return "Activity{" +
                "name='" + name + '\'' +
                ", budget=" + budget +
                ", spentBudget=" + spentBudget +
                ", events=" + eventsIds +
                '}';
    }
}
