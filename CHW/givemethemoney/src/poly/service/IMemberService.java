package poly.service;

import poly.dto.MemberDTO;

public interface IMemberService{

	MemberDTO find_email(MemberDTO mDTO);

	MemberDTO login(MemberDTO mDTO);

	boolean changePassword(MemberDTO mDTO);

}
