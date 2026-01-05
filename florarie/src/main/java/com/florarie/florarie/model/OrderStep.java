package com.florarie.florarie.model;

import jakarta.persistence.*;

@Entity
@Table(name = "order_steps")
public class OrderStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private CustomerOrder order;

    @Column(nullable = false, length = 80)
    private String name; // ex: "Pregătire flori"

    @Column(nullable = false)
    private boolean done = false;

    @Column(nullable = false)
    private int sortOrder;

    public OrderStep() {}

    public OrderStep(String name, int sortOrder) {
        this.name = name;
        this.sortOrder = sortOrder;
    }

    public Long getId() { return id; }

    public CustomerOrder getOrder() { return order; }
    public void setOrder(CustomerOrder order) { this.order = order; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public boolean isDone() { return done; }
    public void setDone(boolean done) { this.done = done; }

    public int getSortOrder() { return sortOrder; }
    public void setSortOrder(int sortOrder) { this.sortOrder = sortOrder; }
}
