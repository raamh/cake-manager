package com.waracle.cakemgr;

import com.sun.istack.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cakes")
class CakeController {

    @Autowired
    private CakeService service;

    @Autowired
    private CakeRepository repository;

    @GetMapping
    List<Cake> getList() {
        return repository.findAll();
    }

    @GetMapping("/{cakeId}")
    Optional<Cake> getById(@PathVariable @NotNull final Integer cakeId) {
        return repository.findById(cakeId);
    }

    @DeleteMapping("/{cakeId}")
    ResponseEntity<String> removeById(@PathVariable @NotNull final Integer cakeId) {
        if (repository.existsById(cakeId)) {
            repository.deleteById(cakeId);
            return new ResponseEntity<>("Cake details deleted successfully", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("Cake details not found by id: " + cakeId, HttpStatus.NOT_FOUND);
    }

    @PostMapping
    ResponseEntity<String> create(@RequestBody @NotNull final Cake cake) {
        if (!repository.findByTitle(cake.getTitle()).isPresent()) {
            repository.save(cake);
            return new ResponseEntity<>("Cake details created successfully", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("Error in creating Cake details as it already exists by title: " + cake.getTitle(), HttpStatus.BAD_REQUEST);
    }

    @PutMapping
    ResponseEntity<String> update(@RequestBody @NotNull final Cake cake) {
        if (repository.existsById(cake.getId())) {
            repository.save(cake);
            return new ResponseEntity<>("Cake details updated successfully", HttpStatus.ACCEPTED);
        }
        return new ResponseEntity<>("Error in updating Cake details as Cake details not found by id: " + cake.getId(), HttpStatus.BAD_REQUEST);
    }

}
