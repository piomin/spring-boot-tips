package pl.piomin.samples.springboot.tips.data.repository;

import org.springframework.data.repository.CrudRepository;
import pl.piomin.samples.springboot.tips.data.model.Tip;

public interface TipRepository extends CrudRepository<Tip, Long> {
}
