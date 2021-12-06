package poly.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import poly.dto.UserInfoDTO;
import poly.service.IUserInfoService;

@RestController
@CrossOrigin("*")
public class MemberRestController {
	@Resource(name = "UserInfoService")
	private IUserInfoService userInfoService;
	private Logger log = Logger.getLogger(this.getClass());
	@RequestMapping(value="/member_rest/overlap_email", method=RequestMethod.GET)
	public Object overlap_email(HttpServletRequest req, HttpServletResponse resp,
			@RequestParam("member_email") String member_email) throws Exception {
		log.info("member_email : "+ member_email);
		UserInfoDTO mDTO = new UserInfoDTO();
		mDTO.setMember_email(member_email);
		int no =(int) userInfoService.overlap_email(mDTO);
		if(no!=0) {
			return 1;
		}
		return null;
	}
	@RequestMapping(value="member_rest/overlap_id", method=RequestMethod.GET)
	public Object overlap_id(HttpServletRequest req) throws Exception {
		String member_id = req.getParameter("member_id");
		log.info("member_id : " +member_id );
		UserInfoDTO mDTO =new UserInfoDTO();
		mDTO.setMember_id(member_id);
		int no = (int) userInfoService.overlap_id(mDTO);
		if(no !=0) {
			return 1;
		}
		return null;
	}
}
