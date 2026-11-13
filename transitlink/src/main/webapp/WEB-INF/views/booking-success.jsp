<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>TransitLink - Booking Confirmed</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light d-flex align-items-center justify-content-center vh-100">
<div class="card shadow-sm border-0 p-5 text-center" style="max-width: 500px; width: 100%;">
    <div class="mb-4">
        <span class="badge bg-success p-3 rounded-circle fs-3">&#10003;</span>
    </div>
    <h2 class="fw-bold mb-2">Booking Confirmed!</h2>
    <p class="text-muted mb-4">Your seat has been successfully reserved. You can view your ticket details in your booking history.</p>

    <div class="d-grid gap-2">
        <a href="${pageContext.request.contextPath}/my-bookings" class="btn btn-primary">View My Bookings</a>
        <a href="${pageContext.request.contextPath}/trips" class="btn btn-outline-secondary">Book Another Trip</a>
    </div>
</div>
</body>
</html>