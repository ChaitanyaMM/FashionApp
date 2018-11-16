package com.fashionapp.util;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

 /**To :Do  this class is to read the templates and send mails accordingly*/

public class MailTemplateHelper {


	@Autowired
	private TemplateEngine templateEngine;
	

	private Map<String, String> inputParams;
	private static MailTemplateHelper instance;

	private MailTemplateHelper() {
	}

	public static synchronized MailTemplateHelper getInstance() {
		if (instance == null) {
			instance = new MailTemplateHelper();
		}
		return instance;
	}


	public String emailTemplate(Map<String, String> inputParams) {

		this.inputParams = inputParams;
		if (!inputParams.containsKey("type"))
			return "";
		int emailType = Integer.parseInt(inputParams.get("type"));
		switch (emailType) {
		case EmailConstants.EMAIL_ABOUT_CREATION:
			return sendRegistrationMail();

		default:
			return "";
		}

	}

	public String sendRegistrationMail() {
		System.out.println("templateEngine := " + templateEngine);
		Context ctx = new Context();
		ctx.setVariable("userName", "crazy!");
		System.out.println("cmng here !!");
		String signup = templateEngine.process("signup", ctx);

		System.out.println("Done!!");
		return signup;

	}

}
