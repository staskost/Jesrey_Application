package com.staskost.jersey_example;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class JerseyExampleApplication extends SpringBootServletInitializer {
	public static void main(String[] args) {
		new JerseyExampleApplication().configure(new SpringApplicationBuilder(JerseyExampleApplication.class)).run(args);

	}
}
