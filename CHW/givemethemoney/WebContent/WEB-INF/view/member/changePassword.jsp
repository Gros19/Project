
<!DOCTYPE html>
<%@page import="poly.util.CmmUtil"%>
<html lang="en">
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width,initial-scale=1">
<title>Quixlab - Bootstrap Admin Dashboard Template by
	Themefisher.com</title>
<!-- Favicon icon -->
<link rel="icon" type="image/png" sizes="16x16"
	href="${pageContext.request.contextPath}/images/favicon.png">
<!-- Custom Stylesheet -->
<link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">

</head>
<style>
.intext {
	width: 100%;
	padding-left: 0.5rem;
	padding: 0.5rem;
}

.incorrect-message {
	display: none;
}

.correct-message {
	display: none;
}
.intext.correct ~.correct-message {
	color: blue;
	font-size: 16;
	display: block;
	font-size: 13px;
	margin: 0.3rem;
}

.intext.incorrect ~.incorrect-message {
	color: red;
	font-size: 16;
	display: block;
	font-size: 13px;
	margin: 0.3rem;
}
</style>
<body>
	<jsp:include page="/WEB-INF/view/templates/sidebar.jsp"></jsp:include>
	<!--**********************************
            Content body start
        ***********************************-->
	<div class="content-body">
<%
	String msg = CmmUtil.nvl((String)request.getAttribute("msg"));
	String member_no = (String)request.getAttribute("member_no");
%>
	<script>
		alert("<%=msg%>");
		
	</script>
	<script type="text/javascript">
	function checkPwd() {

		var regex1 = /^(?=.*[a-zA-z])(?=.*[0-9])(?=.*[$`~!@$!%*#^?&\\(\\)\-_=+]).{8,16}$/;
		var pwTag = document.getElementById("pw");
		var isPw1 = regex1.test(pwTag.value);
		pwTag.classList.remove("correct");
		pwTag.classList.remove("incorrect");
		if (!isPw1) {
			pwTag.classList.add("incorrect");
			return false;
		} else {
			pwTag.classList.add("correct");
			return true;
		}
	}
	function checkCheckPw() {
		var pwTag = document.getElementById("pw");
		var checkPwTag = document.getElementById("checkPw");
		checkPwTag.classList.remove("correct");
		checkPwTag.classList.remove("incorrect");
		if (pwTag.value === checkPwTag.value) {
			checkPwTag.classList.add("correct");
		} else {
			checkPwTag.classList.add("incorrect");
		}
		return pwTag.value === checkPwTag.value;
	}
	function checkForm1() {
		console.log("비밀번호" + checkPwd());
		if (!checkPwd() || !checkCheckPw()) {
			return false;
		} else {
			return true;
		}
	}
	</script>
		<div class="row page-titles mx-0">
			<div class="col p-md-0">
				<ol class="breadcrumb">
					<li class="breadcrumb-item"><a href="javascript:void(0)">Dashboard</a></li>
					<li class="breadcrumb-item active"><a
						href="javascript:void(0)">Home</a></li>
				</ol>
			</div>
		</div>

		<!-- #/ container -->

		<div class="container-fluid">
			<div class="col-lg-12">
				<div class="card">
					<div class="card-body">
						<h4 class="card-title">비밀번호 찾기</h4>
						<p class="text-muted m-b-15 f-s-12">새 비밀번호를 입력하고 완료 버튼을 누르세요</p>
						<div class="basic-form">
							<form
								action="${pageContext.request.contextPath}/member/changePassword.do"
								method="post">
								<div class="form-group">
									<input type="hidden" name="member_no" value="<%=member_no%>">
								</div>
									<div class="form-group row">
										<div class="col-sm-6 mb-3 mb-sm-0">
											<input type="password"
												class="form-control form-control-user intext"
												name="member_pw" id="pw" oninput="checkPwd();"
												placeholder="비밀번호를 입력해주세요"> <span
												class="correct-message">올바른 비밀번호 형식입니다</span> <span
												class="incorrect-message">8 ~ 16자 영문, 숫자, 특수문자를 최소
												한가지씩 조합</span>
										</div>
										<div class="col-sm-6">
											<input type="password"
												class="form-control form-control-user intext"
												name="member_pw2" id="checkPw" oninput="checkCheckPw();"
												placeholder="비밀번호 확인"> <span class="correct-message">비밀번호가
												일치합니다.</span> <span class="incorrect-message">비밀번호가 불일치합니다.</span>
										</div>
									</div>									
								<div class="general-button">
									<button type="submit" class="btn mb-1 btn-primary">완료</button>
								</div>
							</form>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>


	<!--**********************************
            Footer start
        ***********************************-->
	<div class="footer">
		<div class="copyright">
			<p>
				Copyright &copy; Designed & Developed by <a
					href="https://themeforest.net/user/quixlab">Quixlab</a> 2018
			</p>
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

</body>

</html>