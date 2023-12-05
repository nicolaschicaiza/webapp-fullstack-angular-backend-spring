package com.aegon.springboot.backend.apirest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
  public Client show(@PathVariable Long id) {
    return clientService.findById(id);
  }

  @PostMapping("/clients")
  @ResponseStatus(HttpStatus.CREATED)
  public Client create(@RequestBody Client client) {
    return clientService.save(client);
  }

  @PutMapping("/clients/{id}")
  @ResponseStatus(HttpStatus.CREATED)
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
