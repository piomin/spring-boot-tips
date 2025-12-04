package pl.piomin.samples.springboot.tips.client;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;
import pl.piomin.samples.springboot.tips.data.model.Person;

@HttpExchange(url = "http://localhost:8080/persons", headers = {"api-version=1.0"})
public interface PersonService {

    @PostExchange
    Person add(@RequestBody Person person);

    @GetExchange(url = "/{id}")
    Person findById(@PathVariable("id") Long id);

}
