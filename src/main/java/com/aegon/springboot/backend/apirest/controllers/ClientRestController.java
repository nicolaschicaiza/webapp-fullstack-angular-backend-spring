package com.aegon.springboot.backend.apirest.controllers;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aegon.springboot.backend.apirest.models.entity.Client;
import com.aegon.springboot.backend.apirest.models.services.IClientService;

import jakarta.validation.Valid;

/**
 * ClientRestController
 */
@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class ClientRestController {

  @Autowired
  private IClientService clientService;

  private final Logger log = LoggerFactory.getLogger(ClientRestController.class);

  @GetMapping("/clients")
  public List<Client> index() {
    return clientService.findAll();
  }

  @GetMapping("/clients/page/{page}")
  public Page<Client> index(@PathVariable Integer page) {
    Pageable pageable = PageRequest.of(page, 4);
    return clientService.findAll(pageable);
  }

  @GetMapping("/clients/{id}")
  public ResponseEntity<?> show(@PathVariable Long id) {
    Client client = null;
    Map<String, Object> response = new HashMap<>();
    try {
      client = clientService.findById(id);
    } catch (DataAccessException e) {
      response.put("mensaje", "Error al realizar la consulta en la base de datos");
      response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
      return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    if (client == null) {
      response.put("mensaje", "El cliente ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
      return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
    }
    return new ResponseEntity<Client>(client, HttpStatus.OK);
  }

  @PostMapping("/clients")
  public ResponseEntity<?> create(@Valid @RequestBody Client client, BindingResult result) {
    Client newClient = null;
    Map<String, Object> response = new HashMap<>();
    if (result.hasErrors()) {
      List<String> errors = result.getFieldErrors()
          .stream()
          .map(err -> "El campo " + err.getDefaultMessage())
          .collect(Collectors.toList());
      response.put("errors", errors);
      return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
    }
    try {
      newClient = clientService.save(client);
    } catch (DataAccessException e) {
      response.put("mensaje", "Error al realizar el insert en la base de datos");
      response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
      return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    response.put("mensaje", "El cliente ha sido creado con éxito");
    response.put("client", newClient);
    return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
  }

  @PutMapping("/clients/{id}")
  public ResponseEntity<?> update(@Valid @RequestBody Client client, BindingResult result, @PathVariable Long id) {
    Client currentClient = clientService.findById(id);
    Client updatedClient = null;
    Map<String, Object> response = new HashMap<>();
    if (result.hasErrors()) {
      List<String> errors = new ArrayList<>();
      result.getFieldErrors().forEach(err -> {
        errors.add("El campo " + err.getDefaultMessage());
      });
      response.put("errors", errors);
      return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
    }
    if (currentClient == null) {
      response.put("mensaje",
          "Error: no se pudo editar, el cliente ID: ".concat(id.toString().concat(" no existe en la base de datos!")));
      return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);

    }
    try {
      currentClient.setName(client.getName());
      currentClient.setLastname(client.getLastname());
      currentClient.setEmail(client.getEmail());
      currentClient.setCreateAt(client.getCreateAt());
      updatedClient = clientService.save(currentClient);
    } catch (DataAccessException e) {
      response.put("mensaje", "Error al actualizar el cliente en la base de datos");
      response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
      return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    response.put("mensaje", "El cliente ha sido actualizado con éxito");
    response.put("client", updatedClient);
    return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
  }

  @DeleteMapping("/clients/{id}")
  public ResponseEntity<?> delete(@PathVariable Long id) {
    Map<String, Object> response = new HashMap<>();
    try {
      Client client = clientService.findById(id);

      // Delete photo
      String oldFilename = client.getPhoto();
      if (oldFilename != null && oldFilename.length() > 0) {
        Path oldPath = Paths.get("uploads").resolve(oldFilename).toAbsolutePath();
        File oldFile = oldPath.toFile();
        if (oldFile.exists() && oldFile.canRead()) {
          oldFile.delete();
        }
      }

      clientService.delete(id);
    } catch (DataAccessException e) {
      response.put("mensaje", "Error al eliminar el cliente de la base de datos");
      response.put("error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
      return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    response.put("mensaje", "El cliente ha sido eliminado con éxito!");
    return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
  }

  @PostMapping("/clients/upload")
  public ResponseEntity<?> upload(@RequestParam("file") MultipartFile file, @RequestParam("id") Long id) {
    Map<String, Object> response = new HashMap<>();

    Client client = clientService.findById(id);

    if (!file.isEmpty()) {
      String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename().replace(" ", "-");
      Path path = Paths.get("uploads").resolve(filename).toAbsolutePath();
      log.info(path.toString());

      try {
        Files.copy(file.getInputStream(), path);
      } catch (IOException e) {
        response.put("mensaje", "Error al subir la imagen del cliente " + filename);
        response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
      }

      // Delete old photo
      String oldFilename = client.getPhoto();
      if (oldFilename != null && oldFilename.length() > 0) {
        Path oldPath = Paths.get("uploads").resolve(oldFilename).toAbsolutePath();
        File oldFile = oldPath.toFile();
        if (oldFile.exists() && oldFile.canRead()) {
          oldFile.delete();
        }
      }

      client.setPhoto(filename);

      clientService.save(client);

      response.put("client", client);
      response.put("mensaje", "Has subido correctamente la imagen: " + filename);
    }

    return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
  }

  @GetMapping("/uploads/img/{filename:.+}")
  public ResponseEntity<Resource> seePhoto(@PathVariable String filename) {
    Path path = Paths.get("uploads").resolve(filename).toAbsolutePath();
    log.info(path.toString());
    Resource resource = null;
    try {
      resource = new UrlResource(path.toUri());
    } catch (MalformedURLException e) {
      e.printStackTrace();
    }
    if (!resource.exists() && !resource.isReadable()) {
      throw new RuntimeException("Error no se pudo cargar la imagen: " + filename);
    }
    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"");
    return new ResponseEntity<Resource>(resource, headers, HttpStatus.OK);
  }

}
