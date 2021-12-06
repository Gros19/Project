package poly.service.impl;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import poly.dto.CHWDTO;
import poly.persistance.mapper.ICHWMapper;
import poly.service.ICHWService;

@Service("CHWService")
public class CHWService implements ICHWService {
	
	Logger log = Logger.getLogger(this.getClass());
	
	@Resource(name="CHWMapper")
	private ICHWMapper CHWMapper;
	
	@Override
	public void CHWUpdate(CHWDTO rDTO) throws Exception {
		
		
		//controller에서 값이 정상적으로 못 넘어오는 경우 사용
		if(rDTO == null) {
			rDTO = new CHWDTO();
		}
		//DTO는 데이터베이스 변수선언 int String같은 애
		CHWDTO pDTO = new CHWDTO(); 
		
		
		CHWMapper.CHWUpdate(pDTO);
		
		
		//mapper에서 값이 정상적으로 못 넘어오는 경우를 대비하기 위해 사용
		if (pDTO == null) {
	         pDTO = new CHWDTO();
			
	         
		}
	}

	

	@Override
	public CHWDTO CHWUpdateInfo(CHWDTO pDTO) throws Exception {
	
		log.info(this.getClass().getName()+"서비스 진입");
		
		if (pDTO == null) {
	        pDTO = new CHWDTO();
			log.info("널처리");
		}
		
		return CHWMapper.CHWUpdateInfo(pDTO);
		
		
	}

	
	public void CHW123(CHWDTO rDTO) throws Exception {
		CHWMapper.CHW123(rDTO);
		
		
	}

}
