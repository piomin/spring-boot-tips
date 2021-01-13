package pl.piomin.samples.springboot.tips;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import pl.piomin.samples.springboot.tips.controller.TipController;
import pl.piomin.samples.springboot.tips.data.model.Tip;
import pl.piomin.samples.springboot.tips.data.repository.TipRepository;
import pl.piomin.samples.springboot.tips.service.PersonService;
import pl.piomin.samples.springboot.tips.service.TipService;

import javax.persistence.EntityManagerFactory;
import java.util.List;

//@SpringBootTest
@DataJpaTest(showSql = false)
@Import({TipService.class, PersonService.class})
public class TipsControllerTest {

    @Autowired
    private TipService tipService;

    @Test
    void testFindAll() {
        List<Tip> tips = tipService.findAll();
        Assertions.assertEquals(3, tips.size());
    }

}
