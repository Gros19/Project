package poly.persistance.mapper;

import java.util.List;

import config.Mapper;
import poly.dto.CHWDTO;

@Mapper("CHWMapper")
public interface ICHWMapper {

	//업데이트 화면보기 셀렉
	CHWDTO CHWUpdateInfo(CHWDTO pDTO) throws Exception;
	

	//업데이트
	void CHWUpdate(CHWDTO pDTO) throws Exception;
	
	void CHW123(CHWDTO pDTO) throws Exception;
	
}