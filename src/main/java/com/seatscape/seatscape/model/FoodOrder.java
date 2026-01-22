package com.seatscape.seatscape.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "foodorders")
public class FoodOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "ticket_id")  
    private Integer ticketId;

    @ElementCollection
    @CollectionTable(name = "foodorder_items", joinColumns = @JoinColumn(name = "foodorder_id"))
    @Column(name = "item_id")
    private List<Integer> items;  // store item IDs

    private boolean paid;

    @Column(name = "total_price") 
    private int totalPrice;

    public FoodOrder() { }

    public FoodOrder(Integer ticketId, List<Integer> items, boolean paid, int totalPrice) {
        this.ticketId = ticketId;
        this.items = items;
        this.paid = paid;
        this.totalPrice = totalPrice;
    }

    // Getter and Setter methods
    public Integer getTicketId() { return ticketId; }
    public void setTicketId(Integer ticketId) { this.ticketId = ticketId; }

    public List<Integer> getItems() { return items; }
    public void setItems(List<Integer> items) { this.items = items; }

    public boolean isPaid() { return paid; }
    public void setPaid(boolean paid) { this.paid = paid; }

    public int getTotalPrice() { return totalPrice; }
    public void setTotalPrice(int totalPrice) { this.totalPrice = totalPrice; }
}
