package poly.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;


import poly.dto.MemberDTO;
import poly.persistance.mapper.IMemberMapper;
import poly.service.IMemberService;

@Service("MemberService")
public class MemberService implements IMemberService {
	private Logger log = Logger.getLogger(this.getClass());
	@Resource(name="MemberMapper")
	private IMemberMapper memberMapper;

	@Override
	public MemberDTO find_email(MemberDTO mDTO) {
		// TODO Auto-generated method stub
		MemberDTO member_email = memberMapper.find_email(mDTO);
		return member_email;
	}

	@Override
	public MemberDTO login(MemberDTO mDTO) {
		// TODO Auto-generated method stub
		log.info("서비스 로그인 : "+mDTO);
		MemberDTO rDTO = memberMapper.login(mDTO);
		return rDTO;
	}

	@Override
	public boolean changePassword(MemberDTO mDTO) {
		// TODO Auto-generated method stub
		log.info("member_no : "+mDTO.getMember_no());
		int update = memberMapper.changePassword(mDTO);
		if(update >0) {
			return true;
		}else {
			return false;
		}
	}
}
