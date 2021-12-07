package pl.piomin.samples.springboot.tips;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import pl.piomin.samples.springboot.tips.data.model.Tip;
import pl.piomin.samples.springboot.tips.service.PersonService;
import pl.piomin.samples.springboot.tips.service.TipService;

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
