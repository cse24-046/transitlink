<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>TransitLink - Welcome</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light d-flex align-items-center justify-content-center vh-100">
<div class="text-center p-5 bg-white shadow-sm rounded-4" style="max-width: 500px; width: 100%;">
    <h1 class="fw-bold text-primary mb-3">TransitLink</h1>
    <p class="text-muted mb-4">Your reliable express bus ticketing and route reservation system.</p>
    <div class="d-grid gap-2">
        <a href="${pageContext.request.contextPath}/login" class="btn btn-primary btn-lg">Access Portal</a>
    </div>
</div>
</body>
</html>