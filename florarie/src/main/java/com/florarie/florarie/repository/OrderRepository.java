package com.florarie.florarie.repository;

import com.florarie.florarie.model.CustomerOrder;
import com.florarie.florarie.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findByUserOrderByCreatedAtDesc(AppUser user);
}
