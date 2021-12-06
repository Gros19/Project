package poly.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import poly.dto.EventDTO;
import poly.dto.MemberDTO;
import poly.service.ICalenderService;
import poly.service.INoticeService;
import poly.util.CmmUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Controller
public class CalendarController {
    private Logger log = Logger.getLogger(this.getClass().getName());

    @Resource(name = "NoticeService")
    private INoticeService noticeService;

    @Resource(name = "CalendarService")
    private ICalenderService iCalenderService;


    @RequestMapping(value = "getCalendarEvents")
    public String getCalendarEvents(HttpServletRequest req, HttpServletResponse resp, ModelMap model, HttpSession session) throws IOException, GeneralSecurityException {
        log.info("event show start");
        MemberDTO login = (MemberDTO) session.getAttribute("memberinfo");
        log.info(login.getMember_auth()+ "권한의 "+ login.getMember_id()+"가 캘린더에 접속합니다.");
        List<EventDTO> rlist =  null;






       if (Objects.isNull(login.getStored_cred())) {
            log.info("처음 실행하는 리더, 마스터 로그인");
           /*
            leader N null
            master N null
            */
            //권한이 리더랑 마스터이고 처음 로그인 할 떄 실행
            //DB에 blob형은 null 허용이고, 초기값이 null이기 때문에 Objects.isNull()사용
            rlist = iCalenderService.firstGetCredentials(login);
        } else {
            log.info("스탭, 또는 마스터나 리더의 첫번째 이후 로그인");
            /*
            staff Y blob
            leader Y blob
            master Y blob
             */
            rlist = iCalenderService.getCredentialsAtLocal(login);
        }
       model.addAttribute("showSchedule", rlist);
       model.addAttribute("session", login);

        log.info("event show end");
        return "/home";
    }

    @RequestMapping(value = "NewEventAtTheGC", method = RequestMethod.POST)
    public String newEventAtTheGC(HttpServletRequest request, HttpServletResponse response,
                                  ModelMap model) throws Exception {
        log.info("NewEventAtTheGC Start");
        String title = CmmUtil.nvl(request.getParameter("title"));
        String Ydate = CmmUtil.nvl(request.getParameter("date"));
        String until = CmmUtil.nvl(request.getParameter("until"));
        String start = CmmUtil.nvl(request.getParameter("start"));
        String end = CmmUtil.nvl(request.getParameter("end"));
        String[] week = request.getParameterValues("week");
        String need_staff = CmmUtil.nvl(request.getParameter("need_staff"), "0");
        String now_staff = CmmUtil.nvl(request.getParameter("now_staff"), "0");
        /*

        커밋 test
         */

        String days = "";
        for (String day : week) {
            days += day + " ";
        }
        days = days.trim();
        days = days.replaceAll(" ", ",");
        until = until.replaceAll("-", "");
        start = start + ":00+09:00";
        end = end + ":00+09:00";
        title = title + "(" + need_staff + "/" + "0)";


        log.info("title: " + title);
        log.info("start: " + start);
        log.info("end: " + end);
        log.info("until: " + until);
        log.info("days:" + days);
        EventDTO pDTO = new EventDTO();
        pDTO.setTitle(title);
        pDTO.setStart(start);
        pDTO.setEnd(end);
        pDTO.setUntil(until);
        pDTO.setDays(days);
        iCalenderService.insertEvent(pDTO);

        log.info("NewEventAtTheGC End");
        return "redirect:getCalendarEvents.do";
    }

    @RequestMapping(value = "deleteEvent", method = RequestMethod.POST)
    public void remove(HttpServletRequest request) throws GeneralSecurityException, IOException {
        log.info("deleteEvent start");
        String event_id = CmmUtil.nvl(request.getParameter("event_id"));
        log.info("event_id:\n" + event_id);

        iCalenderService.deleteEvent(event_id);

        log.info("deleteEvent end");
//        return "redirect:getCalendarEvents.do";
    }


    @RequestMapping(value = "updateTitle", method = RequestMethod.POST)
    public void updateTitle(HttpServletRequest request) throws GeneralSecurityException, IOException {
        log.info("updateTitle start");
        String event_id = CmmUtil.nvl(request.getParameter("event_id"));
        String title = CmmUtil.nvl(request.getParameter("title"));
        String need_staff = CmmUtil.nvl(request.getParameter("need_staff"));
        String now_staff = CmmUtil.nvl(request.getParameter("now_staff"));
        title = title + "(" + need_staff + "/" + now_staff + ")";
        log.info("event_id:\n" + event_id);
        log.info("title:\n" + title);
        EventDTO pDTO = new EventDTO();

        pDTO.setId(event_id);
        pDTO.setTitle(title);

        iCalenderService.updateEvent(pDTO);

        log.info("updateTitle end");
//        return "redirect:getCalendarEvents.do";
    }


}
