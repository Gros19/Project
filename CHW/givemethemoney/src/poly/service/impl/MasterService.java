package poly.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;


import poly.dto.MemberDTO;
import poly.persistance.mapper.IMasterMapper;
import poly.service.IMasterService;

@Service("MasterService")
public class MasterService implements IMasterService {
	@Resource(name="MasterMapper")
	private IMasterMapper masterMapper;

	@Override
	public List<MemberDTO> leaderList(MemberDTO mDTO) {
		// TODO Auto-generated method stub
		List<MemberDTO> mList = masterMapper.leaderList(mDTO);
		return mList;
	}

	@Override
	public int addLeader(MemberDTO mDTO) {
		// TODO Auto-generated method stub
		String name = masterMapper.findName(mDTO.getMember_no());
		MemberDTO rDTO = new MemberDTO();
		String member_team = Integer.toString(mDTO.getMember_no())+"."+name;
		rDTO.setMember_no(mDTO.getMember_no());
		rDTO.setMember_team(member_team);
		int no = masterMapper.addLeader(rDTO);
		return no;
	}

	@Override
	public int deleteLeader(MemberDTO mDTO) {
		// TODO Auto-generated method stub
		int no = masterMapper.deleteLeader(mDTO);
		return no;
	}

}
