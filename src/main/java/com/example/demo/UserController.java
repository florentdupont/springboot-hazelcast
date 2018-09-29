package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    CacheManager cacheManager;

    @Autowired
    UserService service;

    @PostMapping
    void add(@RequestBody User u) {
        service.createUser(u);
    }

    @GetMapping("/{id}")
    User put(@PathVariable String id) {
        return service.getUser(id);
    }

    @PutMapping("/{id}")
    void put(@RequestBody User u) {
        service.modifyUser(u);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable String id) {
        service.deleteUser(id);
    }

    @GetMapping
    Collection<User> get() {
        return service.getAllUsersByCriteria();
    }

    @GetMapping("/test")
    void test() {
        System.out.println(cacheManager);
    }
}
