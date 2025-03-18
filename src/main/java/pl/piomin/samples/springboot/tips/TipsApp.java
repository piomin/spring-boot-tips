package pl.piomin.samples.springboot.tips;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import pl.piomin.samples.springboot.tips.config.TipsAppProperties;
import pl.piomin.samples.springboot.tips.controller.TipController;
import pl.piomin.samples.springboot.tips.data.model.Person2;

import java.util.Optional;

@SpringBootApplication
@EnableConfigurationProperties({TipsAppProperties.class, Person2.class})
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
        if (git.isPresent()) {
            LOG.info("Git data: {} {}", git.get().getCommitId(), git.get().getCommitTime());
        }
    }

}
