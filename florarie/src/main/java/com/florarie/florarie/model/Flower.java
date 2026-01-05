package com.florarie.florarie.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "flowers")
public class Flower {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Numele florii este obligatoriu")
    @Size(min = 2, max = 200, message = "Floarea trebuie să aibă între 2 și 200 caractere")
    @Pattern(
            regexp = "^[\\p{L}\\d. ]+$",
            message = "Numele florii poate conține doar litere, cifre și caracterul '.'"
    )
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Culoarea este obligatorie")
    @Size(max = 50, message = "Culoarea trebuie să aibă max 50 caractere")
    @Pattern(
            regexp = "^[\\p{L}\\d- ]+$",
            message = "Culoarea florii poate conține doar litere, cifre și caracterul '-'"
    )
    @Column(nullable = false, length = 50)
    private String color;

    @NotNull(message = "Prețul este obligatoriu")
    @Positive(message = "Prețul trebuie să fie > 0")
    @Column(nullable = false)
    private Double price;

    @NotNull(message = "Stocul este obligatoriu")
    @Min(value = 0, message = "Stocul trebuie să fie >= 0")
    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private boolean available = true;

    public Flower() { }

    // ===== getters/setters =====

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
