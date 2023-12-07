package com.aegon.springboot.backend.apirest.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.exception.DataException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.aegon.springboot.backend.apirest.models.entity.Client;
import com.aegon.springboot.backend.apirest.models.services.IClientService;

/**
 * ClientRestController
 */
@CrossOrigin(origins = { "http://localhost:4200" })
@RestController
@RequestMapping("/api")
public class ClientRestController {

  @Autowired
  private IClientService clientService;

  @GetMapping("/clients")
  public List<Client> index() {
    return clientService.findAll();
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
  @ResponseStatus(code = HttpStatus.CREATED)
  public Client create(@RequestBody Client client) {
    return clientService.save(client);
  }

  @PutMapping("/clients/{id}")
  @ResponseStatus(code = HttpStatus.CREATED)
  public Client update(@RequestBody Client client, @PathVariable Long id) {
    Client currentClient = clientService.findById(id);
    currentClient.setName(client.getName());
    currentClient.setLastname(client.getLastname());
    currentClient.setEmail(client.getEmail());
    return clientService.save(currentClient);
  }

  @DeleteMapping("/clients/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void delete(@PathVariable Long id) {
    clientService.delete(id);
  }

}
