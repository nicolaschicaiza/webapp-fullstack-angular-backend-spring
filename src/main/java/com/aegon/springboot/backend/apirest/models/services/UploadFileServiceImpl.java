package com.aegon.springboot.backend.apirest.models.services;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * UploadFileServiceImpl
 */
@Service
public class UploadFileServiceImpl implements IUploadFileService {
  private final Logger log = LoggerFactory.getLogger(UploadFileServiceImpl.class);
  private final static String UPLOADS_FOLDER = "uploads";

  @Override
  public Resource load(String filename) throws MalformedURLException {
    Path path = getPath(filename);
    log.info(path.toString());
    Resource resource = null;
    resource = new UrlResource(path.toUri());
    if (!resource.exists() && !resource.isReadable()) {
      path = Paths.get("src/main/resources/static/images").resolve("no_user.png").toAbsolutePath();
      resource = new UrlResource(path.toUri());
      log.error("Error no se pudo cargar la imagen: " + filename);
    }
    return resource;
  }

  @Override
  public String copy(MultipartFile file) throws IOException {
    String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename().replace(" ", "-");
    Path path = getPath(filename);
    log.info(path.toString());
    Files.copy(file.getInputStream(), path);
    return filename;
  }

  @Override
  public boolean delete(String filename) {
    if (filename != null && filename.length() > 0) {
      Path oldPath = getPath(filename);
      File oldFile = oldPath.toFile();
      if (oldFile.exists() && oldFile.canRead()) {
        oldFile.delete();
        return true;
      }
    }
    return false;
  }

  @Override
  public Path getPath(String filename) {
    return Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
  }

}
