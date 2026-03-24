package pl.piomin.samples.springboot.tips.hints;

import java.io.IOException;
import org.springframework.aot.hint.MemberCategory;
import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;
import org.springframework.aot.hint.TypeReference;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;

public class NativeResourceHints implements RuntimeHintsRegistrar {

    private static final String[] HIBERNATE_GENERATED_LOGGER_PATTERNS = {
            "classpath*:org/hibernate/**/*_$logger.class",
            "classpath*:org/hibernate/**/*_$bundle.class"
    };
    private static final String HIBERNATE_EVENT_LISTENER_PATTERN = "classpath*:org/hibernate/event/spi/*Listener.class";

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
        hints.resources().registerPattern("db/changeLog.sql");
        hints.resources().registerPattern("data.sql");
        registerHibernateGeneratedLoggers(hints, classLoader);
        registerHibernateEventListenerArrays(hints, classLoader);
    }

    private void registerHibernateGeneratedLoggers(RuntimeHints hints, ClassLoader classLoader) {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(classLoader);
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resolver);
        for (String pattern : HIBERNATE_GENERATED_LOGGER_PATTERNS) {
            try {
                for (Resource resource : resolver.getResources(pattern)) {
                    String className = metadataReaderFactory.getMetadataReader(resource).getClassMetadata().getClassName();
                    hints.reflection().registerType(TypeReference.of(className), MemberCategory.INVOKE_DECLARED_CONSTRUCTORS);
                }
            } catch (IOException ex) {
                throw new IllegalStateException("Failed to scan Hibernate generated logger classes", ex);
            }
        }
    }

    private void registerHibernateEventListenerArrays(RuntimeHints hints, ClassLoader classLoader) {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(classLoader);
        MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory(resolver);
        try {
            for (Resource resource : resolver.getResources(HIBERNATE_EVENT_LISTENER_PATTERN)) {
                String className = metadataReaderFactory.getMetadataReader(resource).getClassMetadata().getClassName();
                hints.reflection().registerType(TypeReference.of(className + "[]"));
            }
        } catch (IOException ex) {
            throw new IllegalStateException("Failed to scan Hibernate event listener classes", ex);
        }
    }
}
