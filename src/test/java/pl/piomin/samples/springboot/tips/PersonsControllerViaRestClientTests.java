package pl.piomin.samples.springboot.tips;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.client.RestTestClient;
import org.springframework.web.client.ApiVersionInserter;
import org.springframework.web.client.RestClient;
import org.springframework.web.context.WebApplicationContext;
import pl.piomin.samples.springboot.tips.data.model.Person;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonsControllerViaRestClientTests {

    RestTestClient client;

    @BeforeEach
    void setUp(WebApplicationContext context) {
        client = RestTestClient.bindToApplicationContext(context)
                .apiVersionInserter(ApiVersionInserter.useHeader("api-version"))
                .build();
    }

    @Test
    void shouldGetPerson() {
        client.get().uri("/persons/{id}", 1L)
                .apiVersion("v1")
                .header("api-version", "v1")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(Person.class)
                .value(person1 -> assertNotNull(person1.getId()));
    }
}
