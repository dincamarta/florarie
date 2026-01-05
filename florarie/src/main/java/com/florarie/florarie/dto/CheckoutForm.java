/** Clasa DTO pentru formularul de finalizare comanda
 * @author Dinca (Mateas) Marta
 * @version 05 Ianuarie 2026
 */
package com.florarie.florarie.dto;

import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

public class CheckoutForm {

    @NotBlank(message = "Adresa este obligatorie")
    @Size(min = 5, max = 200, message = "Adresa trebuie să aibă între 5 și 200 caractere")
    @Pattern(
            regexp = "^[\\p{L}\\d. ]+$",
            message = "Adresa poate conține doar litere, cifre și caracterul '.'"
    )
    private String deliveryAddress;

    @NotBlank(message = "Telefonul este obligatoriu")
    @Pattern(regexp = "^0[0-9]{9}$", message = "Telefonul trebuie să înceapă cu 0 și să aibă exact 10 cifre (ex: 0712345678)")
    private String phone;

    @Size(max = 500)
    private String notes;

    @NotNull(message = "Alege deadline-ul")
    @Future(message = "Deadline-ul trebuie să fie în viitor")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime requiredBy;

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDateTime getRequiredBy() { return requiredBy; }
    public void setRequiredBy(LocalDateTime requiredBy) { this.requiredBy = requiredBy; }
}
