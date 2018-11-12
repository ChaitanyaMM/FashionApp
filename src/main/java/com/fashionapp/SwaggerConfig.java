package com.fashionapp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
 
@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket produceApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("com.fashionapp.Controller")).paths(PathSelectors.regex("/api.*")).build();
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Project_y API").description("Project_y API reference for developers")
				.termsOfServiceUrl("http://project_y.com").contact("krishna chaitanya bodala")
				.license("Project_y License").licenseUrl("chaitanya.bodala@xcubelabs.com").version("0.0.1-SNAPSHOT")
				.build();
	}

	// Only select apis that matches the given Predicates.
	private Predicate<String> paths() {
		// Match all paths except /error
		return Predicates.and(PathSelectors.regex("/api.*"), Predicates.not(PathSelectors.regex("/error.*")));
	}

}