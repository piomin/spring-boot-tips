package pl.piomin.samples.springboot.tips;

import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import pl.piomin.samples.springboot.tips.data.model.Address;
import pl.piomin.samples.springboot.tips.data.model.Person;
import pl.piomin.samples.springboot.tips.data.model.Tip;
import pl.piomin.samples.springboot.tips.service.PersonService;
import pl.piomin.samples.springboot.tips.service.TipService;

import java.util.List;

@DataJpaTest(showSql = false)
@Import({TipService.class, PersonService.class})
public class PersonsControllerTest {

    @Autowired
    private PersonService personService;

    @Test
    void testFindAll() {
        List<Person> persons = personService.findAll();
        Assertions.assertEquals(4, persons.size());
    }

    @Test
    void addAndGet() {
        Person person = Instancio.of(Person.class)
                .ignore(Select.field(Person::getId))
                .create();
        person = personService.addPerson(person);
        Assertions.assertNotNull(person.getId());
        person = personService.findById(person.getId());
        Assertions.assertNotNull(person);
        Assertions.assertNotNull(person.getAddress());
    }

    @Test
    void addListAndGet() {
        List<Person> persons = Instancio.ofList(Person.class)
                .size(5)
                .set(Select.field(Address::getCity), "Warsaw")
                .create();
        personService.addPersons(persons);
        persons = personService.findByCity("Warsaw");
        Assertions.assertEquals(5, persons.size());
    }

    @Test
    void addGeneratorAndGet() {
        List<Person> persons = Instancio.ofList(Person.class)
                .size(100)
                .ignore(Select.field(Person::getId))
                .generate(Select.field(Person::getAge), gen -> gen.ints().range(18, 65))
                .create();
        personService.addPersons(persons);
        persons = personService.findAllGreaterThanAge(40);
        Assertions.assertTrue(persons.size() > 0);
    }

}
