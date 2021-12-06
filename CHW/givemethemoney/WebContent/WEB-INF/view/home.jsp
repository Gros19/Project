<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="poly.dto.EventDTO" %>
<%@ page import="java.util.Iterator" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="poly.dto.MemberDTO" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>Quixlab - Bootstrap Admin Dashboard Template by Themefisher.com</title>
    <!-- Favicon icon -->
    <link rel="icon" type="image/png" sizes="16x16" href="${pageContext.request.contextPath}/images/favicon.png">
    <!-- Custom Stylesheet -->
    <%--     <link href="${pageContext.request.contextPath}/plugins/fullcalendar/css/fullcalendar.min.css" rel="stylesheet"> --%>
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/fullcalendar/lib/main.css" rel="stylesheet">
    <script src="${pageContext.request.contextPath}/fullcalendar/lib/main.js"></script>
    <script src="https://code.jquery.com/jquery-latest.min.js"></script>


    <script language="JavaScript" charset="UTF-8">

        /*
        한민 session으로 관리자 권한 기능 다시 설정
         */
        <%MemberDTO SESSION_DTO = (MemberDTO) request.getAttribute("session");%>
        var access = "<%=SESSION_DTO.getMember_auth()%>";
        var events_arr = new Array();

        window.onload = function () {
            document.getElementById("sbtn").onclick = function () {
                if (access == "master" || access == "leader") {
                    var pattern = /[~!@#$%^&*()_+|<>?:{}\\/]/gi;
                    var title_val = document.getElementById("title_name").value;

                    if (pattern.test(title_val)) {
                        alert("제목에 특수문자는 사용할 수 없습니다..!");
                        document.newForm.title.focus();

                        return false;
                    } else if (!document.newForm.title.value) {
                        alert("제목을 입력하세요!");
                        document.newForm.title.focus();
                        return false;
                    } else if (!document.newForm.start.value) {
                        alert("추가할 파트의 시작시간을 입력하세요!");
                        document.newForm.start.focus();
                        return false;
                    } else if (!document.newForm.end.value) {
                        alert("추가할 파트의 종료시간을 입력하세요!");
                        document.newForm.end.focus();
                        return false;
                    } else if (!document.newForm.until.value) {
                        alert("반복 종료 날자를 입력하세요.");
                        document.newForm.until.focus();
                        return false;
                    } else if (!document.newForm.need_staff.value) {
                        alert("추가할 파트의 모집 인원을 입력하세요!");
                        document.newForm.need_staff.focus();
                        return false;
                    }


                    var dateStart = new Date(document.newForm.start.value);
                    var dateEnd = new Date(document.newForm.end.value);
                    console.log("dateStart: " + dateStart);
                    console.log("dateEnd: " + dateEnd);
                    console.log("dateStart < dateEnd: ");
                    console.log(dateStart.getTime() > dateEnd.getTime());
                    if (dateStart.getTime() >= dateEnd.getTime()) {
                        alert("종료시간은 시작시간 이후여야 합니다.");
                        document.newForm.start.focus();
                        document.newForm.end.focus();
                        return false;
                    }


                    alert("체크박스 체크 수: " + $("input[name='week']:checked").length);
                    if ($("input[name='week']:checked").length == 0) {
                        $(".default-check").prop("checked", true);
                    }

                    alert("수정 후 체크 수: " + $("input[name='week']:checked").length);
                    alert("전송하기");
                    var x = document.getElementsByName("newForm");
                    x[0].submit();
                    return false;
                } else {
                    alert("접근 권한이 없습니다.");
                }

            };
        };


        document.addEventListener('DOMContentLoaded', function () {
            var calendarE1 = document.getElementById('calendar');
            var calendar = new FullCalendar.Calendar(calendarE1, {
                headerToolbar: {
                    left: 'prev,next today',
                    center: 'title',
                    right: 'dayGridMonth,timeGridWeek,timeGridDay,listMonth'
                },
                locale: "ko",
                weekNumbers: true,
                navLinks: true, // can click day/week names to navigate views
                editable: true,
                contentHeight: 'auto',
                minTime: '00:00:00',
                maxTime: '24:00:00',
                selectable: true,
                nowIndicator: true,
                dayMaxEvents: true, // allow "more" link when too many events
                // showNonCurrentDates: false,

                // select: function(arg) {
                //     var title = prompt('Event Title:');
                //     if (title) {
                //         calendar.addEvent({
                //             title: title,
                //             start: arg.start,
                //             end: arg.end,
                //             allDay: arg.allDay
                //         })
                //     }
                //     calendar.unselect()
                // },
                // eventClick: function(arg) {
                //
                //     if (confirm('AAAAre you sure you want to delete this event?')) {
                //         arg.event.remove()
                //     }
                // },

                dateClick: function (info) {
                    alert('clicked ' + info.dateStr);
                    console.log(' on resource ');
                },
                select: function (info) {
                    alert('selected ' + info.startStr + ' to ' + info.endStr);
                    console.log(' on resource ')
                },

                eventClick: function (info) {
                    var eventObj = info.event;
                    if (access == "master" || access == "leader") {
                        if (eventObj.id) {
                            alert('Clicked Event Id = ' + eventObj.title);
                            if (confirm("삭제 하시겠습니까?") == true) {
                                $.post("/deleteEvent.do", {event_id: eventObj.id});

                                alert(eventObj.title + "삭제중입니다.");
                                setTimeout(function () {
                                    location.href = "/getCalendarEvents.do";
                                }, 1000);
                            }
                        }
                    } else {
                        //12~18(8/7)
                        var evOtitle = eventObj.title;
                        var leftKnife = evOtitle.indexOf('(');
                        var middleKnife = evOtitle.indexOf('/');
                        var rightKnife = evOtitle.indexOf(')');
                        var need_staff = evOtitle.slice(leftKnife + 1, middleKnife);
                        //8
                        var now_staff = evOtitle.slice(middleKnife + 1, rightKnife);
                        //7
                        alert("필요인원은 " + need_staff + "\n현재 인원은 " + now_staff);
                        if (Number(now_staff) < Number(need_staff)) {
                            if (confirm("예약하시겠습니까?") == true) {
                                var new_title = evOtitle.slice(0, leftKnife);
                                now_staff = (Number(now_staff) + 1) + "";
                                //1 counting now_staff  7 = 8
                                //(8/8)
                                // alert("ㅇㅋ 기다려봐");
                                // alert(eventObj.id);
                                // alert(new_title);
                                console.log("new_title: " + new_title);
                                console.log("eventObj.id: " + eventObj.id);
                                console.log("now_staff: " + now_staff);
                                console.log("need_staff: " + need_staff);

                                $.post("/updateTitle.do", {
                                    event_id: eventObj.id,
                                    title: new_title,
                                    now_staff: now_staff,
                                    need_staff: need_staff
                                });
                               // info.event.remove();

                                alert(new_title + "으로 예약중입니다.");
                                setTimeout(function () {
                                    location.href = "/getCalendarEvents.do";
                                }, 800);

                            } else {
                                alert("취소하셨습니다");
                            }
                        } else {
                            alert("인원이 다 찼습니다");
                        }


                    }

                },
                //한민 글자 색 수정
                eventMouseEnter: function (calEvent) {
                    var tooltip = '<div class="tooltipevent" style="color: initial;width:auto;height:120px;vertical-align:middle;background:' + calEvent.el.style.backgroundColor + ';position:absolute;z-index:10001;">' + ' ' + calEvent.event.start.toString().slice(16, 25) + '<br/>' + '~' + calEvent.event.end.toString().slice(16, 25) + '</div>';
                    $("body").append(tooltip);
                    $(this.el).mouseover(function (e) {
                        $(this.el).css('z-index', 10000);
                        $('.tooltipevent').fadeIn('500');
                        $('.tooltipevent').fadeTo('10', 1.9);
                    }).mousemove(function (e) {
                        $('.tooltipevent').css('top', e.pageY + 10);
                        $('.tooltipevent').css('left', e.pageX + 20);
                    });
                },

                eventMouseLeave: function (calEvent, jsEvent) {
                    $(this.el).css('z-index', 8);
                    $('.tooltipevent').remove();
                },

                events: [
                    <%
             List<EventDTO> rlist = (ArrayList<EventDTO>)request.getAttribute("showSchedule");
             %>

                    <%
                    Iterator<EventDTO> it = rlist.iterator();
                    while(it.hasNext())
                    {
                        EventDTO rDTO = it.next();

                    %>
                    {
                        id: '<%=rDTO.getId()%>',
                        title: '<%= rDTO.getTitle() %>',
                        start: '<%= rDTO.getStart() %>',
                        end: '<%= rDTO.getEnd() %>'
                    },
                    <%
                    }
                    %>

                    {
                        title: 'default',
                        start: '2019-01-01',
                        end: '2019-01-01'
                    }
                ],
                loading: function (bool) {
                    document.getElementById('loading').style.display =
                        bool ? 'block' : 'none';
                }

            });
            calendar.render();
        });


    </script>

    <style>

        html, body {
            margin: 0;
            padding: 0;
            font-family: Arial, Helvetica Neue, Helvetica, sans-serif;
            font-size: 14px;
        }
    </style>
</head>

<body>

<jsp:include page="/WEB-INF/view/templates/sidebar.jsp"></jsp:include>
<!--**********************************
    Content body start
***********************************-->
<div class="content-body">

    <div class="row page-titles mx-0">
        <div class="col p-md-0">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="javascript:void(0)">Dashboard</a></li>
                <li class="breadcrumb-item active"><a href="javascript:void(0)">Home</a></li>
            </ol>
        </div>
    </div>
    <!-- row -->

    <div class="container-fluid">
        <div class="row">
            <div class="col-lg-12">
                <div class="card">
                    <div class="card-body">
                        <div class="card-title">
                            <h4>Calendar</h4>
                        </div>
                        <div class="row">
                            <div class="col-lg-4 mt-5">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h4 class="modal-title"><strong>Add a category</strong></h4>
                                    </div>
                                    <div class="modal-body">
                                        <form action="/NewEventAtTheGC.do" name="newForm" method="post">
                                            <div class="row">
                                                <div class="col-md-6">
                                                    <label class="control-label">시작시간</label>
                                                    <input class="form-control form-white" type="datetime-local"
                                                           name="start">
                                                    <label class="control-label">종료시간</label>
                                                    <input class="form-control form-white" type="datetime-local"
                                                           name="end">
                                                    <label class="control-label">반복종료시간</label>
                                                    <input class="form-control form-white" type="date"
                                                           name="until"><br/>


                                                    <fieldset>
                                                        <label class="control-label">요일별 반복 설정</label>
                                                        <div>
                                                            <input type="checkbox" id="MO" class="default-check"
                                                                   name="week" value="MO">
                                                            <label for="MO">MO</label>
                                                            <input type="checkbox" id="TU" class="default-check"
                                                                   name="week" value="TU">
                                                            <label for="TU">TU</label>
                                                            <input type="checkbox" id="WE" class="default-check"
                                                                   name="week" value="WE">
                                                            <label for="WE">WE</label>
                                                            <input type="checkbox" id="TH" class="default-check"
                                                                   name="week" value="TH">
                                                            <label for="TH">TH</label>
                                                            <input type="checkbox" id="FR" class="default-check"
                                                                   name="week" value="FR">
                                                            <label for="FR">FR</label>
                                                        </div>
                                                        <div>
                                                            <input type="checkbox" id="SA" class="default-check"
                                                                   name="week" value="SA">
                                                            <label for="SA">SA</label>
                                                            <input type="checkbox" id="SU" class="default-check"
                                                                   name="week" value="SU">
                                                            <label for="SU">SU</label>
                                                        </div>
                                                    </fieldset>
                                                </div>
                                                <div class="col-md-6">
                                                    <label class="control-label">파트 이름</label>
                                                    <input class="form-control form-white" placeholder="Enter name"
                                                           type="text" name="title" id="title_name">
                                                    <label class="control-label">인원 모집</label>
                                                    <input class="form-control form-white" placeholder="필요인원"
                                                           type="number" name="need_staff">
                                                    <input class="form-control form-white" placeholder="현재인원"
                                                           type="number" name="now_staff">
                                                </div>
                                            </div>
                                            <div class="modal-footer">
                                                <input type="reset" class="btn btn-default waves-effect"
                                                       data-dismiss="modal" value="Reset">
                                                <input type="button" id="sbtn"
                                                       class="btn btn-danger waves-effect waves-light save-category"
                                                       data-dismiss="modal" value="Add">
                                            </div>
                                        </form>
                                    </div>

                                </div>
                            </div>
                            <div class="col-md-8">
                                <div class="card-box m-b-50">
                                    <div id='loading'>loading...</div>
                                    <div id='calendar-container'>
                                        <div id='calendar'></div>
                                    </div>
                                </div>
                            </div>

                            <!-- end col -->
                            <!-- BEGIN MODAL -->

                            <!-- Modal Add Category -->
                            <div class="modal fade none-border" id="add-category">
                                <div class="modal-dialog">

                                </div>
                            </div>
                            <!-- END MODAL -->
                        </div>
                    </div>
                </div>
                <!-- /# card -->
            </div>
            <!-- /# column -->
        </div>
    </div>
    <!-- #/ container -->
</div>
<!--**********************************
    Content body end
***********************************-->


<!--**********************************
    Footer start
***********************************-->
<div class="footer">
    <div class="copyright">
        <p>Copyright &copy; Designed & Developed by <a href="https://themeforest.net/user/quixlab">Quixlab</a> 2018</p>
    </div>
</div>
<!--**********************************
    Footer end
***********************************-->
</div>
<!--**********************************
    Main wrapper end
***********************************-->

<!--**********************************
    Scripts
***********************************-->
<script src="${pageContext.request.contextPath}/plugins/common/common.min.js"></script>
<script src="${pageContext.request.contextPath}/js/custom.min.js"></script>
<script src="${pageContext.request.contextPath}/js/settings.js"></script>
<script src="${pageContext.request.contextPath}/js/gleek.js"></script>
<script src="${pageContext.request.contextPath}/js/styleSwitcher.js"></script>


<script src="${pageContext.request.contextPath}/plugins/jqueryui/js/jquery-ui.min.js"></script>
<script src="${pageContext.request.contextPath}/plugins/moment/moment.min.js"></script>

<%--     <script src="${pageContext.request.contextPath}/plugins/fullcalendar/js/fullcalendar.min.js"></script> --%>
<%--     <script src="${pageContext.request.contextPath}/js/plugins-init/fullcalendar-init.js"></script> --%>

</body>

</html>