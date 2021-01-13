package pl.piomin.samples.springboot.tips.service;

import org.springframework.stereotype.Service;
import pl.piomin.samples.springboot.tips.data.model.Tip;
import pl.piomin.samples.springboot.tips.data.repository.TipRepository;

import java.util.List;

@Service
public class TipService {

    private TipRepository repository;

    public TipService(TipRepository repository) {
        this.repository = repository;
    }

    public List<Tip> findAll() {
        return (List<Tip>) repository.findAll();
    }
}
