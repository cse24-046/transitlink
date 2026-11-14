1.This project is a local academic/AWD practical project, not a production-ready system.

2.Database connection details are hardcoded in:    src/main/java/bac/ac/transitlink/util/DBConnection.java    JDBC URL: jdbc:mysql://localhost:3306/transitlink_db?useSSL=false&serverTimezone=UTC    USER: root    PASSWORD: bianca.1865

3.The app uses role-based access control:
◦PASSENGER users can search trips and book tickets.
◦ADMIN users can access the admin dashboard.

4.The RoleFilter restricts access to /admin/ and protected booking pages.

5.Booking transaction is atomic: the app checks seat availability and updates seats in one database transaction before inserting the booking record.

6.If the app is being run in a fresh environment, create the MySQL database and tables first.

7.Note: BookingServlet points to /WEB-INF/views/my-bookings.jsp, but the JSP file is not currently visible in this repo. Confirm the file exists before testing that route.

PROJECT STRUCTURE
src/main/java/bac/ac/transitlink/   
controller/     AuthServlet.java     TripServlet.java     BookingServlet.java     AdminDashboardServlet.java   
dao/     UserDAO.java     TripDAO.java   
filter/     RoleFilter.java   m
odel/     User.java     Trip.java     Booking.java   
util/     DBConnection.java
src/main/webapp/   index.jsp   
WEB-INF/views/     
login.jsp     trips.jsp     booking-success.jsp     admin-dashboard.jsp

FEATURES
1.Passenger Login and Registration
◦Login form is on the same page as registration.
◦AuthServlet handles /login and /register requests.
◦After successful login, the user is stored in the HTTP session as currentUser.

2.Search Available Trips
◦TripServlet handles /trips.
◦searchTrips() filters by origin and destination.
◦Only trips with available_seats > 0 appear in the list.

3.Ticket Booking
◦BookingServlet handles /book requests.
◦Trips are booked by user_id + trip_id + seats_requested.
◦The system checks seat count before booking and rejects if insufficient seats.

4.Admin Dashboard
◦Only ADMIN users can access /admin/dashboard.
◦Admin dashboard shows all trips and current seat counts.

5.Role Security
◦RoleFilter blocks unauthorized access to protected pages.

CORE CODE SNIPPETS
1)Database connection
package bac.ac.transitlink.util;
import java.sql.Connection; import java.sql.DriverManager; import java.sql.SQLException;
public class DBConnection {     private static final String URL = "jdbc:mysql://localhost:3306/transitlink_db?useSSL=false&serverTimezone=UTC";     private static final String USER = "root";     private static final String PASSWORD = "bianca.1865";
    public static Connection getConnection() throws SQLException {        try {            Class.forName("com.mysql.cj.jdbc.Driver");        } catch (ClassNotFoundException e) {            throw new SQLException("MySQL JDBC Driver not found.", e);        }        return DriverManager.getConnection(URL, USER, PASSWORD);    }
}

2)Login logic
if (user != null && user.getPassword().equals(password)) {     HttpSession session = request.getSession();     session.setAttribute("currentUser", user);
    if ("ADMIN".equalsIgnoreCase(user.getRole())) {        response.sendRedirect(request.getContextPath() + "/admin/dashboard");    } else {        response.sendRedirect(request.getContextPath() + "/trips");    }
} else {     request.setAttribute("errorMessage", "Invalid email or password.");     request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response); }

3)Trip search query
StringBuilder sql = new StringBuilder("SELECT * FROM trips WHERE available_seats > 0"); if (hasOrigin) {     sql.append(" AND origin LIKE ?"); } if (hasDest) {     sql.append(" AND destination LIKE ?"); }

4)Booking transaction logic
String checkSql = "SELECT available_seats, ticket_price FROM trips WHERE id = ?"; String updateSql = "UPDATE trips SET available_seats = available_seats - ? WHERE id = ? AND available_seats >= ?"; String insertSql = "INSERT INTO bookings (user_id, trip_id, seats_booked, total_amount) VALUES (?, ?, ?, ?)";
Connection conn = DBConnection.getConnection(); conn.setAutoCommit(false);
// validate seats // update trip inventory // insert booking // commit

