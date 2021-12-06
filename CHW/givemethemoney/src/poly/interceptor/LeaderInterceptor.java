package poly.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import poly.dto.MemberDTO;


public class LeaderInterceptor extends HandlerInterceptorAdapter{
	
	@Autowired
	HttpSession session;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		//관리자가 로그인 되어있다면
		MemberDTO mDTO = (MemberDTO) session.getAttribute("memberinfo");
		String member_auth = mDTO.getMember_auth();
		if(member_auth.equals("leader")||member_auth.equals("master")) {
			return true;
		}else {
			response.sendRedirect(request.getContextPath()+"/home.do");
			return false;
		}
	}
}
