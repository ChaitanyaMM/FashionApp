package com.fashionapp.filestorage;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {
    
	public void init();

	public void store(MultipartFile file);
	public Stream<Path> loadFiles(); 
    public Resource loadFile(String filename) ;
    public void deleteAll();
        
}