5)Role security filter
@WebFilter(urlPatterns = {"/trips", "/book", "/my-bookings", "/admin/"}) public class RoleFilter implements Filter {     public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)             throws IOException, ServletException {         HttpSession session = req.getSession(false);         User user = (session != null) ? (User) session.getAttribute("currentUser") : null;
        if (user == null) {            res.sendRedirect(req.getContextPath() + "/login?error=unauthorized");            return;        }
        if (requestURI.contains("/admin/")) {            if (!"ADMIN".equalsIgnoreCase(user.getRole())) {                res.sendRedirect(req.getContextPath() + "/trips?error=forbidden");                return;            }        }        chain.doFilter(request, response);    }


SUGGESTED DATABASE SETUP (MYSQL)
CREATE DATABASE transitlink_db; USE transitlink_db;
CREATE TABLE users (     id INT PRIMARY KEY AUTO_INCREMENT,     email VARCHAR(150) NOT NULL UNIQUE,     full_name VARCHAR(150) NOT NULL,     password VARCHAR(255) NOT NULL,     role ENUM('PASSENGER', 'ADMIN') DEFAULT 'PASSENGER' );
CREATE TABLE trips (     id INT PRIMARY KEY AUTO_INCREMENT,     origin VARCHAR(100) NOT NULL,     destination VARCHAR(100) NOT NULL,     departure_datetime DATETIME NOT NULL,     bus_number VARCHAR(50) NOT NULL,     total_seats INT NOT NULL,     available_seats INT NOT NULL,     ticket_price DECIMAL(10,2) NOT NULL );
CREATE TABLE bookings (     id INT PRIMARY KEY AUTO_INCREMENT,     user_id INT NOT NULL,     trip_id INT NOT NULL,     seats_booked INT NOT NULL,     total_amount DECIMAL(10,2) NOT NULL,     booking_status VARCHAR(30) DEFAULT 'CONFIRMED',     booking_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,     FOREIGN KEY (user_id) REFERENCES users(id),     FOREIGN KEY (trip_id) REFERENCES trips(id) );
INSERT INTO users (email, full_name, password, role) VALUES ('admin@transitlink.com', 'System Admin', 'admin123', 'ADMIN');
INSERT INTO trips (origin, destination, departure_datetime, bus_number, total_seats, available_seats, ticket_price) VALUES ('Manila', 'Baguio', '2026-11-15 08:00:00', 'TX-201', 30, 12, 450.00), ('Manila', 'Cebu', '2026-11-15 09:30:00', 'TX-305', 28, 8, 980.00), ('Davao', 'Cagayan de Oro', '2026-11-15 10:15:00', 'DX-118', 25, 15, 520.00);

SQL QUERIES USED IN THE DATABASE
These are the core queries used by the app logic.
1.Find user by email SELECT * FROM users WHERE email = ?;

2.Register new passenger INSERT INTO users (email, full_name, password, role) VALUES (?, ?, ?, 'PASSENGER');

3.Search trips by origin and destination SELECT * FROM trips WHERE available_seats > 0 AND origin LIKE ? AND destination LIKE ?;

4.Check trip availability and price SELECT available_seats, ticket_price FROM trips WHERE id = ?;

5.Decrease available seat count after booking UPDATE trips SET available_seats = available_seats - ? WHERE id = ? AND available_seats >= ?;

6.Insert booking record INSERT INTO bookings (user_id, trip_id, seats_booked, total_amount) VALUES (?, ?, ?, ?);

7.Get all bookings for one user SELECT b., t.origin, t.destination, t.departure_datetime, t.bus_number FROM bookings b JOIN trips t ON b.trip_id = t.id WHERE b.user_id = ? ORDER BY b.booking_date DESC;

8.Admin view: Show all trips SELECT * FROM trips ORDER BY departure_datetime;

9.Common passenger search example SELECT * FROM trips WHERE available_seats > 0 AND origin LIKE '%Manila%' AND destination LIKE '%Baguio%';

10.Booking check example SELECT id, available_seats, ticket_price FROM trips WHERE id = 1;


QUICK REMINDER
This project is designed to demonstrate Java web basics, JDBC, role-based security, and transaction handling. For exams, focus on:
•DB connection logic,
•User authentication,
•transaction-safe booking,
•role filtering,
•JSP front-end flow,
•SQL query understanding.
