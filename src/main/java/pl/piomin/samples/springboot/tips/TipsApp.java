package pl.piomin.samples.springboot.tips;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import pl.piomin.samples.springboot.tips.config.TipsAppProperties;
import pl.piomin.samples.springboot.tips.data.model.Person2;

import javax.annotation.PostConstruct;
import java.util.Optional;

@SpringBootApplication
@EnableConfigurationProperties(TipsAppProperties.class)
@Slf4j
//@EntityScan("pl.piomin.samples.springboot.tips.data.model")
public class TipsApp {

    public static void main(String[] args) {
        SpringApplication.run(TipsApp.class, args);
    }

    @Autowired
    private TipsAppProperties properties;
    @Autowired
    private Optional<GitProperties> git;
    @Autowired
    private Optional<BuildProperties> buildProperties;
//    @Value("${maven.app}")
//    private String name;
//    @Value("${project.artifactId}")
//    private String xname;

    @PostConstruct
    void init() {
        log.info("properties: {}", properties);
//        log.info("Maven properties: {}, {}", buildProperties.getArtifact(), buildProperties.getVersion());
//        log.info("properties name: {}", name);
//        log.info("properties xname: {}", xname);
        if (git.isPresent()) {
            log.info("Git data: {} {}", git.get().getCommitId(), git.get().getCommitTime());
        }
    }

}
