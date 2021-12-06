package poly.service;

import org.springframework.mail.SimpleMailMessage;

public interface IEmailService {

	void sendSimpleMessage(String member_email, String string, String string2);

	void sendSimpleMessage(SimpleMailMessage message);


}
