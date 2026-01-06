/** Repository pentru gestionarea operatiilor cu baza de date pentru comenzi
 * @author Dinca (Mateas) Marta
 * @version 05 Ianuarie 2026
 */
package com.florarie.florarie.repository;

import com.florarie.florarie.model.CustomerOrder;
import com.florarie.florarie.model.AppUser;
import com.florarie.florarie.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<CustomerOrder, Long> {

    // pentru USER: doar comenzile lui
    List<CustomerOrder> findByUserOrderByCreatedAtDesc(AppUser user);

    // pentru ADMIN: filtrare dupa status
    List<CustomerOrder> findByStatus(OrderStatus status, Sort sort);

    // pentru ADMIN: comenzile intarziate (deadline trecut si nu sunt inchise)
    @Query("""
        select o from CustomerOrder o
        where o.requiredBy is not null
          and o.requiredBy < :now
          and o.status not in :closed
        order by o.requiredBy asc
    """)
    List<CustomerOrder> findOverdue(@Param("now") LocalDateTime now,
                                    @Param("closed") List<OrderStatus> closed);
}
