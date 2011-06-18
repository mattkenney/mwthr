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
<title>mwthr.com</title>
</head>
<body>
<div class="overall">
<table class="summary summary-top">
<tbody>
<tr><td class="label">County/Area:</td><td colspan="2"><c:out value="${cwa.name}, ${cwa.state}" /></td></tr>
<tr><td class="label">Now:</td><td colspan="2"><c:if test="${empty current.temp_f}">?</c:if><c:out value="${current.temp_f}" /> &#176;F<c:if test="${!empty current.weather}">, <c:out value="${current.weather}" /></c:if></td></tr>
<tr><td class="label">Next 24 hours:</td><td colspan="2"><c:if test="${empty forecast.temperature}">?</c:if><c:out value="${forecast.temperature}" /> &#176;F<c:if test="${!empty forecast['probability-of-precipitation']}">, <c:out value="${forecast['probability-of-precipitation']}" />% chance of precipitation</c:if></td></tr>
<tr><td class="label">Radar:</td><td><c:out value="${radar.name}" /></td><td class="option"><a href="<c:out value="${radar.statusurl}" />" target="_blank">radar status</a> | <a href="/icao/">change location</a></td></tr>
</tbody>
</table>
<table cellpadding="0" cellspacing="0" class="summary summary-middle">
<tbody>
<tr>
<c:choose>
<c:when test="${duration==24}">
<td width="20%"><div class="tab"><a href="." class="tab-a">Radar</a></div></td>
<td width="20%"><div class="tab tab-active">24 Hours</div></td>
<td width="20%"><div class="tab"><a href="120" class="tab-a">5 Days</a></div></td>
<td width="20%"><div class="tab"><a href="atl" class="tab-a">Atlantic</a></div></td>
<td width="20%"><div class="tab"><a href="pac" class="tab-a">Pacific</a></div></td>
</tr>
</tbody>
</table>
<div class="forecast">
<img alt="24 hour forecast" height="330" src="<c:out value="${graph.graph24}" />" width="596" /></div>
</c:when>
<c:when test="${duration==120}">
<td width="20%"><div class="tab"><a href="." class="tab-a">Radar</a></div></td>
<td width="20%"><div class="tab"><a href="24" class="tab-a">24 Hours</a></div></td>
<td width="20%"><div class="tab tab-active">5 Days</div></td>
<td width="20%"><div class="tab"><a href="atl" class="tab-a">Atlantic</a></div></td>
<td width="20%"><div class="tab"><a href="pac" class="tab-a">Pacific</a></div></td>
</tr>
</tbody>
</table>
<div class="forecast">
<img alt="5 day forecast" height="330" src="<c:out value="${graph.graph120}" />" width="596" /></div>
</c:when>
<c:when test="${ocean=='atl'}">
<td width="20%"><div class="tab"><a href="." class="tab-a">Radar</a></div></td>
<td width="20%"><div class="tab"><a href="24" class="tab-a">24 Hours</a></div></td>
<td width="20%"><div class="tab"><a href="120" class="tab-a">5 Days</a></div></td>
<td width="20%"><div class="tab tab-active">Atlantic</div></td>
<td width="20%"><div class="tab"><a href="pac" class="tab-a">Pacific</a></div></td>
</tr>
</tbody>
</table>
<div class="summary summary-ocean">
<img alt="Atlantic tropical cyclone activity" class="summary-ocean-image" height="353" src="http://www.nhc.noaa.gov/overview_atl/atl_overview.gif" width="565" />
</div>
</c:when>
<c:when test="${ocean=='pac'}">
<td width="20%"><div class="tab"><a href="." class="tab-a">Radar</a></div></td>
<td width="20%"><div class="tab"><a href="24" class="tab-a">24 Hours</a></div></td>
<td width="20%"><div class="tab"><a href="120" class="tab-a">5 Days</a></div></td>
<td width="20%"><div class="tab"><a href="atl" class="tab-a">Atlantic</a></div></td>
<td width="20%"><div class="tab tab-active">Pacific</div></td>
</tr>
</tbody>
</table>
<div class="summary summary-ocean">
<img alt="Eastern pacific tropical cyclone activity" class="summary-ocean-image" height="353" src="http://www.nhc.noaa.gov/overview_epac/epac_overview.gif" width="565" />
</div>
</c:when>
<c:otherwise>
<td width="20%"><div class="tab tab-active">Radar</div></td>
<td width="20%"><div class="tab"><a href="24" class="tab-a">24 Hours</a></div></td>
<td width="20%"><div class="tab"><a href="120" class="tab-a">5 Days</a></div></td>
<td width="20%"><div class="tab"><a href="atl" class="tab-a">Atlantic</a></div></td>
<td width="20%"><div class="tab"><a href="pac" class="tab-a">Pacific</a></div></td>
</tr>
</tbody>
</table>
<noscript><img alt="<c:out value="${radar.icao}" /> Radar" height="550" src="<c:out value="${radar.baseurl}" />Loop.gif" width="600" /></noscript>
<input id="jsloop" name="jsloop" type="hidden" value="<c:out value="${radar.baseurl}" />" />
<script type="text/javascript">
/* <![CDATA[ */
<% pageContext.include("/js/radarloop.js"); %>
/* ]]> */
</script>
</c:otherwise>
</c:choose>
<table class="subtle">
<tbody>
<tr><td>Like? Don't like? Let me know: matt<img alt="" src="/images/at.png" />mwthr.com</td></tr>
</tbody>
</table>
</div>
</body>
</html>
