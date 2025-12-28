package com.florarie.florarie.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CheckoutForm {

    @NotBlank(message = "Adresa este obligatorie")
    @Size(max = 200)
    private String deliveryAddress;

    @NotBlank(message = "Telefonul este obligatoriu")
    @Size(max = 30)
    private String phone;

    @Size(max = 500)
    private String notes;

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
