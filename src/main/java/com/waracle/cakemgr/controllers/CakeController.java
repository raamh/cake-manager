package com.waracle.cakemgr.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.waracle.cakemgr.Cake;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static java.util.Arrays.asList;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentContextPath;

@Controller
class CakeController {

    private RestTemplate restTemplate;

    CakeController() {
        this.restTemplate = new RestTemplateBuilder().build();
    }

    @RequestMapping(method = RequestMethod.GET)
    String getListOfCakes(final Model model) {
        final String url = fromCurrentContextPath().path("/cakes").toUriString();
        final ResponseEntity<Cake[]> responseEntity = restTemplate.getForEntity(url, Cake[].class);
        model.addAttribute("cakes", asList(responseEntity.getBody()));
        return "cake-list";
    }

    @RequestMapping(value = "/add-cake", method = RequestMethod.GET)
    String getAddCakeForm(final Model model) {
        model.addAttribute("cake", new Cake());
        return "add-cake";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    String createCake(@ModelAttribute final Cake cake, final Model model) {
        final String url = fromCurrentContextPath().path("/cakes").toUriString();

        try {
            final ObjectMapper objectMapper = new ObjectMapper();
            final String cakeJSON = objectMapper.writeValueAsString(cake);
            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            final HttpEntity<String> requestEntity = new HttpEntity<>(cakeJSON, headers);
            final ResponseEntity<String> response = restTemplate.postForEntity(url, requestEntity, String.class);
            if (response.getStatusCode().equals(HttpStatus.CREATED)) {
                return getListOfCakes(model);
            }
        } catch (HttpClientErrorException ex) {
            model.addAttribute("errorMessage", ex.getMessage());
        } catch (JsonProcessingException ex) {
            model.addAttribute("errorMessage", "Error in reading POST request data");
        }

        model.addAttribute("cake", cake);

        return "add-cake";
    }

    void setRestTemplate(final RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
}
