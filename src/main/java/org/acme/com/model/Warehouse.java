package org.acme.com.model;

import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "warehouse")
@Getter
@Setter
public class Warehouse {

  @Id
  @Column(name = "warehouseId")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long warehouseId;

  @Column(name = "createData")
  private LocalDate createData;

  @Column(name = "name")
  private String name;

  @Column(name = "phone")
  private String phone;

  @Column(name = "email")
  private String email;

  @Column(name = "identity")
  private String identity;

  @Column(name = "description")
  private String description;
  
  @Column(name = "addressId")
  private Long addressId;

  @Column(name = "supplierId")
  private Long supplierId;
}
