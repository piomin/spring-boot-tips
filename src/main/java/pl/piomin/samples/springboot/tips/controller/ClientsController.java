package pl.piomin.samples.springboot.tips.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import pl.piomin.samples.springboot.tips.client.PersonService;
import pl.piomin.samples.springboot.tips.data.model.Person;

@RestController
@RequestMapping("/clients")
public class ClientsController {

    private final static Logger LOG = LoggerFactory.getLogger(ClientsController.class);
    private final PersonService personService;

    public ClientsController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public Person add(@RequestBody Person person) {
        LOG.info("Add {}", person);
        return personService.add(person);
    }

    @GetMapping("/{id}")
    public Person findById(@PathVariable("id") Long id) {
        LOG.info("Find by id {}", id);
        return personService.findById(id);
    }
}
