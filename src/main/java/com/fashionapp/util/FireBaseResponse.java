package com.fashionapp.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
 

@JsonIgnoreProperties
public class FireBaseResponse {
	
	//unique identifier 
	private long message_id;
	
	private String error;

	public long getMessage_id() {
		return message_id;
	}

	public void setMessage_id(long message_id) {
		this.message_id = message_id;
	}
	
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}	
	
	@Override
	public String toString() {
		
		return "FirebaseResponse{" +
                "message_id=" + message_id +
                "error=" + error +
                "}";
		
	}
}
 
 