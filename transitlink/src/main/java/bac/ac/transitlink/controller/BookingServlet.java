package bac.ac.transitlink.controller;

import bac.ac.transitlink.dao.TripDAO;
import bac.ac.transitlink.model.Booking;
import bac.ac.transitlink.model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/book", "/my-bookings"})
public class BookingServlet extends HttpServlet {
    private TripDAO tripDAO;

    @Override
    public void init() { tripDAO = new TripDAO(); }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        List<Booking> bookings = tripDAO.getUserBookings(user.getId());
        request.setAttribute("bookings", bookings);
        request.getRequestDispatcher("/WEB-INF/views/my-bookings.jsp").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        User user = (session != null) ? (User) session.getAttribute("currentUser") : null;

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        int tripId = Integer.parseInt(request.getParameter("tripId"));
        int seats = Integer.parseInt(request.getParameter("seats"));

        boolean success = tripDAO.bookTrip(user.getId(), tripId, seats);
        if (success) {
            // Forward to dedicated success view
            request.getRequestDispatcher("/WEB-INF/views/booking-success.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/trips?error=true");
        }
    }
}