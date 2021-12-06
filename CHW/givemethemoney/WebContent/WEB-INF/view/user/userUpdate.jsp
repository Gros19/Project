<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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

.unoverlap-message {
	display: none;
}

.overlap-message {
	display: none;
}

.email-input.unoverlap ~.unoverlap-message {
	color: blue;
	font-size: 16;
	display: block;
	font-size: 13px;
	margin: 0.3rem;
}

.email-input.overlap ~.overlap-message {
	color: red;
	font-size: 16;
	display: block;
	font-size: 13px;
	margin: 0.3rem;
}

.id-input.unoverlap ~.unoverlap-message {
	color: blue;
	font-size: 16;
	display: block;
	font-size: 13px;
	margin: 0.3rem;
}

.id-input.overlap ~.overlap-message {
	color: red;
	font-size: 16;
	display: block;
	font-size: 13px;
	margin: 0.3rem;
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

.inemail.correct ~.correct-message {
	color: blue;
	font-size: 16;
	display: block;
	font-size: 13px;
	margin: 0.3rem;
}

.inemail.incorrect ~.incorrect-message {
	color: red;
	font-size: 16;
	display: block;
	font-size: 13px;
	margin: 0.3rem;
}

.inid.correct ~.correct-message {
	color: blue;
	font-size: 16;
	display: block;
	font-size: 13px;
	margin: 0.3rem;
}

.inid.incorrect ~.incorrect-message {
	color: red;
	font-size: 16;
	display: block;
	font-size: 13px;
	margin: 0.3rem;
}
</style>
<script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
    //본 예제에서는 도로명 주소 표기 방식에 대한 법령에 따라, 내려오는 데이터를 조합하여 올바른 주소를 구성하는 방법을 설명합니다.
    function sample4_execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

                // 도로명 주소의 노출 규칙에 따라 주소를 표시한다.
                // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
                var roadAddr = data.roadAddress; // 도로명 주소 변수
                var extraRoadAddr = ''; // 참고 항목 변수

                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
                    extraRoadAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                   extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if(extraRoadAddr !== ''){
                    extraRoadAddr = ' (' + extraRoadAddr + ')';
                }

                // 우편번호와 주소 정보를 해당 필드에 넣는다.
                document.getElementById('sample4_postcode').value = data.zonecode;
                document.getElementById("sample4_roadAddress").value = roadAddr;
                document.getElementById("sample4_jibunAddress").value = data.jibunAddress;
                
                // 참고항목 문자열이 있을 경우 해당 필드에 넣는다.
                if(roadAddr !== ''){
                    document.getElementById("sample4_extraAddress").value = extraRoadAddr;
                } else {
                    document.getElementById("sample4_extraAddress").value = '';
                }

                var guideTextBox = document.getElementById("guide");
                // 사용자가 '선택 안함'을 클릭한 경우, 예상 주소라는 표시를 해준다.
                if(data.autoRoadAddress) {
                    var expRoadAddr = data.autoRoadAddress + extraRoadAddr;
                    guideTextBox.innerHTML = '(예상 도로명 주소 : ' + expRoadAddr + ')';
                    guideTextBox.style.display = 'block';

                } else if(data.autoJibunAddress) {
                    var expJibunAddr = data.autoJibunAddress;
                    guideTextBox.innerHTML = '(예상 지번 주소 : ' + expJibunAddr + ')';
                    guideTextBox.style.display = 'block';
                } else {
                    guideTextBox.innerHTML = '';
                    guideTextBox.style.display = 'none';
                }
            }
        }).open();
    }
