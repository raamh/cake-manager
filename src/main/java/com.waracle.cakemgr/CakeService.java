package com.waracle.cakemgr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;


@Service
class CakeService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CakeJSONSourceProperties cakeJSONSourceProperties;

    Collection<Cake> fetchCakeData() {
        final ResponseEntity<Cake[]> responseEntity = restTemplate.getForEntity(cakeJSONSourceProperties.getUrl(), Cake[].class);
        final Cake[] array = responseEntity.getBody();

        // Get the collection of cakes by unique titles
        final Collection<Cake> cakes = Arrays.stream(array)
                .collect(Collectors.toMap(Cake::getTitle, cake -> cake,
                        (cake1, cake2) -> cake1))
                .values();

        return cakes;
    }

}
