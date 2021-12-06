package poly.service.impl;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import poly.dto.CertDTO;
import poly.persistance.mapper.ICertMapper;
import poly.service.ICertService;
@Component
@Service("CertService")
public class CertService implements ICertService {
	private Logger log = Logger.getLogger(this.getClass());
	private Random r = new Random();
	@Resource(name = "CertMapper")
	private ICertMapper certMapper;

	@Override
	public String generateSecret() {
		int n = r.nextInt(1000000);
		Format f = new DecimalFormat("000000");
		String secret = f.format(n);
		log.info("secret :" + secret);
		return secret;
	}

	@Override
	public String generateCertification(String ip) {
		String secret = this.generateSecret();
		CertDTO certDTO = new CertDTO();
		certDTO.setSecret(secret);
		certDTO.setWho(ip);
		certMapper.regist(certDTO);
		return secret;
	}

	@Override
	@Scheduled(cron = "* * * * * *")
	public void clear() {
		certMapper.clearAll();
	}
	@Override
	public CertDTO validate(CertDTO certDTO) {
		log.info("certDTO : " +certDTO.getSecret() + certDTO.getWho());
		
		CertDTO result = certMapper.validate(certDTO);
		if(result != null) {
			certMapper.remove(result);
		}
		return result;
	}



}
