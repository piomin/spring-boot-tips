package pl.piomin.samples.springboot.tips;

import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import pl.piomin.samples.springboot.tips.data.model.Address;
import pl.piomin.samples.springboot.tips.data.model.Contact;
import pl.piomin.samples.springboot.tips.data.model.Person;
import pl.piomin.samples.springboot.tips.service.PersonService;
import pl.piomin.samples.springboot.tips.service.TipService;

import java.util.List;

@DataJpaTest(showSql = false)
@Import({TipService.class, PersonService.class})
public class PersonsServiceTest {

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
                .generate(Select.field(Contact::getPhoneNumber), gen -> gen.text().pattern("+#d#d #d#d#d #d#d#d #d#d#d"))
                .generate(Select.field(Contact::getEmail), gen -> gen.text().pattern("#c@#c.com"))
                .create();
        person = personService.addPerson(person);
        Assertions.assertNotNull(person.getId());
        person = personService.findById(person.getId());
        Assertions.assertNotNull(person);
        Assertions.assertNotNull(person.getAddress());
    }

    @Test
    void addListAndGet() {
        final int numberOfObjects = 5;
        final String city = "Warsaw";
        List<Person> persons = Instancio.ofList(Person.class)
                .size(numberOfObjects)
                .generate(Select.field(Contact::getPhoneNumber), gen -> gen.text().pattern("+#d#d #d#d#d #d#d#d #d#d#d"))
                .generate(Select.field(Contact::getEmail), gen -> gen.text().pattern("#c@#c.com"))
                .set(Select.field(Address::getCity), city)
                .create();
        personService.addPersons(persons);
        persons = personService.findByCity(city);
        Assertions.assertEquals(numberOfObjects, persons.size());
    }

    @Test
    void addGeneratorAndGet() {
        List<Person> persons = Instancio.ofList(Person.class)
                .size(100)
                .ignore(Select.field(Person::getId))
                .generate(Select.field(Person::getAge), gen -> gen.ints().range(18, 65))
                .generate(Select.field(Contact::getPhoneNumber), gen -> gen.text().pattern("+#d#d #d#d#d #d#d#d #d#d#d"))
                .generate(Select.field(Contact::getEmail), gen -> gen.text().pattern("#c@#c.com"))
                .create();
        personService.addPersons(persons);
        persons = personService.findAllGreaterThanAge(40);
        Assertions.assertTrue(persons.size() > 0);
    }

}
