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
<tr><td class="label">County/Area:</td><td colspan="2"><c:out value="${cwa.name}, ${cwa.state}" /></td></tr>
<tr><td class="label">Now:</td><td colspan="2"><c:out value="${current.temp_f}" /><c:if test="${empty current.temp_f}">?</c:if> &#176;F, <c:out value="${current.weather}" /></td></tr>
<tr><td class="label">Next 24 hours:</td><td colspan="2"><c:out value="${forecast.temperature}" /><c:if test="${empty forecast.temperature}">?</c:if> &#176;F, <c:out value="${forecast['probability-of-precipitation']}" />% chance of precipitation</td></tr>
<tr><td class="label">Radar:</td><td><c:out value="${radar.name}" /></td><td class="option"><a href="/icao/">change location</a></td></tr>
</tbody>
</table>
<noscript><img alt="radar" height="550" src="<c:out value="${radar.baseurl}" />Loop.gif" width="600" /></noscript>
<input id="jsloop" name="jsloop" type="hidden" value="<c:out value="${radar.baseurl}" />" />
</div>
<script type="text/javascript">
/* <![CDATA[ */
<% pageContext.include("/js/radarloop.js"); %>
/* ]]> */
</script>
</body>
</html>
