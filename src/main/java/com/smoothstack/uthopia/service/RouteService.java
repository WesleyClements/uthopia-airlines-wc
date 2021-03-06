package com.smoothstack.uthopia.service;

import java.util.List;
import java.util.Optional;

import com.smoothstack.uthopia.dao.RouteDAO;
import com.smoothstack.uthopia.exception.BadRequestException;
import com.smoothstack.uthopia.exception.NotFoundException;
import com.smoothstack.uthopia.exception.StateConflictException;
import com.smoothstack.uthopia.model.Route;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

@Service
public class RouteService {
  @Autowired
  private RouteDAO routeDAO;

  public List<Route> findAll() {
    return routeDAO.findAll();
  }

  public List<Route> findAllWithOriginAndDestination(final String origin,
      final String destination) {
    if (destination == null && origin == null)
      return routeDAO.findAll();
    else if (destination == null)
      return routeDAO.findAllByOriginId(origin);
    else if (origin == null)
      return routeDAO.findAllByDestinationId(destination);
    else
      return routeDAO.findAllByOriginIdAndDestinationId(origin, destination);
  }

  public Route findById(final Integer id) throws NotFoundException {
    final Optional<Route> optional = routeDAO.findById(id);
    if (!optional.isPresent())
      throw new NotFoundException("cannot find route with id: " + id);
    return optional.get();
  }

  public Route create(final Route route) throws BadRequestException, StateConflictException {
    try {
      return routeDAO.save(route);
    } catch (JpaSystemException e) {
      throw new BadRequestException(e.getMessage());
    } catch (DataIntegrityViolationException e) {
      if (e.getCause() instanceof ConstraintViolationException) {
        final ConstraintViolationException cve = (ConstraintViolationException) e.getCause();
        if (cve.getErrorCode() == 1062)
          throw new StateConflictException(
              route.getOriginId() + " to " + route.getDestinationId() + " already exists");
      }
      throw new BadRequestException();
    }
  }

  public void delete(final Integer id) throws NotFoundException {
    try {
      routeDAO.deleteById(id);
    } catch (EmptyResultDataAccessException e) {
      throw new NotFoundException();
    }
  }
}
