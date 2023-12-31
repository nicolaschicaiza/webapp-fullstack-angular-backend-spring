package com.aegon.springboot.backend.apirest.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.aegon.springboot.backend.apirest.models.entity.Client;

/**
 * IClientDao
 */
public interface IClientDao extends JpaRepository<Client, Long> {

}
