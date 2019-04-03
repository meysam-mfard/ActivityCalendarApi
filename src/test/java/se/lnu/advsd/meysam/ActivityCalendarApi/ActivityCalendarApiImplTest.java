package se.lnu.advsd.meysam.ActivityCalendarApi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
import se.lnu.advsd.meysam.ActivityCalendarApi.model.Activity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;

class ActivityCalendarApiImplTest {

    private String offlineCalendarId = "test-calendar";
    private ActivityCalendarApi activityCalendarApi;
    private List<Activity> activityList = new ArrayList<>();
    private Map<String, Activity> activityMap = new HashMap<>();
    private Activity activity1, activity2;
    private String activity1Name = "Sports";
    private String activity2Name = "Study";

    /*@Mock
    ActivityCalendarApi activityCalendarApi_mocked;*/

    @BeforeEach
    void setUp() {
        activityCalendarApi = new ActivityCalendarApiImpl();

        activityMap.clear();
        activityList.clear();

        //set data 1
        activity1 = new Activity(activity1Name, 50);
        activity2 = new Activity(activity2Name);

        activityCalendarApi.addActivity(offlineCalendarId, activity1);
        activityCalendarApi.addActivity(offlineCalendarId, activity2);
    }

    @Test
    void addActivity_getActivities() {

        activityCalendarApi.addActivity(offlineCalendarId, activity2Name, 0);

        //get data 1
        activityMap =  activityCalendarApi.getActivities(offlineCalendarId);
        activityList = new ArrayList<>(activityMap.values());

        //test 1
        assertEquals(2, activityMap.size());
        assertTrue(activityList.contains(activity1));
        assertTrue(activityList.contains(activity2));

        assertEquals(activity1.getName(), activityMap.get(activity1.getName()).getName());
        assertEquals(activity1.getBudget(), activityMap.get(activity1.getName()).getBudget());
        assertEquals(activity1.getSpentBudget(), activityMap.get(activity1.getName()).getSpentBudget());
        assertEquals(activity1.getEventsIds(), activityMap.get(activity1.getName()).getEventsIds());

        ///////

        //set data 2
        activity1.increaseSpentBudgetBy(23);//23
        activity1.increaseSpentBudgetBy(5);//23+5=28
        activity2.setBudget(100);
        activity2.increaseSpentBudgetBy(120);//120

        activityCalendarApi.addActivity(offlineCalendarId, activity1);
        activityCalendarApi.addActivity(offlineCalendarId, activity2);


        //get data 2
        activityMap.clear();
        activityList.clear();
        activityMap = activityCalendarApi.getActivities(offlineCalendarId);
        activityList = new ArrayList<>(activityMap.values());

        //test 2
        assertEquals(2, activityMap.size());
        //assertTrue(activityList.contains(activity1));
        //assertTrue(activityList.contains(activity2));

        assertEquals(activity1.getName(), activityMap.get(activity1.getName()).getName());
        assertEquals(activity1.getBudget(), activityMap.get(activity1.getName()).getBudget());
        assertEquals(28, activityMap.get(activity1.getName()).getSpentBudget());
        assertEquals(activity1.getEventsIds(), activityMap.get(activity1.getName()).getEventsIds());
        assertThrows(IllegalArgumentException.class, () -> activity1.increaseSpentBudgetBy(-2));
        assertEquals(120, activityMap.get(activity2.getName()).getSpentBudget());

    }

    @Test
    void setBudget() {
        activityCalendarApi.setBudget(offlineCalendarId, activity1Name, 0);
        Activity activity = activityCalendarApi.getActivity(offlineCalendarId, activity1Name).orElse(null);
        assertEquals(0, activity.getBudget());

        activityCalendarApi.setBudget(offlineCalendarId, activity1Name, 34);
        activity = activityCalendarApi.getActivity(offlineCalendarId, activity1Name).orElse(null);
        assertEquals(34, activity.getBudget());

        assertThrows(NoSuchElementException.class, () ->
                activityCalendarApi.setBudget(offlineCalendarId, "dummy_activity_name_345", 44));

        assertThrows(IllegalArgumentException.class, () -> activity1.setBudget(-2));
    }

