package com.florarie.florarie.repository;

import com.florarie.florarie.model.Flower;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlowerRepository extends JpaRepository<Flower, Long> {
}
