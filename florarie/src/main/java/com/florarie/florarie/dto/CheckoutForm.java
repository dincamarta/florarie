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
    @Pattern(regexp = "^[0-9]+$", message = "Telefonul trebuie să conțină doar cifre")
    @Size(min = 9, max = 15, message = "Telefonul trebuie să aibă între 9 și 15 cifre")
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
