package pl.piomin.samples.springboot.tips.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pl.piomin.samples.springboot.tips.data.model.Tip;
import pl.piomin.samples.springboot.tips.data.repository.TipRepository;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/tips")
public class TipController {

    private static final Logger LOG = LoggerFactory.getLogger(TipController.class);
    private TipRepository repository;

    public TipController(TipRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public Iterable<Tip> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Tip findById(@PathVariable("id") Long id) {
        try {
            return repository.findById(id).orElseThrow();
        } catch (NoSuchElementException e) {
            LOG.error("Not found", e);
            throw new ResponseStatusException(HttpStatus.NO_CONTENT);
        }
    }
}
