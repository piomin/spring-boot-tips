package pl.piomin.samples.springboot.tips;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AppTest {

    @LocalServerPort
    private int port;

    @Test
    void test() {
        Assertions.assertTrue(port > 0);
    }
}
