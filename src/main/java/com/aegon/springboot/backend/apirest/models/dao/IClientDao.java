package com.aegon.springboot.backend.apirest.models.dao;

import org.springframework.data.repository.CrudRepository;

import com.aegon.springboot.backend.apirest.models.entity.Client;

/**
 * IClientDao
 */
public interface IClientDao extends CrudRepository<Client, Long> {

}
