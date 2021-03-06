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
    //??? ??????????????? ????????? ?????? ?????? ????????? ?????? ????????? ??????, ???????????? ???????????? ???????????? ????????? ????????? ???????????? ????????? ???????????????.
    function sample4_execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // ???????????? ???????????? ????????? ??????????????? ????????? ????????? ???????????? ??????.

                // ????????? ????????? ?????? ????????? ?????? ????????? ????????????.
                // ???????????? ????????? ?????? ?????? ????????? ??????('')?????? ????????????, ?????? ???????????? ?????? ??????.
                var roadAddr = data.roadAddress; // ????????? ?????? ??????
                var extraRoadAddr = ''; // ?????? ?????? ??????

                // ??????????????? ?????? ?????? ????????????. (???????????? ??????)
                // ???????????? ?????? ????????? ????????? "???/???/???"??? ?????????.
                if(data.bname !== '' && /[???|???|???]$/g.test(data.bname)){
                    extraRoadAddr += data.bname;
                }
                // ???????????? ??????, ??????????????? ?????? ????????????.
                if(data.buildingName !== '' && data.apartment === 'Y'){
                   extraRoadAddr += (extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // ????????? ??????????????? ?????? ??????, ???????????? ????????? ?????? ???????????? ?????????.
                if(extraRoadAddr !== ''){
                    extraRoadAddr = ' (' + extraRoadAddr + ')';
                }

                // ??????????????? ?????? ????????? ?????? ????????? ?????????.
                document.getElementById('sample4_postcode').value = data.zonecode;
                document.getElementById("sample4_roadAddress").value = roadAddr;
                document.getElementById("sample4_jibunAddress").value = data.jibunAddress;
                
                // ???????????? ???????????? ?????? ?????? ?????? ????????? ?????????.
                if(roadAddr !== ''){
                    document.getElementById("sample4_extraAddress").value = extraRoadAddr;
                } else {
                    document.getElementById("sample4_extraAddress").value = '';
                }

                var guideTextBox = document.getElementById("guide");
                // ???????????? '?????? ??????'??? ????????? ??????, ?????? ???????????? ????????? ?????????.
                if(data.autoRoadAddress) {
                    var expRoadAddr = data.autoRoadAddress + extraRoadAddr;
                    guideTextBox.innerHTML = '(?????? ????????? ?????? : ' + expRoadAddr + ')';
                    guideTextBox.style.display = 'block';

                } else if(data.autoJibunAddress) {
                    var expJibunAddr = data.autoJibunAddress;
                    guideTextBox.innerHTML = '(?????? ?????? ?????? : ' + expJibunAddr + ')';
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
		var regex = /[???-???]{2,7}/g;
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

	//isValid??? ????????? ????????? ???????????? correct / incorrect ???????????? ??????
	//??????????????? ???????????? ?????? : ????????? true ?????? false??? ???????????? checkForm?????? ????????? ????????????
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
		var regex = /[???-???]{1,200}/g;
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
		var regex = /[???-???]{1,200}/g;
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
		console.log("??????" + checkName());
		console.log("????????????" + checkPwd());
		console.log("?????????" + checkPhone());
		console.log("??????1" + checkAddr1());
		console.log("??????2" + checkAddr2());
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
                                            <label class="col-lg-4 col-form-label" for="val-username">?????? <span class="text-danger">*</span>
                                            </label>
                                            <div class="col-lg-6">
                                            <input type="text" class="form-control intext" id="name" name="member_name" oninput="checkName();" value="${memberinfo.member_name}" >
                                            <span class="correct-message">????????? ?????? ???????????????</span>
                                            <span class="incorrect-message">????????? ?????? 2~7?????? ???????????????</span>
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-lg-4 col-form-label" for="val-email">????????? <span class="text-danger">*</span>
                                            </label>
                                            <div class="col-lg-6">
                                                <input type="text" class="form-control" id="val-email" name="member_email" value="${memberinfo.member_email}"readonly="readonly">
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-lg-4 col-form-label" for="val-password">????????? <span class="text-danger">*</span>
                                            </label>
                                            <div class="col-lg-6">
                                                <input type="text" class="form-control" id="val-password" name="member_id" value="${memberinfo.member_id}"readonly="readonly">
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-lg-4 col-form-label" for="val-confirm-password">???????????? <span class="text-danger">*</span>
                                            </label>
                                            <div class="col-lg-6">
                                                <input type="text" class="form-control intext" id="phone" oninput ="checkPhone();" name="member_phone"value="${memberinfo.member_phone}">
                                            <span class="correct-message">????????? ???????????? ???????????????</span> 
                                            <span class="incorrect-message">01x-xxxx-xxxx???????????? ??????????????????</span>
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-lg-4 col-form-label" for="val-confirm-password">???????????? <span class="text-danger">*</span>
                                            </label>
                                            <div class="col-lg-6">
                                                <input type="password" class="form-control intext" id="pw" name="member_pw"
                                                oninput = "checkPwd();">
                                            <span class="correct-message">????????? ???????????? ???????????????</span>
                                             <span class="incorrect-message">8 ~ 16??? ??????, ??????, ??????????????? ?????? ???????????? ??????</span>
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-lg-4 col-form-label" for="val-confirm-password">???????????? ?????? <span class="text-danger">*</span>
                                            </label>
                                            <div class="col-lg-6">
                                                <input type="password" class="form-control intext" id="checkPw" 
                                                name="member_pw2" oninput = "checkCheckPw();">
                                            <span class="correct-message">??????????????? ???????????????.</span> 
                                            <span class="incorrect-message">??????????????? ??????????????????.</span>                                           
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-lg-4 col-form-label" for="val-currency">?????? ??? <span class="text-danger">*</span>
                                            </label>
                                            <div class="col-lg-6">
                                                <input type="text" class="form-control" id="val-currency" name="member_team" value="${memberinfo.member_team}"readonly="readonly">
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-lg-4 col-form-label" for="val-website">????????????<span class="text-danger">*</span>
                                            </label>
                                            <div class="col-lg-6">
                                                <input type="text" class="form-control intext" id="sample4_roadAddress" name="addr1" value="${memberinfo.member_addr1}"
                                                oninput= "checkAddr1();">
                                                <span class="correct-message">????????? ?????? ???????????????</span> <span
											class="incorrect-message">1?????? ????????? ????????? ??????????????????</span>
                                            </div>
                                        </div>
                                        <div class="form-group row">
                                        	<label class="col-lg-4 col-form-label" for="val-website"><span class="text-danger"></span>
                                            </label>
                                        	<div class="col-lg-6">
                                        	<button type="button" onclick="sample4_execDaumPostcode()" class="btn mb-1 btn-rounded btn-info">
											<span class="btn-icon-left">
											<i class="fa fa-plus color-info"></i> 
											</span>?????? ??????</button>		
                                        	</div>
                                        </div>
                                        <div class="form-group row">
                                            <label class="col-lg-4 col-form-label" for="val-phoneus">???????????? <span class="text-danger">*</span>
                                            </label>
                                            <div class="col-lg-6">
                                                <input type="text" class="form-control intext" id="sample4_detailAddress" name="addr2"
                                                 value="${memberinfo.member_addr2}" oninput="checkAddr2();">
                                                 <span class="correct-message">????????? ?????? ???????????????</span> <span
											class="incorrect-message">1?????? ????????? ????????? ??????????????????</span>
                                            </div>
                                            <input type="hidden" id="sample4_postcode" placeholder="????????????">
											<input type="hidden" id="sample4_jibunAddress" placeholder="????????????">
											<span id="guide" style="color:#999;display:none"></span>
											<input type="hidden" id="sample4_extraAddress" placeholder="????????????">
                                        </div>
                                        <div class="form-group row">
                                            <div class="col-lg-8 ml-auto">
                                                <button type="submit" class="btn btn-primary">????????????</button>
                                            </div>
                                            <div class="col-lg-8 ml-auto">
                                                <button type="button" onclick="location.href='javascript:doDelete(${memberinfo.member_no});'" class="btn btn-primary">????????????</button>
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