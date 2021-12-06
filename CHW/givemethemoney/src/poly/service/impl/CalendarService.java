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
    로그아웃 시
    StoredCredential 삭제
     */
    public void deleteCred(){
        log.info("인증서 제거를 시작합니다.");
        dirMethod();
        File deleteCred = new File(TOKENS_DIRECTORY_PATH+"/StoredCredential");
        /*만약 해당 경로에 파일이 존재한다면*/
        if(deleteCred.exists()){
            deleteCred.setReadable(true);
            deleteCred.setWritable(true);
            /*삭제 실행*/
            deleteCred.delete();
            log.info("로그아웃 했습니다. 인증파일을 삭제했습니다.");
        }else{
            log.info("파일을 찾을 수 없습니다.");
        }
    }


    /*
    param: member_no
    return: int res
    member_no쪽 blob 형 데이터를 NULL로 업데이트
     */
    public int removeCredDB(MemberDTO pDTO) throws IOException{
        int res = 0;
        log.info("인증 파일을 member_no: " + pDTO.getMember_no());
        res = iMemberMapper.removeCredDB(pDTO);
        return res;
    }

    /*
    token 파일 저장 경로 설정
     */
    public void dirMethod(){
        String rootPath = this.getClass().getResource("").getPath();
        log.info("\"resources/credentials.json\"을 \n"+rootPath+" <-하위에 위치시켜주세요.");
        TOKENS_DIRECTORY_PATH=rootPath+"resources/tokens";
        MemberDTO pDTO = new MemberDTO();
    }

    /*
    param: member_no
    return: int res
    member_no쪽 stored_cred컬럼에 Storedcredentials파일 업로드
     */
    public int readCredData(MemberDTO pDTO) throws IOException {
        log.info(" 시작");
        dirMethod();

        int res = 0;
        FileInputStream fis= null;
        ByteArrayOutputStream baos =null;


        try{
            baos = new ByteArrayOutputStream();
            /*
            StoredCredential을 읽어 들이는 위치
             */
            log.info("인증 파일을 읽어들이는 위치: "+ TOKENS_DIRECTORY_PATH+"/StoredCredential");
            fis = new FileInputStream(TOKENS_DIRECTORY_PATH+"/StoredCredential");
            byte[] buf = new byte[4096];
            int read = 0;

            while((read= fis.read(buf, 0, buf.length)) != -1){
                baos.write(buf, 0, read);
            }

            /*
            오라클 BLOB형으로 저장하기 위해
             */
            byte[] returnValue = baos.toByteArray();

            /*리더의 인증파일*/
            pDTO.setStored_cred(returnValue);
            /*리더의 이메일*/
            log.info("인증 파일이 저장될 MEEBER_NO: " + pDTO.getMember_no());
            res = iMemberMapper.upcCred(pDTO);
            log.info("로컬에 저장된 인증파일 DB에 update 완료");


            /*
            //업데이트문으로 DB에 넣어주고

            DB에 올린다
            그리고 로컬 파일을 삭제한다.

            로그인 하면 DB에 있는 파일을 로컬로 다운로드 하고
            로그아웃 시 삭제

            토큰이 만료 되거나 갱신되면 전체 DB 같은 Team에 파일도 업데이트
             */

        }catch (FileNotFoundException e){
            e.printStackTrace();
            log.info(e.getMessage());
        }finally {
            pDTO = null;

            log.info(" 종료");
            return res;
        }
    }

    /*
    param: member_no
    return: int res
    member_no쪽 stored_cred컬럼에 blob형 데이터를 로컬에 Storedcredential 파일로 저장
     */
    public void writeCredData(MemberDTO pDTO) throws IOException {
        log.info(" 시작");
        FileOutputStream fos = null;
        MemberDTO rDTO = null;
        try{
            /*db에서 blob에 담긴 바이트데이터를 rDTO에 담아서
             * byte[] array에 담고
             * bais에 입력시켜
             * */
            log.info("조회할 member_no: " + pDTO.getMember_no());
            rDTO = iMemberMapper.storeCredFromDB(pDTO);
            byte[] fileByte = rDTO.getStored_cred();
            log.info("토큰 경로는: " + TOKENS_DIRECTORY_PATH);
            log.info("인증 파일 저장 경로 \n" + TOKENS_DIRECTORY_PATH+"/StoredCredential");
            fos = new FileOutputStream(TOKENS_DIRECTORY_PATH+"/StoredCredential");
            fos.write(fileByte);
        }catch (IOException e){
            e.printStackTrace();
            log.info(e.getMessage());
        }finally {
            pDTO = null;
            rDTO = null;
            fos.close();
            log.info(" 종료");
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


        //클라이언트 정보를 저장
        ClientId = clientSecrets.getDetails().getClientId();
        ClientSecret = clientSecrets.getDetails().getClientSecret();


        log.info("최종 토큰 경로 :" + TOKENS_DIRECTORY_PATH);
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
    처음 로그인하는 승인된 리더와 마스터의 인증파일을 로컬에 저장하고 DB에 올리고
    캘린더 이벤트 정보를 리턴
     */
    @Override
    public List<EventDTO> firstGetCredentials(MemberDTO pDTO) throws IOException, GeneralSecurityException {
        log.info("나는 firstGetCredentials");

        dirMethod();
        /*
        처음 실행 시
        구글 권한 인증 리다이렉트
        StoredCredential 파일 생성 및 캘린더 연동 결과 리턴
         */
        List<EventDTO> rlist = eventShow();

        /*
        pDTO에 포함된 member_id로 StoredCredential파일을 DB업로드
         */
        int res = readCredData(pDTO);
        log.info("firstGetCredentials 끝, 결과는 " + res);
        return rlist;
    }

    /*
    param: member_no
    return List<EventDTO>
    DB에 인증파일을 로컬에 저장한 후
    그것을 통해 캘린더 이벤트를 반환
     */
    @Override
    public List<EventDTO> getCredentialsAtLocal(MemberDTO pDTO) throws IOException, GeneralSecurityException{
        log.info("나는 getCredentialsAtLocal ");

        dirMethod();
        /*
        member_no로 stored_cred 조회를 한 후 로컬에 저장
         */
        writeCredData(pDTO);
        /*
        로컬에 저장된 인증 파일로 캘린더 리스트 반환
         */
        List<EventDTO> rlist = eventShow();

        log.info("getCredentialsAtLocal 종료");

        return rlist;
    }

    @Override
    public void insertEvent(EventDTO pDTO) throws IOException, GeneralSecurityException {



        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName("applicationName").build();



        log.info("이벤트 insert 시작");
        Event event = new Event()
                .setSummary(pDTO.getTitle())
                .setLocation("Pol")
                .setDescription("타이틀과 예약정보");

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
        log.info("이벤트 insert 끝");

        /*
        캘린더에 이벤트 추가하기 끝
         */

    }

    @Override
    public void deleteEvent(String event_id) throws IOException, GeneralSecurityException {
        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName("applicationName").build();
        log.info("삭제할 이벤트 ID: \n" + event_id);
        service.events().delete("primary", event_id).execute();
        log.info("삭제 실행 끝");
    }

    @Override
    public void updateEvent(EventDTO pDTO) throws IOException, GeneralSecurityException {
        log.info("예약시작");
        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName("applicationName").build();

        log.info("업데이트할 제목: " + pDTO.getTitle());
        Event event = service.events().get("primary", pDTO.getId()).execute();

        // Make a change
        event.setSummary(pDTO.getTitle());

        // Update the event
        Event updatedEvent = service.events().update("primary", event.getId(), event).execute();

        log.info(updatedEvent.getUpdated());
        log.info("업데이트 완료");
        log.info("업데이트 후 제목: " + event.getSummary());

        log.info("예약끝");
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
//            log.info("list에 값이 잘 담겼나?" + list.get(i).getStart());
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

//        캘린더 리스트 불러오기
        /**/
        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("------------------------");
        System.out.println("캘린더 목록 불러오기");
        String pageToken = null;
        do {
            CalendarList calendarList = service.calendarList().list().setPageToken(pageToken).execute();
            List<CalendarListEntry> items = calendarList.getItems();


            for (CalendarListEntry calendarListEntry : items) {
                System.out.println("------------------------");
                System.out.println("캘린더 제목: " + calendarListEntry.getSummary());
                System.out.println("접근제어규칙: " + calendarListEntry.getAccessRole());
                System.out.println("아이디: " + calendarListEntry.getId());
                System.out.println("리소스: " + calendarListEntry.getKind());
                System.out.println("접근 권한" + calendarListEntry.getAccessRole());
                System.out.println("------------------------");
            }
            pageToken = calendarList.getNextPageToken();
        } while (pageToken != null);



        System.out.println("캘린더 목록 불러오기 끝");



        //캘린더 목록 불러오기 결과
        /*
        ------------------------
        캘린더 목록 불러오기
        ------------------------
        캘린더 제목: 영어_DA_이현진
        접근제어규칙: reader
        아이디: c_classroom6496b114@group.calendar.google.com
        리소스: calendar#calendarListEntry
        ------------------------
        ------------------------
        캘린더 제목: 대한민국의 휴일
        접근제어규칙: reader
        아이디: ko.south_korea#holiday@group.v.calendar.google.com
        리소스: calendar#calendarListEntry
        ------------------------
        ------------------------
        캘린더 제목: 생일
        접근제어규칙: reader
        아이디: addressbook#contacts@group.v.calendar.google.com
        리소스: calendar#calendarListEntry
        ------------------------
        ------------------------
        캘린더 제목: 새로운 캘린더
        접근제어규칙: owner
        아이디: c_gpj36l9ipnhj092p2f9ctaldto@group.calendar.google.com
        리소스: calendar#calendarListEntry
        ------------------------
        ------------------------
        캘린더 제목: calendarSummaryTest
        접근제어규칙: owner
        아이디: c_ncnll8q659i2tln2fdg3j0hf28@group.calendar.google.com
        리소스: calendar#calendarListEntry
        ------------------------
        ------------------------
        캘린더 제목: 2120110005@gspace.kopo.ac.kr
        접근제어규칙: owner
        아이디: 2120110005@gspace.kopo.ac.kr
        리소스: calendar#calendarListEntry
        ------------------------

        Process finished with exit code 0

         */
        log.info(this.getClass().getName() + "getCalendarList end!");
    }
    public String showAccRUle() throws IOException, GeneralSecurityException{
        // Retrieve access rule
//        AclRule rule = service.acl().get("2120110005@gspace.kopo.ac.kr", "ruleId").execute();
//        System.out.println("calender rule을 보여줍니다.");
//        System.out.println(rule.getId() + ": " + rule.getRole());

        // Initialize Calendar service with valid OAuth credentials

        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName("applicationName").build();


        // Retrieve a specific calendar list entry
        /**/
        CalendarListEntry calendarListEntry = service.calendarList().get("primary").execute();

        System.out.println("제목: " + calendarListEntry.getSummary());
        System.out.println("아이디: "+ calendarListEntry.getId());

        //제목: 2120110005@gspace.kopo.ac.kr
        //아이디: 2120110005@gspace.kopo.ac.kr


        // Iterate over a list of access rules
        /*
        Acl acl = service.acl().list("primary").execute();

        for (AclRule rule : acl.getItems()) {
            System.out.println(":규칙 아이디: "+rule.getId() + " :적용 규칙: " + rule.getRole());

            //:규칙 아이디: user:2120110005@gspace.kopo.ac.kr :적용 규칙: owner
            //:규칙 아이디: domain:kopo.ac.kr :적용 규칙: reader

            }
         */

        // Retrieve access rule
        /*
        AclRule rule = service.acl().get("primary", "user:2120110005@gspace.kopo.ac.kr").execute();
        System.out.println(rule.getId() + ": " + rule.getRole());

        //실행 결과  user:2120110005@gspace.kopo.ac.kr: owner
         */
        return "";
    }
    public String testSample(String... args) throws IOException, GeneralSecurityException {

        NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();


//       Create a new calendar setting
        /*
         com.google.api.services.calendar.model.Calendar calendar = new com.google.api.services.calendar.model.Calendar();
         calendar.setSummary("새로운 캘린더");
         calendar.setTimeZone("Asia/Seoul");
         */



        // Initialize Calendar service with valid OAuth credentials
        Calendar service = new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName("applicationName").build();

        /*
        캘린더에 이벤트 추가하기
         */
        /*
        System.out.println("이벤트 insert 시작");
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
//            기본 캘린더에 추가하려면 primary
//        String calendarId = "primary";
        String calendarId = "c_ncnll8q659i2tln2fdg3j0hf28@group.calendar.google.com";
        event = service.events().insert(calendarId, event).execute();
        System.out.printf("Event created: %s\n", event.getHtmlLink());
        System.out.println("이벤트 insert 끝");
*/
        /*
        캘린더에 이벤트 추가하기 끝
         */





//      Insert the new calendar
        /*
        com.google.api.services.calendar.model.Calendar createdCalendar = service.calendars().insert(calendar).execute();
        System.out.println("###");
        System.out.println(createdCalendar.getId());
        System.out.println(createdCalendar.getSummary());
        System.out.println("###");
         */



//        refreshToken으로 accessToken 재발급

        String refreshtoken = getCredentials(HTTP_TRANSPORT).getRefreshToken();
        System.out.println("refreshToken: "+ refreshtoken);
        System.out.println("기존 access 토큰:\n"+getCredentials(HTTP_TRANSPORT).getAccessToken());
        TokenResponse tokenResponse = new GoogleRefreshTokenRequest(HTTP_TRANSPORT,JSON_FACTORY,
                refreshtoken, ClientId, ClientSecret).setScopes(SCOPES).setGrantType("refresh_token").execute();

        System.out.println("refreshtoken으로 새로 발급한 access 토큰:\n"+tokenResponse.getAccessToken());
        System.out.println("권한 확인: "+ tokenResponse.getScope());



        // Iterate through entries in calendar list 캘린더 목록에 있는 캘린더 리스트 확인
        /*
        String pageToken = null;
        do {
            CalendarList calendarList = service.calendarList().list().setPageToken(pageToken).execute();
            List<CalendarListEntry> items = calendarList.getItems();

            for (CalendarListEntry calendarListEntry : items) {
                System.out.println();
                System.out.println("제목: " + calendarListEntry.getSummary());
                System.out.println("id: " + calendarListEntry.getId());
                System.out.println("설명"+calendarListEntry.getDescription());
                System.out.println();
            }
            pageToken = calendarList.getNextPageToken();
        } while (pageToken != null);
         */


        //캘린더 리스트(목록) 확인 결과
        /*


        제목: 영어_DA_이현진
        id: c_classroom6496b114@group.calendar.google.com


        제목: 대한민국의 휴일
        id: ko.south_korea#holiday@group.v.calendar.google.com


        제목: 생일
        id: addressbook#contacts@group.v.calendar.google.com


        제목: 새로운 캘린더
        id: c_gpj36l9ipnhj092p2f9ctaldto@group.calendar.google.com


        제목: calendarSummaryTest
        id: c_ncnll8q659i2tln2fdg3j0hf28@group.calendar.google.com


        제목: 2120110005@gspace.kopo.ac.kr
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
                System.out.println("제목:"+event.getSummary());
                System.out.println("이벤트 아이디:"+event.getId());
                System.out.println("설명:"+event.getDescription());
                System.out.println("시작:"+event.getStart());
                System.out.println("끝:"+event.getEnd());
                System.out.println("--------------");
            }
            pageToken = events.getNextPageToken();
        } while (pageToken != null);
        */
        /*
        --------------
        제목:파트2
        이벤트 아이디:78d4ltrv0mk6f0eigp4tc3p2pc
        설명:null
        시작:{"dateTime":"2021-11-12T12:00:00.000+09:00","timeZone":"Asia/Seoul"}
        끝:{"dateTime":"2021-11-12T15:00:00.000+09:00","timeZone":"Asia/Seoul"}
        --------------
        --------------
        제목:파트1
        이벤트 아이디:5fbqutv1nhjeg0ahitr51i6pkp
        설명:null
        시작:{"dateTime":"2021-11-12T09:00:00.000+09:00","timeZone":"Asia/Seoul"}
        끝:{"dateTime":"2021-11-12T12:00:00.000+09:00","timeZone":"Asia/Seoul"}
        --------------
        --------------
        제목:테스트-일정
        이벤트 아이디:o3u4asi9e33bui7fm8vthkid00
        설명:일정 추가 테스트
        시작:{"dateTime":"2021-11-12T09:00:00.000+09:00","timeZone":"Asia/Seoul"}
        끝:{"dateTime":"2021-11-12T10:00:00.000+09:00","timeZone":"Asia/Seoul"}
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