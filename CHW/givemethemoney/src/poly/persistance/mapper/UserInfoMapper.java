package poly.persistance.mapper;

import config.Mapper;
import poly.dto.MemberDTO;
import poly.dto.UserInfoDTO;

@Mapper("UserInfoMapper")
public interface UserInfoMapper {

	//회원 가입 ( 회원 정보 등록 )
	int InsertUserInfo(UserInfoDTO pDTO) throws Exception;
	
	// 회원 가입 전 중복 체크 ( DB 조회 )
	UserInfoDTO getUserExists(UserInfoDTO mDTO) throws Exception;
	
	//로그인을 위해 아이디와 비밀번호가 일치하는지 확인하기
	UserInfoDTO getUserLoginCheck(UserInfoDTO pDTO) throws Exception;

	UserInfoDTO getIdExists(UserInfoDTO mDTO);

	void clearMember();

	int userUpdate(MemberDTO mDTO);

	MemberDTO userInfo(int member_no);

	void userDelete(int member_no);

	int checkPassword(MemberDTO mDTO);
}
