package pl.piomin.samples.springboot.tips.data.model;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "person")
public record Person2(String name, int age) { }
