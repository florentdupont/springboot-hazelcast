package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserService {




    @Autowired
    UserRepository repository;

    /**
     * On ne met pas cette collection de recherche en cache car on sait qu'elle pourrait être bien trop importante
     * Les resultats de recherches sont donc retournés tels quels.
     * @return
     */
    @TrackTime
    public Collection<User> getAllUsersByCriteria(/* */) {
        return repository.findAll();
    }

    @TrackTime
    @Cacheable(cacheNames = "users")
    public User getUser(String id) {
        return repository.getOne(id).orElse(null);
    }

    @TrackTime
    @CachePut(value = "users", key = "#result.id")
    public User createUser(User u) {
        return repository.save(u);
    }

    @TrackTime
    @CacheEvict(value = "users")
    public void deleteUser(String u) {
        repository.delete(u);
    }

    @TrackTime
    @CachePut(value = "users", key = "#result.id")
    public User modifyUser(User u) {
        return repository.save(u);
    }

}
