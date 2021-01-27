package com.waracle.cakemgr;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories
@EnableConfigurationProperties(CakeJSONSourceProperties.class)
class AppConfig {

}
