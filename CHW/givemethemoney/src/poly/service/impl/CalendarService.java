package poly.service.impl;



import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import poly.dto.EventDTO;
import poly.dto.MemberDTO;
import poly.persistance.mapper.IMemberMapper;
import poly.service.ICalenderService;
import poly.util.CmmUtil;

import javax.annotation.Resource;
import java.io.*;
import java.security.GeneralSecurityException;
import java.util.*;

import static java.lang.String.format;

@Service("CalendarService")
public class CalendarService implements ICalenderService {

    @Resource(name = "MemberMapper")
    private IMemberMapper iMemberMapper;

    private final Logger log = Logger.getLogger(getClass().getName());
    private static final String APPLICATION_NAME = "Google Calendar API Java Quickstart";
    private static  JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
    private static String TOKENS_DIRECTORY_PATH = "";

    /**
     * Global instance of the scopes required by this quickstart.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private static final List<String> SCOPES = Collections.singletonList(CalendarScopes.CALENDAR);


    ///C:/SpringEx/CHW/givemethemoney/target/SpringPRJ2.0-0.0.1-SNAPSHOT/WEB-INF/classes/poly/service/impl/
    private static final String CREDENTIALS_FILE_PATH = "./resources/credentials.json";
    public static String ClientId = "";
    public static String ClientSecret = "";

    /*
    ???????????? ???
    StoredCredential ??????
     */
    public void deleteCred(){
        log.info("????????? ????????? ???????????????.");
        dirMethod();
        File deleteCred = new File(TOKENS_DIRECTORY_PATH+"/StoredCredential");
        /*?????? ?????? ????????? ????????? ???????????????*/
        if(deleteCred.exists()){
            deleteCred.setReadable(true);
            deleteCred.setWritable(true);
            /*?????? ??????*/
            deleteCred.delete();
            log.info("???????????? ????????????. ??????????????? ??????????????????.");
        }else{
            log.info("????????? ?????? ??? ????????????.");
        }
    }


    /*
    param: member_no
    return: int res
    member_no??? blob ??? ???????????? NULL??? ????????????
     */
    public int removeCredDB(MemberDTO pDTO) throws IOException{
        int res = 0;
        log.info("?????? ????????? member_no: " + pDTO.getMember_no());
        res = iMemberMapper.removeCredDB(pDTO);
        return res;
    }

    /*
    token ?????? ?????? ?????? ??????
     */
    public void dirMethod(){
        String rootPath = this.getClass().getResource("").getPath();
        log.info("\"resources/credentials.json\"??? \n"+rootPath+" <-????????? ?????????????????????.");
        TOKENS_DIRECTORY_PATH=rootPath+"resources/tokens";
        MemberDTO pDTO = new MemberDTO();
    }

    /*
    param: member_no
    return: int res
    member_no??? stored_cred????????? Storedcredentials?????? ?????????
     */
    public int readCredData(MemberDTO pDTO) throws IOException {
        log.info(" ??????");
        dirMethod();

        int res = 0;
        FileInputStream fis= null;
        ByteArrayOutputStream baos =null;


        try{
            baos = new ByteArrayOutputStream();
            /*
            StoredCredential??? ?????? ????????? ??????
             */
            log.info("?????? ????????? ??????????????? ??????: "+ TOKENS_DIRECTORY_PATH+"/StoredCredential");
            fis = new FileInputStream(TOKENS_DIRECTORY_PATH+"/StoredCredential");
            byte[] buf = new byte[4096];
            int read = 0;

            while((read= fis.read(buf, 0, buf.length)) != -1){
                baos.write(buf, 0, read);
            }

            /*
            ????????? BLOB????????? ???????????? ??????
             */
            byte[] returnValue = baos.toByteArray();

            /*????????? ????????????*/
            pDTO.setStored_cred(returnValue);
            /*????????? ?????????*/
            log.info("?????? ????????? ????????? MEEBER_NO: " + pDTO.getMember_no());
            res = iMemberMapper.upcCred(pDTO);
            log.info("????????? ????????? ???????????? DB??? update ??????");


            /*
            //????????????????????? DB??? ????????????

            DB??? ?????????
            ????????? ?????? ????????? ????????????.

            ????????? ?????? DB??? ?????? ????????? ????????? ???????????? ??????
            ???????????? ??? ??????

            ????????? ?????? ????????? ???????????? ?????? DB ?????? Team??? ????????? ????????????
             */

        }catch (FileNotFoundException e){
            e.printStackTrace();
            log.info(e.getMessage());
        }finally {
            pDTO = null;

            log.info(" ??????");
            return res;
        }
    }

