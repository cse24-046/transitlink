package bac.ac.transitlink.filter;

import bac.ac.transitlink.model.User;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter(urlPatterns = {"/trips", "/book", "/my-bookings", "/admin/*"})
public class RoleFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        HttpSession session = req.getSession(false);

        User user = (session != null) ? (User) session.getAttribute("currentUser") : null;
        String requestURI = req.getRequestURI();

        // 1. If not logged in at all, redirect to login page
        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/login?error=unauthorized");
            return;
        }

        // 2. If trying to access any URL containing /admin/, check if user has ADMIN role
        if (requestURI.contains("/admin/")) {
            if (!"ADMIN".equalsIgnoreCase(user.getRole())) {
                // If a passenger tries to access admin tools, block them and send them back to trips
                res.sendRedirect(req.getContextPath() + "/trips?error=forbidden");
                return;
            }
        }

        // 3. Otherwise, allow the request to proceed normally
        chain.doFilter(request, response);
    }
}