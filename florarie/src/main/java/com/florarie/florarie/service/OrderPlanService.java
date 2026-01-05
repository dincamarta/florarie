/** Serviciu pentru calcularea planificarii etapelor unei comenzi
 * @author Dinca (Mateas) Marta
 * @version 05 Ianuarie 2026
 */
package com.florarie.florarie.service;

import com.florarie.florarie.model.CustomerOrder;
import com.florarie.florarie.model.OrderItem;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class OrderPlanService {

    public record StepTimes(LocalDateTime startAt, LocalDateTime dueAt, Duration duration) {}

    public record PlanResult(Map<String, StepTimes> byStepName, boolean fitsInDeadlineDay, String warning) {}

    public PlanResult compute(CustomerOrder order) {
        if (order.getRequiredBy() == null) {
            return new PlanResult(Map.of(), true, "Comanda nu are deadline setat.");
        }

        int totalFlowers = 0;
        if (order.getItems() != null) {
            for (OrderItem it : order.getItems()) {
                totalFlowers += it.getQuantity();
            }
        }

        Duration prep = Duration.ofSeconds(10L * totalFlowers);
        Duration assemble = Duration.ofSeconds(5L * totalFlowers);
        Duration pack = Duration.ofMinutes(3);

        LocalDateTime deadline = order.getRequiredBy();

        // trebuie sa fie in aceeasi zi
        LocalDate day = deadline.toLocalDate();
        LocalDateTime dayStart = day.atStartOfDay();

        // calcule inapoi
        LocalDateTime packEnd = deadline;
        LocalDateTime packStart = packEnd.minus(pack);

        LocalDateTime assembleEnd = packStart;
        LocalDateTime assembleStart = assembleEnd.minus(assemble);

        LocalDateTime prepEnd = assembleStart;
        LocalDateTime prepStart = prepEnd.minus(prep);

        boolean fits = !prepStart.isBefore(dayStart);
        String warning = fits ? null :
                "Atenție: etapele nu încap în ziua deadline-ului. Alege un deadline mai târziu.";

        Map<String, StepTimes> map = new HashMap<>();
        map.put("Pregătire flori", new StepTimes(prepStart, prepEnd, prep));
        map.put("Asamblare buchet", new StepTimes(assembleStart, assembleEnd, assemble));
        map.put("Ambalare", new StepTimes(packStart, packEnd, pack));

        return new PlanResult(map, fits, warning);
    }
}
