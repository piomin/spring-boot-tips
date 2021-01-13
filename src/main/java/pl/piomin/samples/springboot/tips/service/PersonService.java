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
}
