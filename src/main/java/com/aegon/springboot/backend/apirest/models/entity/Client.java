package com.aegon.springboot.backend.apirest.models.entity;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * Client
 */
@Entity
@Table(name = "clients")
public class Client implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @NotEmpty(message = "'Nombre' no debe estar vacío.")
  @Size(min = 4, max = 12, message = "'Nombre' debe ser entre 4 y 12 caracteres.")
  @Column(nullable = false)
  private String name;

  @NotEmpty(message = "'Apellido' no debe estar vacío.")
  private String lastname;

  @NotEmpty(message = "'Correo Electrónico' no debe estar vacío.")
  @Email(message = "'Correo Electrónico' no es una dirección de correo bien formada.")
  @Column(nullable = false, unique = true)
  private String email;

  @Column(name = "create_at")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createAt;

  @PrePersist
  public void prePresist() {
    createAt = new Date();
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Date getCreateAt() {
    return createAt;
  }

  public void setCreateAt(Date createAt) {
    this.createAt = createAt;
  }

  private static final long serialVersionUID = 1L;

}
