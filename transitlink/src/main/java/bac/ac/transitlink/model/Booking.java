package bac.ac.transitlink.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Booking {
    private int id;
    private int userId;
    private int tripId;
    private int seatsBooked;
    private BigDecimal totalAmount;
    private String bookingStatus;
    private Timestamp bookingDate;

    // Joined fields for display
    private String origin;
    private String destination;
    private Timestamp departureDatetime;
    private String busNumber;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public int getTripId() { return tripId; }
    public void setTripId(int tripId) { this.tripId = tripId; }
    public int getSeatsBooked() { return seatsBooked; }
    public void setSeatsBooked(int seatsBooked) { this.seatsBooked = seatsBooked; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }
    public Timestamp getBookingDate() { return bookingDate; }
    public void setBookingDate(Timestamp bookingDate) { this.bookingDate = bookingDate; }
    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public Timestamp getDepartureDatetime() { return departureDatetime; }
    public void setDepartureDatetime(Timestamp departureDatetime) { this.departureDatetime = departureDatetime; }
    public String getBusNumber() { return busNumber; }
    public void setBusNumber(String busNumber) { this.busNumber = busNumber; }
}