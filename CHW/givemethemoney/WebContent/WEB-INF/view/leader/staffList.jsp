<%@page import="poly.util.CmmUtil"%>
<%@page import="java.util.ArrayList"%>
<%@page import="poly.dto.MemberDTO"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width,initial-scale=1">
    <title>Quixlab - Bootstrap Admin Dashboard Template by Themefisher.com</title>
    <!-- Favicon icon -->
    <link rel="icon" type="image/png" sizes="16x16" href="${pageContext.request.contextPath}/images/favicon.png">
    <!-- Custom Stylesheet -->
    <link href="${pageContext.request.contextPath}/plugins/tables/css/datatable/dataTables.bootstrap4.min.css" rel="stylesheet">
    <link href="${pageContext.request.contextPath}/css/style.css" rel="stylesheet">

</head>

<body>
<jsp:include page="/WEB-INF/view/templates/sidebar.jsp"></jsp:include>
 <%
 	List<MemberDTO> mList = (List<MemberDTO>)request.getAttribute("mList");
 	if(mList ==null){
 		mList = new ArrayList<MemberDTO>();
 	}
 %>
 <script type="text/javascript">
 	function doAdd(member_no){
 		location.href="/leader/addStaff.do?member_no="+member_no;
 	}
 </script>
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
                    <div class="col-12">
                        <div class="card">
                            <div class="card-body">
                                <h4 class="card-title">Data Table</h4>
                                <div class="table-responsive">
                                    <table class="table table-striped table-bordered zero-configuration">
                                        <thead>
                                            <tr>
                                                <th>직원번호</th>
                                                <th>이름</th>
                                                <th>아이디</th>
                                                <th>이메일</th>
                                                <th>전화번호</th>
                                                <th>팀이름</th>
                                                <th>팀추가</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        	<%
                                        		for(int i = 0; i<mList.size();i++){
                                        			MemberDTO mDTO = mList.get(i);
                                        			if(mDTO==null){
                                        				mDTO = new MemberDTO();
                                        			}
                                        	%>
                                            <tr>
                                                <td><%=mDTO.getMember_no() %></td>
                                                <td><%=mDTO.getMember_name() %></td>
                                                <td><%=mDTO.getMember_id()%></td>
                                                <td><%=mDTO.getMember_email() %></td>
                                                <td><%=mDTO.getMember_phone() %></td>
                                                <td><%=mDTO.getMember_team() %></td>
                                                <td><a href="javascript:doAdd('<%=Integer.toString(mDTO.getMember_no()) %>');">팀추가</a></td>
                                            </tr>
                                        	<%
                                        		}
                                        	%>
                                           
                                        </tbody>
                                        <tfoot>
                                            <tr>
                                               <th>직원번호</th>
                                                <th>이름</th>
                                                <th>아이디</th>
                                                <th>이메일</th>
                                                <th>전화번호</th>
                                                <th>팀이름</th>
                                                <th>팀추가</th>
                                            </tr>
                                        </tfoot>
                                    </table>
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

    <script src="${pageContext.request.contextPath}/plugins/tables/js/jquery.dataTables.min.js"></script>
    <script src="${pageContext.request.contextPath}/plugins/tables/js/datatable/dataTables.bootstrap4.min.js"></script>
    <script src="${pageContext.request.contextPath}/plugins/tables/js/datatable-init/datatable-basic.min.js"></script>

</body>

</html>