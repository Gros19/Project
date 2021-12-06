package poly.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import poly.dto.MemberDTO;

public class MemberInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	HttpSession session;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// 블락 or 승인 no 되어있다면
		MemberDTO mDTO = (MemberDTO) session.getAttribute("memberinfo");

		if (mDTO != null) {
			return true;
		}
		response.sendRedirect(request.getContextPath() + "/member/login.do");
		return false;
	}
}
