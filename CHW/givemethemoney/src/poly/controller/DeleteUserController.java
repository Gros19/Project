package poly.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import poly.dto.UserInfoDTO;
import poly.service.ICertService;
import poly.service.IUserInfoService;
import poly.util.CmmUtil;
import poly.util.EncryptUtil;

@Controller

public class DeleteUserController {
	private Logger log = Logger.getLogger(this.getClass());

	@Resource(name = "UserInfoService")
	private IUserInfoService userInfoService;


	// 회원탈퇴 화면으로 이동
	@RequestMapping(value = "user/userDeleteForm")
	public String userRegForm() {
		log.info(this.getClass().getName() + ".user/userDeleteForm ok !");

		return "/user/userDeleteForm";
	}

	@RequestMapping(value = "user/DeleteUserInfo")
	public String insertUserInfo(HttpServletRequest request, HttpServletResponse response, ModelMap model)
			throws Exception {
		log.info(this.getClass().getName() + ".insertUserInfo start !");

		UserInfoDTO pDTO =null;
		String msg = "";

		try {
			String MEMBER_ID = CmmUtil.nvl(request.getParameter("member_id"));
			String MEMBER_PW = CmmUtil.nvl(request.getParameter("member_pw"));
			log.info("MEMBER_ID : " + MEMBER_ID);
			log.info("MEMBER__PW : " + MEMBER_PW);
			pDTO = new UserInfoDTO();
			pDTO.setMember_id(MEMBER_ID);
			pDTO.setMember_pw(EncryptUtil.encHashSHA256(MEMBER_PW));
			log.info("끝");
			
		} catch (Exception e) {
			msg = "실패하였습니다. : " + e.toString();
			log.info(e.toString());
			e.printStackTrace();
		}

		return "/msgToHome";
	}
}
