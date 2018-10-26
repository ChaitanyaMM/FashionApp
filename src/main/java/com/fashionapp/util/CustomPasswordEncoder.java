package com.fashionapp.util;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;


public class CustomPasswordEncoder implements PasswordEncoder{
	
	
	@Override
	public String encode(CharSequence rawPassword) {
		// TODO Auto-generated method stub
		String encrypt = null;
		try {
			encrypt = PasswordEncryptDecryptor.encrypt(rawPassword.toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return encrypt;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		// TODO Auto-generated method stub
		if (!StringUtils.isEmpty(rawPassword) && encode(rawPassword).equals(encodedPassword)) {
	        return true;
	    }
	    return false;
	}
	
	

}
