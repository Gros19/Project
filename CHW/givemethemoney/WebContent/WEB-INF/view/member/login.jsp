<!DOCTYPE html>
<%@page import="poly.util.CmmUtil"%>
<html class="h-100" lang="en">
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<script src="${pageContext.request.contextPath}/js/jquery-3.6.0.min.js" type="text/javascript"></script>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>Quixlab - Bootstrap Admin Dashboard Template by Themefisher.com</title>
    <!-- Favicon icon -->
    <link rel="icon" type="image/png" sizes="16x16" href="${pageContext.request.contextPath}/images/favicon.png">
    <!-- <link rel="stylesheet" href="https://use.fontawesome.com/releases/v5.5.0/css/all.css" integrity="sha384-B4dIYHKNBt8Bc12p+WXckhzcICo0wtJAoU8YZTY5qE0Id1GSseTk6S+L3BlXeVIU" crossorigin="anonymous"> -->
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">
    
</head>
    <script>
        $(document).ready(function() {
            var userInputId = getCookie("userInputId");
            var setCookieYN = getCookie("setCookieYN");
            
            if(setCookieYN == 'Y') {
                $("#idSaveCheck").prop("checked", true);
            } else {
                $("#idSaveCheck").prop("checked", false);
            }
            
            $("#inputId").val(userInputId); 
            
            //로그인 버튼 클릭
            $('#loginbtn').click(function() {
                if($("#idSaveCheck").is(":checked")){ 
                    var userInputId = $("#inputId").val();
                    setCookie("userInputId", userInputId, 60); 
                    setCookie("setCookieYN", "Y", 60);
                } else {
                    deleteCookie("userInputId");
                    deleteCookie("setCookieYN");
                }
                
                document.fform.submit();
            });
        });

        //쿠키값 Set
        function setCookie(cookieName, value, exdays){
            var exdate = new Date();
            exdate.setDate(exdate.getDate() + exdays);
            var cookieValue = escape(value) + ((exdays==null) ? "" : "; expires=" + 
            exdate.toGMTString());
            document.cookie = cookieName + "=" + cookieValue;
        }

        //쿠키값 Delete
        function deleteCookie(cookieName){
            var expireDate = new Date();
            expireDate.setDate(expireDate.getDate() - 1);
            document.cookie = cookieName + "= " + "; expires=" + expireDate.toGMTString();
        }

        //쿠키값 가져오기
        function getCookie(cookie_name) {
            var x, y;
            var val = document.cookie.split(';');
            
            for (var i = 0; i < val.length; i++) {
                x = val[i].substr(0, val[i].indexOf('='));
                y = val[i].substr(val[i].indexOf('=') + 1);
                x = x.replace(/^\s+|\s+$/g, ''); // 앞과 뒤의 공백 제거하기
                
                if (x == cookie_name) {
                  return unescape(y); // unescape로 디코딩 후 값 리턴
                }
            }
        }
        </script>
<body class="h-100">
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
    <div class="login-form-bg h-100">
        <div class="container h-100">
            <div class="row justify-content-center h-100">
                <div class="col-xl-6">
                    <div class="form-input-content">
                        <div class="card login-form mb-0">
                            <div class="card-body pt-5">
                                <a class="text-center" href="${pageContext.request.contextPath}/index.do"> <h4>일정 관리 시스템</h4></a>
        
                                <form class="mt-5 mb-5 login-input" method="post" action="${pageContext.request.contextPath}/member/login.do">
                                    <div class="form-group">
                                        <input type="text" class="form-control" placeholder="inputId" id="inputId"
                                        name ="member_id">
                                    </div>
                                    <div class="form-group">
                                        <input type="password" class="form-control" id="inputPassword" placeholder="Password" name= "member_pw">
                                    </div>
									<div class="form-group">
										<div class="checkbox">
											<label> <input type="checkbox" id="idSaveCheck" value="remember-me"> 아이디 기억하기
											</label>
										</div>
									</div>
									<c:if test="${not empty param.error}">
										<div class="form-group">
											<font color="FF0000">입력하신 로그인 정보가 맞지 않습니다.</font>
										</div>
									</c:if>
                                    <button class="btn login-form__btn submit w-100" id="loginbtn">Sign In</button>
                                </form>
                                <p class="mt-5 login-form__footer">계정이 없으신가요? <a href="${pageContext.request.contextPath}/user/userRegForm.do" class="text-primary">회원가입</a></p>
                                <p class="mt-5 login-form__footer">비밀번호를 잊어버리셨나요? <a href="${pageContext.request.contextPath}/member/findPassword.do" class="text-primary">비밀번호 찾기</a></p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!--**********************************
        Scripts
    ***********************************-->
    <script src="${pageContext.request.contextPath}/plugins/common/common.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/custom.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/settings.js"></script>
    <script src="${pageContext.request.contextPath}/js/gleek.js"></script>
    <script src="${pageContext.request.contextPath}/js/styleSwitcher.js"></script>
</body>
</html>