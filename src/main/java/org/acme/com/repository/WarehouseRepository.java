package org.acme.com.repository;

import javax.enterprise.context.ApplicationScoped;

import org.acme.com.model.Warehouse;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

@ApplicationScoped
public class WarehouseRepository implements PanacheRepository<Warehouse>{
  
}
