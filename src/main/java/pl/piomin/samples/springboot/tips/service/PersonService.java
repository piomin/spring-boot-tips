package pl.piomin.samples.springboot.tips.service;

import org.springframework.stereotype.Service;
import pl.piomin.samples.springboot.tips.data.model.Person;
import pl.piomin.samples.springboot.tips.data.model.Tip;
import pl.piomin.samples.springboot.tips.data.repository.PersonRepository;
import pl.piomin.samples.springboot.tips.data.repository.TipRepository;

import java.util.List;

@Service
public class PersonService {

    private PersonRepository repository;

    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    public List<Person> findAll() {
        return (List<Person>) repository.findAll();
    }

    public Person addPerson(Person person) {
        return repository.save(person);
    }

    public List<Person> addPersons(List<Person> person) {
        return (List<Person>) repository.saveAll(person);
    }

    public Person findById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public List<Person> findByCity(String city) {
        return repository.findByAddressCity(city);
    }

    public List<Person> findAllGreaterThanAge(int age) {
        return repository.findByAgeGreaterThan(age);
    }
}
