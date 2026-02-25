package com.training.expenseTracker;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootTest(properties = {
		"spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
		"spring.datasource.driver-class-name=org.h2.Driver",
		"spring.datasource.username=sa",
		"spring.datasource.password=",
		"spring.jpa.hibernate.ddl-auto=create-drop",
		"spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"
})
public class ExpenseTrackerApplicationTest {

	@Autowired
	private ApplicationContext context;

	@Test
	public void contextLoads() {
		assertNotNull(context);
	}

	@Test
	public void testCorsConfigurerBean() {
		ExpenseTrackerApplication app = new ExpenseTrackerApplication();
		WebMvcConfigurer configurer = app.corsConfigurer();
		assertNotNull(configurer);

		// Test that addCorsMappings can be called without error
		CorsRegistry registry = new CorsRegistry();
		configurer.addCorsMappings(registry);

		// Since CorsRegistry does not expose its mappings publicly,
		// we check that the configurer is not null and no exceptions are thrown.
		assertNotNull(configurer);
	}
}
