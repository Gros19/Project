package poly.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import poly.dto.MemberDTO;
import poly.service.ILeaderService;

@Controller
public class LeaderController {
	private Logger log = Logger.getLogger(this.getClass());
	@Resource(name="LeaderService")
	private ILeaderService leaderService;
	@RequestMapping(value="leader/staffList")
	public String staffList(HttpServletRequest req, HttpServletResponse resp, HttpSession session, ModelMap model ) throws Exception {
		List<MemberDTO> mList = leaderService.staffList();//한민 : member_auth="staff" and member_approve='N'인 memberList 데이터 전부
		model.addAttribute("mList", mList);
		log.info("mList :" + mList);
		return "/leader/staffList";
	}
	@RequestMapping(value="leader/myStaffList")
	public String myStaffList(HttpServletRequest req, HttpServletResponse resp, HttpSession session, 
			ModelMap model) throws Exception {
		MemberDTO mDTO = new MemberDTO();
		MemberDTO member_info = (MemberDTO)session.getAttribute("memberinfo");//한민 : (로그인한 계정)세셜 값 가져오기
		mDTO.setMember_team(member_info.getMember_team()); // 한민 : 로그인한 계정의 팀이름을 mDTO에 넣어준다.
		List<MemberDTO> mList = leaderService.myStaffList(mDTO);// 한민 : 로그인한 계정의 팀이름에 해당하는 staff의 모든 값을 가져온다.
		model.addAttribute("mList", mList);
		return "leader/myStaffList";
	}
	@RequestMapping(value="leader/addStaff") //한민 : 팀원을 자신의
	public String addStaff(HttpServletRequest req, HttpServletResponse resp,
			HttpSession session, ModelMap model) throws Exception {
		MemberDTO mDTO = new MemberDTO();
		int member_no = Integer.parseInt(req.getParameter("member_no"));
		mDTO.setMember_no(member_no);
		MemberDTO member_info = (MemberDTO)session.getAttribute("memberinfo"); // 한민: 로그인된 계정의 세션값을 불러온다.
		mDTO.setMember_team(member_info.getMember_team());//
		mDTO.setStored_cred(member_info.getStored_cred());//
		int res = leaderService.addStaff(mDTO);
		String msg = "";
		if(res > 0) {
			msg = "성공했습니다.";


		}else{
			msg = "실패했습니다.";
		}
		model.addAttribute("msg", msg);
		return "/leader/msgToStaffList";
	}
	@RequestMapping(value="leader/deleteStaff")
	public String deleteStaff(HttpServletRequest req, HttpServletResponse resp, ModelMap model) throws Exception {
		MemberDTO mDTO = new MemberDTO();
		int member_no = Integer.parseInt(req.getParameter("member_no"));
		mDTO.setMember_no(member_no);
		int res = leaderService.deleteStaff(mDTO);//한민 :member_team = "이름을 입력해주세요,member_approve = "N"
		// 해야 할 것 :  stored_cred = null로 바꾸기
		String msg = "";
		if(res >0) {
			msg = "성공했습니다.";
		}else {
			msg = "실패했습니다.";
		}
		model.addAttribute("msg", msg);
		return "/leader/msgToMyStaffList";
	}
	@RequestMapping(value = "leader/blockList")//한민 : member_auth="block"인 모든 member 데이터를 가져온다.
	public String blockList(HttpServletRequest req, HttpServletResponse resp, ModelMap model) throws Exception{
		List<MemberDTO> mList = leaderService.blockList();
		model.addAttribute("mList", mList);
		return "/leader/blockList";
	}
	@RequestMapping(value = "leader/addBlock")// 한민 addBlock 시 member_auth="staff" -변경->member_auth="block" /
	// member_team="해당 리더의 팀이름" -(변경)-> member_team="이름을 입력해주세요" /
	// 해야 될 일 LeaderMapper.xml 에 있는 id = "addBlock" 에 stored_cred = ""(null) 추가
	public String addBlock(HttpServletRequest req, HttpServletResponse resp, ModelMap model) {
		int member_no =Integer.parseInt(req.getParameter("member_no"));
		MemberDTO mDTO = new MemberDTO();
		mDTO.setMember_no(member_no);
		int result = leaderService.addBlock(mDTO);
		String msg = "";
		if(result >0) {
			msg = "등록을 완료햇습니다.";
		}else {
			msg = "등록에 실패했습니다.";
		}
		model.addAttribute("msg", msg);
		return "/leader/msgToMyStaffList";			
	}
	@RequestMapping(value = "leader/deleteBlock")//한민 : member_auth="block"을 "staff"로 변경
	public String deleteBlock(HttpServletRequest req, HttpServletResponse resp, ModelMap model) {
		int member_no = Integer.parseInt(req.getParameter("member_no"));
		MemberDTO mDTO = new MemberDTO();
		mDTO.setMember_no(member_no);
		int result = leaderService.deleteBlock(mDTO);
		String msg = "";
		if(result >0) {
			msg = "블락취소를 완료햇습니다.";
		}else {
			msg = "등록에실패했습니다.";
		}
		model.addAttribute("msg", msg);
		return "/leader/msgToBlockList";
	}
	
}
