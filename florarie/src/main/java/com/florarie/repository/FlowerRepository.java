package com.florarie.repository;

import com.florarie.model.Florarie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlowerRepository extends JpaRepository<Florarie, Long> { }
