package pl.piomin.samples.springboot.tips;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
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

}
