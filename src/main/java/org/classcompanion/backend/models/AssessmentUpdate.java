package org.classcompanion.backend.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AssessmentUpdate {
	@JsonProperty("assessment_id")
	private Integer assessmentId;
	@JsonProperty("message_id")
	private String messageId;

	public AssessmentUpdate() {
	}

	public Integer getAssessmentId() {
		return assessmentId;
	}

	public void setAssessmentId(Integer assessmentId) {
		this.assessmentId = assessmentId;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
}
