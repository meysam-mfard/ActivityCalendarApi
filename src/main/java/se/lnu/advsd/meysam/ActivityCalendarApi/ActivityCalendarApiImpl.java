package se.lnu.advsd.meysam.ActivityCalendarApi;

import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import se.lnu.advsd.meysam.ActivityCalendarApi.model.Activity;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class ActivityCalendarApiImpl implements ActivityCalendarApi {

    //Directory to store user credentials.
    private Path dataStorePath =
            Paths.get(System.getProperty("user.home"), ".activityCalendar/activity/");

    /**
     * Default location for storing application data is "user.home"/.activityCalendar/activity/.
     * To set another path, user parameterized constructor.
     */
    public ActivityCalendarApiImpl() {
    }

    /**
     * @param dataStorePath Location for storing application data.
     */
    public ActivityCalendarApiImpl(Path dataStorePath) {
        this.dataStorePath = dataStorePath;
    }

    @Override
    public void addActivity(String calendarId, String activityName, Integer activityBudget) {
        Map<String, Activity> activityMap = loadActivities(calendarId);
        activityMap.put(activityName, new Activity(activityName, activityBudget));
        saveActivities(calendarId, activityMap);
    }

    @Override
    public void addActivity(String calendarId, Activity activity) {
        Map<String, Activity> activityMap = loadActivities(calendarId);
        activityMap.put(activity.getName(), activity);
        saveActivities(calendarId, activityMap);
    }

    @Override
    public void removeActivity(String calendarId, String activityName) {
        Map<String, Activity> activityMap = loadActivities(calendarId);
        activityMap.remove(activityName);
        saveActivities(calendarId, activityMap);
    }

    @Override
    public void setBudget(String calendarId, String activityName, Integer activityBudget) {
        Map<String, Activity> activityMap = loadActivities(calendarId);
        Activity activity = activityMap.get(activityName);
        if (activity == null)
            throw new NoSuchElementException("Activity \""+ activityName + "\" does not exist!");
        activity.setBudget(activityBudget);
        activityMap.put(activity.getName(), activity);
        saveActivities(calendarId, activityMap);
    }

    @Override
    public Event addEvent(Calendar calendar, String calendarId, String activityName, Event event) throws IOException {
        Event result = calendar.events().insert(calendarId, event).execute();
        Map<String, Activity> activityMap = loadActivities(calendarId);

        //Duration in milliseconds
        Long duration = (event.getEnd().getDateTime().getValue() - event.getStart().getDateTime().getValue());
        //duration in hours
        duration = duration / 1000 / 3600;

        if (activityMap.containsKey(activityName)) {
            Activity activity = activityMap.get(activityName);
            activity.increaseSpentBudgetBy(duration.intValue());
            activity.addEvent(result);
            saveActivities(calendarId, activityMap);
        }
        else {
            Activity activity = new Activity((activityName));
            activity.increaseSpentBudgetBy(duration.intValue());
            activity.addEvent(result);
            addActivity(calendarId, activity);
        }
        return result;
    }

    @Override
    public Map<String, Activity> getActivities(String calendarId) {
        Map<String, Activity> activityMap = loadActivities(calendarId);
        return activityMap.isEmpty() ? new HashMap<>() : activityMap;
    }

    @Override
    public Map<String, Double> getActivitiesSpentPercentage(String calendarId) {
        Map<String, Double> percentageMap = new HashMap<>();
        Map<String, Activity> activityMap = getActivities(calendarId);
        if (activityMap.isEmpty())
            return percentageMap;

        Long totalSpentTime = 0L;
        for (Activity activity:activityMap.values()) {
            totalSpentTime += activity.getSpentBudget();
        }

        for (Activity activity:activityMap.values()) {
            percentageMap.put(activity.getName(), activity.getSpentBudget() * 100.0 / totalSpentTime);
        }

        return percentageMap;
    }

    @Override
    public Optional<Activity> getActivity(String calendarId, String activityName) {
        Map<String, Activity> activityMap = getActivities(calendarId);
        if (activityMap == null)
            return Optional.empty();
        Activity activity = activityMap.get(activityName);
        if (activity == null)
            return Optional.empty();
        else
            return Optional.of(activity);
    }



    //Helper Methods:

    //calendarId is used as the file name for persisting user's activities
    private Map<String, Activity> loadActivities(String calendarId) {
        Map<String, Activity> activityMap = new HashMap<>();
        String fileName = calendarId+".data";
        //Path filePath = Paths.get(DATA_STORE_PATH.toString(), calendarId);
        FileInputStream activityFile = null;

        try {
            activityFile = new FileInputStream(getFile(dataStorePath, fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try(ObjectInputStream objectInputStream = new ObjectInputStream(activityFile)) {
            int numOfActivities = objectInputStream.readInt();

            for (int i = 0; i < numOfActivities; i++) {
                Activity activity = (Activity) objectInputStream.readObject();
                activityMap.put(activity.getName(), activity);
            }

        } catch (EOFException e) {
            return activityMap;//empty map
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return activityMap;
    }

    private void saveActivities(String calendarId, Map<String, Activity> activityMap) {
        String fileName = calendarId+".data";
        //Path filePath = Paths.get(DATA_STORE_PATH.toString(), calendarId);
        FileOutputStream activityFile = null;

        try {
            activityFile = new FileOutputStream(getFile(dataStorePath, fileName));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try(ObjectOutputStream objectOutputStream = new ObjectOutputStream(activityFile)) {
            objectOutputStream.writeInt(activityMap.size());//stores the number of objects
            for(Activity activity: activityMap.values())
                objectOutputStream.writeObject(activity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    private String getFile(Path directoryPath, String fileName) {
        Path filePath = Paths.get(directoryPath.toString(), fileName);
        if (Files.exists(filePath))
            return filePath.toString();
        try {
            Files.createDirectories(directoryPath);
            Files.createFile(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return filePath.toString();
    }
}
