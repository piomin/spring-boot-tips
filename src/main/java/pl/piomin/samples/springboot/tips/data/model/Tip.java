package pl.piomin.samples.springboot.tips.data.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tip {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String description;
}
