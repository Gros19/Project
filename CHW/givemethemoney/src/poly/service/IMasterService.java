package poly.service;

import java.util.List;

import poly.dto.MemberDTO;

public interface IMasterService{

	List<MemberDTO> leaderList(MemberDTO mDTO);


	int deleteLeader(MemberDTO mDTO);

	int addLeader(MemberDTO mDTO);

}
