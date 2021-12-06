package poly.component;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import poly.dto.EmailDTO;

@Component
public class EmailSender {
	@Autowired
	protected JavaMailSender mailSender;
	public void SendEmail(EmailDTO email) throws Exception {
        
        MimeMessage msg = mailSender.createMimeMessage();
        msg.setSubject(email.getSubject());
        msg.setText(email.getContent());
        msg.setRecipient(RecipientType.TO , new InternetAddress(email.getReciver()));
         
        mailSender.send(msg); 
    }
}