    /*
    param: member_no
    return: int res
    member_no??? stored_cred????????? blob??? ???????????? ????????? Storedcredential ????????? ??????
     */
    public void writeCredData(MemberDTO pDTO) throws IOException {
        log.info(" ??????");
        FileOutputStream fos = null;
        MemberDTO rDTO = null;
        try{
            /*db?????? blob??? ?????? ????????????????????? rDTO??? ?????????
             * byte[] array??? ??????
             * bais??? ????????????
             * */
            log.info("????????? member_no: " + pDTO.getMember_no());
            rDTO = iMemberMapper.storeCredFromDB(pDTO);
            byte[] fileByte = rDTO.getStored_cred();
            log.info("?????? ?????????: " + TOKENS_DIRECTORY_PATH);
            log.info("?????? ?????? ?????? ?????? \n" + TOKENS_DIRECTORY_PATH+"/StoredCredential");
            fos = new FileOutputStream(TOKENS_DIRECTORY_PATH+"/StoredCredential");
            fos.write(fileByte);
        }catch (IOException e){
            e.printStackTrace();
            log.info(e.getMessage());
        }finally {
            pDTO = null;
            rDTO = null;
            fos.close();
            log.info(" ??????");
        }
    }

    /**
     * Creates an authorized Credential object.
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */

