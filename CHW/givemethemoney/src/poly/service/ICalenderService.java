package poly.service;


import poly.dto.EventDTO;
import poly.dto.MemberDTO;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

public interface ICalenderService {

	MemberDTO memberinfo(MemberDTO mDTO);
	void insertEvent(EventDTO pDTO) throws IOException, GeneralSecurityException;
	void deleteEvent(String event_id) throws IOException, GeneralSecurityException;
	void updateEvent(EventDTO pDTO) throws IOException, GeneralSecurityException;

	void deleteCred();
	int removeCredDB(MemberDTO pDTO) throws IOException;
	List<EventDTO> firstGetCredentials(MemberDTO pDTO) throws IOException, GeneralSecurityException;
	List<EventDTO> getCredentialsAtLocal(MemberDTO pDTO) throws IOException, GeneralSecurityException;
}
