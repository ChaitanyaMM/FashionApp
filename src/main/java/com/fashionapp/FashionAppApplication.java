package com.fashionapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@SpringBootApplication
@Controller
@ComponentScan(basePackages = "com.*")
public class FashionAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(FashionAppApplication.class, args);
	}

	@RequestMapping(value = "/")
	@ResponseBody
	public String OnStartUp() {

		return "FashionApp is listening !...";
	}

}
