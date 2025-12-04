package pl.piomin.samples.springboot.tips;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.web.service.registry.ImportHttpServices;
import pl.piomin.samples.springboot.tips.config.TipsAppProperties;
import pl.piomin.samples.springboot.tips.data.model.Person2;

import java.util.Optional;

@SpringBootApplication
@EnableConfigurationProperties({TipsAppProperties.class, Person2.class})
//@ImportHttpServices(group = "local", types = PersonService.class)
@ImportHttpServices(group = "tips", basePackages = "pl.piomin.samples.springboot.tips.client")
public class TipsApp {

    private static final Logger LOG = LoggerFactory.getLogger(TipsApp.class);

    public static void main(String[] args) {
        SpringApplication.run(TipsApp.class, args);
    }

    @Autowired
    private TipsAppProperties properties;
    @Autowired
    private Optional<GitProperties> git;
    @Autowired
    private Optional<BuildProperties> buildProperties;

    @PostConstruct
    void init() {
        LOG.info("properties: {}", properties);
        git.ifPresent(entries -> LOG.info("Git data: {} {}", entries.getCommitId(), entries.getCommitTime()));
    }

}
