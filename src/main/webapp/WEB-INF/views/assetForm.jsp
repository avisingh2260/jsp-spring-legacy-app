<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <title>Add New Asset — AssetDesk</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/legacy.css"/>
</head>
<body>
<div class="container">
    <h1>Register New IT Asset</h1>
    <form:form method="post" action="createAsset.do" modelAttribute="asset" cssClass="legacy-form">
        <div class="field">
            <label>Serial Number (AAA-YYYY-NNN):</label>
            <form:input path="serialNumber"/>
            <form:errors path="serialNumber" cssClass="error"/>
        </div>
        <div class="field">
            <label>Asset Name:</label>
            <form:input path="name"/>
            <form:errors path="name" cssClass="error"/>
        </div>
        <div class="field">
            <label>Category:</label>
            <form:select path="category">
                <form:option value="" label="-- Select --"/>
                <form:option value="Laptop" label="Laptop"/>
                <form:option value="Monitor" label="Monitor"/>
                <form:option value="Mobile" label="Mobile"/>
                <form:option value="Security" label="Security"/>
            </form:select>
            <form:errors path="category" cssClass="error"/>
        </div>
        <button type="submit">Save Asset</button>
        <a href="assets.do">Cancel</a>
    </form:form>
</div>
</body>
</html>
