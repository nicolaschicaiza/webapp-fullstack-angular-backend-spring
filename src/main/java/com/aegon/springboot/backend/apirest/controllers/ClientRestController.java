package com.aegon.springboot.backend.apirest.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aegon.springboot.backend.apirest.models.entity.Client;
import com.aegon.springboot.backend.apirest.models.services.IClientService;

/**
 * ClientRestController
 */
@RestController
@RequestMapping("/api")
public class ClientRestController {

  @Autowired
  private IClientService clientService;

  @GetMapping("/clients")
  public List<Client> index() {
    return clientService.findAll();

  }

}
