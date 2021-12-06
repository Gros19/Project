package poly.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import poly.dto.MemberDTO;
import poly.persistance.mapper.ILeaderMapper;
import poly.service.ICalenderService;
import poly.service.ILeaderService;
@Service("LeaderService")
public class LeaderService implements ILeaderService{
	@Resource(name="LeaderMapper")
	private ILeaderMapper leaderMapper;
	@Resource(name="CalendarService")
	private ICalenderService iCalenderService;
	@Override
	public List<MemberDTO> staffList() throws Exception {
		// TODO Auto-generated method stub
		
		return leaderMapper.staffList();
	}

	@Override
	public List<MemberDTO> myStaffList(MemberDTO mDTO) throws Exception {
		// TODO Auto-generated method stub
		return leaderMapper.myStaffList(mDTO);
	}

	@Override
	public int addStaff(MemberDTO mDTO) throws Exception {
		// TODO Auto-generated method stub
		int res = 0;
		res = leaderMapper.addStaff(mDTO);
		return res;
	}

	@Override
	public int deleteStaff(MemberDTO mDTO) throws Exception {
		// TODO Auto-generated method stub
		int res = 0;
		res = leaderMapper.deleteStaff(mDTO);
		return res;
	}

	@Override
	public List<MemberDTO> blockList() {
		// TODO Auto-generated method stub
		List<MemberDTO> mList = leaderMapper.blockList();
		return mList;
	}

	@Override
	public int addBlock(MemberDTO mDTO) {
		// TODO Auto-generated method stub
		int result = leaderMapper.addBlock(mDTO);
		return result;
	}

	@Override
	public int deleteBlock(MemberDTO mDTO) {
		// TODO Auto-generated method stub
		int result = leaderMapper.deleteBlock(mDTO);
		return result;
	}

}
