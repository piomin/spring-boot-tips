package pl.piomin.samples.springboot.tips.data.repository;

import org.springframework.data.repository.CrudRepository;
import pl.piomin.samples.springboot.tips.data.model.Person;

public interface PersonRepository extends CrudRepository<Person, Long> {

    private void y() {

    }

    default void x() {

    }
}
