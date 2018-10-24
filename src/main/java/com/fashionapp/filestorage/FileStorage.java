package com.fashionapp.filestorage;


import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorage {
    
	public void init();

	public void store(MultipartFile file);
	public Stream<Path> loadFiles(); 
    public Resource loadFile(String filename) ;
    public void deleteAll();

	public void storemultiple(MultipartFile[] file);
        
}
