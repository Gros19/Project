package poly.persistance.mapper;

import java.util.List;

import config.Mapper;
import poly.dto.MemberDTO;

@Mapper("LeaderMapper")
public interface ILeaderMapper {

	List<MemberDTO> staffList();

	List<MemberDTO> myStaffList(MemberDTO mDTO);

	int addStaff(MemberDTO mDTO);

	int deleteStaff(MemberDTO mDTO);


	int addBlock(MemberDTO mDTO);

	int deleteBlock(MemberDTO mDTO);

	List<MemberDTO> blockList();
	
}
