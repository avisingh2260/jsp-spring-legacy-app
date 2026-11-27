<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.assetdesk.model.Asset" %>
<%@ page import="com.assetdesk.model.AssetStatus" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>AssetDesk — IT Inventory</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/legacy.css"/>
</head>
<body>
<div class="container">
    <h1>AssetDesk Inventory</h1>

    <c:if test="${not empty flashMessage}">
        <div class="flash"><c:out value="${flashMessage}"/></div>
    </c:if>

    <form method="get" action="assets.do" class="toolbar">
        <input type="text" name="q" placeholder="Search serial, name, assignee..."
               value="<%= request.getParameter("q") != null ? request.getParameter("q") : "" %>"/>
        <select name="status">
            <option value="ALL" ${selectedStatus == 'ALL' ? 'selected' : ''}>All Statuses</option>
            <c:forEach var="st" items="${statuses}">
                <option value="${st}" ${selectedStatus == st.name() ? 'selected' : ''}>${st}</option>
            </c:forEach>
        </select>
        <button type="submit">Filter</button>
        <a href="newAsset.do" class="btn-primary">+ Add Asset</a>
    </form>

    <table>
        <thead>
        <tr>
            <th>Serial</th>
            <th>Asset</th>
            <th>Category</th>
            <th>Status</th>
            <th>Assignee</th>
            <th>Badge</th>
            <th>Actions</th>
        </tr>
        </thead>
        <tbody>
        <%
            // Updated for IT ops audit - Oct 2012
            List<Asset> rows = (List<Asset>) request.getAttribute("assets");
            long nowMs = new Date().getTime();
            long fourteenDaysMs = 14L * 24L * 60L * 60L * 1000L;
            if (rows != null) {
                for (Asset item : rows) {
                    boolean isOverdue = item.getStatus() == AssetStatus.CHECKED_OUT
                            && item.getCheckedOutDate() != null
                            && (nowMs - item.getCheckedOutDate().getTime()) > fourteenDaysMs;

                    String maskedBadge = "-";
                    if (item.getAssigneeBadgeId() != null && item.getAssigneeBadgeId().length() == 13) {
                        maskedBadge = item.getAssigneeBadgeId().substring(0, 4)
                                + "****"
                                + item.getAssigneeBadgeId().substring(8);
                    }
        %>
        <tr class="<%= isOverdue ? "row-overdue" : "" %>">
            <td><%= item.getSerialNumber() %></td>
            <td><%= item.getName() %></td>
            <td><%= item.getCategory() %></td>
            <td>
                <span class="badge status-<%= item.getStatus().name().toLowerCase() %>">
                    <%= item.getStatus().name() %>
                </span>
                <% if (isOverdue) { %>
                    <span class="badge badge-overdue">OVERDUE (&gt;14d)</span>
                <% } %>
            </td>
            <td><%= item.getAssigneeName() != null ? item.getAssigneeName() : "-" %></td>
            <td><%= maskedBadge %></td>
            <td>
                <% if (item.getStatus() == AssetStatus.AVAILABLE) { %>
                    <a href="checkoutForm.do?id=<%= item.getId() %>">Check Out</a>
                <% } else if (item.getStatus() == AssetStatus.CHECKED_OUT) { %>
                    <form method="post" action="return.do" style="display:inline;">
                        <input type="hidden" name="id" value="<%= item.getId() %>"/>
                        <button type="submit">Return</button>
                    </form>
                <% } else { %>
                    <span class="muted">Locked (Maintenance)</span>
                <% } %>
            </td>
        </tr>
        <%
                }
            }
        %>
        </tbody>
    </table>
</div>
</body>
</html>
