package com.aegon.springboot.backend.apirest.models.services;

import java.util.List;

import com.aegon.springboot.backend.apirest.models.entity.Client;

/**
 * IClientService
 */
public interface IClientService {

  public List<Client> findAll();
}
