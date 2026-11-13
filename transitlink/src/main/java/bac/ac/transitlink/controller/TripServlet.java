package bac.ac.transitlink.controller;

import bac.ac.transitlink.dao.TripDAO;
import bac.ac.transitlink.model.Trip;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/trips")
public class TripServlet extends HttpServlet {
    private TripDAO tripDAO;

    @Override
    public void init() { tripDAO = new TripDAO(); }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String origin = request.getParameter("origin");
        String destination = request.getParameter("destination");

        List<Trip> trips = tripDAO.searchTrips(origin, destination);
        request.setAttribute("trips", trips);
        request.getRequestDispatcher("/WEB-INF/views/trips.jsp").forward(request, response);
    }
}