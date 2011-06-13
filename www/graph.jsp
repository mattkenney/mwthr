<%@ page contentType="text/html; charset=UTF-8"
%><%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"
%><%
/*
 * Copyright 2011 Matt Kenney
 *
 * This file is part of mwthr.
 *
 * mwthr is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 *
 * mwthr is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with mwthr.  If not, see <http://www.gnu.org/licenses/>.
*/
%><!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta content="text/html;charset=UTF-8" http-equiv="Content-type" />
<!-- source code: https://github.com/mattkenney/mwthr -->
<meta content="initial-scale=1.0" name="viewport" />
<link href="/images/logo.png" rel="apple-touch-icon" />
<style type="text/css">
<% pageContext.include("/css/default.css"); %>
</style>
<title>mwthr: <c:out value="${cwa.name}" />, <c:out value="${cwa.state}" /></title>
</head>
<body>
<div>
<table class="summary">
<tbody>
<tr><td class="label">County/Area:</td><td><c:out value="${cwa.name}, ${cwa.state}" /></td></tr>
<tr><td class="label">Now:</td><td><c:if test="${empty current.temp_f}">?</c:if><c:out value="${current.temp_f}" /> &#176;F<c:if test="${!empty current.weather}">, <c:out value="${current.weather}" /></c:if></td></tr>
<tr><td class="label">Radar:</td><td><a href="."><c:out value="${radar.name}" /></td></tr>
<tr><td class="label">Forecast:</td><td>
<a<c:if test="${duration!='24'}"> href="24"</c:if>>24h</a>
|
<a<c:if test="${duration!='72'}"> href="72"</c:if>>72h</a>
|
<a<c:if test="${duration!='120'}"> href="120"</c:if>>120h</a>
|
<a<c:if test="${duration!='168'}"> href="168"</c:if>>168h</a>
</td></tr>
</tbody>
</table>
<img alt="forecast" height="220" src="<c:out value="${graphurl}" />" width="600" />
<table class="subtle">
<tbody>
<tr><td>Like? Don't like? Let me know: matt<img alt="" src="/images/at.png" />mwthr.com</td></tr>
</tbody>
</table>
</div>
</body>
</html>
