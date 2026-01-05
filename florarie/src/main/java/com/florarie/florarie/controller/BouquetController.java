/** Controller pentru gestionarea cosului de cumparaturi (buchetul)
 * @author Dinca (Mateas) Marta
 * @version 05 Ianuarie 2026
 */
package com.florarie.florarie.controller;

import com.florarie.florarie.dto.BouquetItem;
import com.florarie.florarie.model.Flower;
import com.florarie.florarie.repository.FlowerRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/bouquet")
public class BouquetController {

    private final FlowerRepository flowerRepository;

    public BouquetController(FlowerRepository flowerRepository) {
        this.flowerRepository = flowerRepository;
    }

    @SuppressWarnings("unchecked")
    private Map<Long, BouquetItem> getCart(HttpSession session) {
        Object obj = session.getAttribute("bouquet");
        if (obj == null) {
            Map<Long, BouquetItem> cart = new LinkedHashMap<>();
            session.setAttribute("bouquet", cart);
            return cart;
        }
        return (Map<Long, BouquetItem>) obj;
    }

    private void renderBouquet(HttpSession session, Model model, String errorMessage) {
        Map<Long, BouquetItem> cart = getCart(session);
        double total = cart.values().stream().mapToDouble(BouquetItem::getLineTotal).sum();
        model.addAttribute("items", cart.values());
        model.addAttribute("total", total);
        if (errorMessage != null) {
            model.addAttribute("error", errorMessage);
        }
    }

    @GetMapping
    public String view(HttpSession session, Model model) {
        renderBouquet(session, model, null);
        return "bouquet/view";
    }

    @GetMapping("/add/{id}")
    public String add(@PathVariable Long id, HttpSession session, Model model) {

        Flower flower = flowerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Floare inexistentă: " + id));

        // daca floarea nu e disponibila sau nu are stoc
        if (!flower.isAvailable() || flower.getStock() <= 0) {
            renderBouquet(session, model,
                    "Floarea \"" + flower.getName() + "\" nu este disponibilă în acest moment.");
            return "bouquet/view";
        }

        Map<Long, BouquetItem> cart = getCart(session);
        BouquetItem item = cart.get(id);

        int currentQty = (item == null) ? 0 : item.getQuantity();
        int newQty = currentQty + 1;

        // verifica stoc (tinand cont de cat ai deja in buchet)
        if (newQty > flower.getStock()) {
            renderBouquet(session, model,
                    "Nu sunt disponibile " + newQty + " buc. din \"" + flower.getName() +
                            "\". Stoc disponibil: " + flower.getStock() + ".");
            return "bouquet/view";
        }

        if (item == null) {
            cart.put(id, new BouquetItem(flower.getId(), flower.getName(), flower.getPrice(), 1));
        } else {
            item.setQuantity(newQty);
        }

        return "redirect:/bouquet";
    }

    @PostMapping("/update")
    public String update(@RequestParam Long flowerId,
                         @RequestParam String quantity,
                         HttpSession session,
                         Model model) {

        Map<Long, BouquetItem> cart = getCart(session);

        // Validare cantitate - trebuie să fie număr întreg pozitiv
        int qty;
        try {
            qty = Integer.parseInt(quantity);
        } catch (NumberFormatException e) {
            renderBouquet(session, model, "Cantitatea trebuie să fie un număr întreg (ex: 1, 2, 3). Nu sunt permise numere cu virgulă.");
            return "bouquet/view";
        }

        // daca quantity <=0 => sterge produsul din buchet
        if (qty <= 0) {
            cart.remove(flowerId);
            return "redirect:/bouquet";
        }

        Flower flower = flowerRepository.findById(flowerId)
                .orElseThrow(() -> new IllegalArgumentException("Floare inexistentă: " + flowerId));

        if (!flower.isAvailable()) {
            renderBouquet(session, model,
                    "Floarea \"" + flower.getName() + "\" nu este disponibilă.");
            return "bouquet/view";
        }

        if (qty > flower.getStock()) {
            renderBouquet(session, model,
                    "Ai cerut " + qty + " buc. din \"" + flower.getName() +
                            "\", dar stocul este " + flower.getStock() +
                            ". Alege o cantitate mai mică.");
            return "bouquet/view";
        }

        BouquetItem item = cart.get(flowerId);
        if (item != null) {
            item.setQuantity(qty);
        }

        return "redirect:/bouquet";
    }

    @GetMapping("/remove/{id}")
    public String confirmRemove(@PathVariable Long id, HttpSession session, Model model) {
        Map<Long, BouquetItem> cart = getCart(session);
        BouquetItem item = cart.get(id);
        
        if (item == null) {
            return "redirect:/bouquet";
        }
        
        model.addAttribute("item", item);
        return "bouquet/confirm-remove";
    }

    @PostMapping("/remove/{id}")
    public String remove(@PathVariable Long id, HttpSession session) {
        Map<Long, BouquetItem> cart = getCart(session);
        cart.remove(id);
        return "redirect:/bouquet";
    }
}
