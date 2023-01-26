package pl.piomin.samples.springboot.tips.data.repository;

import org.springframework.data.repository.CrudRepository;
import pl.piomin.samples.springboot.tips.data.model.Person;

import java.util.List;

public interface PersonRepository extends CrudRepository<Person, Long> {

    List<Person> findByAddressCity(String city);
    List<Person> findByAgeGreaterThan(int age);

    private void y() {

    }

    default void x() {

    }
}
