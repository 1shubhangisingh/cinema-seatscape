package com.seatscape.seatscape.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.Arrays;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ticketId;
    @NotNull(message = "showId cannot be null")
    private Integer showId;
    @NotNull(message = "numberOfSeats cannot be null")
    private Integer numberOfSeats;
    
    private String bookedBy;

    @Column(name = "bookedseats")
    private String bookedSeats;

    public Ticket(Integer showId, Integer numberOfSeats, String bookedBy, String bookedSeats) {
        this.showId = showId;
        this.numberOfSeats = numberOfSeats;
        this.bookedBy = bookedBy;
        this.bookedSeats = bookedSeats;
    }

    @Override
    public String toString() {
        return "Ticket{" +
                "ticketId=" + ticketId +
                ", showId=" + showId +
                ", numberOfSeats=" + numberOfSeats +
                ", bookedBy='" + bookedBy + '\'' +
                ", bookedSeats=" + bookedSeats +
                '}';
    }

    // Getters & Setters
    public Integer getTicketId() {
        return ticketId;
    }

    public void setTicketId(Integer ticketId) {
        this.ticketId = ticketId;
    }

    public Integer getShowId() {
        return showId;
    }

    public void setShowId(Integer showId) {
        this.showId = showId;
    }

    public Integer getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(Integer numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public String getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(String bookedBy) {
        this.bookedBy = bookedBy;
    }

    public String getBookedSeats() {
        return bookedSeats;
    }

    public void setBookedSeats(String bookedSeats) {
        this.bookedSeats = bookedSeats;
    }
}
