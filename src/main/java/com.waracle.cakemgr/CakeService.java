package com.waracle.cakemgr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;


@Service
public class CakeService {

    private RestTemplate restTemplate;

    @Autowired
    private CakeJSONSourceProperties cakeJSONSourceProperties;

    CakeService() {
        final RestTemplateBuilder builder = new RestTemplateBuilder();

        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(singletonList(MediaType.ALL));

        this.restTemplate = builder.messageConverters(singletonList(converter)).build();
    }

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

    void setRestTemplate(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