    /*@Test
    void addEvent() throws GeneralSecurityException, IOException {

        String APPLICATION_NAME = "ActivityCalendarAPI";
        JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
        String TOKENS_DIRECTORY_PATH = System.getProperty("user.home")+ "/.activityCalendar/resources/tokens";
        String CREDENTIALS_FILE_PATH = System.getProperty("user.home")+ "/.activityCalendar/resources/client_secret.json";

        //If scopes are modified, delete the previously saved "tokens" folder.
        List<String> SCOPES = Arrays.asList(CalendarScopes.CALENDAR_READONLY, CalendarScopes.CALENDAR_EVENTS);

        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        // Loading client secrets
        //InputStream in = Sample.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        String str = "/client_secret.json";
        InputStream in = ActivityCalendarApiImplTest.class.getResourceAsStream(str);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Building flow and triggering user authorization request
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

        Credential credential = new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");

        com.google.api.services.calendar.Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();



        DateTime now = new DateTime(System.currentTimeMillis());
        *//*Events events = service.events().list("primary")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();

        if (items.isEmpty()) {
            System.out.println("No upcoming events found.");
        } else {
            System.out.println("Upcoming events");
            for (Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                System.out.printf("%s (%s)\n", event.getSummary(), start);
            }
        }*//*

        Long twoHours = new Long(1000*2*3600);
        EventDateTime startTime = new EventDateTime().setDateTime(new DateTime(now.getValue()+twoHours*12));
        //EventDateTime startTime = new EventDateTime().setDateTime(now);
        String calendarId = service.calendars().get("primary").execute().getId();
        Event event = new Event()
                .setSummary("Conference")
                .setLocation("San Francisco")
                .setDescription("A conference.")
                .setStart(startTime)
                .setEnd(new EventDateTime().setDateTime(new DateTime(startTime.getDateTime().getValue()+twoHours)));

        activityCalendarApi.removeActivity(calendarId, activity1Name);
        activityCalendarApi.addEvent(service, calendarId, activity1Name, event);

        Activity activity = activityCalendarApi.getActivity(calendarId, activity1Name).orElse(null);
        assertEquals(2, activity.getSpentBudget());

        activityCalendarApi.addEvent(service, calendarId, activity1Name, event);
        activity = activityCalendarApi.getActivity(calendarId, activity1Name).orElse(null);
        assertEquals(4, activity.getSpentBudget());

        }*/

    @Test
    void getActivity() {
        Activity activity = activityCalendarApi.getActivity(offlineCalendarId, activity1Name).orElse(null);

        assertEquals(activity1, activity);
    }

    /*@Test
    void getActivitiesSpentPercentage() {

        Long twoHours = new Long(1000*2*3600);
        Long fourHours = 2*twoHours;
        Long threeHours = twoHours/2*3;
        Long fiveHours = twoHours+threeHours;
        EventDateTime startTime = new EventDateTime().setDateTime(new DateTime(new DateTime(System.currentTimeMillis()).getValue()+twoHours*12));
        Map<String, Activity> activities_mocked = new HashMap<>();
        Set<Event> eventSet = new HashSet<>();
        Activity activity;
        Event event;

        event = new Event()
                .setStart(startTime)
                .setEnd(new EventDateTime().setDateTime(new DateTime(startTime.getDateTime().getValue()+twoHours)));
        activity = new Activity("activity_twoHour");
        eventSet.add(event);
        activities_mocked.put("activity_twoHour", activity);

        event.setStart(startTime)
                .setEnd(new EventDateTime().setDateTime(new DateTime(startTime.getDateTime().getValue()+fiveHours)));
        activity = new Activity("activity_fiveHour");
        eventSet.add(event);
        activities_mocked.put("activity_fiveHour", activity);

        event.setStart(startTime)
                .setEnd(new EventDateTime().setDateTime(new DateTime(startTime.getDateTime().getValue()+threeHours)));
        activity = new Activity("activity_threeHour");
        eventSet.add(event);
        activities_mocked.put("activity_threeHour", activity);


        MockitoAnnotations.initMocks(this);
        when(activityCalendarApi_mocked.getActivities(anyString())).thenReturn(activities_mocked);


        Map<String, Double> map = activityCalendarApi_mocked.getActivitiesSpentPercentage("foo");

        assertEquals(map.get("activity_threeHour"), 30);


    }*/


   /* @Test
    void loadActivities() {
    }

    @Test
    void saveActivities() {

    }*/
}