package org.classcompanion.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan("org.classcompanion.backend.properties")
public class ClassCompanionBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(ClassCompanionBackendApplication.class, args);
	}
}
