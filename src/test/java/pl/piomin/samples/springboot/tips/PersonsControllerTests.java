package pl.piomin.samples.springboot.tips;

import net.datafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pl.piomin.samples.springboot.tips.data.model.Address;
import pl.piomin.samples.springboot.tips.data.model.Contact;
import pl.piomin.samples.springboot.tips.data.model.Gender;
import pl.piomin.samples.springboot.tips.data.model.Person;

import java.util.Locale;
import java.util.Random;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PersonsControllerTests {

//    @Test
//    void add() {
//        Faker faker = new Faker(Locale.of("pl"), new Random(0));
//        Contact contact = new Contact();
//        contact.setEmail(faker.internet().emailAddress());
//        contact.setPhoneNumber(faker.phoneNumber().cellPhoneInternational());
//        Address address = new Address();
//        address.setCity(faker.address().city());
//        address.setCountry(faker.address().country());
//        address.setStreet(faker.address().streetName());
//        int number = Integer.parseInt(faker.address().streetAddressNumber());
//        address.setHouseNumber(number);
//        number = Integer.parseInt(faker.address().buildingNumber());
//        address.setFlatNumber(number);
//        Person person = new Person();
//        person.setName(faker.name().fullName());
//        person.setContact(contact);
//        person.setAddress(address);
//        person.setGender(Gender.valueOf(faker.gender().binaryTypes().toUpperCase()));
//        person.setAge(faker.number().numberBetween(18, 65));
//        System.out.println(person);
//
//        person = restTemplate.postForObject("/persons", person, Person.class);
//        Assertions.assertNotNull(person);
//        Assertions.assertNotNull(person.getId());
//
//    }
}
