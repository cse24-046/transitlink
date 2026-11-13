<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>TransitLink - Admin Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<nav class="navbar navbar-dark bg-danger mb-4">
    <div class="container">
        <a class="navbar-brand fw-bold" href="#">TransitLink Admin Portal</a>
        <div class="d-flex align-items-center text-white">
            <span class="me-3 fw-semibold">Admin: ${sessionScope.currentUser.fullName}</span>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-outline-light btn-sm">Logout</a>
        </div>
    </div>
</nav>

<div class="container">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="fw-bold">Bus Fleet & Route Management</h2>
        <!-- Placeholder for future Add Trip modal/form -->
        <button class="btn btn-dark" disabled>+ Add New Trip (Exam Feature)</button>
    </div>

    <div class="card shadow-sm border-0">
        <div class="table-responsive">
            <table class="table table-hover align-middle mb-0">
                <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Bus Number</th>
                    <th>Route</th>
                    <th>Departure Time</th>
                    <th>Total Seats</th>
                    <th>Available Seats</th>
                    <th>Ticket Price</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="trip" items="${trips}">
                    <tr>
                        <td class="fw-semibold">#${trip.id}</td>
                        <td>${trip.busNumber}</td>
                        <td>${trip.origin} &rarr; ${trip.destination}</td>
                        <td>${trip.departureDatetime}</td>
                        <td>${trip.totalSeats}</td>
                        <td><span class="badge bg-success">${trip.availableSeats}</span></td>
                        <td><fmt:formatNumber value="${trip.ticketPrice}" type="currency" currencySymbol="P "/></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>
</div>
</body>
</html>