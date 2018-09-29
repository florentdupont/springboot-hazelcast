package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/basic/countries")
public class NoCacheController {

    @Autowired
   CountryService service;

    @PostMapping
    void put(@RequestBody Country country) {
        service.creerCountry(country);
    }

    @GetMapping("/{id}")
    void put(@PathVariable String id) {
        service.getCountry(id);
    }

    @GetMapping
    Collection<Country> get() {
        return service.findAllCountries();
    }

}
