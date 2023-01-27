package pl.piomin.samples.springboot.tips.controller;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.piomin.samples.springboot.tips.data.model.Person;
import pl.piomin.samples.springboot.tips.data.repository.PersonRepository;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private PersonRepository repository;

    public PersonController(PersonRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Person add(@Valid @RequestBody Person person) {
        return repository.save(person);
    }
}
