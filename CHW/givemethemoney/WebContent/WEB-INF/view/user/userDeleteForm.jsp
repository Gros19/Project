<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>userDelete</title>
</head>
<body class="h-100">
	<div class="login-form-bg h-100">
		<div class="container h-100">
			<div class="row justify-content-center h-100">
				<div class="col-xl-6">
					<div class="form-input-content">
						<div class="card login-form mb-0">
							<div class="card-body pt-5">
								<a class="text-center">
									<h4>회원탈퇴</h4>
								</a>
								<form class="mt-5 mb-5 login-input"
									action="${pageContext.request.contextPath}/user/DeleteUserInfo.do"
									method="post" name="f" onsubmit="return checkForm1();">
									<div class="form-group row">
										<div class="col-sm-6 mb-3 mb-sm-0">
											<input type="text"
												class="form-control form-control-user id-input inid" id="id"
												name="member_id" placeholder="아이디를 입력해주세요"
												oninput="checkId();">
										</div>
									</div>
									<div class="form-group row">
										<div class="col-sm-6 mb-3 mb-sm-0">
											<input type="password"
												class="form-control form-control-user intext"
												name="member_pw" id="pw" oninput="checkPwd();"
												placeholder="비밀번호를 입력해주세요">
										</div>
										<button class="btn login-form__btn submit w-100" type="submit">탈퇴</button>
									</div>
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	</html>