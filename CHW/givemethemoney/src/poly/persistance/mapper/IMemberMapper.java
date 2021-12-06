package poly.persistance.mapper;

import config.Mapper;
import poly.dto.MemberDTO;
@Mapper("MemberMapper")
public interface IMemberMapper {

	MemberDTO login(MemberDTO mDTO);

	MemberDTO find_email(MemberDTO mDTO);

	int changePassword(MemberDTO mDTO);


	int upcCred(MemberDTO mDTO);
	int removeCredDB(MemberDTO mDTO);
	MemberDTO storeCredFromDB(MemberDTO mDTO);

    MemberDTO memberinfo(MemberDTO mDTO);
}
