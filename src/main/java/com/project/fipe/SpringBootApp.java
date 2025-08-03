package com.project.fipe;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SpringBootApp {

	private static ConfigurableApplicationContext context;

	public static void main(String[] args) {
		// Inicializa o contexto do Spring antes de iniciar a aplicação JavaFX
		context = new SpringApplicationBuilder(SpringBootApp.class).run(args);
		// Lança a aplicação JavaFX
		JavaFxApplication.launch(JavaFxApplication.class, args);
	}

	public static ConfigurableApplicationContext getContext() {
		return context;
	}
}
