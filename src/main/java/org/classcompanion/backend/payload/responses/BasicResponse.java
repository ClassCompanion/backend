package org.classcompanion.backend.payload.responses;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class BasicResponse {
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd,HH:mm.sss", timezone = "CET")
	private Date timestamp;
	private boolean success;
	private int status;
	private String message;

	public BasicResponse() {
		timestamp = new Date();
	}

	public BasicResponse(boolean success, int status, String message) {
		this.timestamp = new Date();
		this.success = success;
		this.status = status;
		this.message = message;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
