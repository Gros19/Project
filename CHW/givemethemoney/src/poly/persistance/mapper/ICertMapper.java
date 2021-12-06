package poly.persistance.mapper;

import config.Mapper;
import poly.dto.CertDTO;

@Mapper("CertMapper")
public interface ICertMapper {

	void regist(CertDTO certDTO);

	void clearAll();

	CertDTO validate(CertDTO certDTO);

	void remove(CertDTO result);

	
}