    public Credential getCredentials(NetHttpTransport HTTP_TRANSPORT) throws IOException {
        log.info("start");
        dirMethod();
        InputStream in = CalendarService.class.getResourceAsStream(CREDENTIALS_FILE_PATH);


        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));


        //??????????????? ????????? ??????
        ClientId = clientSecrets.getDetails().getClientId();
        ClientSecret = clientSecrets.getDetails().getClientSecret();


        log.info("?????? ?????? ?????? :" + TOKENS_DIRECTORY_PATH);
        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();

        log.info("end");
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /*
    param:  member_no
    return: List<EventDTO>
    ?????? ??????????????? ????????? ????????? ???????????? ??????????????? ????????? ???????????? DB??? ?????????
    ????????? ????????? ????????? ??????
     */
    @Override
    public List<EventDTO> firstGetCredentials(MemberDTO pDTO) throws IOException, GeneralSecurityException {
        log.info("?????? firstGetCredentials");

        dirMethod();
        /*
        ?????? ?????? ???
        ?????? ?????? ?????? ???????????????
        StoredCredential ?????? ?????? ??? ????????? ?????? ?????? ??????
         */
        List<EventDTO> rlist = eventShow();

        /*
        pDTO??? ????????? member_id??? StoredCredential????????? DB?????????
         */
        int res = readCredData(pDTO);
        log.info("firstGetCredentials ???, ????????? " + res);
        return rlist;
    }

    /*
    param: member_no
    return List<EventDTO>
    DB??? ??????????????? ????????? ????????? ???
    ????????? ?????? ????????? ???????????? ??????
     */
    @Override
    public List<EventDTO> getCredentialsAtLocal(MemberDTO pDTO) throws IOException, GeneralSecurityException{
        log.info("?????? getCredentialsAtLocal ");

        dirMethod();
        /*
        member_no??? stored_cred ????????? ??? ??? ????????? ??????
         */
        writeCredData(pDTO);
        /*
        ????????? ????????? ?????? ????????? ????????? ????????? ??????
         */
        List<EventDTO> rlist = eventShow();

        log.info("getCredentialsAtLocal ??????");

        return rlist;
    }

    @Override
    public void insertEvent(EventDTO pDTO) throws IOException, GeneralSecurityException {



        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName("applicationName").build();



        log.info("????????? insert ??????");
        Event event = new Event()
                .setSummary(pDTO.getTitle())
                .setLocation("Pol")
                .setDescription("???????????? ????????????");

        DateTime startDateTime = new DateTime(pDTO.getStart());
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Asia/Seoul");
        event.setStart(start);

        DateTime endDateTime = new DateTime(pDTO.getEnd());
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Asia/Seoul");
        event.setEnd(end);
        String[] recurrence = new String[] {"RRULE:FREQ=WEEKLY;BYDAY="+pDTO.getDays()+";UNTIL="+pDTO.getUntil()};
        event.setRecurrence(Arrays.asList(recurrence));
//
//        EventAttendee[] attendees = new EventAttendee[] {
//                new EventAttendee().setEmail("kjj6393@mail.com"),
//                new EventAttendee().setEmail("ohhanmin000@gmail.com"),
//        };
//        event.setAttendees(Arrays.asList(attendees));
//
//        EventReminder[] reminderOverrides = new EventReminder[] {
//                new EventReminder().setMethod("email").setMinutes(24 * 60),
//                new EventReminder().setMethod("popup").setMinutes(10),
//        };
//        Event.Reminders reminders = new Event.Reminders()
//                .setUseDefault(true)
//                .setOverrides(Arrays.asList(reminderOverrides));
//        event.setReminders(reminders);
        String calendarId = "primary";
        event = service.events().insert(calendarId, event).execute();
        System.out.printf("Event created: %s\n", event.getHtmlLink());
        log.info("????????? insert ???");

        /*
        ???????????? ????????? ???????????? ???
         */

    }

    @Override
    public void deleteEvent(String event_id) throws IOException, GeneralSecurityException {
        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName("applicationName").build();
        log.info("????????? ????????? ID: \n" + event_id);
        service.events().delete("primary", event_id).execute();
        log.info("?????? ?????? ???");
    }

    @Override
    public void updateEvent(EventDTO pDTO) throws IOException, GeneralSecurityException {
        log.info("????????????");
        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName("applicationName").build();

        log.info("??????????????? ??????: " + pDTO.getTitle());
        Event event = service.events().get("primary", pDTO.getId()).execute();

        // Make a change
        event.setSummary(pDTO.getTitle());

        // Update the event
        Event updatedEvent = service.events().update("primary", event.getId(), event).execute();

        log.info(updatedEvent.getUpdated());
        log.info("???????????? ??????");
        log.info("???????????? ??? ??????: " + event.getSummary());

        log.info("?????????");
    }


    public List<EventDTO> eventShow() throws IOException, GeneralSecurityException {
        log.info("start !");



        List<EventDTO> list = new ArrayList<EventDTO>();

        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();

        // Initialize Calendar service with valid OAuth credentials
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName("applicationName").build();

        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = service.events().list("primary")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        int i = 0;
        List<Event> items = events.getItems();
        if (items.isEmpty()) {
            System.out.println("No upcoming events found.");
        } else {
            System.out.println("Upcoming events");
            for (Event event : items) {
                EventDTO pDTO = new EventDTO();
                DateTime start = event.getStart().getDateTime();
                DateTime end = event.getEnd().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }


                pDTO.setId(CmmUtil.nvl(event.getId()));
//                pDTO.setDescription(CmmUtil.nvl(event.getDescription()));
                pDTO.setTitle(CmmUtil.nvl(event.getSummary()));
                pDTO.setStart(CmmUtil.nvl(start.toString()));
                pDTO.setEnd(CmmUtil.nvl(end.toString()));
                list.add(pDTO);
                pDTO = null;
//                log.info(event.getStart()+"::" +event.getEnd()+"::"+event.getSummary());


            }
        }
