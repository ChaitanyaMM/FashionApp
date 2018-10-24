package com.fashionapp;

import javax.annotation.Resource;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fashionapp.filestorage.FileStorage;

@SpringBootApplication
@Controller
@ComponentScan(basePackages = "com.*")
public class FashionAppApplication implements CommandLineRunner {
	
	@Resource
	FileStorage filestorage;

	public static void main(String[] args) {
		SpringApplication.run(FashionAppApplication.class, args);
	}

	@RequestMapping(value = "/")
	@ResponseBody
	public String OnStartUp() {

		return "FashionApp is listening !...";
	}

	@Override
	public void run(String... args) throws Exception {
		//filestorage.deleteAll();
		filestorage.init();
	}

}
