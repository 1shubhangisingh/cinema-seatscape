package com.seatscape.seatscape.service;

import com.seatscape.seatscape.dao.TicketDAO;
import com.seatscape.seatscape.dao.ShowDAO;
import com.seatscape.seatscape.exceptions.*;
import com.seatscape.seatscape.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TicketService {

    @Autowired
    private TicketDAO ticketDAO;

    @Autowired
    private ShowDAO showDAO;

    private final Object lock = new Object();

    /* ================= HELPER ================= */
    private int[] parseSeats(String seats) {
        if (seats == null || seats.trim().isEmpty()) {
            return new int[0];
        }
        return Arrays.stream(seats.split(","))
                .map(String::trim)
                .mapToInt(Integer::parseInt)
                .toArray();
    }

    /* ================= CREATE TICKET ================= */
    public ResponseEntity<Optional<Ticket>> createTicket(Ticket ticket)
            throws HouseFullException, InsufficientTicketsException,
            SeatAlreadyBookedException, CountMismatchException,
            CountOfSeatsZero, TooManySeatsException {

        int numSeats = ticket.getNumberOfSeats();

        if (numSeats == 0)
            throw new CountOfSeatsZero("The count of tickets is Zero.");

        if (numSeats > 9)
            throw new TooManySeatsException("The number of seats is too high.");

        Integer availableSeatsObj = showDAO.getAvailableSeatsFromShowId(ticket.getShowId());
        if (availableSeatsObj == null)
            throw new RuntimeException("Show not found");

        int availableSeats = availableSeatsObj;

        if (availableSeats == 0)
            throw new HouseFullException("The show is housefull.");

        if (availableSeats < numSeats)
            throw new InsufficientTicketsException("Only " + availableSeats + " tickets are available.");

        int[] requestedSeats = parseSeats(ticket.getBookedSeats());

        if (numSeats != requestedSeats.length)
            throw new CountMismatchException("Seat count mismatch.");

        String bookedSeatsStr = showDAO.getBookedSeatsByShowId(ticket.getShowId());
        if (bookedSeatsStr == null || bookedSeatsStr.isEmpty()) {
            bookedSeatsStr = "0".repeat(100); // assume 100 seats
        }

        StringBuilder sb = new StringBuilder(bookedSeatsStr);

        for (int seatId : requestedSeats) {
            if (seatId < 0 || seatId >= sb.length() / 2)
                throw new SeatAlreadyBookedException("Invalid seat " + seatId);

            if (sb.charAt(seatId * 2) != '0')
                throw new SeatAlreadyBookedException("Seat already booked " + seatId);

            sb.setCharAt(seatId * 2, '1');
        }

        try {
            synchronized (lock) {
                showDAO.updateSeats(ticket.getShowId(), availableSeats - numSeats);
                showDAO.setBookedSeats(sb.toString(), ticket.getShowId());
                ticketDAO.save(ticket);
                return new ResponseEntity<>(Optional.of(ticket), HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    /* ================= GET APIs ================= */
    public ResponseEntity<List<Ticket>> getAllByShowId(Integer showId) {
        return new ResponseEntity<>(ticketDAO.getAllByShowId(showId), HttpStatus.OK);
    }

    public ResponseEntity<Optional<Ticket>> getTicketByTicketId(Integer ticketId) {
        return new ResponseEntity<>(
                Optional.ofNullable(ticketDAO.getTicketByTicketId(ticketId)),
                HttpStatus.OK
        );
    }

    public ResponseEntity<List<Ticket>> getTicketsByUsername(String username) {
        return new ResponseEntity<>(ticketDAO.getTicketsByUsername(username), HttpStatus.OK);
    }

    public ResponseEntity<List<Ticket>> getAllTickets() {
        return new ResponseEntity<>(ticketDAO.findAll(), HttpStatus.OK);
    }

    /* ================= CANCEL TICKET ================= */
    public ResponseEntity<String> cancelTicket(Integer id)
            throws TicketDoesNotExistException, SeatsAreInconsistentStateException {

        Ticket ticket = ticketDAO.findById(id)
                .orElseThrow(() -> new TicketDoesNotExistException("Ticket not found"));

        int showId = ticket.getShowId();
        int numSeats = ticket.getNumberOfSeats();
        int[] seatsToFree = parseSeats(ticket.getBookedSeats());

        String bookedSeatsStr = showDAO.getBookedSeatsByShowId(showId);
        StringBuilder sb = new StringBuilder(bookedSeatsStr);

        for (int seat : seatsToFree) {
            if (seat < 0 || seat >= sb.length() / 2 || sb.charAt(seat * 2) != '1')
                throw new SeatsAreInconsistentStateException("Seat state inconsistent");
            sb.setCharAt(seat * 2, '0');
        }

        synchronized (lock) {
            ticketDAO.deleteById(id);
            showDAO.updateSeats(showId, showDAO.getAvailableSeatsFromShowId(showId) + numSeats);
            showDAO.setBookedSeats(sb.toString(), showId);
        }
        return new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    }

    /* ================= PARTIAL CANCELLATION ================= */
    public ResponseEntity<String> partialCancellation(Ticket ticket) throws InvalidTicketException {

        Ticket dbTicket = ticketDAO.findById(ticket.getTicketId())
                .orElseThrow(() -> new InvalidTicketException("Invalid ticket"));

        int[] seatsToCancel = parseSeats(ticket.getBookedSeats());
        int[] bookedSeats = parseSeats(dbTicket.getBookedSeats());
        Arrays.sort(bookedSeats);

        String bookedSeatsStr = showDAO.getBookedSeatsByShowId(ticket.getShowId());
        StringBuilder sb = new StringBuilder(bookedSeatsStr);

        for (int seat : seatsToCancel) {
            if (Arrays.binarySearch(bookedSeats, seat) < 0)
                return new ResponseEntity<>("Seat not booked by you", HttpStatus.BAD_REQUEST);
            sb.setCharAt(seat * 2, '0');
        }

        List<Integer> remaining = new ArrayList<>();
        for (int seat : bookedSeats) {
            if (Arrays.binarySearch(seatsToCancel, seat) < 0)
                remaining.add(seat);
        }

        String remainingSeatsStr = String.join(",",
                remaining.stream().map(String::valueOf).toList());

        synchronized (lock) {
            showDAO.updateSeats(ticket.getShowId(),
                    showDAO.getAvailableSeatsFromShowId(ticket.getShowId()) + seatsToCancel.length);
            showDAO.setBookedSeats(sb.toString(), ticket.getShowId());
            ticketDAO.updateBookedSeats(remainingSeatsStr, dbTicket.getTicketId());
            ticketDAO.setTicketCount(remaining.size(), dbTicket.getTicketId());
        }

        return new ResponseEntity<>("SUCCESSFULLY CANCELLED", HttpStatus.OK);
    }

    /* ================= AVAILABLE SEATS ================= */
    public ResponseEntity<List<Integer>> getAvailableSeatsForShowId(Integer showId) {

        String bookedSeats = showDAO.getBookedSeatsByShowId(showId);
        List<Integer> availableSeats = new ArrayList<>();

        for (int i = 0; i < bookedSeats.length() / 2; i++) {
            if (bookedSeats.charAt(i * 2) == '0')
                availableSeats.add(i);
        }
        return new ResponseEntity<>(availableSeats, HttpStatus.OK);
    }
}
