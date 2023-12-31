package com.aegon.springboot.backend.apirest.models.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

/**
 * IUploadFileService
 */
public interface IUploadFileService {
  public Resource load(String filename) throws MalformedURLException;

  public String copy(MultipartFile file) throws IOException;

  public boolean delete(String filename);

  public Path getPath(String filename);

}
