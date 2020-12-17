package com.smoothstack.ethopiaairlines.controller;

import java.util.List;

import com.smoothstack.ethopiaairlines.dao.AirportDAO;
import com.smoothstack.ethopiaairlines.model.Airport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/api/airports")
public class AirportController {
  @Autowired
  private AirportDAO airportDAO;

  @GetMapping("")
  public @ResponseBody List<Airport> getAirports() {
    return airportDAO.getAirports();
  }

  @GetMapping("/{id}")
  public @ResponseBody Airport getAirportById(@PathVariable final String id) {
    return airportDAO.getAirport(id);
  }

  @PostMapping("")
  public @ResponseBody Airport createAirport(@RequestBody final Airport airport) {
    airportDAO.createAirport(airport);
    return airport;
  }
}
