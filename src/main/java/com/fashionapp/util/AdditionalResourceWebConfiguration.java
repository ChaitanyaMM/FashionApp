package com.fashionapp.util;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AdditionalResourceWebConfiguration implements WebMvcConfigurer {

 /*	
  * 
  * From the respective folder we can browse the file
	example :--http://localhost:9090/filestorage/sample.mp4
  */	
	
	
	private final Path rootLocation = Paths.get("filestorage");
	private final Path profieImageLocation = Paths.get("profileimages");


  @Override
  public void addResourceHandlers(final ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/filestorage/**","/profileimages/**").addResourceLocations("file:"+rootLocation +"/","file:"+profieImageLocation +"/");
  }
 
}