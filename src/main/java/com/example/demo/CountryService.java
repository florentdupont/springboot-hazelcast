package com.example.demo;


import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * Exemple de All-In Cache
 *
 *
 */
@Transactional

@Service
public class CountryService {

    @Autowired
    private HazelcastInstance hazelcastInstance;

    @Autowired
    private CountryRepository repository;

    @TrackTime
    public Collection<Country> findAllCountries() {

        Map<String, Map<String, Country>> m = hazelcastInstance.getReplicatedMap("countries");

        // c'est vraiment dans l'idée de retourné une valeur sépcifique...
        // une instance spécifique d'un objet n'est pas présent : alors on le récupère
        // de la base.

        // a cache is not a database !
        // https://speakerdeck.com/pycon2014/cache-me-if-you-can-memcached-caching-patterns-and-best-practices-by-guillaume-ardaud?slide=16
        // c'est donc particulièrement adapté pour les findByID : .

        // on peut considérer un cas "à la marge" pour nous dans certains cas :
        // car on sait que les données référentielles ne sont pas niombreusse:

        // Les données éligibles :
        //  - les données accédées souvent
        //  - les données qui changent peu
        //  - les collections assez réduites ! (genre, liste de pays)

       // hazelcastInstance.getDistributedObjects().forEach(x -> System.out.println(x));

        // https://www.baeldung.com/hibernate-second-level-cache

       return getCountriesMap().values();

        // un ordre d'idée : 45 fois plus rapide !
        // get initial : 240-250ms
        // get suivants : 3-5 ms

        // pas sur pour les chiffres...lors de l'appel initial, Java fait de la compil JIT

        // creer : 45ms


        // difficile a tester car POSTGRESQL gère un cache aussi de son côté...
        // du coup le test est pas hyper représentatif..
        // https://stackoverflow.com/questions/24252455/how-disable-postgresql-cache-optimization



    }

    @TrackTime
    public void creerCountry(Country c) {

        Country savedCountry = repository.save(c);

        // qu'est ce qui se passe si la transaction ne se fait pas ?
        // on resoud le probleme en mettant à jour le cache à partir de la base à chaque ecriture
        // le save va donc être un peu plus long ca tombe bien : on fait très peu d'écriture !
        refreshCache();

    }

    @TrackTime
    public Country getCountry(String code) {
        return getCountriesMap().get(code);

    }

    @TrackTime
    public void deleteCountry(Country c) {
        repository.delete(c);
        refreshCache();
    }


    private Map<String, Country> getCountriesMap() {
        Map<String, Map<String, Country>> m = hazelcastInstance.getReplicatedMap("countries");
        if(m.get("collection") == null) {
           return refreshCache();
        } else {
            return  m.get("collection");
        }
    }


    private Map<String, Country> refreshCache() {
        Map<String, Map<String, Country>> m = hazelcastInstance.getReplicatedMap("countries");

        List<Country> countries = repository.findAll();
        Map<String, Country> collection = countries.stream().collect(Collectors.toMap(x -> x.code, x->x));
        m.put("collection", collection);

        return collection;
    }








    // Don't do this at home
    private void simulateSlowService() {
        try {
            long time = 500L;
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }


}
