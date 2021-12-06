 <%@page import="poly.dto.MemberDTO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
	MemberDTO mDTO = (MemberDTO)session.getAttribute("memberinfo");
%>
    <!--*******************
        Preloader start
    ********************-->
    <div id="preloader">
        <div class="loader">
            <svg class="circular" viewBox="25 25 50 50">
                <circle class="path" cx="50" cy="50" r="20" fill="none" stroke-width="3" stroke-miterlimit="10" />
            </svg>
        </div>
    </div>
    <!--*******************
        Preloader end
    ********************-->

    
    <!--**********************************
        Main wrapper start
    ***********************************-->
    <div id="main-wrapper">

        <!--**********************************
            Nav header start
        ***********************************-->
        <div class="nav-header">
            <div class="brand-logo">
                <a href="${pageContext.request.contextPath}/home.do">
                    <b class="logo-abbr"><img src="${pageContext.request.contextPath}/images/logo.png" alt=""></b>
                    <span class="logo-compact"><img src="${pageContext.request.contextPath}/images/logo-compact.png" alt=""></span>
                    <span class="brand-title">
                        <img src="${pageContext.request.contextPath}/images/logo-text.png" alt="">
                    </span>
                </a>
            </div>
        </div>
        <!--**********************************
            Nav header end
        ***********************************-->

        <!--**********************************
            Header start
        ***********************************-->
        <div class="header">    
            <div class="header-content clearfix">
                
                <div class="nav-control">
                    <div class="hamburger">
                        <span class="toggle-icon"><i class="icon-menu"></i></span>
                    </div>
                </div>
                <div class="header-left">
                    <div class="input-group icons">
                        <div class="input-group-prepend">
                        </div>
                        <div class="drop-down   d-md-none">

                        </div>
                    </div>
                </div>
                <div class="header-right">
                    <ul class="clearfix">
                        <li class="icons dropdown">
                            <div class="drop-down animated fadeIn dropdown-menu">
                                <div class="dropdown-content-heading d-flex justify-content-between">
                                </div>
                                <div class="dropdown-content-body">
                                    <ul>
                                    </ul>
                                    
                                </div>
                            </div>
                        </li>
                        <li class="icons dropdown">
                            <div class="drop-down animated fadeIn dropdown-menu dropdown-notfication">
                                <div class="dropdown-content-body">
                                    <ul>
                                    </ul>
                                </div>
                            </div>
                        </li>
                        <li class="icons dropdown d-none d-md-flex">
                            <div class="drop-down dropdown-language animated fadeIn  dropdown-menu">
                            </div>
                        </li>
                        <li class="icons dropdown">
                            <div class="user-img c-pointer position-relative"   data-toggle="dropdown">
                                <span class="activity active"></span>
                                <img src="${pageContext.request.contextPath}/images/user/1.png" height="40" width="40" alt="">
                            </div>
                            <div class="drop-down dropdown-profile   dropdown-menu">
                                <div class="dropdown-content-body">
                                    <ul>
                                        <li>
                                            <a href="${pageContext.request.contextPath}/user/userInfo.do"><i class="icon-user"></i> <span>Profile</span></a>
                                        </li>

                                        
                                        <hr class="my-2">

                                        <li><a href="${pageContext.request.contextPath}/member/logout.do"><i class="icon-key"></i> <span>Logout</span></a></li>
                                    </ul>
                                </div>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
        <!--**********************************
            Header end ti-comment-alt
        ***********************************-->

        <!--**********************************
            Sidebar start
        ***********************************-->
        <div class="nk-sidebar">           
            <div class="nk-nav-scroll">
                <ul class="metismenu" id="menu">
                    <li class="nav-label">Dashboard</li>
                    <li>
                        <a class="has-arrow" href="javascript:void()" aria-expanded="false">
                            <i class="icon-speedometer menu-icon"></i><span class="nav-text">캘린더관리</span>
                        </a>
                        <ul aria-expanded="false">
                            <li><a href="${pageContext.request.contextPath}/getCalendarEvents.do">캘린더</a></li>                            
                        </ul>
                    </li>
                    <c:choose>
                    	<c:when test="${memberinfo.member_auth ne 'staff'}">
                    <li class="mega-menu mega-menu-sm">
                        <a class="has-arrow" href="javascript:void()" aria-expanded="false">
                            <i class="icon-globe-alt menu-icon"></i><span class="nav-text">인사 관리</span>
                        </a>
                        <ul aria-expanded="false">
                        	<c:choose>
                        		<c:when test="${memberinfo.member_auth eq 'leader'}">
                            <li><a href="${pageContext.request.contextPath}/leader/staffList.do">직원 리스트</a></li>
                            <li><a href="${pageContext.request.contextPath}/leader/myStaffList.do">팀원 리스트</a></li>
                            <li><a href="${pageContext.request.contextPath}/leader/blockList.do">블락 리스트</a></li>
                        		</c:when>
                        		<c:when test="${memberinfo.member_auth eq 'master'}">
                            <li><a href="${pageContext.request.contextPath}/leader/blockList.do">블락 리스트</a></li>
                            <li><a href="${pageContext.request.contextPath}/master/leaderList.do">리더 리스트</a></li>
                        		</c:when>
                        	</c:choose>
                        </ul>
                   </li>
                    	</c:when>
                    </c:choose>

                    <li class="nav-label">Pages</li>
                    <li>
                        <a class="has-arrow" href="javascript:void()" aria-expanded="false">
                            <i class="icon-notebook menu-icon"></i><span class="nav-text">회원정보</span>
                        </a>
                        <ul aria-expanded="false">
                            <li><a href="${pageContext.request.contextPath}/user/userInfo.do">회원정보</a></li>
                            <li><a href="${pageContext.request.contextPath}/member/logout.do">로그아웃</a></li>
                            
                        </ul>
                    </li>
                </ul>
            </div>
        </div>
        <!--**********************************
            Sidebar end
        ***********************************-->