package poly.service;

import java.util.List;

import poly.dto.CHWDTO;
import poly.dto.NoticeDTO;

public interface ICHWService {

	CHWDTO CHWUpdateInfo(CHWDTO rDTO) throws Exception;
	
	void CHWUpdate(CHWDTO rDTO) throws Exception;

	
	void CHW123(CHWDTO rDTO) throws Exception;
	
	
	
}
