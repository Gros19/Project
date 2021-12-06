package poly.persistance.mapper;

import config.Mapper;
import poly.dto.MemberDTO;
import poly.dto.UserInfoDTO;

@Mapper("DeleteUserMapper")
public interface IDeleteUserMapper {
		//회원 탈퇴
		int DeleteUserInfo(UserInfoDTO pDTO) throws Exception;
		//..
}
