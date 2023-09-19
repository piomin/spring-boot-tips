package pl.piomin.samples.springboot.tips;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;
import pl.piomin.samples.springboot.tips.data.model.Person;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonsControllerViaRestClientTests {

    @TestConfiguration
    public static class RestClientConfiguration {
        @LocalServerPort
        private Integer port;

        @Bean
        public RestClient restClient(RestClient.Builder builder) {
            return builder.baseUrl("http://localhost:" + port).build();
        }
    }

    @Autowired
    RestClient client;

    @Test
    void shouldGetPerson() {
        Person person = client.get()
                .uri("/persons/{id}", 1L)
                .header("X-Version", "v1")
                .retrieve()
                .body(Person.class);
        assertNotNull(person);
        assertNotNull(person.getId());
    }
}
