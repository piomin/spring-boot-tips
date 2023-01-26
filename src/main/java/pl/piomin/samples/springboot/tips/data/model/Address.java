package pl.piomin.samples.springboot.tips.data.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String country;
    private String city;
    private String street;
    private int houseNumber;
    private int flatNumber;
}
