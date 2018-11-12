package com.fashionapp.util;

import java.util.HashMap;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailSender {

	@Autowired
	private TemplateEngine templateEngine;
	@Autowired
	private JavaMailSender mailsender;

	private Map<String, String> inputParams;


	public void sendEmail(String mail, String subject, String htmlbody) {
		MimeMessage msg = mailsender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(msg);
 		try {
			helper.setTo(mail);
 			msg.setContent(htmlbody, "text/html");
			helper.setSubject(subject);

		} catch (MessagingException e) {
			e.printStackTrace();
		}
		mailsender.send(msg);
	}
  
	public void sendOnRegistration(String userName, String mailId) throws MessagingException {
 
		if (mailId == null || mailId.isEmpty())
			return;

		Map<String, String> inputParams = new HashMap<String, String>();
		inputParams.put("type", "" + EmailConstants.EMAIL_ABOUT_CREATION);
		inputParams.put("userName", userName);
 		String htmlBody =MailTemplateHelper.getInstance().emailTemplate(inputParams);
		System.out.println("htmlBody := "+htmlBody);
		if (htmlBody == null || htmlBody.isEmpty())
			return;
 
 		sendEmail(mailId, "Registration for Project_y", htmlBody);


	}

	  

	private String sendRegistrationMail(String userName) {
		System.out.println("templateEngine :=" + templateEngine);

		Context ctx = new Context();
		ctx.setVariable("userName", userName);
 		String htmlbody = templateEngine.process("signup", ctx);
 		
		return htmlbody;

	}

}
