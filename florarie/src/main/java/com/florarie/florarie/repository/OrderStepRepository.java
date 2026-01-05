/** Repository pentru gestionarea operatiilor cu baza de date pentru etapele comenzilor
 * @author Dinca (Mateas) Marta
 * @version 05 Ianuarie 2026
 */
package com.florarie.florarie.repository;

import com.florarie.florarie.model.OrderStep;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderStepRepository extends JpaRepository<OrderStep, Long> {
    List<OrderStep> findByOrderIdOrderBySortOrderAsc(Long orderId);
    boolean existsByOrderId(Long orderId);
}