</script>
<script type="text/javascript">
	function checkName() {
		var regex = /[가-힣]{2,7}/g;
		var nameTag = document.getElementById("name");

		var isName = regex.test(nameTag.value);
		nameTag.classList.remove("correct");
		nameTag.classList.remove("incorrect");

		if (!isName) {
			nameTag.classList.add("incorrect");
			return false;
		} else {
			nameTag.classList.add("correct");
			return true;
		}
	}

	//isValid의 결과에 따라서 입력창에 correct / incorrect 클래스를 추가
	//비밀번호를 검사하는 함수 : 반드시 true 또는 false를 반환해야 checkForm에서 사용이 가능하다
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
	function checkAddr1() {
		var regex = /[가-힣]{1,200}/g;
		var nameTag = document.getElementById("sample4_roadAddress");

		var isName = regex.test(nameTag.value);
		nameTag.classList.remove("correct");
		nameTag.classList.remove("incorrect");

		if (!isName) {
			nameTag.classList.add("incorrect");
			return false;
		} else {
			nameTag.classList.add("correct");
			return true;
		}
	}
	function checkAddr2() {
		var regex = /[가-힣]{1,200}/g;
		var nameTag = document.getElementById("sample4_detailAddress");

		var isName = regex.test(nameTag.value);
		nameTag.classList.remove("correct");
		nameTag.classList.remove("incorrect");

		if (!isName) {
			nameTag.classList.add("incorrect");
			return false;
		} else {
			nameTag.classList.add("correct");
			return true;
		}
	}
	function checkPhone() {
		var regExp = /^01(?:0|1|[6-9])-(?:\d{3}|\d{4})-\d{4}$/;
		var phoneTag = document.getElementById("phone");
		var isPhone = regExp.test(phoneTag.value);
		phoneTag.classList.remove("correct");
		phoneTag.classList.remove("incorrect");
		console.log("isPhone :" + isPhone);
		if (!isPhone) {
			phoneTag.classList.add("incorrect");
			return false;
		} else {
			phoneTag.classList.add("correct");
			return true;
		}
	}

	function checkForm1() {
		console.log("이름" + checkName());
		console.log("비밀번호" + checkPwd());
		console.log("폰번호" + checkPhone());
		console.log("주소1" + checkAddr1());
		console.log("주소2" + checkAddr2());
		if ( !checkName() || !checkPwd() || !checkCheckPw() || !checkAddr1()
				|| !checkAddr2() || !checkPhone()) {
			return false;
		} else {

			return true;
		}
	}
</script>
<script type="text/javascript">
	function doDelete(member_no){
		location.href="/user/userDelete.do?member_no="+member_no;
	}
