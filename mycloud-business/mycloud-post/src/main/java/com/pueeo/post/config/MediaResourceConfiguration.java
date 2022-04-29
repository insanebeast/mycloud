package com.pueeo.post.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Data
@Component
@ConfigurationProperties(prefix = "media")
public class MediaResourceConfiguration {
    private String video;
    private String image;
}
