package com.waracle.cakemgr;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CakeRestControllerMvcTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CakeRepository repository;

    @Test
    public void shouldReturnList() throws Exception {
        final List<Cake> cakes = repository.findAll();

        final String json = objectMapper.writeValueAsString(cakes);

        this.mockMvc.perform(get("/cakes"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(json));
    }

    @Test
    public void shouldReturnAnItem() throws Exception {
        final Cake cake = new Cake(1, "title1", "desc1", "image1");
        repository.save(cake);

        final String json = objectMapper.writeValueAsString(cake);

        this.mockMvc.perform(get("/cakes/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(json));
    }

    @Test
    public void shouldReturnEmpty() throws Exception {
        repository.deleteById(1);

        this.mockMvc.perform(get("/cakes/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("null"));
    }

    @Test
    public void shouldRemoveAnItem() throws Exception {
        final Cake cake = new Cake(2, "title2", "desc2", "image2");
        repository.save(cake);

        this.mockMvc.perform(delete("/cakes/2"))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(content().string("Cake details deleted successfully"));

        Assertions.assertEquals(Optional.empty(), repository.findById(2));
    }

    @Test
    public void shouldReturnNotFoundWhenRemovingNonExistentItem() throws Exception {
        if (repository.existsById(1)) {
            repository.deleteById(1);
        }

        this.mockMvc.perform(delete("/cakes/1"))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().string("Cake details not found by id: 1"));
    }

    @Test
    public void shouldCreateAnItemSuccessfully() throws Exception {
        final Cake cake = new Cake(null, "title999999", "desc999999", "image999999");
        final String json = objectMapper.writeValueAsString(cake);

        this.mockMvc.perform(post("/cakes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("Cake details created successfully"));

        final Cake cake1 = repository.findByTitle("title999999").get();
        Assertions.assertNotNull(cake1.getId());
        Assertions.assertEquals(cake.getTitle(), cake1.getTitle());
        Assertions.assertEquals(cake.getDesc(), cake1.getDesc());
        Assertions.assertEquals(cake.getImage(), cake1.getImage());
    }

    @Test
    public void shouldReturnBadRequestIfItemAlreadyExistsForPost() throws Exception {
        final Cake cake = new Cake(2, "title2", "desc2", "image2");
        repository.save(cake);

        final Cake cake1 = new Cake(null, "title2", "desc2", "image3");
        final String json = objectMapper.writeValueAsString(cake1);

        this.mockMvc.perform(post("/cakes")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error in creating Cake details as it already exists by title: title2"));

        final Cake cake2 = repository.findByTitle("title2").get();
        Assertions.assertEquals(cake, cake2);
    }

    @Test
    public void shouldUpdateAnItemSuccessfully() throws Exception {
        final Cake cake = new Cake(3, "title3", "desc3", "image3");
        repository.save(cake);

        final Cake cake1 = new Cake(3, "title4", "desc4", "image4");
        final String json = objectMapper.writeValueAsString(cake1);

        this.mockMvc.perform(put("/cakes")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(content().string("Cake details updated successfully"));

        final Cake cake2 = repository.findById(3).get();
        Assertions.assertEquals(cake1, cake2);
    }

    @Test
    public void shouldReturnBadRequestIfItemNotExistsForPut() throws Exception {
        if (repository.existsById(1)) {
            repository.deleteById(1);
        }

        final Cake cake1 = new Cake(1, "title3", "desc3", "image4");
        final String json = objectMapper.writeValueAsString(cake1);

        this.mockMvc.perform(put("/cakes")
                .contentType(MediaType.APPLICATION_JSON).content(json))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Error in updating Cake details as Cake details not found by id: 1"));

        Assertions.assertEquals(Optional.empty(), repository.findById(1));
    }
}
