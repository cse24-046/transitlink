<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>TransitLink - Available Trips</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
<nav class="navbar navbar-dark bg-dark mb-4">
    <div class="container">
        <a class="navbar-brand fw-bold" href="#">TransitLink</a>
        <div class="d-flex align-items-center text-white">
            <a href="${pageContext.request.contextPath}/my-bookings" class="btn btn-outline-light btn-sm me-3">My Bookings</a>
            <span class="me-3">${sessionScope.currentUser.fullName}</span>
            <a href="${pageContext.request.contextPath}/logout" class="btn btn-danger btn-sm">Logout</a>
        </div>
    </div>
</nav>

<div class="container">
    <h2 class="fw-bold mb-3">Book Express Bus Trips</h2>

    <c:if test="${param.error eq 'true'}"><div class="alert alert-danger">Booking failed. Not enough available seats.</div></c:if>

    <form action="${pageContext.request.contextPath}/trips" method="get" class="row g-3 mb-4">
        <div class="col-md-4">
            <input type="text" name="origin" class="form-control" placeholder="Origin city..." value="${param.origin}">
        </div>
        <div class="col-md-4">
            <input type="text" name="destination" class="form-control" placeholder="Destination city..." value="${param.destination}">
        </div>
        <div class="col-md-4">
            <button type="submit" class="btn btn-primary w-100">Search Trips</button>
        </div>
    </form>

    <div class="card shadow-sm border-0">
        <table class="table table-hover align-middle mb-0">
            <thead class="table-dark">
            <tr>
                <th>Bus</th>
                <th>Route</th>
                <th>Departure</th>
                <th>Available Seats</th>
                <th>Price</th>
                <th>Action</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="trip" items="${trips}">
                <tr>
                    <td class="fw-semibold">${trip.busNumber}</td>
                    <td>${trip.origin} &rarr; ${trip.destination}</td>
                    <td>${trip.departureDatetime}</td>
                    <td><span class="badge bg-success">${trip.availableSeats} seats</span></td>
                    <td><fmt:formatNumber value="${trip.ticketPrice}" type="currency" currencySymbol="P "/></td>
                    <td>
                        <form action="${pageContext.request.contextPath}/book" method="post" class="d-flex gap-2 m-0">
                            <input type="hidden" name="tripId" value="${trip.id}">
                            <input type="number" name="seats" value="1" min="1" max="${trip.availableSeats}" class="form-control form-control-sm w-25" required>
                            <button type="submit" class="btn btn-sm btn-success">Book</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>