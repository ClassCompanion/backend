package org.classcompanion.backend.properties;

import org.classcompanion.extensions.eduimpl.EduImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "eduimpl")
public class EduImplProperties {
	private EduImpl implementation;
	private String username;
	private String password;

	public EduImpl getImplementation() {
		return implementation;
	}

	public void setImplementation(EduImpl implementation) {
		this.implementation = implementation;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