//        for(i= 0; i< list.size(); i++){
//            log.info("list??? ?????? ??? ??????????" + list.get(i).getStart());
//        }

        log.info("end !");

        return list;
    }


    @Override
    public MemberDTO memberinfo(MemberDTO mDTO) {
        return iMemberMapper.memberinfo(mDTO);
    }


    public void getCalendarList() throws IOException, GeneralSecurityException{
        log.info(this.getClass().getName() + "getCalendarList start!");
        /* */
//        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
//                .setApplicationName("applicationName").build();




        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(new NetHttpTransport())
                .setJsonFactory(JSON_FACTORY)
                .setClientSecrets("client_id", "client_secret")
                .setServiceAccountScopes(SCOPES)
                .build();

        credential.setAccessToken("access_token");



        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName("applicationName").build();

//        ????????? ????????? ????????????
        /**/
        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("????????? ?????? ????????????");
        String pageToken = null;
        do {
            CalendarList calendarList = service.calendarList().list().setPageToken(pageToken).execute();
            List<CalendarListEntry> items = calendarList.getItems();


            for (CalendarListEntry calendarListEntry : items) {
                System.out.println("------------------------");
                System.out.println("????????? ??????: " + calendarListEntry.getSummary());
                System.out.println("??????????????????: " + calendarListEntry.getAccessRole());
                System.out.println("?????????: " + calendarListEntry.getId());
                System.out.println("?????????: " + calendarListEntry.getKind());
                System.out.println("?????? ??????" + calendarListEntry.getAccessRole());
                System.out.println("------------------------");
            }
            pageToken = calendarList.getNextPageToken();
        } while (pageToken != null);



        System.out.println("????????? ?????? ???????????? ???");



        //????????? ?????? ???????????? ??????
        /*
        ------------------------
        ????????? ?????? ????????????
        ------------------------
        ????????? ??????: ??????_DA_?????????
        ??????????????????: reader
        ?????????: c_classroom6496b114@group.calendar.google.com
        ?????????: calendar#calendarListEntry
        ------------------------
        ------------------------
        ????????? ??????: ??????????????? ??????
        ??????????????????: reader
        ?????????: ko.south_korea#holiday@group.v.calendar.google.com
        ?????????: calendar#calendarListEntry
        ------------------------
        ------------------------
        ????????? ??????: ??????
        ??????????????????: reader
        ?????????: addressbook#contacts@group.v.calendar.google.com
        ?????????: calendar#calendarListEntry
        ------------------------
        ------------------------
        ????????? ??????: ????????? ?????????
        ??????????????????: owner
        ?????????: c_gpj36l9ipnhj092p2f9ctaldto@group.calendar.google.com
        ?????????: calendar#calendarListEntry
        ------------------------
        ------------------------
        ????????? ??????: calendarSummaryTest
        ??????????????????: owner
        ?????????: c_ncnll8q659i2tln2fdg3j0hf28@group.calendar.google.com
        ?????????: calendar#calendarListEntry
        ------------------------
        ------------------------
        ????????? ??????: 2120110005@gspace.kopo.ac.kr
        ??????????????????: owner
        ?????????: 2120110005@gspace.kopo.ac.kr
        ?????????: calendar#calendarListEntry
        ------------------------

        Process finished with exit code 0

         */
        log.info(this.getClass().getName() + "getCalendarList end!");
    }
    public String showAccRUle() throws IOException, GeneralSecurityException{
        // Retrieve access rule
//        AclRule rule = service.acl().get("2120110005@gspace.kopo.ac.kr", "ruleId").execute();
//        System.out.println("calender rule??? ???????????????.");
//        System.out.println(rule.getId() + ": " + rule.getRole());

        // Initialize Calendar service with valid OAuth credentials

        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName("applicationName").build();


        // Retrieve a specific calendar list entry
        /**/
        CalendarListEntry calendarListEntry = service.calendarList().get("primary").execute();

        System.out.println("??????: " + calendarListEntry.getSummary());
        System.out.println("?????????: "+ calendarListEntry.getId());

        //??????: 2120110005@gspace.kopo.ac.kr
        //?????????: 2120110005@gspace.kopo.ac.kr


        // Iterate over a list of access rules
        /*
        Acl acl = service.acl().list("primary").execute();

        for (AclRule rule : acl.getItems()) {
            System.out.println(":?????? ?????????: "+rule.getId() + " :?????? ??????: " + rule.getRole());

            //:?????? ?????????: user:2120110005@gspace.kopo.ac.kr :?????? ??????: owner
            //:?????? ?????????: domain:kopo.ac.kr :?????? ??????: reader

            }
         */

        // Retrieve access rule
        /*
        AclRule rule = service.acl().get("primary", "user:2120110005@gspace.kopo.ac.kr").execute();
        System.out.println(rule.getId() + ": " + rule.getRole());

        //?????? ??????  user:2120110005@gspace.kopo.ac.kr: owner
         */
        return "";
    }
    public String testSample(String... args) throws IOException, GeneralSecurityException {

        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();


//       Create a new calendar setting
        /*
         com.google.api.services.calendar.model.Calendar calendar = new com.google.api.services.calendar.model.Calendar();
         calendar.setSummary("????????? ?????????");
         calendar.setTimeZone("Asia/Seoul");
         */



        // Initialize Calendar service with valid OAuth credentials
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName("applicationName").build();

        /*
        ???????????? ????????? ????????????
         */
        /*
        System.out.println("????????? insert ??????");
        Event event = new Event()
                .setSummary("Google I/O 2015")
                .setLocation("800 Howard St., San Francisco, CA 94103")
                .setDescription("A chance to hear more about Google's developer products.");

        DateTime startDateTime = new DateTime("2021-11-19T09:00:00-07:00");
        EventDateTime start = new EventDateTime()
                .setDateTime(startDateTime)
                .setTimeZone("Asia/Seoul");
        event.setStart(start);

        DateTime endDateTime = new DateTime("2021-11-26T17:00:00-09:00");
        EventDateTime end = new EventDateTime()
                .setDateTime(endDateTime)
                .setTimeZone("Asia/Seoul");
        event.setEnd(end);

        String[] recurrence = new String[] {"RRULE:FREQ=DAILY;COUNT=2"};
        event.setRecurrence(Arrays.asList(recurrence));

        EventAttendee[] attendees = new EventAttendee[] {
                new EventAttendee().setEmail("kjj6393@mail.com"),
                new EventAttendee().setEmail("ohhanmin000@gmail.com"),
        };
        event.setAttendees(Arrays.asList(attendees));

        EventReminder[] reminderOverrides = new EventReminder[] {
                new EventReminder().setMethod("email").setMinutes(24 * 60),
                new EventReminder().setMethod("popup").setMinutes(10),
        };
        Event.Reminders reminders = new Event.Reminders()
                .setUseDefault(true)
                .setOverrides(Arrays.asList(reminderOverrides));
        event.setReminders(reminders);
//            ?????? ???????????? ??????????????? primary
//        String calendarId = "primary";
        String calendarId = "c_ncnll8q659i2tln2fdg3j0hf28@group.calendar.google.com";
        event = service.events().insert(calendarId, event).execute();
        System.out.printf("Event created: %s\n", event.getHtmlLink());
        System.out.println("????????? insert ???");
*/
        /*
        ???????????? ????????? ???????????? ???
         */





//      Insert the new calendar
        /*
        com.google.api.services.calendar.model.Calendar createdCalendar = service.calendars().insert(calendar).execute();
        System.out.println("###");
        System.out.println(createdCalendar.getId());
        System.out.println(createdCalendar.getSummary());
        System.out.println("###");
         */



//        refreshToken?????? accessToken ?????????

        String refreshtoken = getCredentials(HTTP_TRANSPORT).getRefreshToken();
        System.out.println("refreshToken: "+ refreshtoken);
        System.out.println("?????? access ??????:\n"+getCredentials(HTTP_TRANSPORT).getAccessToken());
        TokenResponse tokenResponse = new GoogleRefreshTokenRequest(HTTP_TRANSPORT,JSON_FACTORY,
                refreshtoken, ClientId, ClientSecret).setScopes(SCOPES).setGrantType("refresh_token").execute();

        System.out.println("refreshtoken?????? ?????? ????????? access ??????:\n"+tokenResponse.getAccessToken());
        System.out.println("?????? ??????: "+ tokenResponse.getScope());



        // Iterate through entries in calendar list ????????? ????????? ?????? ????????? ????????? ??????
        /*
        String pageToken = null;
        do {
            CalendarList calendarList = service.calendarList().list().setPageToken(pageToken).execute();
            List<CalendarListEntry> items = calendarList.getItems();

            for (CalendarListEntry calendarListEntry : items) {
                System.out.println();
                System.out.println("??????: " + calendarListEntry.getSummary());
                System.out.println("id: " + calendarListEntry.getId());
                System.out.println("??????"+calendarListEntry.getDescription());
                System.out.println();
            }
            pageToken = calendarList.getNextPageToken();
        } while (pageToken != null);
         */


        //????????? ?????????(??????) ?????? ??????
        /*


        ??????: ??????_DA_?????????
        id: c_classroom6496b114@group.calendar.google.com


        ??????: ??????????????? ??????
        id: ko.south_korea#holiday@group.v.calendar.google.com


        ??????: ??????
        id: addressbook#contacts@group.v.calendar.google.com


        ??????: ????????? ?????????
        id: c_gpj36l9ipnhj092p2f9ctaldto@group.calendar.google.com


        ??????: calendarSummaryTest
        id: c_ncnll8q659i2tln2fdg3j0hf28@group.calendar.google.com


        ??????: 2120110005@gspace.kopo.ac.kr
        id: 2120110005@gspace.kopo.ac.kr


        Process finished with exit code 0

         */






//
// Insert new access rule
        /*


        role	string	The role assigned to the scope. Possible values are:
            "none" - Provides no access.
            "freeBusyReader" - Provides read access to free/busy information.
            "reader" - Provides read access to the calendar. Private events will appear to users with reader access, but event details will be hidden.
            "writer" - Provides read and write access to the calendar. Private events will appear to users with writer access, and event details will be visible.
            "owner" - Provides ownership of the calendar. This role has all of the permissions of the writer role with the additional ability to see and manipulate ACLs.
            writable

        scope	object	The extent to which calendar access is granted by this ACL rule.
            none	Provides no access.
            freeBusyReader	Lets the grantee see whether the calendar is free or busy at a given time, but does not allow access to event details. Free/busy information can be retrieved using the freeBusy.query operation.
            reader	Lets the grantee read events on the calendar.
            writer	Lets the grantee read and write events on the calendar.
            owner	Provides ownership of the calendar. This role has all of the permissions of the writer role with the additional ability to see and manipulate ACLs.
        scope.type	string	The type of the scope. Possible values are:
            "default" - The public scope. This is the default value.
            "user" - Limits the scope to a single user.
            "group" - Limits the scope to a group.
            "domain" - Limits the scope to a domain.
            Note: The permissions granted to the "default", or public, scope apply to any user, authenticated or not.

        scope.value
            string	The email address of a user or group, or the name of a domain, depending on the scope type. Omitted for type "default".
         */
        /*
        // Create access rule with associated scope
        AclRule rule = new AclRule();
        AclRule.Scope scope = new AclRule.Scope();
        scope.setType("user").setValue("kjj6393@gmail.com");
        rule.setScope(scope).setRole("writer");

        AclRule createdRule = service.acl().insert("primary", rule).execute();
        System.out.println("ruleID : "+ createdRule.getId());
        System.out.println("reuleScope : " + createdRule.getScope());
         */
        /*
        ruleID : user:kjj6393@gmail.com
        reuleScope : {"type":"user","value":"kjj6393@gmail.com"}
         */


        // Iterate over the events in the specified calendar
        /*


        String pageToken = null;
        do {
            Events events = service.events().list("primary").setPageToken(pageToken).execute();
            List<Event> items = events.getItems();
            for (Event event : items) {
                System.out.println("--------------");
                System.out.println("??????:"+event.getSummary());
                System.out.println("????????? ?????????:"+event.getId());
                System.out.println("??????:"+event.getDescription());
                System.out.println("??????:"+event.getStart());
                System.out.println("???:"+event.getEnd());
                System.out.println("--------------");
            }
            pageToken = events.getNextPageToken();
        } while (pageToken != null);
        */
        /*
        --------------
        ??????:??????2
        ????????? ?????????:78d4ltrv0mk6f0eigp4tc3p2pc
        ??????:null
        ??????:{"dateTime":"2021-11-12T12:00:00.000+09:00","timeZone":"Asia/Seoul"}
        ???:{"dateTime":"2021-11-12T15:00:00.000+09:00","timeZone":"Asia/Seoul"}
        --------------
        --------------
        ??????:??????1
        ????????? ?????????:5fbqutv1nhjeg0ahitr51i6pkp
        ??????:null
        ??????:{"dateTime":"2021-11-12T09:00:00.000+09:00","timeZone":"Asia/Seoul"}
        ???:{"dateTime":"2021-11-12T12:00:00.000+09:00","timeZone":"Asia/Seoul"}
        --------------
        --------------
        ??????:?????????-??????
        ????????? ?????????:o3u4asi9e33bui7fm8vthkid00
        ??????:?????? ?????? ?????????
        ??????:{"dateTime":"2021-11-12T09:00:00.000+09:00","timeZone":"Asia/Seoul"}
        ???:{"dateTime":"2021-11-12T10:00:00.000+09:00","timeZone":"Asia/Seoul"}
        --------------

        Process finished with exit code 0

         */





        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = service.events().list("primary")
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
                DateTime end = event.getEnd().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                System.out.printf("%s \n (%s) :: (%s)\n", event.getSummary() , start, end);

                System.out.println("--------------------------------------------------");
            }
        }
        return "1";


    }
}