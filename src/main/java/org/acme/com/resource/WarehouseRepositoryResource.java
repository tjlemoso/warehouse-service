package org.acme.com.resource;

import java.time.LocalDate;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.acme.com.model.Warehouse;
import org.acme.com.repository.WarehouseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;


import io.quarkus.panache.common.Sort;

@Path("warehouse")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class WarehouseRepositoryResource {

    @Inject
    WarehouseRepository warehouseRepository;

    private static final Logger LOGGER = LoggerFactory.getLogger(WarehouseRepositoryResource.class.getName());

    @GET
    public List<Warehouse> get() {
        return warehouseRepository.listAll(Sort.by("name"));
    }

    @GET
    @Path("{id}")
    public Warehouse getSingle(Long id) {
      Warehouse entity = warehouseRepository.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Warehouse with id of " + id + " does not exist.", 404);
        }
        return entity;
    }

    @POST
    @Transactional
    public Response create(Warehouse warehouse) {
        if (warehouse.getName() == "") {
            throw new WebApplicationException("Warehouse was invalidly set on request.", 422);
        }
        warehouse.setCreateDate(LocalDate.now());
        warehouseRepository.persist(warehouse);
        return Response.ok(warehouse).status(201).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Warehouse update(Long id, Warehouse warehouse) {
        if (warehouse.getName() == null) {
            throw new WebApplicationException("Warehouse was not set on request.", 422);
        }

        Warehouse entity = warehouseRepository.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Warehouse with id of " + id + " does not exist.", 404);
        }

        entity.setName(warehouse.getName());  
        entity.setPhone(warehouse.getPhone());
        entity.setEmail(warehouse.getEmail());
        entity.setAddress(warehouse.getAddress());
        entity.setAddress2(warehouse.getAddress2());
        entity.setCity(warehouse.getCity());
        entity.setState(warehouse.getState());
        entity.setZip(warehouse.getZip());
        entity.setCountry(warehouse.getCountry());
        
        return entity;
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public Response delete(Long id) {
      Warehouse entity = warehouseRepository.findById(id);
      if (entity == null) {
          throw new WebApplicationException("Warehouse with id of " + id + " does not exist.", 404);
      }

      warehouseRepository.delete(entity);
      return Response.status(204).build();
    }

    @Provider
    public static class ErrorMapper implements ExceptionMapper<Exception> {

        @Inject
        ObjectMapper objectMapper;

        @Override
        public Response toResponse(Exception exception) {
            LOGGER.error("Failed to handle request", exception);

            int code = 500;
            if (exception instanceof WebApplicationException) {
                code = ((WebApplicationException) exception).getResponse().getStatus();
            }

            ObjectNode exceptionJson = objectMapper.createObjectNode();
            exceptionJson.put("exceptionType", exception.getClass().getName());
            exceptionJson.put("code", code);

            if (exception.getMessage() != null) {
                exceptionJson.put("error", exception.getMessage());
            }

            return Response.status(code)
                    .entity(exceptionJson)
                    .build();
        }

    }
}