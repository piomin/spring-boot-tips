package pl.piomin.samples.springboot.tips.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pl.piomin.samples.springboot.tips.data.model.Person;
import pl.piomin.samples.springboot.tips.data.repository.PersonRepository;

@RestController
@RequestMapping("/persons")
public class PersonController {

    private static final Logger LOG = LoggerFactory.getLogger(PersonController.class);

    private PersonRepository repository;

    public PersonController(PersonRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    public Person add(@Valid @RequestBody Person person) {
        return repository.save(person);
    }

    @GetMapping("/{id}")
    public Person findById(@PathVariable("id") Long id, @RequestHeader("api-version") String version) {
        LOG.info("FindById: id={}, version={}", id, version);
        return repository.findById(id).orElseThrow();
    }
}
