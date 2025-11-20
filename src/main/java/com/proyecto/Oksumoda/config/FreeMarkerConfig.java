package com.proyecto.Oksumoda.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

@Configuration
public class FreeMarkerConfig {

    @Bean
    public FreeMarkerConfigurer freeMarkerConfigurer() {
        FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
        // Las plantillas FreeMarker estarán en src/main/resources/templates/reportes/
        configurer.setTemplateLoaderPath("classpath:/templates/reportes/");
        configurer.setDefaultEncoding("UTF-8");
        return configurer;
    }
}