package com.bambino.shared.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;

@Configuration
public class UploadResourceConfig implements WebMvcConfigurer {

    private final String uploadDir;
    private final long cacheMaxAgeSeconds;

    public UploadResourceConfig(
        @Value("${app.upload.dir:${UPLOAD_DIR:/app/uploads}}") String uploadDir,
        @Value("${app.upload.cache.max-age-seconds:2592000}") long cacheMaxAgeSeconds
    ) {
        this.uploadDir = uploadDir;
        this.cacheMaxAgeSeconds = Math.max(cacheMaxAgeSeconds, 0);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path root = Paths.get(uploadDir).toAbsolutePath().normalize();
        String location = root.toUri().toString();
        if (!location.endsWith("/")) {
            location = location + "/";
        }
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(location)
                .setCacheControl(CacheControl.maxAge(Duration.ofSeconds(cacheMaxAgeSeconds)).cachePublic());
    }
}
