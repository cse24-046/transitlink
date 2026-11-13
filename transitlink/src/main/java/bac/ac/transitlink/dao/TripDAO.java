package bac.ac.transitlink.dao;

import bac.ac.transitlink.model.Booking;
import bac.ac.transitlink.model.Trip;
import bac.ac.transitlink.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TripDAO {

    public List<Trip> searchTrips(String origin, String destination) {
        List<Trip> list = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM trips WHERE available_seats > 0");

        boolean hasOrigin = (origin != null && !origin.trim().isEmpty() && !origin.equals("ALL"));
        boolean hasDest = (destination != null && !destination.trim().isEmpty() && !destination.equals("ALL"));

        if (hasOrigin) {
            sql.append(" AND origin LIKE ?");
        }
        if (hasDest) {
            sql.append(" AND destination LIKE ?");
        }

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {

            int idx = 1;
            if (hasOrigin) {
                stmt.setString(idx++, "%" + origin.trim() + "%");
            }
            if (hasDest) {
                stmt.setString(idx++, "%" + destination.trim() + "%");
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Trip t = new Trip();
                    t.setId(rs.getInt("id"));
                    t.setOrigin(rs.getString("origin"));
                    t.setDestination(rs.getString("destination"));
                    t.setDepartureDatetime(rs.getTimestamp("departure_datetime"));
                    t.setBusNumber(rs.getString("bus_number"));
                    t.setTotalSeats(rs.getInt("total_seats"));
                    t.setAvailableSeats(rs.getInt("available_seats"));
                    t.setTicketPrice(rs.getBigDecimal("ticket_price"));
                    list.add(t);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;

    }

    public boolean bookTrip(int userId, int tripId, int seatsRequested) {
        String checkSql = "SELECT available_seats, ticket_price FROM trips WHERE id = ?";
        String updateSql = "UPDATE trips SET available_seats = available_seats - ? WHERE id = ? AND available_seats >= ?";
        String insertSql = "INSERT INTO bookings (user_id, trip_id, seats_booked, total_amount) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            BigDecimal unitPrice;
            try (PreparedStatement pstCheck = conn.prepareStatement(checkSql)) {
                pstCheck.setInt(1, tripId);
                try (ResultSet rs = pstCheck.executeQuery()) {
                    if (!rs.next() || rs.getInt("available_seats") < seatsRequested) {
                        conn.rollback();
                        return false;
                    }
                    unitPrice = rs.getBigDecimal("ticket_price");
                }
            }

            try (PreparedStatement pstUpdate = conn.prepareStatement(updateSql)) {
                pstUpdate.setInt(1, seatsRequested);
                pstUpdate.setInt(2, tripId);
                pstUpdate.setInt(3, seatsRequested);
                if (pstUpdate.executeUpdate() == 0) {
                    conn.rollback();
                    return false;
                }
            }

            BigDecimal totalAmount = unitPrice.multiply(BigDecimal.valueOf(seatsRequested));
            try (PreparedStatement pstInsert = conn.prepareStatement(insertSql)) {
                pstInsert.setInt(1, userId);
                pstInsert.setInt(2, tripId);
                pstInsert.setInt(3, seatsRequested);
                pstInsert.setBigDecimal(4, totalAmount);
                pstInsert.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); } }
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) { try { conn.setAutoCommit(true); conn.close(); } catch (SQLException e) { e.printStackTrace(); } }
        }
    }

    public List<Booking> getUserBookings(int userId) {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT b.*, t.origin, t.destination, t.departure_datetime, t.bus_number " +
                "FROM bookings b JOIN trips t ON b.trip_id = t.id WHERE b.user_id = ? ORDER BY b.booking_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Booking bk = new Booking();
                    bk.setId(rs.getInt("id"));
                    bk.setUserId(rs.getInt("user_id"));
                    bk.setTripId(rs.getInt("trip_id"));
                    bk.setSeatsBooked(rs.getInt("seats_booked"));
                    bk.setTotalAmount(rs.getBigDecimal("total_amount"));
                    bk.setBookingStatus(rs.getString("booking_status"));
                    bk.setBookingDate(rs.getTimestamp("booking_date"));
                    bk.setOrigin(rs.getString("origin"));
                    bk.setDestination(rs.getString("destination"));
                    bk.setDepartureDatetime(rs.getTimestamp("departure_datetime"));
                    bk.setBusNumber(rs.getString("bus_number"));
                    list.add(bk);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}