</script>
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
                <div class="row justify-content-center">
                    <div class="col-lg-12">
                        <div class="card">
                            <div class="card-body">
                                <div class="form-validation">
                                    <form class="form-valide" action="${pageContext.request.contextPath}/user/userUpdate.do"method="post" onsubmit="return checkForm1();">                          	
                                        <div class="form-group row">
                                          
                                            <div class="col-lg-6">
                                                <input type="hidden" class="form-control" id="" name="member_no" value="${memberinfo.member_no}">
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-lg-4 col-form-label" for="val-username">이름 <span class="text-danger">*</span>
                                            </label>
                                            <div class="col-lg-6">
                                            <input type="text" class="form-control intext" id="name" name="member_name" oninput="checkName();" value="${memberinfo.member_name}" >
                                            <span class="correct-message">올바른 이름 형식입니다</span>
                                            <span class="incorrect-message">이름은 한글 2~7자로 구성하세요</span>
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-lg-4 col-form-label" for="val-email">이메일 <span class="text-danger">*</span>
                                            </label>
                                            <div class="col-lg-6">
                                                <input type="text" class="form-control" id="val-email" name="member_email" value="${memberinfo.member_email}"readonly="readonly">
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-lg-4 col-form-label" for="val-password">아이디 <span class="text-danger">*</span>
                                            </label>
                                            <div class="col-lg-6">
                                                <input type="text" class="form-control" id="val-password" name="member_id" value="${memberinfo.member_id}"readonly="readonly">
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-lg-4 col-form-label" for="val-confirm-password">전화번호 <span class="text-danger">*</span>
                                            </label>
                                            <div class="col-lg-6">
                                                <input type="text" class="form-control intext" id="phone" oninput ="checkPhone();" name="member_phone"value="${memberinfo.member_phone}">
                                            <span class="correct-message">올바른 전화번호 형식입니다</span> 
                                            <span class="incorrect-message">01x-xxxx-xxxx형식으로 입력해주세요</span>
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-lg-4 col-form-label" for="val-confirm-password">비밀번호 <span class="text-danger">*</span>
                                            </label>
                                            <div class="col-lg-6">
                                                <input type="password" class="form-control intext" id="pw" name="member_pw"
                                                oninput = "checkPwd();">
                                            <span class="correct-message">올바른 비밀번호 형식입니다</span>
                                             <span class="incorrect-message">8 ~ 16자 영문, 숫자, 특수문자를 최소 한가지씩 조합</span>
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-lg-4 col-form-label" for="val-confirm-password">비밀번호 확인 <span class="text-danger">*</span>
                                            </label>
                                            <div class="col-lg-6">
                                                <input type="password" class="form-control intext" id="checkPw" 
                                                name="member_pw2" oninput = "checkCheckPw();">
                                            <span class="correct-message">비밀번호가 일치합니다.</span> 
                                            <span class="incorrect-message">비밀번호가 불일치합니다.</span>                                           
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-lg-4 col-form-label" for="val-currency">소속 팀 <span class="text-danger">*</span>
                                            </label>
                                            <div class="col-lg-6">
                                                <input type="text" class="form-control" id="val-currency" name="member_team" value="${memberinfo.member_team}"readonly="readonly">
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-lg-4 col-form-label" for="val-website">기본주소<span class="text-danger">*</span>
                                            </label>
                                            <div class="col-lg-6">
                                                <input type="text" class="form-control intext" id="sample4_roadAddress" name="addr1" value="${memberinfo.member_addr1}"
                                                oninput= "checkAddr1();">
                                                <span class="correct-message">올바른 주소 형식입니다</span> <span
											class="incorrect-message">1글자 이상의 한글로 입력해주세요</span>
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                        	<label class="col-lg-4 col-form-label" for="val-website"><span class="text-danger"></span>
                                            </label>
                                        	<div class="col-lg-6">
                                        	<button type="button" onclick="sample4_execDaumPostcode()" class="btn mb-1 btn-rounded btn-info">
											<span class="btn-icon-left">
											<i class="fa fa-plus color-info"></i> 
											</span>주소 검색</button>		
                                        	</div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-lg-4 col-form-label" for="val-phoneus">상세주소 <span class="text-danger">*</span>
                                            </label>
                                            <div class="col-lg-6">
                                                <input type="text" class="form-control intext" id="sample4_detailAddress" name="addr2"
                                                 value="${memberinfo.member_addr2}" oninput="checkAddr2();">
                                                 <span class="correct-message">올바른 주소 형식입니다</span> <span
											class="incorrect-message">1글자 이상의 한글로 입력해주세요</span>
                                            </div>
                                            <input type="hidden" id="sample4_postcode" placeholder="우편번호">
											<input type="hidden" id="sample4_jibunAddress" placeholder="지번주소">
											<span id="guide" style="color:#999;display:none"></span>
											<input type="hidden" id="sample4_extraAddress" placeholder="참고항목">
                                        </div>
                                        <div class="form-group row">
                                            <div class="col-lg-8 ml-auto">
                                                <button type="submit" class="btn btn-primary">수정하기</button>
                                            </div>
                                            <div class="col-lg-8 ml-auto">
                                                <button type="button" onclick="location.href='javascript:doDelete(${memberinfo.member_no});'" class="btn btn-primary">탈퇴하기</button>
                                            </div>
                                        </div>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </div>
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

    <script src="${pageContext.request.contextPath}/plugins/validation/jquery.validate.min.js"></script>
    <script src="${pageContext.request.contextPath}/plugins/validation/jquery.validate-init.js"></script>

</body>

</html>