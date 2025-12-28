package com.florarie.florarie.dto;

public class BouquetItem {
    private Long flowerId;
    private String name;
    private double price;
    private int quantity;

    public BouquetItem() { }

    public BouquetItem(Long flowerId, String name, double price, int quantity) {
        this.flowerId = flowerId;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getFlowerId() { return flowerId; }
    public void setFlowerId(Long flowerId) { this.flowerId = flowerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getLineTotal() {
        return price * quantity;
    }
}
