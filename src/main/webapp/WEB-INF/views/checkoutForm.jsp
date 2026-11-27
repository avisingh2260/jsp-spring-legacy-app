<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Check Out Asset — AssetDesk</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/legacy.css"/>
</head>
<body>
<div class="container">
    <h1>Check Out Asset</h1>
    <p><strong><c:out value="${asset.serialNumber}"/></strong> — <c:out value="${asset.name}"/></p>

    <c:if test="${not empty errorMessage}">
        <div class="error-box"><c:out value="${errorMessage}"/></div>
    </c:if>

    <form method="post" action="checkout.do" class="legacy-form">
        <input type="hidden" name="id" value="${asset.id}"/>
        <div class="field">
            <label>Employee Name:</label>
            <input type="text" name="assigneeName" required/>
        </div>
        <div class="field">
            <label>Employee Badge ID (EMP-XXXX-XXXX):</label>
            <input type="text" name="assigneeBadgeId" placeholder="EMP-1234-5678" required/>
        </div>
        <button type="submit">Confirm Checkout</button>
        <a href="assets.do">Cancel</a>
    </form>
</div>
</body>
</html>
