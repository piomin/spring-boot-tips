package pl.piomin.samples.springboot.tips.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.piomin.samples.springboot.tips.data.model.Tip;
import pl.piomin.samples.springboot.tips.data.repository.TipRepository;

@Profile("demo")
@Component
public class ApplicationStartupListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private TipRepository repository;

    public void onApplicationEvent(final ApplicationReadyEvent event) {
        repository.save(new Tip(null, "Test1", "Desc1"));
        repository.save(new Tip(null, "Test2", "Desc2"));
        repository.save(new Tip(null, "Test3", "Desc3"));
    }
}
