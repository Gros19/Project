package poly.service;


import poly.dto.MemberDTO;
import poly.dto.UserInfoDTO;

public interface IUserInfoService {
	
	int insertUserInfo(UserInfoDTO pDTO) throws Exception;
	
	//로그인을 위해 아이디와 비밀번호가 일치하는지 확인하기
	int getUserLoginCheck(UserInfoDTO pDTO) throws Exception;

	int overlap_email(UserInfoDTO mDTO) throws Exception;

	int overlap_id(UserInfoDTO mDTO) throws Exception;

	void clearMember();

	int userUpdate(MemberDTO mDTO);

	MemberDTO userInfo(int member_no);

	void userDelete(int member_no);

	int checkPassword(MemberDTO mDTO);







}
