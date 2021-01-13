package pl.piomin.samples.springboot.tips;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
    "spring.config.location=classpath:/global.properties,classpath:/app.properties"
})
public class TipsAppTest {

    @Value("${property1}")
    private String property1;
    @Value("${property2}")
    private String property2;

    @Test
    void testProperties() {
        Assertions.assertEquals("App specific property1", property1);
        Assertions.assertEquals("Global property2", property2);
    }
}
