package com.waracle.cakemgr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Collection;

@Configuration
@ConditionalOnProperty(value = "app.startup.enable", havingValue = "true", matchIfMissing = true)
class AppStartup {

    @Autowired
    private CakeService service;

    @Autowired
    private CakeRepository repository;

    @PostConstruct
    private void loadCakeData() {
        final Collection<Cake> cakes = service.fetchCakeData();
        repository.saveAll(cakes);
    }
}
