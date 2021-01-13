package pl.piomin.samples.springboot.tips;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pl.piomin.samples.springboot.tips.data.model.Tip;
import pl.piomin.samples.springboot.tips.data.repository.TipRepository;

import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class TipsRepositoryTest {

    @Autowired
    private TipRepository tipRepository;

    @Test
    @Order(1)
    public void testAdd() {
        Tip tip = tipRepository.save(new Tip(null, "Tip1", "Desc1"));
        Assertions.assertNotNull(tip);
//        tipRepository.deleteById(tip.getId());
    }

    @Test
    @Order(2)
    public void testFindAll() {
        Iterable<Tip> tips = tipRepository.findAll();
        Assertions.assertEquals(0, ((List<Tip>) tips).size());
    }
}
