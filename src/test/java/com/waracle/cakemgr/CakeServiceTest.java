package com.waracle.cakemgr;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CakeService.class)
public class CakeServiceTest {

    @Autowired
    private CakeService service;

    @MockBean
    private CakeJSONSourceProperties properties;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    public void testFetchCakeData_ReturnsData() {
        when(properties.getUrl()).thenReturn("http://test");
        final Cake[] cakes = {
                new Cake(null, "title1", "desc1", "image1"),
                new Cake(null, "title2", "desc2", "image2"),
                new Cake(null, "title3", "desc3", "image3")
        };
        final Collection<Cake> cakes1 = Arrays.stream(cakes).collect(Collectors.toMap(Cake::getTitle, cake -> cake)).values();
        when(restTemplate.getForEntity(properties.getUrl(), Cake[].class)).thenReturn(new ResponseEntity<>(cakes, HttpStatus.OK));

        final Collection<Cake> cakes2 = service.fetchCakeData();

        Assertions.assertEquals(new ArrayList<>(cakes1), new ArrayList<>(cakes2));
    }

    @Test
    public void testFetchCakeData_ReturnsNoData() {
        when(properties.getUrl()).thenReturn("http://test");
        final Cake[] cakes = {
        };
        when(restTemplate.getForEntity(properties.getUrl(), Cake[].class)).thenReturn(new ResponseEntity<>(cakes, HttpStatus.OK));

        final Collection<Cake> cakes2 = service.fetchCakeData();

        Assertions.assertEquals(emptyList(), new ArrayList<>(cakes2));
    }

    @Test
    public void testFetchCakeData_ThrowsRemoteError() {
        when(properties.getUrl()).thenReturn("http://test");
        final RuntimeException exception = new RuntimeException("something");
        when(restTemplate.getForEntity(properties.getUrl(), Cake[].class)).thenThrow(exception);

        try {
            service.fetchCakeData();
        } catch (RuntimeException ex) {
            Assertions.assertEquals(exception, ex);
        }
    }
}
