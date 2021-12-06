
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

<body>
	<jsp:include page="/WEB-INF/view/templates/sidebar.jsp"></jsp:include>
	<!--**********************************
            Content body start
        ***********************************-->
	<div class="content-body">
<%
	String msg = CmmUtil.nvl((String)request.getAttribute("msg"));
	//int member_no = (int)request.getAttribute("member_no");
	int member_no = 3;
%>
	<script>
		alert("<%=msg%>");
		
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
						<p class="text-muted m-b-15 f-s-12">발급받은 인증번호를 입력한 뒤 확인 버튼을 누르세요</p>
						<div class="basic-form">
							<form
								action="${pageContext.request.contextPath}/member/certificate.do"
								method="post">
								<div class="form-group">
									<input type="hidden" name = "member_no" value="<%=member_no%>">
									<input type="text" name="secret"
										class="form-control input-default" placeholder="인증번호를 입력하세요">
								</div>
								<div class="general-button">
									<button type="submit" class="btn mb-1 btn-primary">인증번호 확인</button>
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