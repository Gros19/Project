package poly.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import poly.service.IEmailService;

@Service("EmailService")
public class EmailService implements IEmailService {
	@Autowired
	private JavaMailSender sender;
	@Override
	public void sendSimpleMessage(SimpleMailMessage message) {
		sender.send(message);
	}
	@Override
	public void sendSimpleMessage(String member_email, String title, String text) {
		// TODO Auto-generated method stub
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo(member_email);
		message.setSubject(title);
		message.setText(text);
		this.sendSimpleMessage(message);
	}

}
