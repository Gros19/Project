package poly.persistance.mapper;

import java.util.List;

import config.Mapper;
import poly.dto.MemberDTO;

@Mapper("MasterMapper")
public interface IMasterMapper {

	List<MemberDTO> leaderList(MemberDTO mDTO);

	String findName(int member_no);

	int addLeader(MemberDTO mDTO);

	int deleteLeader(MemberDTO mDTO);

}
