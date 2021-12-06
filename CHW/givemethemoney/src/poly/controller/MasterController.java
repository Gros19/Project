package poly.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import poly.dto.MemberDTO;
import poly.service.IMasterService;

@Controller
public class MasterController {
	private Logger log = Logger.getLogger(this.getClass());
	@Resource(name = "MasterService")
	private IMasterService masterService;
	@RequestMapping(value = "master/leaderList")
	public String leaderList(HttpServletRequest req, HttpServletResponse resp, ModelMap model) {
		MemberDTO mDTO = new MemberDTO();
		mDTO.setMember_auth("leader");
		List<MemberDTO> mList = masterService.leaderList(mDTO);
		model.addAttribute("mList", mList);
		return "/master/leaderList";
	}
	@RequestMapping(value = "master/addLeader")
	public String addLeader(HttpServletRequest req, HttpServletResponse resp, ModelMap model) {
		int member_no = Integer.parseInt(req.getParameter("member_no"));
		MemberDTO mDTO = new MemberDTO();
		mDTO.setMember_no(member_no);
		int no = masterService.addLeader(mDTO);
		String msg = "";
		if(no>0) {
			msg = "회원가입을 승인하였습니다.";
		}else {
			msg = "요청에 있어 오류가 발생했습니다.";
		}
		model.addAttribute("msg", msg);
		return "/master/msgToLeaderList";
	}
	@RequestMapping(value = "master/deleteLeader")
	public String deleteLeader(HttpServletRequest req, HttpServletResponse resp, ModelMap model) {
		int member_no = Integer.parseInt(req.getParameter("member_no"));
		MemberDTO mDTO = new MemberDTO();
		mDTO.setMember_no(member_no);
		int no = masterService.deleteLeader(mDTO);
		String msg = "";
		if(member_no>0) {
			msg = "리더를 삭제했습니다.";
		}else {
			msg = "요청에 있어 오류가 발생했습니다.";
		}
		model.addAttribute("msg", msg);
		return "/master/msgToLeaderList";

	}
}
