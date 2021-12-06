package poly.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import poly.dto.MemberDTO;
import poly.dto.UserInfoDTO;
import poly.persistance.mapper.UserInfoMapper;
import poly.service.IUserInfoService;
import poly.util.CmmUtil;

@Service("UserInfoService")
public class UserInfoService implements IUserInfoService {
	private Logger log = Logger.getLogger(this.getClass());
	@Resource(name = "UserInfoMapper")
	private UserInfoMapper userInfoMapper;

	@Override
	public int insertUserInfo(UserInfoDTO pDTO) throws Exception {

		// 회원가입 성공 : 1, 아이디중복으로인한 가입취소 : 2, 기타 에러 발생 : 0
		int res = 0;
		String member_email  = pDTO.getMember_email();
		log.info("member_email서비스 :"+member_email);
		
		
	//	
		// controller에서 값이 정상적으로 못 넘어오는 경우를 대비하기 위해 사용함
		if (pDTO == null) {
			pDTO = new UserInfoDTO();
		}

		// 회원 가입 중복 방지를 위해 DB에서 데이터 조회
		UserInfoDTO rDTO = userInfoMapper.getUserExists(pDTO);
		log.info("서비스이메일중복확인 rDTO : "+rDTO);
		// mapper에서 값이 정상적으로 못 넘어오는 경우를 대비하기 위해 사용함
		if (rDTO == null) {
			rDTO = new UserInfoDTO();
		}

		// 중복된 회원정보가 있는 경우, 결과값을 2로 변경하고, 더 이상 작업을 진행하지 않음
		if (CmmUtil.nvl(rDTO.getExists_yn()).equals("Y")) {
			res = 2;

			// 회원가입이 중복이 아니므로, 회원가입 진행함
		} else {

			// 회원가입
			int success = userInfoMapper.InsertUserInfo(pDTO);

			// DB에 데이터가 등록되었다면,
			if (success > 0) {
				res = 1;

			} else {
				res = 0;
			}
		}

		return res;
	}

	@Override
	public int getUserLoginCheck(UserInfoDTO pDTO) throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int overlap_email(UserInfoDTO mDTO) throws Exception {
		// TODO Auto-generated method stub
		// 회원가입 성공 : 1, 아이디중복으로인한 가입취소 : 2, 기타 에러 발생 : 0
				int res = 0;
				String member_email  = mDTO.getMember_email();
				log.info("member_email서비스 :"+member_email);
				
				
				
				// controller에서 값이 정상적으로 못 넘어오는 경우를 대비하기 위해 사용함
				if (mDTO == null) {
					mDTO = new UserInfoDTO();
				}

				// 회원 가입 중복 방지를 위해 DB에서 데이터 조회
				UserInfoDTO rDTO = userInfoMapper.getUserExists(mDTO);
				log.info("서비스이메일중복확인 rDTO : "+rDTO);
				// mapper에서 값이 정상적으로 못 넘어오는 경우를 대비하기 위해 사용함
				if (rDTO == null) {
					rDTO = new UserInfoDTO();
				}

				// 중복된 회원정보가 있는 경우, 결과값을 2로 변경하고, 더 이상 작업을 진행하지 않음
				if (CmmUtil.nvl(rDTO.getExists_yn()).equals("Y")) {
					res = 1;

					// 회원가입이 중복이 아니므로, 회원가입 진행함
				} else {

					res =0;
				}

				return res;
	}

	@Override
	public int overlap_id(UserInfoDTO mDTO) throws Exception {
		int res = 0;
		String member_id  = mDTO.getMember_id();
		log.info("member_email서비스 :"+member_id);
		
		
		
		// controller에서 값이 정상적으로 못 넘어오는 경우를 대비하기 위해 사용함
		if (mDTO == null) {
			mDTO = new UserInfoDTO();
		}

		// 회원 가입 중복 방지를 위해 DB에서 데이터 조회
		UserInfoDTO rDTO = userInfoMapper.getIdExists(mDTO);
		log.info("서비스아이디중복확인 rDTO : "+rDTO);
		// mapper에서 값이 정상적으로 못 넘어오는 경우를 대비하기 위해 사용함
		if (rDTO == null) {
			rDTO = new UserInfoDTO();
		}

		// 중복된 회원정보가 있는 경우, 결과값을 2로 변경하고, 더 이상 작업을 진행하지 않음
		if (CmmUtil.nvl(rDTO.getExists_yn()).equals("Y")) {
			res = 1;

			// 회원가입이 중복이 아니므로, 회원가입 진행함
		} else {

			res =0;
		}

		return res;
	}

	@Override
	@Scheduled(cron = "* * * * * *")
	public void clearMember() {
		// TODO Auto-generated method stub
		userInfoMapper.clearMember();
	}

	@Override
	public int userUpdate(MemberDTO mDTO) {
		// TODO Auto-generated method stub
		int result = userInfoMapper.userUpdate(mDTO);
		return result;
	}

	@Override
	public MemberDTO userInfo(int member_no) {
		// TODO Auto-generated method stub
		MemberDTO mDTO = userInfoMapper.userInfo(member_no);
		return mDTO;
	}

	@Override
	public void userDelete(int member_no) {
		// TODO Auto-generated method stub
		userInfoMapper.userDelete(member_no);
	}

	@Override
	public int checkPassword(MemberDTO mDTO) {
		// TODO Auto-generated method stub
		int result = userInfoMapper.checkPassword(mDTO);
		return result;
	}

}
