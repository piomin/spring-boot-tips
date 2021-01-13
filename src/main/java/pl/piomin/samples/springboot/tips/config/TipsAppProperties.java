package pl.piomin.samples.springboot.tips.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties("app")
@AllArgsConstructor
@Getter
@ToString
public class TipsAppProperties {
    private final String name;
    private final String version;
}
