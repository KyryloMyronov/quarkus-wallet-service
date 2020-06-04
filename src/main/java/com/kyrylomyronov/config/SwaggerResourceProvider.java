package com.kyrylomyronov.leovegastest.config;

import static java.util.Collections.singletonList;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class SwaggerResourceProvider implements WebMvcConfigurer {

	@Primary
	@Bean
	public SwaggerResourcesProvider swaggerResourcesProvider() {
		return () -> {
			SwaggerResource wsResource = new SwaggerResource();
			wsResource.setName("Wallet Service API");
			wsResource.setSwaggerVersion("2.0");
			wsResource.setUrl("/wallet-api.yaml");

			return singletonList(wsResource);
		};
	}
}
