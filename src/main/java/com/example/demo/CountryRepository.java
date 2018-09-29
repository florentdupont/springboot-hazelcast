package com.example.demo;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

/**
 * Classe de simulation de temps d'accès un peu plus long....
 */
@Repository
public class CountryRepository {

    @Autowired
    DelegateCountryRepository delegate;

    @PostConstruct
    void init() throws IOException {
        // après l'injection
        InputStream is = getClass().getResourceAsStream("/country.csv");
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(new InputStreamReader(is));
        for (CSVRecord record : records) {
            String name = record.get(0);
            String code = record.get(1);
            delegate.save(Country.builder().code(code).label(name).build());
        }
    }

    @SlowDown
    public List<Country> findAll() {
        return delegate.findAll();
    }

    @SlowDown(100)
    public Country save(Country c) {
        return delegate.save(c);
    }

    @SlowDown(75)
    public Optional<Country> getOne(String id) {
        return delegate.findById(id);
    }

    @SlowDown(100)
    public void delete(Country c) {
        delegate.delete(c);
    }

    public interface DelegateCountryRepository extends JpaRepository<Country, String> {}

}
