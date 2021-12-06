package poly.service;

import java.util.List;

import poly.dto.MemberDTO;

public interface ILeaderService {

	List<MemberDTO> staffList() throws Exception;

	List<MemberDTO> myStaffList(MemberDTO mDTO) throws Exception;

	int addStaff(MemberDTO mDTO) throws Exception;

	int deleteStaff(MemberDTO mDTO) throws Exception;
	List<MemberDTO> blockList();

	int addBlock(MemberDTO mDTO);

	int deleteBlock(MemberDTO mDTO);
}