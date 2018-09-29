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
public class UserRepository {

    @Autowired
    DelegateUserRepository delegate;

    @PostConstruct
    void init() throws IOException {
        // après l'injection
        InputStream is = getClass().getResourceAsStream("/user.csv");
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(new InputStreamReader(is));
        for (CSVRecord record : records) {
            String name = record.get(0);
            String code = record.get(1);
            delegate.save(User.builder().id(code).name(name).build());
        }
    }

    @SlowDown
    public List<User> findAll() {
        return delegate.findAll();
    }

    @SlowDown(100)
    public User save(User c) {
        return delegate.save(c);
    }

    @SlowDown(75)
    public Optional<User> getOne(String id) {
        return delegate.findById(id);
    }

    @SlowDown(100)
    public void delete(String id) {
        delegate.deleteById(id);
    }

    public interface DelegateUserRepository extends JpaRepository<User, String> {}

}
