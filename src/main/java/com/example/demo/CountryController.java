package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/countries")
public class CountryController {

    @Autowired
    CountryService countryService;

    @GetMapping
    Collection<Country> getCountries() {
        return countryService.findAllCountries();
    }

    @PostMapping
    void creerCountry(@RequestBody Country country) {
        countryService.creerCountry(country);
    }

    @GetMapping("/{id}")
    void put(@PathVariable String id) {
        countryService.getCountry(id);
    }


}
