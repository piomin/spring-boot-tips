package pl.piomin.samples.springboot.tips;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import pl.piomin.samples.springboot.tips.data.model.Person2;

@SpringBootTest(properties = {
    "spring.config.location=classpath:/global.properties,classpath:/app.properties"
})
public class TipsAppTest {

    @Value("${property1}")
    private String property1;
    @Value("${property2}")
    private String property2;
    @Autowired
    private Person2 person2;

    @Test
    void testProperties() {
        Assertions.assertEquals("App specific property1", property1);
        Assertions.assertEquals("Global property2", property2);
    }

    @Test
    void testConfigurationProperties() {
        Assertions.assertEquals("Piotr Minkowski", person2.name());
        Assertions.assertEquals(18, person2.age());
    }
}
