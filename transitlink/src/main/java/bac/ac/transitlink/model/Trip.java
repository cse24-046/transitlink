package bac.ac.transitlink.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Trip {
    private int id;
    private String origin;
    private String destination;
    private Timestamp departureDatetime;
    private String busNumber;
    private int totalSeats;
    private int availableSeats;
    private BigDecimal ticketPrice;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public Timestamp getDepartureDatetime() { return departureDatetime; }
    public void setDepartureDatetime(Timestamp departureDatetime) { this.departureDatetime = departureDatetime; }
    public String getBusNumber() { return busNumber; }
    public void setBusNumber(String busNumber) { this.busNumber = busNumber; }
    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }
    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
    public BigDecimal getTicketPrice() { return ticketPrice; }
    public void setTicketPrice(BigDecimal ticketPrice) { this.ticketPrice = ticketPrice; }
}