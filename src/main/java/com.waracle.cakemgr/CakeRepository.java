package com.waracle.cakemgr;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CakeRepository extends JpaRepository<Cake, Integer> {

    Optional<Cake> findByTitle(String title);
}
